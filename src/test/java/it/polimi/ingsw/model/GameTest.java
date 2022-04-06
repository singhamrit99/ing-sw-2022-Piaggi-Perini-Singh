package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.IncorrectPlayerException;
import it.polimi.ingsw.model.exceptions.IncorrectStateException;
import it.polimi.ingsw.model.tiles.CloudTile;
import it.polimi.ingsw.model.tiles.IslandTile;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {


    @Test
    public void testPlayersInit() throws IncorrectArgumentException {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Amrit");
        nicknames.add("Lore");
        nicknames.add("Tino");
        nicknames.add("Tony Stark");
        Game game = new Game(false, 4, nicknames);
        ArrayList<Player> players = game.getPlayers();
        assertTrue(nicknames.contains(game.getCurrentPlayer().getNickname()));
        assertEquals(game.getCurrentState(), State.PLANNINGPHASE);
        assertEquals(players.size(), 4);
    }

    @Test
    public void testDrawFromBag() throws IncorrectArgumentException,IncorrectPlayerException{
        Game game = initGame2players();
        game.drawFromBag(game.getCurrentPlayer().getNickname());
        assertTrue(game.getPlayerDrawnOut());
    }

    @Test
    public void testDrawFromBagFalse() throws IncorrectArgumentException{
        Game game = initGame2players();
        assertThrows(IncorrectPlayerException.class,()->game.drawFromBag("randomPlayer"));
    }

    public Game initGame2players() throws IncorrectArgumentException{
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Luke Skywalker");
        nicknames.add("Dark Fener");
        return new Game(false, 2, nicknames);
    }

    public Game initGame3players() throws IncorrectArgumentException{
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Bruce Wayne");
        nicknames.add("Joker");
        nicknames.add("Bane");
        return new Game(false, 3, nicknames);
    }

    public Game initGame4players() throws IncorrectArgumentException{
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.add("Michelangelo");
        nicknames.add("Raffaello");
        nicknames.add("Donatello");
        nicknames.add("Leonardo");
        return new Game(true, 4, nicknames);
    }

    @Test
    public void testNextPlayer3Players() throws IncorrectArgumentException, IncorrectStateException {
        Game game = initGame3players();
        String oldPlayerNickname = game.getCurrentPlayer().getNickname();
        game.nextPlayer();
        String newPlayerNickname = game.getCurrentPlayer().getNickname();
        assertNotEquals(oldPlayerNickname, newPlayerNickname);
    }

    @Test
    public void testNextPlayer2and4Players() throws IncorrectArgumentException, IncorrectStateException {
        Game game = initGame4players();
        String oldPlayerNickname = game.getCurrentPlayer().getNickname();
        game.nextPlayer();
        assertNotEquals(oldPlayerNickname, game.getCurrentPlayer().getNickname());
    }

    @Test
    public void testFindPlayerFromTeam() throws IncorrectArgumentException{
        Game game = initGame3players();
        assertEquals(game.findPlayerFromTeam(Towers.GREY).size(),1);
    }

    @Test
    public void testFindPlayerFromTeam4players() throws IncorrectArgumentException{
        Game game = initGame4players();
        assertEquals(game.findPlayerFromTeam(Towers.GREY).size(),0);
        assertEquals(game.findPlayerFromTeam(Towers.WHITE).size(),2);
        assertEquals(game.findPlayerFromTeam(Towers.BLACK).size(),2);
    }

    @Test
    void testNextRound() throws IncorrectArgumentException{
        Game game = initGame2players();
        assertThrows(IncorrectStateException.class,()->game.nextRound());
    }

    @Test
    void testPlayAssistantCard() throws IncorrectArgumentException, IncorrectPlayerException, IncorrectStateException {
        Game game = initGame4players();
        game.drawFromBag(game.getCurrentPlayer().getNickname());
        String oldPlayer = game.getCurrentPlayer().getNickname();
        game.playAssistantCard(game.getCurrentPlayer().getNickname(),3);
    }

    @Test
    void testPlanningPhase() throws IncorrectArgumentException, IncorrectStateException, IncorrectPlayerException{
        Game game = initGame4players();
        String oldPlayer="null";
        for(int i = 0; i<4;i++){
            oldPlayer = game.getCurrentPlayer().getNickname();
            game.drawFromBag(oldPlayer);
            game.playAssistantCard(game.getCurrentPlayer().getNickname(), 3);
            String newPlayer = game.getCurrentPlayer().getNickname();
            assertNotEquals(oldPlayer,newPlayer);
        }
        assertEquals(game.getCurrentState(),State.ACTIONPHASE);
    }

    Game planningPhaseComplete()throws IncorrectArgumentException,IncorrectPlayerException,IncorrectStateException{
        Game game = initGame4players();
        String oldPlayer="null";
        for(int i = 0; i<4;i++){
            oldPlayer = game.getCurrentPlayer().getNickname();
            game.drawFromBag(oldPlayer);
            game.playAssistantCard(game.getCurrentPlayer().getNickname(), 3);
            String newPlayer = game.getCurrentPlayer().getNickname();
            assertNotEquals(oldPlayer,newPlayer);
        }
        return game;
    }

    @Test
    void moveStudents() throws IncorrectArgumentException,IncorrectStateException,IncorrectPlayerException{
        Game game = planningPhaseComplete();
        assertEquals(game.getCurrentState(),State.ACTIONPHASE);
        EnumMap<Colors,Integer> s = game.getCloudTile(0).getStudents();
        System.out.println("PINK: "+s.get(Colors.PINK));
        System.out.println("YELLOW: "+s.get(Colors.YELLOW));
        System.out.println("GREEN: "+s.get(Colors.GREEN));
        System.out.println("RED: "+s.get(Colors.RED));
        System.out.println("BLUE: "+s.get(Colors.BLUE));
    }

    @Test
    void takeStudentsFromCloud(){
    }

    @Test
    void moveMotherNature() {
    }

    @Test
    void checkAndPlaceProfessor() {
    }

    @Test
    void testMoveTowersFromTeam() throws IncorrectArgumentException{
        Game game = initGame4players();
        ArrayList<Player> team = game.findPlayerFromTeam(Towers.WHITE);
        int initialTowers=-1;
        for (Player p : team) {
            if(initialTowers==-1) initialTowers = p.getPlayerTowers();
            else assertEquals(initialTowers, p.getPlayerTowers());
        }
        int endingTowers = -1;
        game.moveTowersFromTeam(team,-4);
        for (Player p : team) {
            if(endingTowers==-1) endingTowers = p.getPlayerTowers();
            else assertEquals(endingTowers, p.getPlayerTowers());
        }
        assertNotEquals(initialTowers, endingTowers);
    }

    @Test
    void checkUnificationIslands() {
    }

    @Test
    void testIsGameOver() throws IncorrectArgumentException{
        Game game = initGame4players();
        assertFalse(game.isGameOver());
    }

    @Test
    void testCheckWinner() throws IncorrectArgumentException{
        Game game = initGame4players();
        assertNull(game.checkWinner());
    }


    @Test
    void testIsGameOver3players() throws IncorrectArgumentException{
        Game game = initGame3players();
        assertFalse(game.isGameOver());
    }



}