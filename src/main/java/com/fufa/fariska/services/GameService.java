package com.fufa.fariska.services;

import com.fufa.fariska.dto.GameRequestDto;
import com.fufa.fariska.dto.MoveRequestDto;
import com.fufa.fariska.dto.SecretRequestDto;
import com.fufa.fariska.dto.VoteRequestDto;
import com.fufa.fariska.entities.*;
import com.fufa.fariska.entities.enums.Avatar;
import com.fufa.fariska.entities.enums.GameStatus;
import com.fufa.fariska.entities.enums.RoundStatus;
import com.fufa.fariska.repositories.GameRepository;
import com.fufa.fariska.repositories.PackRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class GameService {
    public GameRepository gameRepository;
    public PackRepository packRepository;
    private static int totalGames;
    private Map<Integer, Game> createdGames;

    public synchronized Game makeNewGame(GameRequestDto gameRequestDto, User user) {

        int gameId = ++totalGames;

        Set<Integer> packsId = gameRequestDto.getPacksId();
        List<Pack> packs = packRepository.getPacks(packsId);
        LinkedList<Card> cards = collectAllCardsAndShuffle(packs);

        Game game = Game.builder()
                .id(gameId)
                .create_time(Instant.now())
                .creator(user)
                .status(GameStatus.WAITING_FOR_PLAYERS)
                .packsId(packsId)
                .cards(cards)
                .freeAvatars(Avatar.getAll())
                .build();
        List<Player> players = new ArrayList<>();

        players.add(Player.builder()
                .user(user)
                .gameId(gameId)
                .avatar(game.getFreeAvatar())
                .build());
        game.setPlayers(players);

        createdGames.put(gameId, game);

        return game;
    }

    public Game findGame(final int id) {
        return createdGames.get(id);
    }

    public synchronized Map<Integer, Game> findAllCreatedGames() {
        return createdGames;
    }

    public synchronized Player joinNewPlayer(User user, int gameId) {
        Game game = createdGames.get(gameId);

        if (game.getPlayers().size() >= 9 || game.getStatus() == GameStatus.GAME_OVER) {
            return null;                                     // make exception
        }
        Player player = Player.builder()
                .user(user)
                .gameId(gameId)
                .avatar(game.getFreeAvatar())
                .build();
        List<Player> players = game.getPlayers();
        players.add(player);
        game.setPlayers(players);

        return player;
    }

    public synchronized Game startGame(User user, int gameId) {

        Game game = createdGames.get(gameId);
        if (user.getId() != game.getCreator().getId()
                || game.getPlayers().size() < 2 || game.getStatus() != GameStatus.WAITING_FOR_PLAYERS) {
            return game;                                     // make exception
        }

        List<Player> players = game.getPlayers();
        putPlayersOnTheirPlaces(players);
        game.setLeader(1);
        game.dealCards(); //or can deal for each when create

        Round round = Round.builder()
                .gameId(gameId)
                .number(1)
                .leader(players.get(0))
                .tableCards(Move.makeEmptyTableCards(players.size() + 1))
                .status(RoundStatus.WRITING_SECRET)
                .build();
        game.setCurrentRound(round);
        game.setStatus(GameStatus.PLAYING);
        return game;
    }

    public synchronized Game makeSecret(User user, int gameId, SecretRequestDto secretRequestDto) {

        Game game = createdGames.get(gameId);
        Round round = game.getCurrentRound();

        if (user.getId() != game.getLeader() || round.getNumber() != secretRequestDto.getRound() || game.getStatus() != GameStatus.PLAYING) {
            return null;                                     // make exception
        }

        if (round.getStatus() != RoundStatus.WRITING_SECRET) {
            return null;                                     // make exception
        }

        Player leader = round.getLeader();
        int leaderPlace = game.getLeader();

        Card leaderCard = leader.getHandCards().remove(secretRequestDto.getCardHandNumber());
        round.setLeaderCard(leaderCard);

        round.putCardOnTable(leaderPlace, leaderCard);

        round.setSecret(secretRequestDto.getSecret());

        leader.getHandCards().add(game.takeSomeCards(1).get(0)); // make method to take one card

        round.setStatus(RoundStatus.MAKING_MOVIES);
        return game;
    }

    public synchronized Game makeMove(User user, int gameId, MoveRequestDto moveRequestDto) {

        Game game = createdGames.get(gameId);
        Round round = game.getCurrentRound();
        int place = moveRequestDto.getPlayerPlace();


        if (game.getPlayers().get(place).getUser().getId() != user.getId()) {
            return null;                                     // make exception
        }

        if (round.getNumber() != moveRequestDto.getRound() || game.getStatus() != GameStatus.PLAYING
        || round.getStatus() != RoundStatus.MAKING_MOVIES) {
            return null;                                     // make exception
        }

        if (round.getTableCards().get(place) != null) {
            return null;                                     // make exception
        }

        List<Player> players = game.getPlayers();
        Player player = players.get(place);
        Card card = player.putOneCard(moveRequestDto.getCardHandNumber());
        round.putCardOnTable(place, card);

        player.takeOneCard(game.takeOneCard());


        if (game.getPlayers().size() <= (round.getNumberMoves())) {
            endMoveAndStartVoting(round);
        }
        return game;
    }

    public synchronized Game makeVote(User user, int gameId, VoteRequestDto voteRequestDto) {

        Game game = createdGames.get(gameId);
        Round round = game.getCurrentRound();
        int place = voteRequestDto.getPlayerPlace();

        if (game.getPlayers().get(place).getUser().getId() != user.getId()) {
            return null;                                     // make exception
        }

        if (round.getNumber() != voteRequestDto.getRound() || game.getStatus() != GameStatus.PLAYING
                || round.getStatus() != RoundStatus.VOTING) {
            return null;                                     // make exception
        }

        if (game.getLeader() == place) {
            return null;                                     // make exception
        }

        if (round.getPlayerVotes()[place] != 0) {
            return null;                                     // make exception
        }

        int vote = voteRequestDto.getCardTableNumber();

        if (round.getPlayerMoves()[place] == vote) {
            return null;                                     // make exception
        }

        round.getPlayerVotes()[place] = vote;
        round.setNumberVotes(round.getNumberVotes() + 1);

        if (game.getPlayers().size() <= round.getNumberVotes() + 1) {
            game.addAllPoints(endVoteAndCalcPoints(round));
            round.setStatus(RoundStatus.WAITING_FOR_NEXT_ROUND);
        }

        return game;
    }

    public synchronized Game startNextRound(User user, int gameId) {

        Game game = createdGames.get(gameId);

        if (game.getStatus() != GameStatus.PLAYING
                || game.getCurrentRound().getStatus() != RoundStatus.WAITING_FOR_NEXT_ROUND) {
            return null;                                     // make exception
        }

        if (game.getCurrentRound().isLastRound()) {
            return game.finish();
        }

        List<Player> players = game.getPlayers();

        Round round = Round.builder()
                .gameId(gameId)
                .number(game.getCurrentRound().getNumber() + 1)
                .leader(players.get(game.getNextLeader() - 1))
                .tableCards(List.of(new Move[players.size() + 1]))
                .status(RoundStatus.WRITING_SECRET)
                .build();
        game.setCurrentRound(round);
        return game;
    }

    public synchronized Game deleteGame(User user, int gameId) {

        Game game = createdGames.remove(gameId);                    //may be required game obj to remove
        if (user.getId() != game.getCreator().getId()) {
            return null;                                     // make exception
        }

        return game;
    }

    private synchronized LinkedList<Card> collectAllCardsAndShuffle(List<Pack> packs) {

        LinkedList<Card> cards = new LinkedList<>();
        for (Pack pack : packs) {
            cards.addAll(pack.getCards());
        }
        Collections.shuffle(cards);
        return cards;
    }

    private synchronized void putPlayersOnTheirPlaces(List<Player> players) {

        Collections.shuffle(players);
        players.get(0).setLeader(true);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setPlace(i + 1);
        }
    }

    private void endMoveAndStartVoting(Round round) {

        Collections.shuffle(round.getTableCards());
        int[] movies = new int[round.getTableCards().size() + 1];
        int cardPosition = 1;
        for (Move move : round.getTableCards()) {
            movies[move.getPlayerPlace()] = cardPosition++;
        }
        round.setPlayerMoves(movies);

        int[] votes = new int[round.getTableCards().size() + 1];
        round.setPlayerVotes(votes);

        round.setStatus(RoundStatus.VOTING);
    }

    private int[] endVoteAndCalcPoints(Round round) {

        round.setStatus(RoundStatus.POINTS_CALC);
        int[] votes = round.getPlayerVotes();
        int number = votes.length;
        int[] points = new int[number];

        int leaderCardNumber = round.findNumberLeaderCard();
        int numberGuessedLeaderCard = 0;

        for (int i = 1; i < number; i++ ) {
            if (votes[i] == leaderCardNumber) {
                numberGuessedLeaderCard++;
                points[i] += 3;
            } else {
                points[round.getTableCards().get(votes[i]).getPlayerPlace()] += 1;
            }
        }
        if (numberGuessedLeaderCard > 0 && numberGuessedLeaderCard < number - 1) {
            points[round.getLeader().getPlace()] += 3 + numberGuessedLeaderCard;
        }
        round.setPlayerPoints(points);
        return points;
    }





    }
