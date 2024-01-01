package com.fufa.fariska.services;


import com.fufa.fariska.dto.GameRequestDto;
import com.fufa.fariska.entities.Card;
import com.fufa.fariska.entities.Pack;
import com.fufa.fariska.entities.User;
import com.fufa.fariska.repositories.CardRepository;
import com.fufa.fariska.repositories.PackRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class GameServiceTest {

    //    @Mock
    public GameService gameService;

    @Mock
    public CardRepository cardRepository;

//    @Mock
//    public PackRepository packRepository;

    //    static GameService gameService;
    static GameRequestDto gameRequestDto;
    static Set<Integer> packsId;
    static Set<Pack> packs;
    static User user;

    @BeforeEach
    public void makeData() {

        Set<Card> cards1 = new HashSet<>();
        Set<Card> cards2 = new HashSet<>();

        for (int i = 0; i < 50; i++) {
            cards1.add(Card.builder()
                    .packId(1)
                    .name("one-" + i)
                    .build());
            cards2.add(Card.builder()
                    .packId(2)
                    .name("two-" + i)
                    .build());
        }

//        cardRepository = new CardRepository();
//        cardRepository.makeFirstPack();

        Set<Integer> packsId = new HashSet<>(List.of(1, 2));
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


    @Nested
    public class MakeNewGameTest {

        @Test
        public void shouldMakeNewGameWhenAllIsGood() {

            int a = 1;


            PackRepository packRepository = Mockito.mock(PackRepository.class);
            Mockito.when(packRepository.getPacks(packsId)).thenReturn(packs);
            System.out.println(packRepository.getPacks(packsId));

            Assertions.assertEquals(1, a);
        }
    }
}


//    public static void main(String[] args) {
//
//        List<Card> cards = new ArrayList<>(9);
//        cards.add(0, Card.builder().name("Test").build());
//
//        System.out.println(new Move[9]);
//    }


//            PackRepository packRepository = Mockito.mock(PackRepository.class);
//            Mockito.when(packRepository.getPacks(packsId)).thenReturn(packs);
//            Game game = gameService.makeNewGame(gameRequestDto, user);
////            Game expectedGame = Game.builder()
////                    .id(1)
////                    .create_time(Instant.now())
////                    .creator(user)
////                    .status(GameStatus.WAITING_FOR_PLAYERS)
////                    .packsId(packsId)
////                    .cards(cards)
////                    .build();
//            Assertions.assertEquals(1, game.getPlayers().size());
//            Assertions.assertEquals(100, game.getCards().size());
////            Assertions.assertEquals(, game.getCards());
//            Assertions.assertEquals(GameStatus.WAITING_FOR_PLAYERS, game.getStatus());
//            Assertions.assertEquals(new HashSet<>(List.of(1, 2)), game.getPacksId());
//            Assertions.assertEquals(user, game.getCreator());
