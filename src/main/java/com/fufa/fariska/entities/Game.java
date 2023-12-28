package com.fufa.fariska.entities;

import com.fufa.fariska.entities.enums.Avatar;
import com.fufa.fariska.entities.enums.GameStatus;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@SuperBuilder
@Data
public class Game {
    private long id;
    private Instant create_time;
    private User creator;
    private GameStatus status;
    private List<Player> players;
    private Round currentRound;
    private int leader;
    private Set<Integer> packsId;
    private List<Card> cards;
    private LinkedList<Avatar> freeAvatars;

    public Avatar getFreeAvatar() {
        return freeAvatars.removeLast();
    }

    public void dealCards() {
        for(Player player : players) {
            player.setHandCards(this.takeSomeCards(6));
        }
    }

    public List<Card> takeSomeCards(int n) {
        LinkedList<Card> ans = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            ans.add(cards.remove(i));
        }
        return ans;
    }

    public Card takeOneCard() {
        return cards.remove(cards.size() - 1);
    }

    public void addAllPoints(int[] pointsArray) {
        int i = 1;
        for (Player player : players) {
            player.addPoints(pointsArray[i++]);
        }
    }


}
