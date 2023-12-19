package com.fufa.fariska.entities.enums;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Avatar {

    RED_CAR,
    YELLOW_SUB,
    PINK_ELEPHANT,
    BLACK_CAT;

    public static List<Avatar> getAll() {
        Avatar[] avatars = values();
        List<Avatar> list = List.of(avatars);
        Collections.shuffle(list);
        return list;
    }
}
