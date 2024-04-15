package com.fufa.fariska.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.File;

@SuperBuilder
@Data
public class Card {
    private Integer packId;
    private Integer number;
    private String name;
    private File picture;
}
