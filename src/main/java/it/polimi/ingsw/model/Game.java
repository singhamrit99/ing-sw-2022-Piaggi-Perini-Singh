package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;

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
    private MotherNaturePawn motherNature;

    public void initializeGame() {
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

    public void updateState() {
        boolean playing = true;
        while (playing) {
            switch (state) {

                case PLANNINGPHASE:
                    if (currentPlayer < numOfPlayer) {
                        drawFromBag();
                        playAssistantCard(currentPlayer);
                    } else {
                        firstPlayerPlanPhase = orderPlayers[0]; //first of the next planning phase
                        state = State.ACTIONPHASE;
                        updateState();
                    }
                    break;

                case ACTIONPHASE:
                    //moveStudents()
                    //takeStudentsFromCloud();
                    //checkAndPlaceProfessor();
                    //checkAndPlaceTower();
                    //if(orderPlayers(has length 0) state==State.ENDTURN
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

                //default:
                //exception
            }
        }
    }


    public void drawFromBag() {
        for (int c = 0; c < clouds.length; c++) {
            int studentsDraw = -1;
            if (numOfPlayer == 3) studentsDraw = 4;
            else studentsDraw = 3;
            try {
                clouds[c].addStudents(bag.drawStudents(studentsDraw));
            } catch (IncorrectArgumentException e) {
                System.out.println("Incorrect Argument Exception");
            }
        }
    }

    public boolean checkGameOver() {
        return true;
    }

    public void playAssistantCard(int currentPlayer) {
        //players[currentplayer].playAssistantCard();
        //it has to update the priority(current of orderPlayer[]) //AMRIT should implement Comparable interface to Player
        currentPlayer++; // next turn into Planning Phase (clockwise)
        updateState();
    }

    public void moveMotherNature(int distance) {
        motherNature.moveTile(distance);
    }

    public void checkUnificationIslands(){
        boolean listChanged = false;
        Iterator<IslandTile> it = islands.iterator();
        IslandTile current = islands.getFirst();
        while (it.hasNext()){
            if (it.next().getOwner().equals(current.getOwner())) {
                current.sumStudentsUnification(it.next().getStudents());
                current.sumTowersUnification(it.next().getTowers());
                islands.remove(it.next());
                current = it.next();
                listChanged = true;
            }
        }

        if(islands.getLast().equals(current)){
            IslandTile head = islands.getFirst();
            if (head.getOwner().equals(current.getOwner())){
                current.sumStudentsUnification(head.getStudents());
                current.sumTowersUnification(head.getTowers());
                islands.remove(head);
                listChanged= true;
            }
        }

        if(listChanged)checkUnificationIslands();

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
