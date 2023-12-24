package com.fufa.fariska.entities;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.File;

@SuperBuilder
@Data
public class Move {

    private int placePlayer;
    private Card card;
}
