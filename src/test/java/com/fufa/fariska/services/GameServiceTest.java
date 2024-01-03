package com.fufa.fariska.services;


import com.fufa.fariska.dto.GameRequestDto;
import com.fufa.fariska.entities.Card;
import com.fufa.fariska.entities.Game;
import com.fufa.fariska.entities.Pack;
import com.fufa.fariska.entities.User;
import com.fufa.fariska.entities.enums.Avatar;
import com.fufa.fariska.entities.enums.GameStatus;
import com.fufa.fariska.repositories.CardRepository;
import com.fufa.fariska.repositories.PackRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;


public class GameServiceTest {

    //    @Mock
    public GameService gameService;

    @Mock
    public CardRepository cardRepository;

//    @Mock
    PackRepository packRepository;


    //    static GameService gameService;
    static GameRequestDto gameRequestDto;
    static Set<Integer> packsId;
    static List<Pack> packs;

    static List<Card> cards1;
    static List<Card> cards2;
    static User user;

    @BeforeEach
    public void makeData() {

        cards1 = new ArrayList<>();
        cards2 = new ArrayList<>();

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

        packsId = new HashSet<>(List.of(1, 2));
        gameRequestDto = GameRequestDto.builder()
                .packsId(packsId)
                .build();
        user = User.builder()
                .name("Fufa")
                .id(1)
                .build();
        packs = new ArrayList<>();
        packs.add(null);
        packs.add(1, Pack.builder()
                .id(1)
                .cards(cards1)
                .build());
        packs.add(2, Pack.builder()
                .id(2)
                .cards(cards2)
                .build());

        packRepository = new PackRepository(packs);
//        packRepository = Mockito.mock(PackRepository.class);
//        Mockito.when(packRepository.getPacks(packsId)).thenReturn(packs);
        gameService = new GameService(null, packRepository, new HashMap<>());
    }


    @Nested
    public class MakeNewGameTest {

        @Test
        public void shouldMakeNewGameWhenAllIsGood() {

            Game game = gameService.makeNewGame(gameRequestDto, user);

            Assertions.assertEquals(gameService.findAllCreatedGames().get(1), game);
            Assertions.assertEquals(1, gameService.findAllCreatedGames().size());
            Assertions.assertEquals(1, game.getId());
            Assertions.assertEquals(user, game.getCreator());
            Assertions.assertEquals(GameStatus.WAITING_FOR_PLAYERS, game.getStatus());
            Assertions.assertEquals(user, game.getPlayers().get(0).getUser());
            Assertions.assertEquals(packsId, game.getPacksId());
            Assertions.assertEquals(cards1.size() + cards2.size(), game.getCards().size());
            Assertions.assertTrue(game.getCards().containsAll(cards1));
            Assertions.assertTrue(game.getCards().containsAll(cards2));
            Assertions.assertFalse(game.getFreeAvatars().contains(game.getPlayers().get(0).getAvatar()));
            Assertions.assertEquals(Avatar.values().length - 1, game.getFreeAvatars().size());
            System.out.println();
        }
    }

    @Nested
    public class FindGameTest {

        @Test
        public void shouldFindTwoGamesWhenCreatedTwoOnce() {

            GameRequestDto gameRequestDto2 = GameRequestDto.builder()
                    .packsId(new HashSet<>(List.of(2)))
                    .build();
            User user2 = User.builder()
                    .name("Fariska")
                    .id(2)
                    .build();
            Game game1 = gameService.makeNewGame(gameRequestDto, user);
            Game game2 = gameService.makeNewGame(gameRequestDto2, user2);

            Assertions.assertEquals(gameService.findGame(1), game1);
            Assertions.assertEquals(gameService.findGame(2), game2);
        }
    }

    @Nested
    public class FindAllCreatedGamesTest {

        @Test
        public void shouldFindAllGamesWhenCreatedThreeOnce() {

            GameRequestDto gameRequestDto2 = GameRequestDto.builder()
                    .packsId(new HashSet<>(List.of(2)))
                    .build();
            GameRequestDto gameRequestDto3 = GameRequestDto.builder()
                    .packsId(new HashSet<>(List.of(1)))
                    .build();
            User user2 = User.builder()
                    .name("Fariska")
                    .id(2)
                    .build();
            Game game1 = gameService.makeNewGame(gameRequestDto, user);
            Game game2 = gameService.makeNewGame(gameRequestDto2, user2);
            Game game3 = gameService.makeNewGame(gameRequestDto3, user);
            HashMap<Integer, Game> expected = new HashMap<>();
            expected.put(3, game3);
            expected.put(1, game1);
            expected.put(2, game2);

            Assertions.assertEquals(expected, gameService.findAllCreatedGames());
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
