package com.fufa.fariska.controllers;

import com.fufa.fariska.entities.Game;
import com.fufa.fariska.services.GameService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {

    public GameService gameService;

    @GetMapping("/games")
    public List<Game> getCreatedGames() {
        return new ArrayList<>();
    }

    @GetMapping("/games/{id}")
    public String getGame(@PathVariable int id) {
        return "Game " + id;
    }

    @PostMapping("/games")
    public Game createGame(@RequestBody Game game) {
        return gameService.makeNewGame(game);
    }



}
