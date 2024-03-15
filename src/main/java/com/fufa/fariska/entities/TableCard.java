package com.fufa.fariska.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Data
public class TableCard {

    private int playerPlace;
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
