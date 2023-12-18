package com.fufa.fariska.dto;

import com.fufa.fariska.entities.User;
import com.fufa.fariska.entities.enums.GameStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder
@Data
public class GameDto {
    private long id;
    private Instant create_time;
    private User creator;
    private GameStatus status;
    private int[] players;
    private int currentRound;
}
