package com.fufa.fariska.entities;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class User {
    private int id;
    private String gmail;
    private String name;
}
