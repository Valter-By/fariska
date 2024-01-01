package com.fufa.fariska.repositories;

import com.fufa.fariska.entities.Pack;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PackRepository {

    private List<Pack> allPacks = new ArrayList<>();

    public void makeFirstPack() {
        allPacks.add(1, Pack.builder()
                        .id(1)
                        .ownerId(1)
                        .cards(new LinkedHashSet<>()) //make fnc to generate int 1...100 or take from CardRepository
                        .name("First")
                .build());
    }

    public Set<Pack> getPacks(Set<Integer> packsId) {
        Set<Pack> ans = new HashSet<>();
        for (Integer i : packsId) {
            ans.add(allPacks.get(i));
        }
        return ans;
    }
}
