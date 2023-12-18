package com.fufa.fariska.services;

import com.fufa.fariska.dto.GameDto;
import com.fufa.fariska.dto.GameRequestDto;
import com.fufa.fariska.entities.Game;
import com.fufa.fariska.entities.User;
import com.fufa.fariska.entities.enums.GameStatus;
import com.fufa.fariska.repositories.GameRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class GameService {
    public GameRepository gameRepository;
    private int totalGames;
    private Map<Integer, Game> currentGames;

    public List<Game> createdGames;

    public synchronized Game makeNewGame(GameRequestDto gameRequestDto, User user) {
        Game game = Game.builder()
                .id(++totalGames)
                .create_time(Instant.now())
                .creator(user)
                .status(GameStatus.WAITING_FOR_PLAYERS)
                .players(new int[] {user.getId()})
                .packs(gameRequestDto.getPacks())
                .build();



        //carry cards from packs
        //for()


        return game;
    }

    public Game findGame(final int id) {

        return gameRepository.findById(id).get();
    }

}
