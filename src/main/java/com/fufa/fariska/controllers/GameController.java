package com.fufa.fariska.controllers;

import com.fufa.fariska.dto.*;
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
import java.util.Map;

@RestController
public class GameController {

    public GameService gameService;

    @GetMapping("/games")
    public Map<Integer, Game> getCreatedGames() {
        return gameService.findAllCreatedGames(); // may be return just don't finished games in Map by id
    }

    @GetMapping("/games/{id}")
    public Game getGame(@PathVariable int id) {
        return gameService.findGame(id);
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

    @PostMapping("/games/{id}/secret")
    public Game createSecret(@AuthenticationPrincipal User user, @PathVariable int gameId,
                             @RequestBody SecretRequestDto secretRequestDto) {

        return gameService.makeSecret(user, gameId, secretRequestDto);                 // make DTO
    }

    @PostMapping("/games/{id}/move")
    public Game createMove(@AuthenticationPrincipal User user, @PathVariable int gameId,
                             @RequestBody MoveRequestDto secretRequestDto) {

        return gameService.makeMove(user, gameId, secretRequestDto);                 // make DTO
    }

    @PostMapping("/games/{id}/vote")
    public Game createVote(@AuthenticationPrincipal User user, @PathVariable int gameId,
                           @RequestBody VoteRequestDto voteRequestDto) {

        return gameService.makeVote(user, gameId, voteRequestDto);                 // make DTO
    }

    @PostMapping("/games/{id}/next")
    public Game createNextRound(@AuthenticationPrincipal User user, @PathVariable int gameId) {

        return gameService.startNextRound(user, gameId);                 // make DTO
    }




}
