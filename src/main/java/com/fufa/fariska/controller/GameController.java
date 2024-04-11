package com.fufa.fariska.controller;

import com.fufa.fariska.dto.*;
import com.fufa.fariska.entity.GameUser;
import com.fufa.fariska.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class GameController {

    public final GameService gameService;

    @GetMapping("/hello")
    public String hello() {
        return "<h2> Hey, everyone! </h2>";
    }

    @GetMapping("/admin")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin() {
        return "<h2> Hey, admin! </h2>";
    }

    @CrossOrigin
    @GetMapping("/games")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Set<Integer> getCreatedGames() {
        return gameService.findAllCreatedGames(); // may be return some game's info in Map by id
    }

    @CrossOrigin
    @GetMapping("/games/{id}")
    public GameDto getGame(@PathVariable("id") final int id) {
        return gameService.findGame(id).makeDto();
    }

    //get current round separately from a game
    @CrossOrigin
    @GetMapping("/games/{id}/round")
    public RoundDto getRound(@PathVariable("id") final int id) {
        return gameService.findGame(id).getCurrentRound().makeDto();
    }

    @CrossOrigin
    @GetMapping("/games/{id}/my-cards/{place}")
    public PlayerCardsDto getHandCards(@PathVariable("id") final int id, @PathVariable("place") final int place) {
        return gameService.findGame(id).getPlayers().get(place).makePlayerCardsDto();
    }

    @PostMapping("/games")
    public GameDto createGame(@Valid @RequestBody GameRequestDto gameRequestDto) { //@AuthenticationPrincipal User user // GameDto
        return gameService.makeNewGame(GameUser.builder().id(17).nickname("Nick").build(), gameRequestDto).makeDto();
    }

    @PostMapping("/games/{id}/join")
    public PlayerDto joinGame(@AuthenticationPrincipal GameUser user, @PathVariable int gameId) {

        return gameService.joinNewPlayer(user, gameId).makeDto();
    }

    @PostMapping("/games/{id}/start")
    public GameDto startGame(@AuthenticationPrincipal GameUser user, @PathVariable int gameId) {

        return gameService.startGame(user, gameId).makeDto();
    }

    @PostMapping("/games/{id}/secret")
    public GameDto createSecret(@AuthenticationPrincipal GameUser user, @PathVariable int gameId,
                                @RequestBody SecretRequestDto secretRequestDto) {

        return gameService.makeSecret(user, gameId, secretRequestDto).makeDto();
    }

    @PostMapping("/games/{id}/move")
    public GameDto createMove(@AuthenticationPrincipal GameUser user, @PathVariable int gameId,
                              @RequestBody MoveRequestDto secretRequestDto) {

        return gameService.makeMove(user, gameId, secretRequestDto).makeDto();
    }

    @PostMapping("/games/{id}/vote")
    public GameDto createVote(@AuthenticationPrincipal GameUser user, @PathVariable int gameId,
                              @RequestBody VoteRequestDto voteRequestDto) {

        return gameService.makeVote(user, gameId, voteRequestDto).makeDto();
    }

    @PostMapping("/games/{id}/next")
    public GameDto createNextRound(@AuthenticationPrincipal GameUser user, @PathVariable int gameId) {

        return gameService.startNextRound(user, gameId).makeDto();
    }

    @PostMapping("/games/{id}/delete")
    public GameDto removeGame(@AuthenticationPrincipal GameUser user, @PathVariable int gameId) {

        return gameService.deleteGame(user, gameId).makeDto();
    }
}
