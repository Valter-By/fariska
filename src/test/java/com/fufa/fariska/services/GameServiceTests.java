package com.fufa.fariska.services;

import com.fufa.fariska.dto.GameRequestDto;
import com.fufa.fariska.entities.*;
import com.fufa.fariska.entities.enums.GameStatus;
import com.fufa.fariska.repositories.PackRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.testcontainers.shaded.com.google.common.cache.Cache;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(Enclosed.class)
public class GameServiceTests {

    static GameService gameService;
    static GameRequestDto gameRequestDto;
    static Set<Integer> packsId;
    static Set<Pack> packs;
    static User user;


    public static void main(String[] args) {

        List<Card> cards = new ArrayList<>(9);
        cards.add(0, Card.builder().name("Test").build());

        System.out.println(new Move[9]);
    }

    @BeforeEach
    static void makeData() {
        Set<Card> cards1 = new HashSet<>();
        Set<Card> cards2 = new HashSet<>();
        for (int i = 0; i < 50; i++) {
            cards1.add(Card.builder()
                    .name("one-" + i)
                    .build());
            cards2.add(Card.builder()
                    .name("two-" + i)
                    .build());
        }

        packsId = new HashSet<>(List.of(1, 2));
        gameRequestDto = GameRequestDto.builder()
                .packsId(packsId)
                .build();
        user = User.builder()
                .name("Fufa")
                .id(1)
                .build();
        packs = new HashSet<>();
        packs.add(Pack.builder()
                .id(1)
                .cards(cards1)
                .build());
        packs.add(Pack.builder()
                .id(2)
                .cards(cards2)
                .build());
    }

    public static class MakeGameTests {
        @Test
        public void shouldMakeNewGameWhenAllIsGood() {
            PackRepository packRepository = Mockito.mock(PackRepository.class);
            Mockito.when(packRepository.getPacks(packsId)).thenReturn(packs);
            Game game = gameService.makeNewGame(gameRequestDto, user);
//            Game expectedGame = Game.builder()
//                    .id(1)
//                    .create_time(Instant.now())
//                    .creator(user)
//                    .status(GameStatus.WAITING_FOR_PLAYERS)
//                    .packsId(packsId)
//                    .cards(cards)
//                    .build();
            Assertions.assertEquals(1, game.getPlayers().size());
            Assertions.assertEquals(100, game.getCards().size());
//            Assertions.assertEquals(, game.getCards());
            Assertions.assertEquals(GameStatus.WAITING_FOR_PLAYERS, game.getStatus());
            Assertions.assertEquals(new HashSet<>(List.of(1, 2)), game.getPacksId());
            Assertions.assertEquals(user, game.getCreator());

        }
    }

}
