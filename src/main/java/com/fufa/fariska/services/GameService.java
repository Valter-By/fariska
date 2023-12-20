package com.fufa.fariska.services;

import com.fufa.fariska.dto.GameDto;
import com.fufa.fariska.dto.GameRequestDto;
import com.fufa.fariska.entities.*;
import com.fufa.fariska.entities.enums.Avatar;
import com.fufa.fariska.entities.enums.GameStatus;
import com.fufa.fariska.entities.enums.RoundStatus;
import com.fufa.fariska.repositories.GameRepository;
import com.fufa.fariska.repositories.PackRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class GameService {
    public GameRepository gameRepository;
    public PackRepository packRepository;
    private int totalGames;
    private Map<Integer, Game> currentGames;

    public List<Game> createdGames;

    public synchronized Game makeNewGame(GameRequestDto gameRequestDto, User user) {
        int gameId = ++totalGames;

        Set<Integer> packsId = gameRequestDto.getPacksId();
        Set<Pack> packs = packRepository.getPacks(packsId);
        List<Card> cards = collectAllCardsAndShuffle(packs);

        Game game = Game.builder()
                .id(gameId)
                .create_time(Instant.now())
                .creator(user)
                .status(GameStatus.WAITING_FOR_PLAYERS)
                .packsId(packsId)
                .cards(cards)
                .build();
        List<Player> players = new ArrayList<>();

        players.add(Player.builder()
                .user(user)
                .gameId(gameId)
                .avatar(game.getFreeAvatar())
                .build());
        game.setPlayers(players);

        return game;
    }

    public Game findGame(final int id) {

        return createdGames.get(id);
    }

    public synchronized List<Game> findAllCreatedGames() {
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
        if (user.getId() != game.getCreator().getId() || game.getPlayers().size() < 2 || game.getStatus() != GameStatus.WAITING_FOR_PLAYERS) {
            return null;                                     // make exception
        }

        List<Player> players = game.getPlayers();
        putPlayersOnTheirPlaces(players);
        game.setLeader(0);
        game.dealCards();

        Round round = Round.builder()
                .gameId(gameId)
                .number(1)
                .leader(players.get(0))
                .status(RoundStatus.DEALING)
                .build();

        game.setCurrentRound(round);
        game.setStatus(GameStatus.PLAYING);
        return game;
    }



    private synchronized List<Card> collectAllCardsAndShuffle(Set<Pack> packs) {
        List<Card> cards = new LinkedList<>();
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
            players.get(i).setPlace(i);
        }
    }



}
