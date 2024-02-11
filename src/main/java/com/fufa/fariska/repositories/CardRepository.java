package com.fufa.fariska.repositories;

import com.fufa.fariska.entities.Card;
import com.fufa.fariska.entities.Pack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.*;

//@Repository
//@RequiredArgsConstructor

public class CardRepository {

    private final List<LinkedList<Card>> allPacksCard = new ArrayList<>(); //may be make Map

    {
        makeFirstPack();
        makeSecondPack();
    }

    public void makeFirstPack() {                   //may be make method for any standard Pack
//        if (allPacksCard.size() )
        LinkedList<Card> firstPack = new LinkedList<>();
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

    public void makeSecondPack() {                   //may be make method for any standard Pack
        LinkedList<Card> secondPack = new LinkedList<>();
        for (int i = 1; i <= 100; i++) {
            secondPack.add(Card.builder()
                    .packId(2)
                    .number(i)
                    .name("Second - " + i)
                    .picture(new File("/resources/pictures/2/picture_2_" + i + ".jpg"))
                    .build());
        }
        allPacksCard.add(secondPack);
    }
    public Card getCard(int packId, int cardNumber) {
        return allPacksCard.get(packId).get(cardNumber);
    }

    public LinkedList<Card> getOnePackCards(int packId) {
        return allPacksCard.get(packId);
    }

}
