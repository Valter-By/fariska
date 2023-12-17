package com.fufa.fariska.controllers;

import com.fufa.fariska.entities.Card;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CardController {
    @GetMapping("/cards/{packId}")
    public List<Card> getAllCards() {
        return new ArrayList<>();
    }

    @GetMapping("/cards/{packId}/{number}")
    public String getCard(@PathVariable int packId, @PathVariable int number) {
        return "Card " + number + " from Pack " + packId;
    }

    @PostMapping("/cards")
    public Card createCard(@RequestBody Card card) {
        return new Card();
    }
}
