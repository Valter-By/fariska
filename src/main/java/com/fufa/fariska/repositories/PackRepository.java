package com.fufa.fariska.repositories;

import com.fufa.fariska.entities.Card;
import com.fufa.fariska.entities.Pack;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Array2DHashSet;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class PackRepository {

    private final List<Pack> allPacks = new ArrayList<>();

    {
        //basic packs
        LinkedList<Card> cards1 = new LinkedList<>();
        LinkedList<Card> cards2 = new LinkedList<>();
        for (int i = 0; i < 50; i++) {
            cards1.add(Card.builder()
                    .packId(1)
                    .number(i)
                    .name("First-" + i)
                    .picture(new File("/resources/pictures/1/picture_1_" + i + ".jpg"))
                    .build());
            cards2.add(Card.builder()
                    .packId(2)
                    .number(i)
                    .name("Second-" + i)
                    .picture(new File("/resources/pictures/2/picture_2_" + i + ".jpg"))
                    .build());
        }
        for (int i = 0; i < 2; i++) {
            allPacks.add(Pack.builder()
                    .id(i)
                    .ownerId(0)
                    .cards(i == 0 ? cards1 : cards2)
                    .name(i == 0 ? "First" : "Second")
                    .build());
        }
    }

    public Pack getPack(Integer packId) {
        return allPacks.get(packId);
    }

    public List<Pack> getPacks(Set<Integer> packsId) {
        List<Pack> ans = new ArrayList<>();
        for (Integer i : packsId) {
            ans.add(allPacks.get(i));
        }
        return ans;
    }
}
