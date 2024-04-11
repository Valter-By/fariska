package com.fufa.fariska.dto;

import com.fufa.fariska.entity.Card;
import com.fufa.fariska.entity.TableCard;
import com.fufa.fariska.entity.enums.RoundStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
public class RoundDto {
    private int gameId;
    private int number;
    private Card leaderCard;
    private List<TableCard> tableCards;
    private String secret;
    private int numberMoves;
    private int numberVotes;
    private int[] playerPoints;
    private RoundStatus status;
    private boolean lastRound;
}
