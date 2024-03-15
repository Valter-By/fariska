package com.fufa.fariska.services;


import com.fufa.fariska.dto.GameRequestDto;
import com.fufa.fariska.dto.MoveRequestDto;
import com.fufa.fariska.dto.SecretRequestDto;
import com.fufa.fariska.dto.VoteRequestDto;
import com.fufa.fariska.entities.*;
import com.fufa.fariska.entities.enums.Avatar;
import com.fufa.fariska.entities.enums.GameStatus;
import com.fufa.fariska.entities.enums.RoundStatus;
import com.fufa.fariska.repositories.PackRepository;
import org.junit.jupiter.api.*;

import java.util.*;


public class GameServiceTest {

    GameService gameService;
    //    @Mock
    PackRepository packRepository;

    GameRequestDto gameRequestDto;
    GameRequestDto gameRequestDto2;
    GameRequestDto gameRequestDto3;
    SecretRequestDto secretRequestDto;
    MoveRequestDto moveRequestDto;
    MoveRequestDto moveRequestDto2;
//    Set<Integer> packsId;
    Integer packsId;
    List<Pack> packs;

    List<Card> cards1;
    List<Card> cards2;
    User user;
    User user2;
    User user3;

    @BeforeEach
    public synchronized void makeData() {

        Game.totalGames = 0;

//        packsId = new HashSet<>(List.of(1, 2)); -- issue 12
        packsId = 1;
        gameRequestDto = GameRequestDto.builder()
                .packsId(packsId)
                .build();
        gameRequestDto2 = GameRequestDto.builder()
                .packsId(0)
//                .packsId(new HashSet<>(List.of(2)))
                .build();
        gameRequestDto3 = GameRequestDto.builder()
                .packsId(1)
//                .packsId(new HashSet<>(List.of(1)))
                .build();

        secretRequestDto = SecretRequestDto.builder()
                .secret("Fucking secret!")
                .round(1)
                .cardHandNumber(2)
                .build();

        moveRequestDto = MoveRequestDto.builder()
                .round(1)
                .playerPlace(1)
                .cardHandNumber(3)
                .build();
        moveRequestDto2 = MoveRequestDto.builder()
                .round(1)
                .playerPlace(2)
                .cardHandNumber(4)
                .build();

        user = User.builder()
                .nickname("Fufa")
                .id(1)
                .build();
        user2 = User.builder()
                .nickname("Fariska")
                .id(2)
                .build();
        user3 = User.builder()
                .nickname("Fedora")
                .id(3)
                .build();

//        packs = new ArrayList<>();
//        packs.add(null);
//        packs.add(1, Pack.builder()
//                .id(1)
//                .cards(cards1)
//                .build());
//        packs.add(2, Pack.builder()
//                .id(2)
//                .cards(cards2)
//                .build());

//        packRepository = Mockito.mock(PackRepository.class);
//        Mockito.when(packRepository.getPacks(packsId)).thenReturn(packs);

        packRepository = new PackRepository();
        gameService = new GameService(packRepository);
//        gameService.setTotalGames();
    }

    @Nested
    public class MakeNewGameTest {

        @Test
        public synchronized void shouldMakeNewGameWhenAllIsGood() {

            Game game = gameService.makeNewGame(user, gameRequestDto);

            Assertions.assertTrue(gameService.findAllCreatedGames().contains(1));
            Assertions.assertEquals(1, gameService.findAllCreatedGames().size());
            Assertions.assertEquals(1, game.getId());
            Assertions.assertEquals(user, game.getCreator());
            Assertions.assertEquals(GameStatus.WAITING_FOR_PLAYERS, game.getStatus());
            Assertions.assertEquals(user, game.getPlayers().get(0).getUser());
            Assertions.assertEquals(packsId, game.getPacksId());
            Assertions.assertEquals(50, game.getCards().size());
            Assertions.assertTrue(game.getCards().containsAll(cards1));
            Assertions.assertTrue(game.getCards().containsAll(cards2));
            Assertions.assertFalse(game.getFreeAvatars().contains(game.getPlayers().get(0).getAvatar()));
            Assertions.assertEquals(Avatar.values().length - 1, game.getFreeAvatars().size());
        }
    }

    @Nested
    public class FindGameTest {

        @Test
        public synchronized void shouldFindTwoGamesWhenCreatedTwoOnce() {

            Game game1 = gameService.makeNewGame(user, gameRequestDto);
            Game game2 = gameService.makeNewGame(user2, gameRequestDto2);

            Assertions.assertEquals(gameService.findGame(1), game1);
            Assertions.assertEquals(gameService.findGame(2), game2);
        }
    }

    @Nested
    public class FindAllCreatedGamesTest {

        @Test
        public synchronized void shouldFindAllGamesWhenCreatedThreeOnce() {

            Game game1 = gameService.makeNewGame(user, gameRequestDto);
            Game game2 = gameService.makeNewGame(user2, gameRequestDto2);
            Game game3 = gameService.makeNewGame(user, gameRequestDto3);
            Set<Integer> expected = new HashSet<>();
            expected.add(3);
            expected.add(1);
            expected.add(2);

            Assertions.assertEquals(expected, gameService.findAllCreatedGames());
        }
    }

    @Nested
    public class JoinNewPlayerTest {

        @Test
        public synchronized void shouldJoinNewPlayerWhenHaveGame() {

            Game game1 = gameService.makeNewGame(user, gameRequestDto);
            Game game2 = gameService.makeNewGame(user2, gameRequestDto2);

            gameService.joinNewPlayer(user3, 1);

            List<Player> players1 = game1.getPlayers();
            List<Player> players2 = game2.getPlayers();

            Assertions.assertEquals(2, players1.size());
            Assertions.assertEquals(1, players2.size());
            Assertions.assertEquals(user3, players1.get(1).getUser());
            Assertions.assertEquals(1, players1.get(1).getGameId());
            Assertions.assertFalse(game1.getFreeAvatars().contains(game1.getPlayers().get(1).getAvatar()));
            Assertions.assertEquals(Avatar.values().length - 2, game1.getFreeAvatars().size());
        }
    }

    @Nested
    public class StartGameTest {

        @Test
        public synchronized void shouldStartOneGameWhenHaveTwoPlayers() {

            Game game1 = gameService.makeNewGame(user, gameRequestDto);
            Game game2 = gameService.makeNewGame(user2, gameRequestDto2);
            gameService.joinNewPlayer(user3, 1);

            gameService.startGame(user, 1);

            Player player1 = game1.getPlayers().get(0);
            Player player2 = game1.getPlayers().get(1);
            Round round = game1.getCurrentRound();

            Assertions.assertEquals(2, gameService.findAllCreatedGames().size());
            Assertions.assertEquals(1, game1.getId());
            Assertions.assertEquals(GameStatus.PLAYING, game1.getStatus());
            Assertions.assertEquals(2, game1.getPlayers().size());
            Assertions.assertEquals(0, game1.getLeader());
            Assertions.assertEquals(38, game1.getCards().size());
            Assertions.assertEquals(Avatar.values().length - 2, game1.getFreeAvatars().size());
            Assertions.assertEquals(6, player1.getHandCards().size());
            Assertions.assertEquals(6, player2.getHandCards().size());
            Assertions.assertEquals(1, round.getNumber());
            Assertions.assertEquals(player1, round.getLeader());
            Assertions.assertEquals(2, round.getTableCards().size());
            Assertions.assertEquals(RoundStatus.WRITING_SECRET, round.getStatus());

            Assertions.assertEquals(GameStatus.WAITING_FOR_PLAYERS, game2.getStatus());
            Assertions.assertEquals(1, game2.getPlayers().size());
        }

        @Test
        public synchronized void shouldDoesNotStartOneGameWhenHaveOnePlayer() {

            Game game = gameService.makeNewGame(user, gameRequestDto);

            gameService.startGame(user, 1);

            List<Player> players = game.getPlayers();
            Round round = game.getCurrentRound();

            Assertions.assertEquals(1, gameService.findAllCreatedGames().size());
            Assertions.assertEquals(1, game.getId());
            Assertions.assertEquals(GameStatus.WAITING_FOR_PLAYERS, game.getStatus());
            Assertions.assertEquals(1, game.getPlayers().size());
            Assertions.assertEquals(0, game.getLeader());
            Assertions.assertEquals(50, game.getCards().size());
            Assertions.assertEquals(Avatar.values().length - 1, game.getFreeAvatars().size());
            Assertions.assertNull(players.get(0).getHandCards());
            Assertions.assertNull(round);
        }
    }

    @Nested
    public class MakeSecretTest {

        @Test
        public synchronized void shouldMakeSecretWhenHaveThreePlayers() {

            Game game = gameService.makeNewGame(user, gameRequestDto);
            gameService.joinNewPlayer(user2, 1);
            gameService.joinNewPlayer(user3, 1);
            gameService.startGame(user, 1);

            Player leader = game.getPlayers().get(0);
            List<Card> leaderCards = leader.getHandCards();
            Card expectedSecretCard = leaderCards.get(2);
            Round round = game.getCurrentRound();
            gameService.makeSecret(leader.getUser(),1, secretRequestDto);

            Assertions.assertEquals(GameStatus.PLAYING, game.getStatus());
            Assertions.assertEquals(3, game.getPlayers().size());
            Assertions.assertEquals(0, game.getLeader());
            Assertions.assertEquals(31, game.getCards().size());
            Assertions.assertEquals(6, leader.getHandCards().size());
            Assertions.assertEquals(1, round.getNumber());
            Assertions.assertEquals(leader, round.getLeader());
            Assertions.assertEquals(secretRequestDto.getSecret(), round.getSecret());
            Assertions.assertEquals(expectedSecretCard, round.getTableCards().get(0).getCard());
            Assertions.assertEquals(RoundStatus.MAKING_MOVIES, round.getStatus());
        }

    }

    @Nested
    public class MakeMoveTest {

        @Test
        public synchronized void shouldMakeFirstMoveWhenHaveThreePlayers() {

            Game game = gameService.makeNewGame(user, gameRequestDto);
            gameService.joinNewPlayer(user2, 1);
            gameService.joinNewPlayer(user3, 1);
            gameService.startGame(user, 1);

            Player leader = game.getPlayers().get(0);
            Player player1 = game.getPlayers().get(1);
            Player player2 = game.getPlayers().get(2);

            gameService.makeSecret(leader.getUser(), 1, secretRequestDto);

            List<Card> movingPlayerCards = player1.getHandCards();
            Card expectedMoveCard = movingPlayerCards.get(3);
            Round round = game.getCurrentRound();

            gameService.makeMove(player1.getUser(), 1, moveRequestDto);

            Assertions.assertEquals(GameStatus.PLAYING, game.getStatus());
            Assertions.assertEquals(30, game.getCards().size());
            Assertions.assertEquals(6, leader.getHandCards().size());
            Assertions.assertEquals(6, player1.getHandCards().size());
            Assertions.assertEquals(6, player2.getHandCards().size());
            Assertions.assertEquals(1, round.getNumber());
            Assertions.assertEquals(expectedMoveCard, round.getTableCards().get(1).getCard());
            Assertions.assertEquals(1, round.getTableCards().get(1).getPlayerPlace());
            Assertions.assertEquals(2, round.getNumberMoves());
            Assertions.assertEquals(RoundStatus.MAKING_MOVIES, round.getStatus());
        }

        @Test
        public synchronized void shouldMakeTwoMovesAndStartVotingWhenHaveThreePlayers() {

            Game game = gameService.makeNewGame(user, gameRequestDto);
            gameService.joinNewPlayer(user2, 1);
            gameService.joinNewPlayer(user3, 1);
            gameService.startGame(user, 1);

            Player leader = game.getPlayers().get(0);
            Player player1 = game.getPlayers().get(1);
            Player player2 = game.getPlayers().get(2);

            List<Card> leaderCards = leader.getHandCards();
            List<Card> movingPlayerCards1 = player1.getHandCards();
            List<Card> movingPlayerCards2 = player2.getHandCards();

            Card[] movedCards = new Card[3];
            movedCards[0] = leaderCards.get(2);
            movedCards[1] = movingPlayerCards1.get(3);
            movedCards[2] = movingPlayerCards2.get(4);

            Round round = game.getCurrentRound();
            gameService.makeSecret(leader.getUser(), 1, secretRequestDto);
            gameService.makeMove(player1.getUser(), 1, moveRequestDto);
            gameService.makeMove(player2.getUser(), 1, moveRequestDto2);

            Assertions.assertEquals(GameStatus.PLAYING, game.getStatus());
            Assertions.assertEquals(29, game.getCards().size());
            Assertions.assertEquals(6, player1.getHandCards().size());
            Assertions.assertEquals(6, player2.getHandCards().size());
            Assertions.assertEquals(1, round.getNumber());
            Assertions.assertEquals(3, round.getNumberMoves());
            for (int i = 0; i < 3; i++) {
                Assertions.assertEquals(
                        movedCards[round.getTableCards().get(i).getPlayerPlace()]
                        , round.getTableCards().get(i).getCard()
                );
            }
            Assertions.assertEquals(RoundStatus.VOTING, round.getStatus());
        }
    }

    @Nested
    public class MakeVoteTest {

        @Test
        public synchronized void shouldMakeOneVoteForSecretCardAfterMovingWhenHaveThreePlayers() {

            Game game = gameService.makeNewGame(user, gameRequestDto);
            gameService.joinNewPlayer(user2, 1);
            gameService.joinNewPlayer(user3, 1);
            gameService.startGame(user, 1);

            Player leader = game.getPlayers().get(0);
            Player player1 = game.getPlayers().get(1);
            Player player2 = game.getPlayers().get(2);

            Card[] movedCards = new Card[3];
            List<Card> leaderCards = leader.getHandCards();
            List<Card> movingPlayerCards1 = player1.getHandCards();
            List<Card> movingPlayerCards2 = player2.getHandCards();

            movedCards[0] = leaderCards.get(2);
            movedCards[1] = movingPlayerCards1.get(3);
            movedCards[2] = movingPlayerCards2.get(4);

            Round round = game.getCurrentRound();
            gameService.makeSecret(leader.getUser(), 1, secretRequestDto);
            gameService.makeMove(player1.getUser(), 1, moveRequestDto);
            gameService.makeMove(player2.getUser(), 1, moveRequestDto2);

            int secretPlace = round.findNumberLeaderCard();

            VoteRequestDto voteRequestDto = VoteRequestDto.builder()
                    .round(1)
                    .playerPlace(1)
                    .cardTableNumber(secretPlace)
                    .build();

            gameService.makeVote(player1.getUser(), 1, voteRequestDto);

            Assertions.assertEquals(29, game.getCards().size());
            Assertions.assertEquals(1, round.getNumber());
            Assertions.assertEquals(round.getLeaderCard(), round.getTableCards().get(secretPlace).getCard());
            Assertions.assertEquals(List.of(1), round.getTableCards().get(secretPlace).getVotes());
            Assertions.assertEquals(1, round.getNumberVotes());
            Assertions.assertEquals(RoundStatus.VOTING, round.getStatus());
        }

        @Test
        public synchronized void shouldMakeTwoVotesForSecretCardAndOtherWhenHaveThreePlayers() {

            Game game = gameService.makeNewGame(user, gameRequestDto);
            gameService.joinNewPlayer(user2, 1);
            gameService.joinNewPlayer(user3, 1);
            gameService.startGame(user, 1);

            Player leader = game.getPlayers().get(0);
            Player player1 = game.getPlayers().get(1);
            Player player2 = game.getPlayers().get(2);

            Card[] movedCards = new Card[3];
            List<Card> leaderCards = leader.getHandCards();
            List<Card> movingPlayerCards1 = player1.getHandCards();
            List<Card> movingPlayerCards2 = player2.getHandCards();

            movedCards[0] = leaderCards.get(2);
            movedCards[1] = movingPlayerCards1.get(3);
            movedCards[2] = movingPlayerCards2.get(4);

            gameService.makeSecret(leader.getUser(), 1, secretRequestDto);
            gameService.makeMove(player1.getUser(), 1, moveRequestDto);
            gameService.makeMove(player2.getUser(), 1, moveRequestDto2);
            Round round = game.getCurrentRound();

            int secretCardPlace = round.findNumberLeaderCard();
            int otherCardPlace = round.findNumberNoLeaderCardNoPlayer(2);

            VoteRequestDto voteRequestDto1 = VoteRequestDto.builder()
                    .round(1)
                    .playerPlace(1)
                    .cardTableNumber(secretCardPlace)
                    .build();
            VoteRequestDto voteRequestDto2 = VoteRequestDto.builder()
                    .round(1)
                    .playerPlace(2)
                    .cardTableNumber(otherCardPlace)
                    .build();

            gameService.makeVote(player1.getUser(), 1, voteRequestDto1);
            gameService.makeVote(player2.getUser(), 1, voteRequestDto2);

            Assertions.assertEquals(29, game.getCards().size());
            Assertions.assertEquals(1, round.getNumber());
            Assertions.assertEquals(movedCards[0], round.getTableCards().get(secretCardPlace).getCard());
            Assertions.assertEquals(movedCards[1], round.getTableCards().get(otherCardPlace).getCard());
            Assertions.assertEquals(List.of(1), round.getTableCards().get(secretCardPlace).getVotes());
            Assertions.assertEquals(List.of(2), round.getTableCards().get(otherCardPlace).getVotes());
            Assertions.assertEquals(2, round.getNumberVotes());
            Assertions.assertArrayEquals(new int[]{4, 4, 0}, round.getPlayerPoints());
            Assertions.assertEquals(4, leader.getPoints());
            Assertions.assertEquals(4, player1.getPoints());
            Assertions.assertEquals(0, player2.getPoints());
            Assertions.assertEquals(RoundStatus.WAITING_FOR_NEXT_ROUND, round.getStatus());
        }
    }

    @Nested
    public class StartNextRoundTest {

        @Test
        public synchronized void shouldStartNextRoundWhenHaveThreePlayers() {

            Game game = gameService.makeNewGame(user, gameRequestDto);
            gameService.joinNewPlayer(user2, 1);
            gameService.joinNewPlayer(user3, 1);
            gameService.startGame(user, 1);

            Player leader = game.getPlayers().get(0);
            Player player1 = game.getPlayers().get(1);
            Player player2 = game.getPlayers().get(2);

            gameService.makeSecret(leader.getUser(), 1, secretRequestDto);
            gameService.makeMove(player1.getUser(), 1, moveRequestDto);
            gameService.makeMove(player2.getUser(), 1, moveRequestDto2);
            Round round = game.getCurrentRound();

            VoteRequestDto voteRequestDto1 = VoteRequestDto.builder()
                    .round(1)
                    .playerPlace(1)
                    .cardTableNumber(round.findNumberLeaderCard())
                    .build();
            VoteRequestDto voteRequestDto2 = VoteRequestDto.builder()
                    .round(1)
                    .playerPlace(2)
                    .cardTableNumber(round.findNumberNoLeaderCardNoPlayer(2))
                    .build();

            gameService.makeVote(player1.getUser(), 1, voteRequestDto1);
            gameService.makeVote(player2.getUser(), 1, voteRequestDto2);

            gameService.startNextRound(user2, 1);

            Round newRound = game.getCurrentRound();

            Assertions.assertEquals(2, newRound.getNumber());
            Assertions.assertEquals(player1, newRound.getLeader());
            Assertions.assertNull(newRound.getLeaderCard());
            Assertions.assertNull(newRound.getSecret());
            for (int i = 0; i < 2; i++) {
                Assertions.assertNull(newRound.getTableCards().get(i));
            }
            Assertions.assertEquals(0, newRound.getNumberMoves());
            Assertions.assertEquals(0, newRound.getNumberVotes());
            Assertions.assertNull(newRound.getPlayerPoints());
            Assertions.assertEquals(RoundStatus.WRITING_SECRET, newRound.getStatus());
            Assertions.assertFalse(newRound.isLastRound());

            Assertions.assertEquals(1, game.getLeader());
            Assertions.assertEquals(GameStatus.PLAYING, game.getStatus());
            Assertions.assertEquals(29, game.getCards().size());

            Assertions.assertEquals(4, leader.getPoints());
            Assertions.assertEquals(4, player1.getPoints());
            Assertions.assertEquals(0, player2.getPoints());
        }
    }
}
