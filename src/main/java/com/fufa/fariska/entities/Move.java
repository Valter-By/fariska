package com.fufa.fariska.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.File;

@SuperBuilder
@Data
@NoArgsConstructor
public class Move {

    private int playerPlace;
    private Card card;
}
