package com.fufa.fariska.repositories;

import com.fufa.fariska.entities.Card;
import com.fufa.fariska.entities.Pack;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.*;

@Repository
public class CardRepository {

    private static List<List<Card>> allPacksCard = new ArrayList<>();
    static {
        List<Card> firstPack = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            firstPack.add(Card.builder()
                            .packId(1)
                            .number(i)
                            .name("First - " + i)
                            .picture(new File("/resources/pictures/1/picture_1_" + i + ".jpg"))
                    .build());
        }
        allPacksCard.add(firstPack);
    }

    public Card getCard(int packId, int cardNumber) {
        return allPacksCard.get(packId).get(cardNumber);
    }

}
