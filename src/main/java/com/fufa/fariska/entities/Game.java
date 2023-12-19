package com.fufa.fariska.entities;

import com.fufa.fariska.entities.enums.Avatar;
import com.fufa.fariska.entities.enums.GameStatus;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@SuperBuilder
@Data
public class Game {
    private long id;
    private Instant create_time;
    private User creator;
    private GameStatus status;
    private List<Player> players;
    private int currentRound;
    private int leader;
    private Set<Integer> packsId;
    private List<Card> cards;
    private LinkedList<Avatar> freeAvatars;

    public Avatar getFreeAvatar() {
        return freeAvatars.removeLast();
    }

}
