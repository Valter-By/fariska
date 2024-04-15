package com.fufa.fariska.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Data
public class TableCard {

    private Integer playerPlace;
    private boolean isSecretCard;
    private Card card;
    private List<Integer> votes;

    public static List<TableCard> makeEmptyTableCards(int n) {
        ArrayList<TableCard> list = new ArrayList<>(9);
        for (int i = 0; i < n; i++) {
            list.add(null);
        }
        return list;
    }
}
