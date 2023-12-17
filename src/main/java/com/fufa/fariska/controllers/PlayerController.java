package com.fufa.fariska.controllers;

import com.fufa.fariska.entities.Player;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PlayerController {
    @GetMapping("/players/{gameId}")
    public List<Player> getAllPlayers() {
        return new ArrayList<>();
    }

    @GetMapping("/players/{gameId}/{userId}")
    public String getPlayer(@PathVariable int gameId, @PathVariable int userId) {
        return "Player " + userId + " from Game " + gameId;
    }

    @PostMapping("/players")
    public Player createPlayer(@RequestBody Player player) {
        return new Player();
    }
}
