package com.fufa.fariska.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Data
@NoArgsConstructor
public class Move {

    private int playerPlace;
    private Card card;

    public static List<Move> makeEmptyTableCards(int n) {
        ArrayList<Move> list = new ArrayList<>(n + 1);
        for (int i = 0; i < n; i++) {
            list.add(null);
        }
        return list;
    }
}
