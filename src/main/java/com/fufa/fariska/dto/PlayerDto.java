package com.fufa.fariska.dto;

import com.fufa.fariska.entities.Card;
import com.fufa.fariska.entities.User;
import com.fufa.fariska.entities.enums.Avatar;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
public class PlayerDto {
    private int gameId; //is required?
    private int place;
    private Avatar avatar;
    private int points;
}
