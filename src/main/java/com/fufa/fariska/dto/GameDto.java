package com.fufa.fariska.dto;

import com.fufa.fariska.entity.enums.GameStatus;
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
    private List<PlayerDto> players;
    private Integer currentRound;
    private Integer leader;
}
