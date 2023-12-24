package com.fufa.fariska.services;

import com.fufa.fariska.entities.Card;
import org.testcontainers.shaded.com.google.common.cache.Cache;

import java.util.ArrayList;
import java.util.List;

public class GameServiceTests {

    public static void main(String[] args) {

        List<Card> cards = new ArrayList<>(9);
        cards.add(0, Card.builder().name("Test").build());
        System.out.println(cards);
    }
}
