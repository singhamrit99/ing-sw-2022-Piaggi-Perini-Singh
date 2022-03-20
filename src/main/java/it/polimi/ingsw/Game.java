package it.polimi.ingsw;

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
                //checkUnificationIslands();
                //checkAndPlaceProfessor();
                //checkAndPlaceTower();
                //if(orderPlayers queue has length 0) state==State.ENDTURN
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


    public void drawFromBag() {
        for (int c = 0; c < clouds.length; c++) {
            // 3 students for clouds in 2 and 4 players mode, else if 3 players mode we need 4 students
            try {
                clouds[c].addStudents(bag.drawStudents()); //bag.drawStudents should have how many students n have to be spawned
            } catch (IncorrectArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkGameOver() {
        return true;
    }

    public void playAssistantCard(int currentPlayer) {
        //players[currentPlayer].playAssistantCard();
        //it has to update the priority queue of orderPlayer[]
        currentPlayer++; // next turn into Planning Phase (clockwise)
        updateState();
    }

    public void moveMotherNature(int distance) {
        motherNature.moveTile(distance);
    }

    public void checkUnificationIslands() {
        //IslandTile currentIsland;
        //currentIsland = islands.getFirst();
        //for(int i = 0; i<= islands.size();i++){
        //if(islands.
        //}
    }

    public void checkAndPlaceProfessor() {

    }

    public void checkAndPlaceTower() {

    }

    public void takeStudentsFromCloud(int index) {

    }

    public void checkWinner() {

    }

    public void pickCharacter(Player player) {
        // this function needs to modify Player Nickname that is private attribute.
    }

}
