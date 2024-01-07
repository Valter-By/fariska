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
    boolean isSecretCard;
    Card card;
    List<Integer> votes;

    public static List<TableCard> makeEmptyTableCards(int n) {
        ArrayList<TableCard> list = new ArrayList<>(n + 1);
        for (int i = 0; i < n; i++) {
            list.add(null);
        }
        System.out.println("makeEmptyTableCards" + list.size());
        return list;
    }
}
