package com.fufa.fariska.entity;

import com.fufa.fariska.dto.PlayerCardsDto;
import com.fufa.fariska.dto.PlayerDto;
import com.fufa.fariska.entity.enums.Avatar;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
public class Player {
    private GameUser user;
    private int gameId;
    private int place;
    private Avatar avatar;
    private List<Card> handCards;
    private boolean isLeader;
    private int points;

    public Card putOneCard(int number) {
        return handCards.remove(number);
    }

    public void takeOneCard(Card card) {
        handCards.add(card);
    }

    public void addPoints(int somePoints) {
        points += somePoints;
    }

    public PlayerDto makeDto() {
        return PlayerDto.builder()
                .gameId(gameId)
                .place(place)
                .avatar(avatar)
                .points(points)
                .build();
    }

    public PlayerCardsDto makePlayerCardsDto() {
        return PlayerCardsDto.builder()
                .handCards(handCards)
                .build();
    }
}
