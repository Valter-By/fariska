package com.fufa.fariska.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class MoveRequestDto {
    private Integer round;
    private Integer playerPlace;
    private Integer cardHandNumber;
}