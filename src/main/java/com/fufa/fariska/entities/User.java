package com.fufa.fariska.entities;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class User {
    int id;
    String gmail;
    String name;
}
