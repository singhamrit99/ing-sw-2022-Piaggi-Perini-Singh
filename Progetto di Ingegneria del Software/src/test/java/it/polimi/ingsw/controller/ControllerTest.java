package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.StudentManager;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.network.server.ClientConnection;
import it.polimi.ingsw.network.server.Room;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    @Test
    void testCallMoveMotherNature() throws IncorrectArgumentException, IncorrectStateException, IncorrectPlayerException, NegativeValueException, ProfessorNotFoundException, AssistantCardNotFoundException, MotherNatureLostException, AssistantCardAlreadyPlayed {
        Controller controller = new Controller();
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        ArrayList<ClientConnection> emptyClientListsTest = new ArrayList<>();
        Room roomTest = new Room("testRoom", emptyClientListsTest);
        Game game = controller.initializeGame(roomTest, true, 4, nicknames);
        game.drawFromBag(game.getCurrentPlayer().getNickname());
        for (int i = 0; i < 4; i++) {
            game.playAssistantCard(game.getCurrentPlayer().getNickname(), String.valueOf(i + 1));
        }
        EnumMap<Colors, Integer> entrance = game.getCurrentPlayer().getSchoolBoard().getEntrance();
        int countNumStudents = 3;
        for (Colors c : Colors.values()) {
            for (int x = entrance.get(c); countNumStudents > 0 && x > 0; x--) {
                if (Math.random() > 0.5) {
                    game.moveStudents(game.getCurrentPlayer().getNickname(), c, "dining");
                } else game.moveStudents(game.getCurrentPlayer().getNickname(), c, "island4");
                countNumStudents--;
            }
        }
        int oldPosMotherNature = game.getMotherNaturePosition();
        Random casual = new Random();
        int randomMovement = casual.nextInt(game.getCurrentPlayer().getPlayedAssistantCard().getMove()) + 1;
        controller.callMoveMotherNature(game.getCurrentPlayer().getNickname(), randomMovement);
        assertNotEquals(game.getMotherNaturePosition(), oldPosMotherNature);
    }

    @Test
    void testCallMoveStudent() throws IncorrectStateException, AssistantCardNotFoundException, IncorrectPlayerException, IncorrectArgumentException, NegativeValueException, ProfessorNotFoundException, AssistantCardAlreadyPlayed {
        Controller controller = new Controller();
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        ArrayList<ClientConnection> emptyClientListsTest = new ArrayList<>();
        Room roomTest = new Room("testRoom", emptyClientListsTest);
        Game game = controller.initializeGame(roomTest, true, 4, nicknames);
        game.drawFromBag(game.getCurrentPlayer().getNickname());
        for (int i = 0; i < 4; i++) {
            game.playAssistantCard(game.getCurrentPlayer().getNickname(), String.valueOf(i + 1));
        }
        assertEquals(game.getCurrentState(), State.ACTIONPHASE_1);
        EnumMap<Colors, Integer> entrance = game.getCurrentPlayer().getSchoolBoard().getEntrance();
        assertEquals(7, game.valueOfEnum(entrance));
        int countNumStudents = 3;
        for (Colors c : Colors.values()) {
            for (int x = entrance.get(c); countNumStudents > 0 && x > 0; x--) {
                if (Math.random() > 0.5) {
                    controller.callMoveStudent(game.getCurrentPlayer().getNickname(), c, "dining");
                } else controller.callMoveStudent(game.getCurrentPlayer().getNickname(), c, "island4");
                countNumStudents--;
            }
        }
        assertEquals(4, game.valueOfEnum(entrance));
    }

    @Test
    void testCallPickCloud() throws IncorrectStateException, AssistantCardNotFoundException, IncorrectPlayerException, IncorrectArgumentException, NegativeValueException, MotherNatureLostException, ProfessorNotFoundException, AssistantCardAlreadyPlayed {
        Controller controller = new Controller();
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        ArrayList<ClientConnection> emptyClientListsTest = new ArrayList<>();
        Room roomTest = new Room("testRoom", emptyClientListsTest);
        Game game = controller.initializeGame(roomTest, true, 4, nicknames);
        game.drawFromBag(game.getCurrentPlayer().getNickname());
        for (int i = 0; i < 4; i++) {
            game.playAssistantCard(game.getCurrentPlayer().getNickname(), String.valueOf(i + 4));
        }
        EnumMap<Colors, Integer> entrance = game.getCurrentPlayer().getSchoolBoard().getEntrance();
        int countNumStudents = 3;
        for (Colors c : Colors.values()) {
            for (int x = entrance.get(c); countNumStudents > 0 && x > 0; x--) {
                if (Math.random() > 0.5) {
                    controller.callMoveStudent(game.getCurrentPlayer().getNickname(), c, "dining");
                } else controller.callMoveStudent(game.getCurrentPlayer().getNickname(), c, "island4");
                countNumStudents--;
            }
        }
        controller.callMoveMotherNature(game.getCurrentPlayer().getNickname(), 1);
        controller.callPickCloud(game.getCurrentPlayer().getNickname(), "cloud1");
        EnumMap<Colors, Integer> studentsoncloud = game.getCloudTile(0).getStudents();
        for (Colors color : Colors.values()) {
            assertEquals(0, studentsoncloud.get(color));
        }
    }

    @Test
    void testCallPlayAssistantCard() throws IncorrectArgumentException, IncorrectPlayerException, NegativeValueException, AssistantCardNotFoundException, IncorrectStateException, AssistantCardAlreadyPlayed {
        Controller controller = new Controller();
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        ArrayList<ClientConnection> emptyClientListsTest = new ArrayList<>();
        Room roomTest = new Room("testRoom", emptyClientListsTest);
        Game game = controller.initializeGame(roomTest, true, 4, nicknames);
        game.drawFromBag(game.getCurrentPlayer().getNickname());
        String oldPlayer = game.getCurrentPlayer().getNickname();
        controller.callPlayAssistantCard(game.getCurrentPlayer().getNickname(), "1");
        assertNotEquals(oldPlayer, game.getCurrentPlayer().getNickname());
    }

    @Test
    void testCallDrawFromBag() throws NegativeValueException, IncorrectArgumentException, IncorrectPlayerException {
        Controller controller = new Controller();
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        ArrayList<ClientConnection> emptyClientListsTest = new ArrayList<>();
        Room roomTest = new Room("testRoom", emptyClientListsTest);
        Game game = controller.initializeGame(roomTest, true, 4, nicknames);
        controller.callDrawFromBag(game.getCurrentPlayer().getNickname());
    }

    @Test
    void testCallPlayCharacterA() throws IncorrectArgumentException, IncorrectPlayerException, NegativeValueException {
        Controller controller = new Controller();
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        ArrayList<ClientConnection> emptyClientListsTest = new ArrayList<>();
        Room roomTest = new Room("testRoom", emptyClientListsTest);
        Game game = controller.initializeGame(roomTest, true, 4, nicknames);
        game.drawFromBag(game.getCurrentPlayer().getNickname());
        assertThrows(NotEnoughCoinsException.class, () -> controller.callPlayCharacterCard(0));
    }

    @Test
    void testCallPlayCharacterB() throws IncorrectArgumentException, IncorrectPlayerException, NegativeValueException {
        Controller controller = new Controller();
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        ArrayList<ClientConnection> emptyClientListsTest = new ArrayList<>();
        Room roomTest = new Room("testRoom", emptyClientListsTest);
        Game game = controller.initializeGame(roomTest, true, 4, nicknames);
        game.drawFromBag(game.getCurrentPlayer().getNickname());
        assertThrows(NotEnoughCoinsException.class, () -> controller.callPlayCharacterCard(0, 0));
    }

    @Test
    void testCallPlayCharacterC() throws IncorrectArgumentException, IncorrectPlayerException, NegativeValueException {
        Controller controller = new Controller();
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        ArrayList<ClientConnection> emptyClientListsTest = new ArrayList<>();
        Room roomTest = new Room("testRoom", emptyClientListsTest);
        Game game = controller.initializeGame(roomTest, true, 4, nicknames);
        game.drawFromBag(game.getCurrentPlayer().getNickname());
        assertThrows(NotEnoughCoinsException.class, () -> controller.callPlayCharacterCard(0, 0, 0));

    }

    @Test
    void testCallPlayCharacterD() throws IncorrectArgumentException, IncorrectPlayerException, NegativeValueException {
        Controller controller = new Controller();
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        ArrayList<ClientConnection> emptyClientListsTest = new ArrayList<>();
        Room roomTest = new Room("testRoom", emptyClientListsTest);
        Game game = controller.initializeGame(roomTest, true, 4, nicknames);
        game.drawFromBag(game.getCurrentPlayer().getNickname());
        assertThrows(NotEnoughCoinsException.class, () -> controller.callPlayCharacterCard(0, StudentManager.createEmptyStudentsEnum(), StudentManager.createEmptyStudentsEnum()));
    }
}