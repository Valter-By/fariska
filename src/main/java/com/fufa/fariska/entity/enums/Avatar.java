package com.fufa.fariska.entity.enums;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public enum Avatar {

    RED_CAR,
    YELLOW_SUB,
    PINK_ELEPHANT,
    BLACK_CAT,
    WHITE_SNOW,
    BLUE_STAR,
    PURPLE_MOON,
    GREEN_APPLE,
    BROWN_CAKE;

    public static LinkedList<Avatar> getAll() {
        Avatar[] avatars = values();
        LinkedList<Avatar> list = new LinkedList<>(List.of(avatars));
        Collections.shuffle(list);
        return list;
    }
}
