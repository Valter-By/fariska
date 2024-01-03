package com.fufa.fariska.entities;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@SuperBuilder
@Data
public class Pack {
    private int id;
    private int ownerId;
    private List<Card> cards;
    private String name;
}
