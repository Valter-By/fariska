package com.fufa.fariska.entities;

import com.fufa.fariska.entities.enums.RoundStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@SuperBuilder
@Data
public class Round {
    private int gameId;
    private int number;
    private Player leader;
    private Card leaderCard;
    private String secret;
//    private Card[] playerMoves; - lost info about player place
    private Move[] playerMoves;
//    private List<Move> playerMoves;
    private List<Boolean> playersMoved; // may be not necessary
    private Map<Integer, Card> playerVotes;
    private Map<Integer, Integer> playerPoints;
    private RoundStatus status;

    public void putCardOnTable(int place, Card card) {
        Move[] moves = this.getPlayerMoves();
        moves[place] = Move.builder()
                .placePlayer(place)
                .card(card)
                .build();
    }


}
