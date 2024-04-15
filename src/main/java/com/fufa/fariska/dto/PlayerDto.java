package com.fufa.fariska.dto;

import com.fufa.fariska.entity.enums.Avatar;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class PlayerDto {
    private Integer gameId; //is required?
    private int place;
    private Avatar avatar;
    private String nick;
    private int points;
}
