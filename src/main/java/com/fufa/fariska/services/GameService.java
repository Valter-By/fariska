package com.fufa.fariska.services;

import com.fufa.fariska.entities.Game;
import com.fufa.fariska.repositories.GameRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    public GameRepository gameRepository;

    public List<Game> createdGames;

    public Game makeNewGame(Game game) {


        return gameRepository.saveAndFlush(game);
    }

    public Game findGame(final int id) {

        return gameRepository.findById(id).get();
    }

}
