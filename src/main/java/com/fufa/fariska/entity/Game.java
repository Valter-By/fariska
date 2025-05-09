package com.fufa.fariska.entity;

import com.fufa.fariska.dto.GameDto;
import com.fufa.fariska.entity.enums.Avatar;
import com.fufa.fariska.entity.enums.GameStatus;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import static com.fufa.fariska.utils.MakeListDto.makeListPlayerDto;

@SuperBuilder
@Data
public class Game {

    public static Integer totalGames = 0;

    private Integer id;
    private Instant createTime;
    private GameUser creator;
    private GameStatus status;
    private List<Player> players;
    private Round currentRound;
    private Integer leader;
//    private Set<Integer> packsId;
    private Integer packsId;
    private LinkedList<Card> cards;
    private LinkedList<Avatar> freeAvatars;

    public Avatar getFreeAvatar() {
        return freeAvatars.removeLast();
    }

    public void dealCards() {
        for(Player player : players) {
            player.setHandCards(this.takeSomeCards(6));
        }
    }

    public LinkedList<Card> takeSomeCards(int n) {

        int m = cards.size();
        if (m <= n) {
            currentRound.setLastRound(true);
            if (m == 0) {
                return null;
            }
            n = m;
        }
        LinkedList<Card> ans = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            ans.add(cards.removeLast());
        }
        return ans;
    }

    public Card takeOneCard() {
        if (cards.isEmpty()) {
            currentRound.setLastRound(true); //depends on the playing type
            return null;
        }
        return cards.removeLast();
    }

    public void addAllPoints(int[] pointsArray) {
        int i = 0;
        for (Player player : players) {
            player.addPoints(pointsArray[i++]);
        }
    }

    public int getNextLeader() {
        if (leader < players.size() - 1) {
            leader += 1;
        } else {
            leader = 0;
        }
        return leader;
    }

    public Game finish() {
        status = GameStatus.GAME_OVER;
        return this;
    }

    public GameDto makeDto() {
        return GameDto.builder()
                .id(id)
                .create_time(createTime)
                .status(status)
                .players(makeListPlayerDto(players))
                .currentRound(currentRound == null ? 0 : currentRound.getNumber())
                .leader(leader)
                .build();
    }
}
