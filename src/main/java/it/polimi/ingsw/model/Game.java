package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.IncorrectStateException;
import it.polimi.ingsw.model.tiles.CloudTile;
import it.polimi.ingsw.model.tiles.IslandTile;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Game {
    private int expertMode = 0;
    private State state;
    private int numOfPlayer;
    private Player[] players;
    private PriorityQueue<Player> orderPlayers;
    private Player currentPlayer;
    private Player firstPlayerPlanPhase;
    private LinkedList<IslandTile> islands;
    private CloudTile[] clouds;
    private Bag bag;
    private int motherNaturePosition;
    private int numRounds;
    private int numDrawnStudents;

    public void initializeGame() throws IncorrectArgumentException, IncorrectStateException {
        expertMode = 0;
        motherNaturePosition = 0;
        numRounds = 0;

        if (numOfPlayer == 3) numDrawnStudents = 4;
        else numDrawnStudents = 3;

        players = new Player[numOfPlayer];
        //for (int i = 0; i < numOfPlayer; i++) {  //doubt iterator
        //    orderPlayers.add(players[i]);
        //}
        //currentPlayer = 0;

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

    public void updateState() throws IncorrectArgumentException, IncorrectStateException {
        boolean playing = true;
        while (playing) {
            switch (state) {
                case PLANNINGPHASE:
                    int counter = 0;
                    while (state == State.PLANNINGPHASE) {
                        if (counter < numOfPlayer) {  //clockwise
                            counter++;
                            drawFromBag();
                            //playAssistantCard(player);
                        } else {
                            state = State.ACTIONPHASE;
                            firstPlayerPlanPhase = orderPlayers.peek(); //first of the next planning phase
                        }
                    }
                    break;

                case ACTIONPHASE:
                    while (state == State.ACTIONPHASE) {
                        if (islands.size() <= 3) state = State.END; //the game finishes with 3 islands
                        else if (!orderPlayers.isEmpty()) { //3 islands immediately terminate the game
                            currentPlayer = orderPlayers.poll();
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
                    numRounds++;
                    if (isGameOver()) {
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
            cloud.addStudents(bag.drawStudents(numDrawnStudents));
        }
    }


    public void playAssistantCard(Player player) throws IncorrectArgumentException, IncorrectStateException {
        //players[currentplayer].playAssistantCard();
        //it has to update the priority(current of orderPlayer[]) //amrit should implement Comparable interface to Player
        updateState();
    }

    public void moveMotherNature(int distanceChoosen) {
        int destinationMotherNature = motherNaturePosition + distanceChoosen;
        if (islands.get(motherNaturePosition).hasMotherNature()) {
            if (currentPlayer.moveMotherNature(distanceChoosen)) {
                islands.get(motherNaturePosition).removeMotherNature();
                islands.get(destinationMotherNature).moveMotherNature();
            } else {
                //return exception to view
            }

        } else {
            //eccezione ho perso madre natura
        }
    }

    public void checkUnificationIslands() throws IncorrectArgumentException {
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

    public boolean isGameOver() throws IncorrectArgumentException {
        if (!bag.hasEnoughStudents(numDrawnStudents)) return true;
        else if (numRounds >= 9) return true;
        else return false;
    }

    public void checkAndPlaceProfessor() {

    }

    public void checkAndPlaceTower() throws IncorrectArgumentException {
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
