package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.cards.charactercard.CharacterCardFactory;
import it.polimi.ingsw.model.deck.characterdeck.CharacterCardDeck;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.network.server.ClientConnection;
import it.polimi.ingsw.network.server.Room;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game game;
    EnumMap<Colors, Integer> enumMap;
    EnumMap<Colors, ArrayList<java.lang.String>> enumToMove;

    GameTest() throws AssistantCardNotFoundException, NegativeValueException, IncorrectArgumentException, IncorrectPlayerException, IncorrectStateException {
    }

    @Test
    public void testPlayersInit() throws IncorrectArgumentException, NegativeValueException {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Amrit");
        nicknames.add("Lore");
        nicknames.add("Tino");
        nicknames.add("Tony Stark");
        ArrayList<ClientConnection> emptyClientListsTest = new ArrayList<>();
        Room roomTest = new Room("testRoom", emptyClientListsTest);
        Game game = new Game(roomTest, false, 4, nicknames);
        ArrayList<Player> players = game.getPlayers();
        assertTrue(nicknames.contains(game.getCurrentPlayer().getNickname()));
        assertEquals(game.getCurrentState(), State.PLANNINGPHASE);
        assertEquals(players.size(), 4);
    }

    @Test
    public void testDrawFromBag() throws IncorrectArgumentException, IncorrectPlayerException, NegativeValueException {
        Game game = initGame2players();
        game.drawFromBag(game.getCurrentPlayer().getNickname());
        assertTrue(game.getPlayerDrawnOut());
    }

    @Test
    public void testDrawFromBagFalse() throws IncorrectArgumentException, NegativeValueException {
        Game game = initGame2players();
        assertThrows(IncorrectPlayerException.class, () -> game.drawFromBag("randomPlayer"));
    }

    public Game initGame2players() throws IncorrectArgumentException, NegativeValueException {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Luke Skywalker");
        nicknames.add("Dark Fener");
        ArrayList<ClientConnection> emptyClientListsTest = new ArrayList<>();
        Room roomTest = new Room("testRoom", emptyClientListsTest);
        return new Game(roomTest, true, 2, nicknames);
    }

    public Game initGame3players() throws IncorrectArgumentException, NegativeValueException {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Bruce Wayne");
        nicknames.add("Joker");
        nicknames.add("Bane");
        ArrayList<ClientConnection> emptyClientListsTest = new ArrayList<>();
        Room roomTest = new Room("testRoom", emptyClientListsTest);
        return new Game(roomTest, false, 3, nicknames);
    }

    public Game initGame4players() throws IncorrectArgumentException, NegativeValueException {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        ArrayList<ClientConnection> emptyClientListsTest = new ArrayList<>();
        Room roomTest = new Room("testRoom", emptyClientListsTest);
        return new Game(roomTest, true, 4, nicknames);
    }

    @Test
    public void testNextPlayer3Players() throws IncorrectArgumentException, IncorrectStateException, NegativeValueException {
        Game game = initGame3players();

        String oldPlayerNickname = game.getCurrentPlayer().getNickname();
        game.nextPlayer();
        String newPlayerNickname = game.getCurrentPlayer().getNickname();
        assertNotEquals(oldPlayerNickname, newPlayerNickname);
    }

    @Test
    public void testNextPlayer2and4Players() throws IncorrectArgumentException, IncorrectStateException, NegativeValueException {
        Game game = initGame4players();
        String oldPlayerNickname = game.getCurrentPlayer().getNickname();
        game.nextPlayer();
        assertNotEquals(oldPlayerNickname, game.getCurrentPlayer().getNickname());
    }

    @Test
    public void testFindPlayerFromTeam() throws IncorrectArgumentException, NegativeValueException {
        Game game = initGame3players();
        assertEquals(game.findPlayerFromTeam(Towers.GREY).size(), 1);
    }

    @Test
    public void testFindPlayerFromTeam4players() throws IncorrectArgumentException, NegativeValueException {
        Game game = initGame4players();
        assertEquals(game.findPlayerFromTeam(Towers.GREY).size(), 0);
        assertEquals(game.findPlayerFromTeam(Towers.WHITE).size(), 2);
        assertEquals(game.findPlayerFromTeam(Towers.BLACK).size(), 2);
    }

    @Test
    void testPlayAssistantCard() throws IncorrectArgumentException, IncorrectPlayerException, IncorrectStateException, AssistantCardNotFoundException, NegativeValueException, AssistantCardAlreadyPlayed {
        Game game = initGame4players();
        game.drawFromBag(game.getCurrentPlayer().getNickname());
        String oldPlayer = game.getCurrentPlayer().getNickname();
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "1");
        assertNotEquals(oldPlayer, game.getCurrentPlayer().getNickname());
    }

    @Test
    void testPlanningPhase() throws IncorrectArgumentException, IncorrectStateException, AssistantCardNotFoundException, IncorrectPlayerException, NegativeValueException, AssistantCardAlreadyPlayed {
        Game game = initGame4players();
        String oldPlayer = "null";
        for (int i = 0; i < 4; i++) {
            oldPlayer = game.getCurrentPlayer().getNickname();
            if (i == 0) game.drawFromBag(oldPlayer);
            game.playAssistantCard(game.getCurrentPlayer().getNickname(), String.valueOf(i + 1));
            String newPlayer = game.getCurrentPlayer().getNickname();
            assertNotEquals(oldPlayer, newPlayer);
        }
        assertEquals(game.getCurrentState(), State.ACTIONPHASE_1);
    }

    Game planningPhaseComplete() throws IncorrectArgumentException, AssistantCardNotFoundException, IncorrectPlayerException, IncorrectStateException, NegativeValueException, AssistantCardAlreadyPlayed {
        Game game = initGame4players();
        game.drawFromBag(game.getCurrentPlayer().getNickname());
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "1");
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "2");
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "9");
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "10");
        return game;
    }

    @Test
    void testMoveStudents() throws IncorrectArgumentException, AssistantCardNotFoundException, IncorrectPlayerException, IncorrectStateException, NegativeValueException, ProfessorNotFoundException, AssistantCardAlreadyPlayed {
        Game g = planningPhaseComplete();
        assertEquals(g.getCurrentState(), State.ACTIONPHASE_1);
        EnumMap<Colors, Integer> entrance = g.getCurrentPlayer().getSchoolBoard().getEntrance();
        assertEquals(7, g.valueOfEnum(entrance));
        EnumMap<Colors, ArrayList<String>> movingStudents = new EnumMap<>(Colors.class);
        int countNumStudents = 3;

        for (Colors c : Colors.values()) {
            for (int x = entrance.get(c); countNumStudents > 0 && x > 0; x--) {
                if (Math.random() > 0.5) {
                    g.moveStudents(g.getCurrentPlayer().getNickname(), c, "dining");
                } else g.moveStudents(g.getCurrentPlayer().getNickname(), c, "island4");
                countNumStudents--;
            }
        }

        assertEquals(4, g.valueOfEnum(entrance));
    }

    @Test
    void moveMotherNature() throws IncorrectArgumentException, AssistantCardNotFoundException, IncorrectStateException, IncorrectPlayerException, MotherNatureLostException, NegativeValueException, ProfessorNotFoundException, AssistantCardAlreadyPlayed {
        Game g = planningPhaseComplete();
        EnumMap<Colors, Integer> entrance = g.getCurrentPlayer().getSchoolBoard().getEntrance();
        EnumMap<Colors, ArrayList<String>> movingStudents = new EnumMap<>(Colors.class);
        int countNumStudents = 3;
        for (Colors c : Colors.values()) {
            for (int x = entrance.get(c); countNumStudents > 0 && x > 0; x--) {
                if (Math.random() > 0.5) {
                    g.moveStudents(g.getCurrentPlayer().getNickname(), c, "dining");
                } else g.moveStudents(g.getCurrentPlayer().getNickname(), c, "island4");
                countNumStudents--;
            }
        }
        int oldPosMN = g.getMotherNaturePosition();
        g.moveMotherNature(g.getCurrentPlayer().getNickname(), 1);
        assertNotEquals(g.getMotherNaturePosition(), oldPosMN);
    }

    @Test
    void testTakeStudentsFromCloud() throws IncorrectArgumentException, AssistantCardNotFoundException, IncorrectPlayerException, IncorrectStateException, MotherNatureLostException, NegativeValueException, ProfessorNotFoundException, AssistantCardAlreadyPlayed {
        Game g = planningPhaseComplete();
        EnumMap<Colors, Integer> entrance = g.getCurrentPlayer().getSchoolBoard().getEntrance();
        EnumMap<Colors, ArrayList<String>> movingStudents = new EnumMap<>(Colors.class);
        int countNumStudents = 3;
        for (Colors c : Colors.values()) {
            for (int x = entrance.get(c); countNumStudents > 0 && x > 0; x--) {
                if (Math.random() > 0.5) {
                    g.moveStudents(g.getCurrentPlayer().getNickname(), c, "dining");
                } else g.moveStudents(g.getCurrentPlayer().getNickname(), c, "island4");
                countNumStudents--;
            }
        }
        g.moveMotherNature(g.getCurrentPlayer().getNickname(), 1);
        EnumMap<Colors, Integer> s = g.getCloudTile(0).getStudents();
        g.takeStudentsFromCloud(g.getCurrentPlayer().getNickname(), "cloud1");
        s = g.getCloudTile(0).getStudents();
        for (Colors c : Colors.values()) {
            assertEquals(s.get(c), 0);
        }
    }

    @Test
    void testCompleteRound() throws IncorrectArgumentException, IncorrectPlayerException, AssistantCardNotFoundException,
            IncorrectStateException, MotherNatureLostException, NegativeValueException, ProfessorNotFoundException, AssistantCardAlreadyPlayed {
        Game g = planningPhaseComplete();
        for (int playersTurn = 0; playersTurn < 4; playersTurn++) {
            EnumMap<Colors, Integer> entrance = g.getCurrentPlayer().getSchoolBoard().getEntrance();
            EnumMap<Colors, ArrayList<String>> movingStudents = new EnumMap<>(Colors.class);
            int countNumStudents = 3;
            for (Colors c : Colors.values()) {
                for (int x = entrance.get(c); countNumStudents > 0 && x > 0; x--) {
                    if (Math.random() > 0.5) {
                        g.moveStudents(g.getCurrentPlayer().getNickname(), c, "dining");
                    } else g.moveStudents(g.getCurrentPlayer().getNickname(), c, "island4");
                    countNumStudents--;
                }
            }
            g.moveMotherNature(g.getCurrentPlayer().getNickname(), 1);
            EnumMap<Colors, Integer> s = g.getCloudTile(0).getStudents();
            g.takeStudentsFromCloud(g.getCurrentPlayer().getNickname(), "cloud1");

        }
        assertEquals(State.PLANNINGPHASE, g.getCurrentState());
    }

    @Test
    void testTakeStudentsFromCloudException() throws IncorrectArgumentException, AssistantCardNotFoundException, IncorrectPlayerException, IncorrectStateException, NegativeValueException, AssistantCardAlreadyPlayed {
        Game g = planningPhaseComplete();
        assertThrows(IncorrectStateException.class, () -> g.takeStudentsFromCloud("wrong player", "cloud1"));
    }

    @Test
    void testMoveTowersFromTeam() throws IncorrectArgumentException, NegativeValueException {
        Game game = initGame4players();
        ArrayList<Player> team = game.findPlayerFromTeam(Towers.WHITE);
        int initialTowers = -1;
        for (Player p : team) {
            if (initialTowers == -1) initialTowers = p.getPlayerTowers();
            else assertEquals(initialTowers, p.getPlayerTowers());
        }
        int endingTowers = -1;
        game.moveTowersFromTeam(team, -4);
        for (Player p : team) {
            if (endingTowers == -1) endingTowers = p.getPlayerTowers();
            else assertEquals(endingTowers, p.getPlayerTowers());
        }
        assertNotEquals(initialTowers, endingTowers);
    }

    @Test
    void checkAndPlaceProfessor() throws IncorrectArgumentException, AssistantCardNotFoundException, IncorrectPlayerException, IncorrectStateException, NegativeValueException, ProfessorNotFoundException, AssistantCardAlreadyPlayed {
        Game g = planningPhaseComplete();
        g.checkAndPlaceProfessor();
    }

    @Test
    void checkUnificationIslands() {

    }

    @Test
    void testIsGameOver() throws IncorrectArgumentException, NegativeValueException {
        Game game = initGame4players();
        assertFalse(game.isGameOver());
    }

    @Test
    void testCheckWinner() throws IncorrectArgumentException, NegativeValueException {
        Game game = initGame4players();
        assertNull(game.checkWinner());
    }

    @Test
    void testIsGameOver3players() throws IncorrectArgumentException, NegativeValueException {
        Game game = initGame3players();
        assertFalse(game.isGameOver());
    }

    void prepareForCards() throws NegativeValueException, IncorrectPlayerException, IncorrectArgumentException, ProfessorNotFoundException, IncorrectStateException, AssistantCardNotFoundException, AssistantCardAlreadyPlayed {
        game = initGame2players();
        game.drawFromBag(game.getCurrentPlayer().getNickname());
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "1");
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "2");

        enumMap = StudentManager.createEmptyStudentsEnum();
        enumMap.put(Colors.BLUE, 9);
        enumMap.put(Colors.YELLOW, 6);
        enumMap.put(Colors.PINK, 6);
        enumMap.put(Colors.RED, 6);
        enumMap.put(Colors.GREEN, 6);
        game.getCurrentPlayer().addStudents(enumMap);
    }

    @Test
    void testBuyCharacterCards1() throws AssistantCardNotFoundException, NegativeValueException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, IncorrectStateException, MotherNatureLostException, NotEnoughCoinsException, FullDiningException, AssistantCardAlreadyPlayed, CardPlayedInTurnException {
        prepareForCards();
        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.drawFromBag(game.getCurrentPlayer().getNickname());
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "3");
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "4");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");

        CharacterCardDeck characterCardDeck = new CharacterCardDeck();
        characterCardDeck.fillDeck();
        CharacterCardFactory factory = new CharacterCardFactory();
        int index;

        index = 0;
        CharacterCard card = characterCardDeck.get(index);
        game.setCharacterCards(0, factory.getCard(card.getImageName(), card.getPrice(), card.getDescription(), card.getType(), card.getAbility(), card.getRequirements()));

        game.activateCharacterCard(0, 0, 0);

        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
    }

    @Test
    void testBuyCharacterCards2() throws NegativeValueException, IncorrectArgumentException, AssistantCardNotFoundException, IncorrectStateException, IncorrectPlayerException, NotEnoughCoinsException, ProfessorNotFoundException, MotherNatureLostException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed {
        prepareForCards();
        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");

        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");

        CharacterCardDeck characterCardDeck = new CharacterCardDeck();
        characterCardDeck.fillDeck();
        CharacterCardFactory factory = new CharacterCardFactory();
        int index;

        index = 1;
        CharacterCard card = characterCardDeck.get(index);
        game.setCharacterCards(0, factory.getCard(card.getImageName(), card.getPrice(), card.getDescription(), card.getType(), card.getAbility(), card.getRequirements()));
        assertEquals(0, game.getCurrentPlayer().getSchoolBoard().getProfessorsTable().size());

        game.activateCharacterCard(0);

    }

    @Test
    void testBuyCharacterCards3() throws AssistantCardNotFoundException, NegativeValueException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, IncorrectStateException, NotEnoughCoinsException, MotherNatureLostException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed {
        prepareForCards();
        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.drawFromBag(game.getCurrentPlayer().getNickname());
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "3");
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "4");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        CharacterCardDeck characterCardDeck = new CharacterCardDeck();
        characterCardDeck.fillDeck();
        CharacterCardFactory factory = new CharacterCardFactory();
        int index;

        index = 2;
        CharacterCard card = characterCardDeck.get(index);
        game.setCharacterCards(0, factory.getCard(card.getImageName(), card.getPrice(), card.getDescription(), card.getType(), card.getAbility(), card.getRequirements()));

        game.activateCharacterCard(0, 2);
    }

    @Test
    void testBuyCharacterCards4() throws AssistantCardNotFoundException, NegativeValueException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, IncorrectStateException, MotherNatureLostException, NotEnoughCoinsException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed {
        prepareForCards();
        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");

        CharacterCardDeck characterCardDeck = new CharacterCardDeck();
        characterCardDeck.fillDeck();
        CharacterCardFactory factory = new CharacterCardFactory();
        int index;

        index = 3;
        CharacterCard card = characterCardDeck.get(index);
        game.setCharacterCards(0, factory.getCard(card.getImageName(), card.getPrice(), card.getDescription(), card.getType(), card.getAbility(), card.getRequirements()));

        game.activateCharacterCard(0);

        game.moveMotherNature(game.getCurrentPlayer().getNickname(), game.getCurrentPlayer().getPlayedAssistantCard().getMove() + 1);
    }

    @Test
    void testBuyCharacterCards5() throws AssistantCardNotFoundException, NegativeValueException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, IncorrectStateException, MotherNatureLostException, NotEnoughCoinsException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed {
        prepareForCards();
        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.drawFromBag(game.getCurrentPlayer().getNickname());
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "3");
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "4");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");

        CharacterCardDeck characterCardDeck = new CharacterCardDeck();
        characterCardDeck.fillDeck();
        CharacterCardFactory factory = new CharacterCardFactory();
        int index;

        index = 4;
        CharacterCard card = characterCardDeck.get(index);
        game.setCharacterCards(0, factory.getCard(card.getImageName(), card.getPrice(), card.getDescription(), card.getType(), card.getAbility(), card.getRequirements()));

        game.activateCharacterCard(0);

        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
    }

    @Test
    void testBuyCharacterCards6() throws AssistantCardNotFoundException, NegativeValueException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, IncorrectStateException, MotherNatureLostException, NotEnoughCoinsException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed {
        prepareForCards();
        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.drawFromBag(game.getCurrentPlayer().getNickname());
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "3");
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "4");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");

        CharacterCardDeck characterCardDeck = new CharacterCardDeck();
        characterCardDeck.fillDeck();
        CharacterCardFactory factory = new CharacterCardFactory();
        int index;

        index = 5;
        CharacterCard card = characterCardDeck.get(index);
        game.setCharacterCards(0, factory.getCard(card.getImageName(), card.getPrice(), card.getDescription(), card.getType(), card.getAbility(), card.getRequirements()));
        game.activateCharacterCard(0);

        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
    }

    @Test
    void testBuyCharacterCards7() throws AssistantCardNotFoundException, NegativeValueException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, IncorrectStateException, MotherNatureLostException, NotEnoughCoinsException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed {
        prepareForCards();
        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.drawFromBag(game.getCurrentPlayer().getNickname());
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "3");
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "4");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");

        CharacterCardDeck characterCardDeck = new CharacterCardDeck();
        characterCardDeck.fillDeck();
        CharacterCardFactory factory = new CharacterCardFactory();
        int index;

        index = 6;
        CharacterCard card = characterCardDeck.get(index);
        game.setCharacterCards(0, factory.getCard(card.getImageName(), card.getPrice(), card.getDescription(), card.getType(), card.getAbility(), card.getRequirements()));

        EnumMap<Colors, Integer> swap1, swap2;
        swap1 = StudentManager.createEmptyStudentsEnum();
        swap2 = StudentManager.createEmptyStudentsEnum();

        for (int i = 0, j = 0; i < 3; j++) {
            if (game.getCurrentPlayer().getNumOfStudent(Colors.getStudent(j)) > 0) {
                swap2.put(Colors.getStudent(j), Math.min(game.getCurrentPlayer().getNumOfStudent(Colors.getStudent(j)), 3 - i));
                i += Math.min(game.getCurrentPlayer().getNumOfStudent(Colors.getStudent(j)), 3 - i);
            }
        }


        for (int i = 0, j = 0; i < 3; j++) {
            if (game.getCharacterCards().get(0).getStudents().get(Colors.getStudent(j)) > 0) {
                swap1.put(Colors.getStudent(j), Math.min(game.getCharacterCards().get(0).getStudents().get(Colors.getStudent(j)), 3 - i));
                i += Math.min(game.getCharacterCards().get(0).getStudents().get(Colors.getStudent(j)), 3 - i);
            }
        }

        game.activateCharacterCard(0, swap1, swap2);

        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
    }

    @Test
    void testBuyCharacterCards8() throws AssistantCardNotFoundException, NegativeValueException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, IncorrectStateException, MotherNatureLostException, NotEnoughCoinsException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed {
        prepareForCards();
        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.drawFromBag(game.getCurrentPlayer().getNickname());
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "3");
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "4");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");

        CharacterCardDeck characterCardDeck = new CharacterCardDeck();
        characterCardDeck.fillDeck();
        CharacterCardFactory factory = new CharacterCardFactory();
        int index;

        index = 7;
        CharacterCard card = characterCardDeck.get(index);
        game.setCharacterCards(0, factory.getCard(card.getImageName(), card.getPrice(), card.getDescription(), card.getType(), card.getAbility(), card.getRequirements()));

        game.activateCharacterCard(0);

        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
    }

    @Test
    void testBuyCharacterCards9() throws AssistantCardNotFoundException, NegativeValueException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, IncorrectStateException, MotherNatureLostException, NotEnoughCoinsException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed {
        prepareForCards();
        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.drawFromBag(game.getCurrentPlayer().getNickname());
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "3");
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "4");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");

        CharacterCardDeck characterCardDeck = new CharacterCardDeck();
        characterCardDeck.fillDeck();
        CharacterCardFactory factory = new CharacterCardFactory();
        int index;

        index = 8;
        CharacterCard card = characterCardDeck.get(index);
        game.setCharacterCards(0, factory.getCard(card.getImageName(), card.getPrice(), card.getDescription(), card.getType(), card.getAbility(), card.getRequirements()));
        game.activateCharacterCard(0, 0);

        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
    }

    @Test
    void testBuyCharacterCards10() throws AssistantCardNotFoundException, NegativeValueException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, IncorrectStateException, MotherNatureLostException, NotEnoughCoinsException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed {
        prepareForCards();
        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud2");

        game.drawFromBag(game.getCurrentPlayer().getNickname());
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "3");
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "4");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");

        CharacterCardDeck characterCardDeck = new CharacterCardDeck();
        characterCardDeck.fillDeck();
        CharacterCardFactory factory = new CharacterCardFactory();
        int index;

        index = 9;
        CharacterCard card = characterCardDeck.get(index);
        game.setCharacterCards(0, factory.getCard(card.getImageName(), card.getPrice(), card.getDescription(), card.getType(), card.getAbility(), card.getRequirements()));

        EnumMap enumMap = StudentManager.createEmptyStudentsEnum();
        enumMap.put(Colors.BLUE, 4);
        game.getCurrentPlayer().getSchoolBoard().addStudents(enumMap);
        EnumMap<Colors, Integer> swap1, swap2;
        swap1 = StudentManager.createEmptyStudentsEnum();
        swap2 = StudentManager.createEmptyStudentsEnum();


        for (int i = 0, j = 0; i < 2; j++) {
            if (game.getCurrentPlayer().getSchoolBoard().getDining().get(Colors.getStudent(j)) > 0) {
                swap2.put(Colors.getStudent(j), Math.min(game.getCurrentPlayer().getSchoolBoard().getDining().get(Colors.getStudent(j)), 2 - i));
                i += Math.min(game.getCurrentPlayer().getNumOfStudent(Colors.getStudent(j)), 2 - i);
            }
        }


        for (int i = 0, j = 0; i < 2; j++) {
            if (game.getCurrentPlayer().getSchoolBoard().getEntrance().get(Colors.getStudent(j)) > 0) {
                swap1.put(Colors.getStudent(j), Math.min(game.getCurrentPlayer().getSchoolBoard().getEntrance().get(Colors.getStudent(j)), 2 - i));
                i += Math.min(game.getCurrentPlayer().getSchoolBoard().getEntrance().get(Colors.getStudent(j)), 2 - i);

            }
        }

        game.activateCharacterCard(0, swap1, swap2);

        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
    }

    @Test
    void testBuyCharacterCards11() throws AssistantCardNotFoundException, NegativeValueException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, IncorrectStateException, MotherNatureLostException, NotEnoughCoinsException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed {
        prepareForCards();
        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.drawFromBag(game.getCurrentPlayer().getNickname());
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "3");
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "4");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");

        CharacterCardDeck characterCardDeck = new CharacterCardDeck();
        characterCardDeck.fillDeck();
        CharacterCardFactory factory = new CharacterCardFactory();
        int index;

        index = 10;
        CharacterCard card = characterCardDeck.get(index);
        game.setCharacterCards(0, factory.getCard(card.getImageName(), card.getPrice(), card.getDescription(), card.getType(), card.getAbility(), card.getRequirements()));

        game.activateCharacterCard(0, 0);

        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
    }

    @Test
    void testBuyCharacterCards12() throws AssistantCardNotFoundException, NegativeValueException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, IncorrectStateException, MotherNatureLostException, NotEnoughCoinsException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed {
        prepareForCards();
        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud1");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        game.takeStudentsFromCloud(game.getCurrentPlayer().getNickname(), "cloud2");

        game.drawFromBag(game.getCurrentPlayer().getNickname());
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "3");
        game.playAssistantCard(game.getCurrentPlayer().getNickname(), "4");

        game.getCurrentPlayer().addStudents(enumMap);
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");
        game.moveStudents(game.getCurrentPlayer().getNickname(), Colors.BLUE, "dining");

        CharacterCardDeck characterCardDeck = new CharacterCardDeck();
        characterCardDeck.fillDeck();
        CharacterCardFactory factory = new CharacterCardFactory();
        int index;

        index = 11;
        CharacterCard card = characterCardDeck.get(index);
        game.setCharacterCards(0, factory.getCard(card.getImageName(), card.getPrice(), card.getDescription(), card.getType(), card.getAbility(), card.getRequirements()));
        game.activateCharacterCard(0, 0);

        game.moveMotherNature(game.getCurrentPlayer().getNickname(), 1);
    }
}