package com.fufa.fariska.entities;

import com.fufa.fariska.entities.enums.Avatar;

import java.util.Set;

public class Player {
    private User user;
    private Game game;
    private Avatar avatar;
    private Set<Card> handCards;
    private boolean isLeader;
    private int points;
}
