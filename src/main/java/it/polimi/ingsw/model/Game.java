package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.FillDeck;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.exceptions.GetPaths;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.IncorrectStateException;
import it.polimi.ingsw.model.tiles.CloudTile;
import it.polimi.ingsw.model.tiles.IslandTile;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Game {
    private int expertMode = 0;
    private State state;
    private int numOfPlayer;
    private Player[] players;
    private int[] orderPlayers;
    private int firstPlayerPlanPhase;
    private int currentPlayer;
    private LinkedList<IslandTile> islands;
    private CloudTile[] clouds;
    private Bag bag;
    private int motherNaturePosition = 0;
    private ArrayList<IslandTile> jislands;

    public void initializeGame() throws IncorrectArgumentException, IncorrectStateException {
        expertMode = 0;
        players = new Player[numOfPlayer];
        for (int i = 0; i < numOfPlayer; i++) {
            orderPlayers[i] = i;
        }
        currentPlayer = 0;

        for (Player p : players) {
            pickCharacter(p);
        }

        // initialization LinkedList<IslandTile> islands;
        //Import islands from json
        Gson gson= new Gson();
        try{
        InputStreamReader streamReader = new InputStreamReader(FillDeck.class.getResourceAsStream(GetPaths.ISLAND_TILES_LOCATION), StandardCharsets.UTF_8);
        JsonReader jsonReader = new JsonReader(streamReader);
        String fileContent = new String(Files.readAllBytes(Paths.get(GetPaths.ISLAND_TILES_LOCATION)));
        jislands= gson.fromJson(jsonReader, IslandTile.class);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        islands = new LinkedList<>(islands);
        for (int i = 0; i < 12; i++) {
            IslandTile island = new IslandTile("Island name from Json");
            islands.add(island);
        }

        // initialization clouds[];
        clouds = new CloudTile[numOfPlayer];
        for (int i = 0; i < numOfPlayer; i++) {
            CloudTile cloud = new CloudTile("Cloud name from Json");
            clouds[i] = cloud;
        }

        state = State.PLANNINGPHASE;
        updateState();
    }

    public void updateState() throws IncorrectArgumentException, IncorrectStateException {
        boolean playing = true;
        while (playing) {
            switch (state) {

                case PLANNINGPHASE:
                    while (state == State.PLANNINGPHASE) {
                        if (currentPlayer < numOfPlayer) {
                            drawFromBag();
                            playAssistantCard(currentPlayer);
                        } else {
                            firstPlayerPlanPhase = orderPlayers[0]; //first of the next planning phase
                            state = State.ACTIONPHASE;
                        }
                    }
                    break;

                case ACTIONPHASE:
                    while (state == State.ACTIONPHASE) {
                        if (orderPlayers.length > 0) {
                            currentPlayer = orderPlayers[0];
                            //orderPlayers.remove
                            //moveStudents()
                            //takeStudentsFromCloud();
                            //checkAndPlaceProfessor();
                            //checkAndPlaceTower();
                        } else {
                            state = State.ENDTURN;
                        }
                    }

                    break;

                case ENDTURN:
                    if (checkGameOver()) {
                        state = State.END;
                    } else {
                        state = State.PLANNINGPHASE;
                        currentPlayer = firstPlayerPlanPhase;
                    }
                    break;

                case END:
                    checkWinner();
                    break;

                default:
                    throw new IncorrectStateException();
            }
        }
    }


    public void drawFromBag() throws IncorrectArgumentException {
        for (CloudTile cloud : clouds) {
            int studentsDraw = -1;
            if (numOfPlayer == 3) studentsDraw = 4;
            else studentsDraw = 3;

            cloud.addStudents(bag.drawStudents(studentsDraw));
        }
    }

    public boolean checkGameOver() {
        return true;
    }

    public void playAssistantCard(int currentPlayer) throws IncorrectArgumentException, IncorrectStateException {
        //players[currentplayer].playAssistantCard();
        //it has to update the priority(current of orderPlayer[]) //amrit should implement Comparable interface to Player
        currentPlayer++; //next turn into Planning Phase (clockwise)
        updateState();
    }

    public void moveMotherNature(int distanceChoosen) { // UML to change
        int destinationMotherNature = motherNaturePosition+distanceChoosen;
        if(islands.get(motherNaturePosition).hasMotherNature()){
            if(players[currentPlayer].moveMotherNature(distanceChoosen)){
                islands.get(motherNaturePosition).removeMotherNature();
                islands.get(destinationMotherNature).moveMotherNature();
            }
            else{
                //return exception to view
            }

        }
        else{
            //eccezione ho perso madre natura
        }
    }

    public void checkUnificationIslands() {
        boolean listChanged = false;
        Iterator<IslandTile> it = islands.iterator();
        IslandTile current = islands.getFirst();
        while (it.hasNext()) {
            if (it.next().getOwner().equals(current.getOwner())) {
                current.sumStudentsUnification(it.next().getStudents());
                current.sumTowersUnification(it.next().getTowers());
                islands.remove(it.next());
                current = it.next();
                listChanged = true;
            }
        }

        if (islands.getLast().equals(current)) {
            IslandTile head = islands.getFirst();
            if (head.getOwner().equals(current.getOwner())) {
                current.sumStudentsUnification(head.getStudents());
                current.sumTowersUnification(head.getTowers());
                islands.remove(head);
                listChanged = true;
            }
        }

        if (listChanged) checkUnificationIslands();

    }

    public void checkAndPlaceProfessor() {

    }

    public void checkAndPlaceTower() {
        //ok
        checkUnificationIslands();
    }

    public void takeStudentsFromCloud(int index) {

    }

    public void checkWinner() {

    }

    public void pickCharacter(Player player) {
        // this function needs to modify Player Nickname that is private attribute.
    }

}
