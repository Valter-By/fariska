package com.fufa.fariska.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class VoteRequestDto {
    private Integer round;
    private Integer playerPlace;
    private Integer cardTableNumber;
}