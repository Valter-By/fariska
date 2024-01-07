package com.fufa.fariska.entities;

import com.fufa.fariska.entities.enums.RoundStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuperBuilder
@Data
public class Round {
    private int gameId;
    private int number;
    private Player leader;
    private Card leaderCard;
    private int leaderCardNumber;
    private String secret;
    private List<TableCard> tableCards;
    private int[] playerMoves; // may be - to TableCard
    private int numberMoves;
    private int[] playerVotes; // may be - to TableCard
    private int numberVotes;
    private int[] playerPoints;
    private RoundStatus status;
    private boolean lastRound;

    public void putCardOnTable(int place, Card card, boolean isLeaderCard) {
        tableCards.add(TableCard.builder()
                .playerPlace(place)
                .isSecretCard(isLeaderCard)
                .card(card)
                .votes(new ArrayList<>(9))
                .build());
        numberMoves++;
        System.out.println("putCardOnTable" + card + "- from -" + place);
    }

    public int findNumberLeaderCard() {
        int ans = 0;
        for (TableCard tableCard : tableCards) {
            ans++;
            if (tableCard.getCard().equals(leaderCard)) {
                return ans;
            }
        }
        return 0;
    }

    public boolean didPlayerMakeMove(int place) {
        //check if player made move
        return false;
    }

    public boolean didPlayerMakeVote(int place) {
        //check if player made vote
        return false;
    }

    public void endMoveAndStartVoting() {

        System.out.println("endMoveAndStartVoting " + this.getTableCards());

        Collections.shuffle(tableCards);
//        int[] movies = new int[round.getTableCards().size() + 1];
//        int cardPosition = 1;
//        for (TableCard tableCard : tableCards) {
//            movies[tableCard.getPlayerPlace()] = cardPosition++;
//        }
//        round.setPlayerMoves(movies);
//
//        int[] votes = new int[round.getTableCards().size() + 1];
//        round.setPlayerVotes(votes);

        status = RoundStatus.VOTING;
    }


}
