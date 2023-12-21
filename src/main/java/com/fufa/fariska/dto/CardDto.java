package com.fufa.fariska.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.File;

@SuperBuilder
@Data
public class CardDto {
    private int packId;
    private int number;
}
