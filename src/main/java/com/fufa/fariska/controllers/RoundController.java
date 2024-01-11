package com.fufa.fariska.controllers;

import com.fufa.fariska.entities.Round;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoundController {
    
    @GetMapping("/round/{gameId}/{roundNumber}")
    public String getRound(@PathVariable int gameId, @PathVariable int roundNumber) {
        return "Round " + roundNumber + " in Game " + gameId;
    }

//    @PostMapping("/rounds")
//    public Round createRound(@RequestBody Round round) {
//        return new Round();
//    }



}
