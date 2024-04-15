package com.fufa.fariska.entity;

import com.fufa.fariska.dto.RoundDto;
import com.fufa.fariska.entity.enums.RoundStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuperBuilder
@Data
public class Round {
    private Integer gameId;
    private Integer number;
    private Player leader;
    private Card leaderCard;
    private String secret;
    private List<TableCard> tableCards;
    private int numberMoves;
    private int numberVotes;
    private int[] playerPoints;
    private RoundStatus status;
    private boolean lastRound;

    public void putCardOnTable(Integer place, Card card, boolean isLeaderCard) {
        tableCards.set(place, TableCard.builder()
                .playerPlace(place)
                .isSecretCard(isLeaderCard)
                .card(card)
                .votes(new ArrayList<>(9))
                .build());
        numberMoves++;
        System.out.println("putCardOnTable" + card + "- from -" + place);
    }

    public Integer findNumberLeaderCard() {
        Integer ans = 0;
        for (TableCard tableCard : tableCards) {
            if (tableCard.isSecretCard()) {
                return ans;
            }
            ans++;
        }
        return -1;
    }

    public Integer findNumberNoLeaderCardNoPlayer(Integer playerPlace) {
        Integer ans = 0;
        for (TableCard tableCard : tableCards) {
            if (!tableCard.isSecretCard() && tableCard.getPlayerPlace() != playerPlace) {
                return ans;
            }
            ans++;
        }
        return -1;
    }

    public boolean didPlayerMakeMove(Integer place) {
        return !(tableCards.get(place) == null);
    }

    public boolean didPlayerMakeVote(Integer place) {
        //check if player made vote
        return false;
    }

    public void endMoveAndStartVoting() {

        Collections.shuffle(tableCards);
        status = RoundStatus.VOTING;

        System.out.println("After -> endMoveAndStartVoting " + this.tableCards);
    }

    public int[] endVoteAndCalcPoints() {

        this.status = RoundStatus.POINTS_CALC;
        int number = tableCards.size();
        playerPoints = new int[number];

        Integer leaderCardNumber = findNumberLeaderCard();
        int numberGuessedLeaderCard = tableCards.get(leaderCardNumber).getVotes().size();

        for (TableCard tableCard : tableCards) {
            if (tableCard.isSecretCard()) {
                List<Integer> guessedSecret = tableCard.getVotes();
                for (Integer place : guessedSecret) {
                    playerPoints[place] += 3;
                }
            } else {
                playerPoints[tableCard.getPlayerPlace()] += tableCard.getVotes().size();
            }
        }
        if (numberGuessedLeaderCard > 0 && numberGuessedLeaderCard < number - 1) {
            playerPoints[tableCards.get(leaderCardNumber).getPlayerPlace()] += 3 + numberGuessedLeaderCard;
        }
        return playerPoints;
    }

    public RoundDto makeDto() {
        return RoundDto.builder()
                .gameId(gameId)
                .number(number)
                .leaderCard(leaderCard)
                .tableCards(tableCards)
                .secret(secret)
                .numberMoves(numberMoves)
                .numberVotes(numberVotes)
                .playerPoints(playerPoints)
                .status(status)
                .lastRound(lastRound)
                .build();
    }
}
