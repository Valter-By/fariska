package com.fufa.fariska.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
public class Pack {
    private int id;
    private int ownerId;
    private List<Card> cards;
    private String name;
}
