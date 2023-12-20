package com.fufa.fariska.controllers;

import com.fufa.fariska.dto.GameDto;
import com.fufa.fariska.dto.GameRequestDto;
import com.fufa.fariska.entities.Game;
import com.fufa.fariska.entities.Player;
import com.fufa.fariska.entities.User;
import com.fufa.fariska.entities.enums.GameStatus;
import com.fufa.fariska.services.GameService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {

    public GameService gameService;

    @GetMapping("/games")
    public List<Game> getCreatedGames() {
        return gameService.findAllCreatedGames();
    }

    @GetMapping("/games/{id}")
    public String getGame(@PathVariable int id) {
        return "Game " + id;
    } //return GameDTO?

    @PostMapping("/games")
    public GameDto createGame(@AuthenticationPrincipal User user, @RequestBody GameRequestDto gameRequestDto) {
        Game newGame = gameService.makeNewGame(gameRequestDto, user);
        return GameDto.builder()
                .id(newGame.getId())
                .create_time(newGame.getCreate_time())
                .creator(newGame.getCreator())
                .status(newGame.getStatus())
                .players(newGame.getPlayers())
                .build();
    }

    @PostMapping("/games/{id}/join")
    public Player joinGame(@AuthenticationPrincipal User user, @PathVariable int gameId) {

        return gameService.joinNewPlayer(user, gameId);                 // make DTO
    }

    @PostMapping("/games/{id}/start")
    public Game startGame(@AuthenticationPrincipal User user, @PathVariable int gameId) {

        return gameService.startGame(user, gameId);                 // make DTO
    }
}
