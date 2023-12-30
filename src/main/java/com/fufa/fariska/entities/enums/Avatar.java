package com.fufa.fariska.entities.enums;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public enum Avatar {

    RED_CAR,
    YELLOW_SUB,
    PINK_ELEPHANT,
    BLACK_CAT;

    public static LinkedList<Avatar> getAll() {
        Avatar[] avatars = values();
        LinkedList<Avatar> list = new LinkedList<>(List.of(avatars));
        Collections.shuffle(list);
        return list;
    }
}
