package com.fufa.fariska.services;


import com.fufa.fariska.dto.GameRequestDto;
import com.fufa.fariska.entities.*;
import com.fufa.fariska.entities.enums.Avatar;
import com.fufa.fariska.entities.enums.GameStatus;
import com.fufa.fariska.entities.enums.RoundStatus;
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

//    @Mock
//    public CardRepository cardRepository;

//    @Mock
    PackRepository packRepository;

    static GameRequestDto gameRequestDto;
    static Set<Integer> packsId;
    static List<Pack> packs;

    static List<Card> cards1;
    static List<Card> cards2;
    static User user;

    @BeforeEach
    public void makeData() {
        System.out.println("BeforeEach makeData() method called");

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
            Assertions.assertEquals(100, game.getCards().size());
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

    @Nested
    public class JoinNewPlayerTest {

        @Test
        public void shouldJoinNewPlayerWhenHaveGame() {

            GameRequestDto gameRequestDto2 = GameRequestDto.builder()
                    .packsId(new HashSet<>(List.of(2)))
                    .build();
            User user2 = User.builder()
                    .name("Fariska")
                    .id(2)
                    .build();
            User newUser = User.builder()
                    .name("New")
                    .id(3)
                    .build();
            Game game1 = gameService.makeNewGame(gameRequestDto, user);
            Game game2 = gameService.makeNewGame(gameRequestDto2, user2);

            gameService.joinNewPlayer(newUser, 1);

            List<Player> players1 = game1.getPlayers();
            List<Player> players2 = game2.getPlayers();

            Assertions.assertEquals(2, players1.size());
            Assertions.assertEquals(1, players2.size());
            Assertions.assertEquals(newUser, players1.get(1).getUser());
            Assertions.assertEquals(1, players1.get(1).getGameId());
            Assertions.assertFalse(game1.getFreeAvatars().contains(game1.getPlayers().get(1).getAvatar()));
            Assertions.assertEquals(Avatar.values().length - 2, game1.getFreeAvatars().size());
        }
    }

    @Nested
    public class StartGameTest {

        @Test
        public void shouldStartOneGameWhenHaveTwoPlayers() {

            GameRequestDto gameRequestDto2 = GameRequestDto.builder()
                    .packsId(new HashSet<>(List.of(2)))
                    .build();
            User user2 = User.builder()
                    .name("Fariska")
                    .id(2)
                    .build();
            User newUser = User.builder()
                    .name("New")
                    .id(3)
                    .build();
            Game game1 = gameService.makeNewGame(gameRequestDto, user);
            Game game2 = gameService.makeNewGame(gameRequestDto2, user2);
            gameService.joinNewPlayer(newUser, 1);

            gameService.startGame(user, 1);

            Player player1 = game1.getPlayers().get(0);
            Player player2 = game1.getPlayers().get(1);
            Round round = game1.getCurrentRound();

            Assertions.assertEquals(2, gameService.findAllCreatedGames().size());
            Assertions.assertEquals(1, game1.getId());
            Assertions.assertEquals(GameStatus.PLAYING, game1.getStatus());
            Assertions.assertEquals(2, game1.getPlayers().size());
            Assertions.assertEquals(1, game1.getLeader());
            Assertions.assertEquals(88, game1.getCards().size());
            Assertions.assertEquals(Avatar.values().length - 2, game1.getFreeAvatars().size());
            Assertions.assertEquals(6, player1.getHandCards().size());
            Assertions.assertEquals(6, player2.getHandCards().size());
            Assertions.assertEquals(1, round.getNumber());
            Assertions.assertEquals(player1, round.getLeader());
            Assertions.assertEquals(3, round.getTableCards().size());
            Assertions.assertEquals(RoundStatus.WRITING_SECRET, round.getStatus());

            Assertions.assertEquals(GameStatus.WAITING_FOR_PLAYERS, game2.getStatus());
            Assertions.assertEquals(1, game2.getPlayers().size());
        }

        @Test
        public void shouldDoesNotStartOneGameWhenHaveOnePlayer() {

            Game game = gameService.makeNewGame(gameRequestDto, user);

            gameService.startGame(user, 1);

            List<Player> players = game.getPlayers();

            Round round = game.getCurrentRound();

            Assertions.assertEquals(1, gameService.findAllCreatedGames().size());
            Assertions.assertEquals(1, game.getId());
            Assertions.assertEquals(GameStatus.WAITING_FOR_PLAYERS, game.getStatus());
            Assertions.assertEquals(1, game.getPlayers().size());
            Assertions.assertEquals(0, game.getLeader());
            Assertions.assertEquals(100, game.getCards().size());
            Assertions.assertEquals(Avatar.values().length - 1, game.getFreeAvatars().size());
            Assertions.assertNull(players.get(0).getHandCards());
            Assertions.assertNull(round);
        }
    }
}
