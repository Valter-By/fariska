package com.fufa.fariska.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
public class Pack {
    private Integer id;
    private Integer ownerId;
    private List<Card> cards;
    private String name;
}
