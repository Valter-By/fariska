package com.fufa.fariska.service;

import com.fufa.fariska.dto.*;
import com.fufa.fariska.entity.*;
import com.fufa.fariska.entity.enums.Avatar;
import com.fufa.fariska.entity.enums.GameStatus;
import com.fufa.fariska.entity.enums.RoundStatus;
import com.fufa.fariska.repository.PackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GameService {

    public final PackRepository packRepository;
    public final Map<Integer, Game> createdGames = new HashMap<>();

    public synchronized Game makeNewGame(GameUser user, GameRequestDto gameRequestDto) {

        int gameId = ++Game.totalGames;

//        Set<Integer> packsId = gameRequestDto.getPacksId();
        Integer packsId = gameRequestDto.getPacksId();
        List<Pack> packs = packRepository.getPacks(Set.of(packsId));
        LinkedList<Card> cards = collectAllCardsAndShuffle(packs);

        Game game = Game.builder()
                .id(gameId)
                .createTime(Instant.now())
                .creator(user)
                .status(GameStatus.WAITING_FOR_PLAYERS)
//                .currentRound(Round.builder().number(0).build())
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

    public synchronized Set<Integer> findAllCreatedGames() {
        return this.createdGames.keySet();
    }

    public synchronized Player joinNewPlayer(GameUser user, int gameId) {
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
        return player;
    }

    public synchronized Game startGame(GameUser user, int gameId) {

        Game game = createdGames.get(gameId);
        if (user.getId() != game.getCreator().getId()
                || game.getPlayers().size() < 2 || game.getStatus() != GameStatus.WAITING_FOR_PLAYERS) {
            return null;                                     // make exception
        }

        List<Player> players = game.getPlayers();
        putPlayersOnTheirPlaces(players);
        game.setLeader(0);
        game.dealCards(); //or can deal for each when create

        Round round = Round.builder()
                .gameId(gameId)
                .number(1)
                .leader(players.get(0))
                .tableCards(TableCard.makeEmptyTableCards(players.size()))
                .status(RoundStatus.WRITING_SECRET)
                .build();
        game.setCurrentRound(round);
        game.setStatus(GameStatus.PLAYING);
        return game;
    }

    public synchronized Game makeSecret(GameUser user, int gameId, SecretRequestDto secretRequestDto) {

        Game game = createdGames.get(gameId);
        Round round = game.getCurrentRound();
        Player leader = round.getLeader();
        int leaderPlace = game.getLeader();

        if (!Objects.equals(leader.getUser(), user)
                || round.getNumber() != secretRequestDto.getRound() || game.getStatus() != GameStatus.PLAYING) {
            return null;                                     // make exception
        }

        if (round.getStatus() != RoundStatus.WRITING_SECRET) {
            return null;                                     // make exception
        }

        Card leaderCard = leader.putOneCard(secretRequestDto.getCardHandNumber());
        round.setLeaderCard(leaderCard);

        round.putCardOnTable(leaderPlace, leaderCard, true);

        round.setSecret(secretRequestDto.getSecret());

        leader.takeOneCard(game.takeOneCard());

        round.setStatus(RoundStatus.MAKING_MOVIES);
        return game;
    }

    public synchronized Game makeMove(GameUser user, int gameId, MoveRequestDto moveRequestDto) {

        Game game = createdGames.get(gameId);
        Round round = game.getCurrentRound();
        int place = moveRequestDto.getPlayerPlace();
        List<Player> players = game.getPlayers();
        Player player = players.get(place);

        if (player.getUser().getId() != user.getId()) {
            return null;                                     // make exception
        }

        if (round.getNumber() != moveRequestDto.getRound() || game.getStatus() != GameStatus.PLAYING
                || round.getStatus() != RoundStatus.MAKING_MOVIES) {
            return null;                                     // make exception
        }

        if (round.didPlayerMakeMove(place)) {
            return null;                                     // make exception
        }

        Card card = player.putOneCard(moveRequestDto.getCardHandNumber());
        round.putCardOnTable(place, card, false);

        player.takeOneCard(game.takeOneCard());

        if (game.getPlayers().size() <= (round.getNumberMoves())) {
            round.endMoveAndStartVoting();
        }
        return game;
    }

    public synchronized Game makeVote(GameUser user, int gameId, VoteRequestDto voteRequestDto) {

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

        if (round.didPlayerMakeVote(place)) {
            return null;                                     // make exception
        }

        TableCard voteCard = round.getTableCards().get(voteRequestDto.getCardTableNumber());

        if (voteCard.getPlayerPlace() == place) {
            return null;                                     // make exception
        }

        voteCard.getVotes().add(place);

        round.setNumberVotes(round.getNumberVotes() + 1);

        if (game.getPlayers().size() <= round.getNumberVotes() + 1) {
            game.addAllPoints(round.endVoteAndCalcPoints());
            round.setStatus(RoundStatus.WAITING_FOR_NEXT_ROUND);
        }

        return game;
    }

    public synchronized Game startNextRound(GameUser user, int gameId) {

        Game game = createdGames.get(gameId);
        Round round = game.getCurrentRound();

        if (game.getStatus() != GameStatus.PLAYING
                || round.getStatus() != RoundStatus.WAITING_FOR_NEXT_ROUND) {
            return null;                                     // make exception
        }

        if (game.getCurrentRound().isLastRound()) {
            return game.finish();
        }

        List<Player> players = game.getPlayers();
        Player oldLeader = players.get(game.getLeader());
        int nextLeaderPlace = game.getNextLeader();
        Player nextLeader = players.get(nextLeaderPlace);
        oldLeader.setLeader(false);
        nextLeader.setLeader(true);

        Round newRound = Round.builder()
                .gameId(gameId)
                .number(round.getNumber() + 1)
                .leader(nextLeader)
                .tableCards(TableCard.makeEmptyTableCards(players.size()))
                .status(RoundStatus.WRITING_SECRET)
                .build();
        game.setCurrentRound(newRound);
        return game;
    }

    public synchronized Game deleteGame(GameUser user, int gameId) {

        Game game = createdGames.get(gameId);
        if (user.getId() != game.getCreator().getId()) {
            return null;                                     // make exception
        } else {
            return createdGames.remove(gameId);
        }
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
        int i = 0;
        for (Player player : players) {
            player.setPlace(i++);
        }
    }


}
