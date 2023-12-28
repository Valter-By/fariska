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
    private int leaderCardNumber;
    private String secret;
//    private Card[] playerMoves; - lost info about player place
//    private Move[] playerMoves;
    private int[] playerMoves;
    private List<Move> tableCards;
    private int numberMoves;
    //    private List<Boolean> playersMoved; // may be not necessary
    private int[] playerVotes;
    private int numberVotes;
//    private Map<Integer, Card> playerVotes;
    private int[] playerPoints;
    private RoundStatus status;

    public void putCardOnTable(int place, Card card) {
        tableCards.add(place, Move.builder()
                .playerPlace(place)
                .card(card)
                .build());
        numberMoves++;
    }

    public int findNumberLeaderCard() {
        int ans = 0;
        for (Move move : tableCards) {
            ans++;
            if (move.getCard().equals(leaderCard)) {
                return ans;
            }
        }
        return ans;
    }


}
