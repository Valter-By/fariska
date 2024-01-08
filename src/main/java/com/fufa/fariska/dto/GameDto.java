package com.fufa.fariska.dto;

import com.fufa.fariska.entities.Player;
import com.fufa.fariska.entities.User;
import com.fufa.fariska.entities.enums.GameStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;

@SuperBuilder
@Data
public class GameDto {
    private long id;
    private Instant create_time;
    private GameStatus status;
    private List<Player> players;
    private int currentRound;
    private int leader;
}
