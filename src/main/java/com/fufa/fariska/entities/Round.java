package com.fufa.fariska.entities;

import com.fufa.fariska.entities.enums.RoundStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@SuperBuilder
@Data
public class Round {
    private int gameId;
    private int number;
    private Player leader;
    private Card leaderCard;
    private String secret;
    private Map<Integer, Card> playerCards;
    private Map<Integer, Card> playerVotes;
    private Map<Integer, Integer> playerPoints;
    private RoundStatus status;
}
