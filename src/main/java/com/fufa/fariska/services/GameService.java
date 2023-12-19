package com.fufa.fariska.services;

import com.fufa.fariska.dto.GameDto;
import com.fufa.fariska.dto.GameRequestDto;
import com.fufa.fariska.entities.*;
import com.fufa.fariska.entities.enums.Avatar;
import com.fufa.fariska.entities.enums.GameStatus;
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
                .avatar(game.getFreeAvatar()) //make randomSelect fnc
                .build());
        game.setPlayers(players);

        return game;
    }

    public Game findGame(final int id) {

        return gameRepository.findById(id).get();
    }

    private List<Card> collectAllCardsAndShuffle(Set<Pack> packs) {
        List<Card> cards = new LinkedList<>();
        for (Pack pack : packs) {
            cards.addAll(pack.getCards());
        }
        Collections.shuffle(cards);
        return cards;
    }

}
