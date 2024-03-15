package com.fufa.fariska.dto;

import com.fufa.fariska.entities.Card;
import com.fufa.fariska.entities.Player;
import com.fufa.fariska.entities.TableCard;
import com.fufa.fariska.entities.enums.RoundStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collections;
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
