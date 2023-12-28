package com.fufa.fariska.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class VoteRequestDto {
    private int round;
    private int playerPlace;
    private int cardTableNumber;
}