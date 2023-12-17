package com.fufa.fariska.entities;

import com.fufa.fariska.entities.enums.RoundStatus;

import java.util.Map;

public class Round {
    private Game game;
    private int number;
    private Player leader;
    private Card leaderCard;
    private String secret;
    private Map<Integer, Card> playerCards;
    private Map<Integer, Card> playerVotes;
    private Map<Integer, Integer> playerPoints;
    private RoundStatus status;
}
