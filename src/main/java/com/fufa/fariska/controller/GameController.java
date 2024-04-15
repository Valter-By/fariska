package com.fufa.fariska.controller;

import com.fufa.fariska.config.GameUserDetails;
import com.fufa.fariska.dto.*;
import com.fufa.fariska.entity.GameUser;
import com.fufa.fariska.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("game")
@RequiredArgsConstructor
public class GameController {

    private GameService gameService;

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
    @GetMapping("/all")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Set<Integer> getCreatedGames() {
        return gameService.findAllCreatedGames(); // may be return some game's info in Map by id
    }

    @CrossOrigin
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public GameDto getGame(@PathVariable("id") final Integer id) {
        return gameService.findGame(id).makeDto();
    }

    //get current round separately from a game
    @CrossOrigin
    @GetMapping("/round/{id}")
    public RoundDto getRound(@PathVariable("id") final Integer id) {
        return gameService.findGame(id).getCurrentRound().makeDto();
    }

    @CrossOrigin
    @GetMapping("/my-cards/{id}/{place}") //can make by AuthenticationPrincipal and to store user's places
    public PlayerCardsDto getHandCards(@PathVariable("id") final Integer id, @PathVariable("place") final Integer place) {
        return gameService.findGame(id).getPlayers().get(place).makePlayerCardsDto();
    }

    @PostMapping
    public GameDto createGame(@Valid @RequestBody GameRequestDto gameRequestDto,
                              @AuthenticationPrincipal GameUserDetails userDetails) { // GameDto
        return gameService.makeNewGame(userDetails.getUser(), gameRequestDto).makeDto(); //GameUser.builder().id(17).nickname("Nick").build()
    }

    @PostMapping("/join/{id}")
    public PlayerDto joinGame(@AuthenticationPrincipal GameUserDetails userDetails, @PathVariable("id") Integer gameId) {

        return gameService.joinNewPlayer(userDetails.getUser(), gameId).makeDto();
    }

    @PostMapping("/start/{id}")
    public GameDto startGame(@AuthenticationPrincipal GameUserDetails userDetails, @PathVariable("id") Integer gameId) {

        return gameService.startGame(userDetails.getUser(), gameId).makeDto();
    }

    @PostMapping("/secret/{id}")
    public GameDto createSecret(@AuthenticationPrincipal GameUserDetails userDetails, @PathVariable("id") Integer gameId,
                                @RequestBody SecretRequestDto secretRequestDto) {

        return gameService.makeSecret(userDetails.getUser(), gameId, secretRequestDto).makeDto();
    }

    @PostMapping("/move/{id}")
    public GameDto createMove(@AuthenticationPrincipal GameUserDetails userDetails, @PathVariable("id") Integer gameId,
                              @RequestBody MoveRequestDto secretRequestDto) {

        return gameService.makeMove(userDetails.getUser(), gameId, secretRequestDto).makeDto();
    }

    @PostMapping("/vote/{id}")
    public GameDto createVote(@AuthenticationPrincipal GameUserDetails userDetails, @PathVariable("id") Integer gameId,
                              @RequestBody VoteRequestDto voteRequestDto) {

        return gameService.makeVote(userDetails.getUser(), gameId, voteRequestDto).makeDto();
    }

    @PostMapping("/next/{id}")
    public GameDto createNextRound(@AuthenticationPrincipal GameUserDetails userDetails, @PathVariable("id") Integer gameId) {

        return gameService.startNextRound(userDetails.getUser(), gameId).makeDto();
    }

    @DeleteMapping("/{id}")
    public GameDto removeGame(@AuthenticationPrincipal GameUserDetails userDetails, @PathVariable("id") Integer gameId) {

        return gameService.deleteGame(userDetails.getUser(), gameId).makeDto();
    }
}
