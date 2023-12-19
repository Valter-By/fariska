package com.fufa.fariska.entities;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.File;

@SuperBuilder
@Data
public class Card {
    private int packId;
    private int number;
    private String name;
    private File picture;
}
