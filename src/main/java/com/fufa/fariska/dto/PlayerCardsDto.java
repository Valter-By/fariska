package com.fufa.fariska.dto;

import com.fufa.fariska.entities.Card;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
public class PlayerCardsDto {
    private List<Card> handCards;
}
