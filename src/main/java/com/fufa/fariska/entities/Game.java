package com.fufa.fariska.entities;

import java.time.Instant;
import java.util.Set;

public class Game {
    long id;
    Instant create_time;
    User creator;
    int statusGame;
    int[] players;
    int currentRound;
    int leader;
    Set<Integer> packs;

    Set<Long> cards;

}
