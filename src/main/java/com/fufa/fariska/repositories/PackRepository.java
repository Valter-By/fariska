package com.fufa.fariska.repositories;

import com.fufa.fariska.entities.Pack;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Array2DHashSet;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PackRepository {

    private final List<Pack> allPacks = new ArrayList<>();

    public void makeFirstPack() {
        allPacks.add(1, Pack.builder()
                        .id(1)
                        .ownerId(1)
                        .cards(new ArrayList<>()) //make fnc to generate int 1...100 or take from CardRepository
                        .name("First")
                .build());
    }

    public List<Pack> getPacks(Set<Integer> packsId) {
        List<Pack> ans = new ArrayList<>();
        for (Integer i : packsId) {
            ans.add(allPacks.get(i));
        }
        return ans;
    }
}
