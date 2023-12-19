package com.fufa.fariska.entities;

import com.fufa.fariska.entities.enums.Avatar;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@SuperBuilder
@Data
public class Player {
    private User user;
    private int gameId;
    private Avatar avatar;
    private Set<Card> handCards;
    private boolean isLeader;
    private int points;
}
