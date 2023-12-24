package com.fufa.fariska.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class MoveRequestDto {
    private int round;
    private int playerPlace;
    private int cardHandNumber;
}