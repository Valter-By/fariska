package com.fufa.fariska.controllers;

import com.fufa.fariska.dto.*;
import com.fufa.fariska.entities.*;
import com.fufa.fariska.entities.enums.GameStatus;
import com.fufa.fariska.repositories.PackRepository;
import com.fufa.fariska.services.GameService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class GameController {

    public final GameService gameService;

    @GetMapping("/games")
    public Set<Integer> getCreatedGames() {
        return gameService.findAllCreatedGames(); // may be return some game's info in Map by id
    }

    @GetMapping("/games/{id}")
    public GameDto getGame(@PathVariable("id") final int id) {
        return gameService.findGame(id).makeDto();
    }

    @GetMapping("/games/{id}/round")
    public RoundDto getRound(@PathVariable int id) {
        return gameService.findGame(id).getCurrentRound().makeDto();
    }

    @PostMapping("/games")
    public GameDto createGame(@AuthenticationPrincipal User user, @RequestBody GameRequestDto gameRequestDto) {

        return gameService.makeNewGame(user, gameRequestDto).makeDto();
    }

    @PostMapping("/games/{id}/join")
    public PlayerDto joinGame(@AuthenticationPrincipal User user, @PathVariable int gameId) {

        return gameService.joinNewPlayer(user, gameId).makeDto();
    }

    @PostMapping("/games/{id}/start")
    public GameDto startGame(@AuthenticationPrincipal User user, @PathVariable int gameId) {

        return gameService.startGame(user, gameId).makeDto();
    }

    @PostMapping("/games/{id}/secret")
    public GameDto createSecret(@AuthenticationPrincipal User user, @PathVariable int gameId,
                             @RequestBody SecretRequestDto secretRequestDto) {

        return gameService.makeSecret(user, gameId, secretRequestDto).makeDto();
    }

    @PostMapping("/games/{id}/move")
    public GameDto createMove(@AuthenticationPrincipal User user, @PathVariable int gameId,
                             @RequestBody MoveRequestDto secretRequestDto) {

        return gameService.makeMove(user, gameId, secretRequestDto).makeDto();
    }

    @PostMapping("/games/{id}/vote")
    public GameDto createVote(@AuthenticationPrincipal User user, @PathVariable int gameId,
                           @RequestBody VoteRequestDto voteRequestDto) {

        return gameService.makeVote(user, gameId, voteRequestDto).makeDto();
    }

    @PostMapping("/games/{id}/next")
    public GameDto createNextRound(@AuthenticationPrincipal User user, @PathVariable int gameId) {

        return gameService.startNextRound(user, gameId).makeDto();
    }

    @PostMapping("/games/{id}/delete")
    public GameDto removeGame(@AuthenticationPrincipal User user, @PathVariable int gameId) {

        return gameService.deleteGame(user, gameId).makeDto();
    }
}
