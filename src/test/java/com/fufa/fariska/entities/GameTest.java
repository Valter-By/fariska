package com.fufa.fariska.entities;

import com.fufa.fariska.entities.enums.Avatar;
import com.fufa.fariska.entities.enums.GameStatus;
import com.fufa.fariska.repositories.CardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

public class GameTest {

    @Nested
    public class GetFreeAvatar {
        @Test
        public void shouldGetFreeAvatarWhenUseForAll() {
            LinkedList<Avatar> avatars = Avatar.getAll();
            Game game = Game.builder()
                    .freeAvatars(avatars)
                    .build();
            Avatar avatar = game.getFreeAvatar();
            System.out.println(avatar);
            Assertions.assertNotNull(avatar);
        }

        @Test
        public void shouldGetTwoDifferentAvatarsWhenUseForAll() {
            LinkedList<Avatar> avatars = Avatar.getAll();
            Game game = Game.builder()
                    .freeAvatars(avatars)
                    .build();
            Avatar avatar1 = game.getFreeAvatar();
            Avatar avatar2 = game.getFreeAvatar();
            Assertions.assertNotNull(avatar1);
            Assertions.assertNotNull(avatar2);
            Assertions.assertNotEquals(avatar1, avatar2);
        }
    }

    @Nested
    public class TakeOneCard {

        @Test
        public void shouldTakeOneCardWhenUseFor100pcs() {
            CardRepository cardRepository = new CardRepository();
            cardRepository.makeFirstPack();
            Game game = Game.builder()
                    .cards(cardRepository.getOnePackCards(0))
                    .build();
            Assertions.assertEquals(100, game.getCards().size());
            Card card = game.takeOneCard();
            Assertions.assertNotNull(card);
            Assertions.assertEquals(99, game.getCards().size());
            Assertions.assertFalse(game.getCards().contains(card));
        }

        @Test
        public void shouldTakeTwoCardsWhenUseFor100pcs() {
            CardRepository cardRepository = new CardRepository();
            cardRepository.makeFirstPack();
            Game game = Game.builder()
                    .cards(cardRepository.getOnePackCards(0))
                    .build();
            Assertions.assertEquals(100, game.getCards().size());
            Card card1 = game.takeOneCard();
            Card card2 = game.takeOneCard();
            Assertions.assertEquals(98, game.getCards().size());
            Assertions.assertFalse(game.getCards().contains(card1));
            Assertions.assertFalse(game.getCards().contains(card2));
            Assertions.assertNotEquals(card1, card2);
        }
    }

    @Nested
    public class TakeSomeCards {

        @Test
        public void shouldTake5CardsWhenUseFor100pcs() {
            CardRepository cardRepository = new CardRepository();
            cardRepository.makeFirstPack();
            Game game = Game.builder()
                    .cards(cardRepository.getOnePackCards(0))
                    .build();
            Assertions.assertEquals(100, game.getCards().size());
            LinkedList<Card> cards = game.takeSomeCards(5);
            Assertions.assertNotNull(cards);
            Assertions.assertEquals(95, game.getCards().size());
            Assertions.assertEquals(5, cards.size());
            Assertions.assertFalse(game.getCards().contains(cards.get(2)));
        }
    }

    @Nested
    public class DealCards {

        @Test
        public void shouldDealFor6WhenUseFor100pcsCardsAnd4Players() {
            CardRepository cardRepository = new CardRepository();
            cardRepository.makeFirstPack();

            Game game = Game.builder()
                    .freeAvatars(Avatar.getAll())
                    .cards(cardRepository.getOnePackCards(0))
                    .build();

            ArrayList<Player> players = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                players.add(Player.builder()
                        .avatar(game.getFreeAvatar())
                        .build());
            }

            game.setPlayers(players);

            game.dealCards();

            for (int i = 0; i < 4; i++) {
                Assertions.assertEquals(6, players.get(i).getHandCards().size());
            }
            Assertions.assertEquals(76, game.getCards().size());
        }
    }

    @Nested
    public class AddAllPoints {

        @Test
        public void shouldAddAllPointsWhenUseFor4PlayersWithoutPoints() {

            Game game = Game.builder()
                    .freeAvatars(Avatar.getAll())
                    .build();

            ArrayList<Player> players = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                players.add(Player.builder()
                        .avatar(game.getFreeAvatar())
                        .build());
            }
            game.setPlayers(players);

            int[] points = new int[]{1, 2, 3, 4};

            game.addAllPoints(points);

            for (int i = 0; i < 4; i++) {
                Assertions.assertEquals(i + 1, players.get(i).getPoints());
            }
        }

        @Test
        public void shouldAddAllPointsWhenUseFor4PlayersWithPoints() {

            Game game = Game.builder()
                    .freeAvatars(Avatar.getAll())
                    .build();

            ArrayList<Player> players = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                players.add(Player.builder()
                        .points(4 - i)
                        .avatar(game.getFreeAvatar())
                        .build());
            }
            game.setPlayers(players);

            int[] points = new int[]{1, 2, 3, 4};

            game.addAllPoints(points);

            for (int i = 0; i < 4; i++) {
                Assertions.assertEquals(5, players.get(i).getPoints());
            }
        }
    }

    @Nested
    public class GetNextLeader {

        @Test
        public void shouldGetNextLeaderWhenUseForFirstLeaderPlayer() {

            Game game = Game.builder()
                    .leader(0)
                    .freeAvatars(Avatar.getAll())
                    .build();

            ArrayList<Player> players = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                players.add(Player.builder()
                        .avatar(game.getFreeAvatar())
                        .build());
            }

            game.setPlayers(players);

            Assertions.assertEquals(1, game.getNextLeader());
            Assertions.assertEquals(1, game.getLeader());
        }

        @Test
        public void shouldGetNextLeaderWhenUseForMiddleLeaderPlayer() {

            Game game = Game.builder()
                    .leader(2)
                    .freeAvatars(Avatar.getAll())
                    .build();

            ArrayList<Player> players = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                players.add(Player.builder()
                        .avatar(game.getFreeAvatar())
                        .build());
            }

            game.setPlayers(players);

            Assertions.assertEquals(3, game.getNextLeader());
            Assertions.assertEquals(3, game.getLeader());
        }

        @Test
        public void shouldGetNextLeaderWhenUseForLastLeaderPlayer() {

            Game game = Game.builder()
                    .leader(3)
                    .freeAvatars(Avatar.getAll())
                    .build();

            ArrayList<Player> players = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                players.add(Player.builder()
                        .avatar(game.getFreeAvatar())
                        .build());
            }

            game.setPlayers(players);

            Assertions.assertEquals(0, game.getNextLeader());
            Assertions.assertEquals(0, game.getLeader());
        }
    }

    @Nested
    public class Finish {

        @Test
        public void shouldFinishGameWhenUseForGameOverGame() {

            Game game = Game.builder()
                    .status(GameStatus.PLAYING)
                    .build();

            game.finish();

            Assertions.assertEquals(GameStatus.GAME_OVER, game.getStatus());
        }
    }
}

