package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.tiles.CloudTile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {


    Game planningPhaseComplete() throws IncorrectArgumentException, IncorrectPlayerException, IncorrectStateException, NegativeValueException {
        Game game = initGame4players();
        for (int i = 0; i < 4; i++) {
            game.drawFromBag(game.getCurrentPlayer().getNickname());
            game.playAssistantCard(game.getCurrentPlayer().getNickname(), (int) (Math.random() * 9));
        }
        return game;
    }

    public Game initGame4players() throws IncorrectArgumentException, NegativeValueException {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        return new Game(true, 4, nicknames);
    }

    @Test
    void testCallMoveMotherNature() throws IncorrectArgumentException, IncorrectStateException, IncorrectPlayerException, NegativeValueException, ProfessorNotFoundException {
        Controller controller = new Controller();
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        Game game= controller.initializeGame(true, 4, nicknames);
        for (int i = 0; i < 4; i++) {
            game.drawFromBag(game.getCurrentPlayer().getNickname());
            game.playAssistantCard(game.getCurrentPlayer().getNickname(), (int) (Math.random() * 9));
        }
        EnumMap<Colors, Integer> entrance = game.getCurrentPlayer().getSchoolBoard().getEntrance();
        EnumMap<Colors, ArrayList<String>> movingStudents = new EnumMap(Colors.class);
        int countNumStudents = 3;
        for (Colors c : Colors.values()) {
            ArrayList<String> tmp = new ArrayList<>();
            movingStudents.put(c, tmp);
            for (int x = entrance.get(c); countNumStudents > 0 && x > 0; x--) {
                tmp = movingStudents.get(c);
                if (Math.random() > 0.5) {
                    tmp.add("dining");
                } else tmp.add("island4");
                movingStudents.put(c, tmp);
                countNumStudents--;
            }
        }
        game.moveStudents(game.getCurrentPlayer().getNickname(), movingStudents);
        int oldPosMotherNature = game.getMotherNaturePosition();
        controller.callMoveMotherNature();
        assertNotEquals(game.getMotherNaturePosition(), oldPosMotherNature);
    }

    @Test
    void testCallMoveStudent() throws IncorrectStateException, IncorrectPlayerException, IncorrectArgumentException, NegativeValueException {
        Controller controller = new Controller();
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        Game game = controller.initializeGame(true,4,nicknames);
        for (int i = 0; i < 4; i++) {
            game.drawFromBag(game.getCurrentPlayer().getNickname());
            game.playAssistantCard(game.getCurrentPlayer().getNickname(), (int) (Math.random() * 9));
        }
        assertEquals(game.getCurrentState(), State.ACTIONPHASE_1);
        EnumMap<Colors, Integer> entrance = game.getCurrentPlayer().getSchoolBoard().getEntrance();
        assertEquals(7, game.valueOfEnum(entrance));
        EnumMap<Colors, ArrayList<String>> movingStudents = new EnumMap(Colors.class);
        int countNumStudents = 3;
        for (Colors c : Colors.values()) {
            ArrayList<String> tmp = new ArrayList<>();
            movingStudents.put(c, tmp);
            for (int x = entrance.get(c); countNumStudents > 0 && x > 0; x--) {
                tmp = movingStudents.get(c);
                if (Math.random() > 0.5) {
                    tmp.add("dining");
                } else tmp.add("island4");
                movingStudents.put(c, tmp);
                countNumStudents--;
            }
        }
        controller.callMoveStudent(movingStudents);
        assertEquals(4, game.valueOfEnum(entrance));
    }

    @Test
    void testCallPickCloud() throws IncorrectStateException, IncorrectPlayerException, IncorrectArgumentException, NegativeValueException {
        Controller controller = new Controller();
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        Game game= controller.initializeGame(true, 4, nicknames);
        for (int i = 0; i < 4; i++) {
            game.drawFromBag(game.getCurrentPlayer().getNickname());
            game.playAssistantCard(game.getCurrentPlayer().getNickname(), (int) (Math.random() * 9));
        }
        EnumMap<Colors, Integer> entrance = game.getCurrentPlayer().getSchoolBoard().getEntrance();
        EnumMap<Colors, ArrayList<String>> movingStudents = new EnumMap(Colors.class);
        int countNumStudents = 3;
        for (Colors c : Colors.values()) {
            ArrayList<String> tmp = new ArrayList<>();
            movingStudents.put(c, tmp);
            for (int x = entrance.get(c); countNumStudents > 0 && x > 0; x--) {
                tmp = movingStudents.get(c);
                if (Math.random() > 0.5) {
                    tmp.add("dining");
                } else tmp.add("island4");
                movingStudents.put(c, tmp);
                countNumStudents--;
            }
        }
        controller.callMoveStudent(movingStudents);
        controller.callMoveMotherNature();
        controller.callPickCloud(0);
        EnumMap<Colors, Integer> studentsoncloud = game.getCloudTile(0).getStudents();
        for (Colors color : Colors.values()) {
            assertEquals(0, studentsoncloud.get(color));
        }
    }

    @Test
    void testCallPlayAssistantCard() throws IncorrectArgumentException, IncorrectPlayerException, NegativeValueException {
        Controller controller = new Controller();
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        Game game= controller.initializeGame(true, 4, nicknames);
        game.drawFromBag(game.getCurrentPlayer().getNickname());
        String oldplayer = game.getCurrentPlayer().getNickname();
        controller.callPlayAssistantCard(3);
        assertNotEquals(oldplayer, game.getCurrentPlayer().getNickname());


    }
}