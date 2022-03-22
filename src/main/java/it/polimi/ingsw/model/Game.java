package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.IncorrectStateException;
import it.polimi.ingsw.model.exceptions.MotherNatureLostException;
import it.polimi.ingsw.model.tiles.CloudTile;
import it.polimi.ingsw.model.tiles.IslandTile;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.PriorityQueue;

public class Game {
    private int expertMode = 0;
    private State state;
    private int numOfPlayer;
    private LinkedList<Player> players;
    private PriorityQueue<Player> orderPlayers;
    private Player currentPlayer;
    private Player firstPlayerPlanPhase;
    private LinkedList<IslandTile> islands;
    private CloudTile[] clouds;
    private Bag bag;
    private int motherNaturePosition;
    private int numRounds;
    private int numDrawnStudents;
    private int counter;
    private int playerDrawnOut;
    private ListIterator<Player> playerIterator;

    public void initializeGame() throws IncorrectArgumentException, IncorrectStateException {
        expertMode = 0;
        motherNaturePosition = 0;
        numRounds = 0;
        counter = numOfPlayer;
        if (numOfPlayer == 3) numDrawnStudents = 4;
        else numDrawnStudents = 3;

        //for (int i=0;i<numOfPlayer;i++) {
        //players.add(new Player());
        //pickCharacter(p);
        //}

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

    }

    public void nextPlayer(Player callerPlayer) {
        if (state == State.PLANNINGPHASE) { //TODO Player equals method
            if (counter > 0) {
                counter--;
                //TODO
            } else {
                state = State.ACTIONPHASE;
                counter = numOfPlayer;
                firstPlayerPlanPhase = orderPlayers.peek();
            }
        }
    }

    public void drawFromBag() throws IncorrectArgumentException {
        if (state == State.PLANNINGPHASE)
            for (CloudTile cloud : clouds) {
                cloud.addStudents(bag.drawStudents(numDrawnStudents));
            }

    }


    public void playAssistantCard(Player player) throws IncorrectArgumentException, IncorrectStateException {
        //players[currentplayer].playAssistantCard();
        //it has to update the priority(current of orderPlayer[]) //amrit should implement Comparable interface to Player

    }

    public void moveMotherNature(int distanceChoosen) throws IncorrectArgumentException, MotherNatureLostException {
        int destinationMotherNature = motherNaturePosition + distanceChoosen;
        if (islands.get(motherNaturePosition).hasMotherNature()) {
            if (currentPlayer.moveMotherNature(distanceChoosen)) {
                islands.get(motherNaturePosition).removeMotherNature();
                islands.get(destinationMotherNature).moveMotherNature();
            } else {
                throw new IncorrectArgumentException();
            }
        } else {
            throw new MotherNatureLostException();
        }
    }

    public void checkUnificationIslands() throws IncorrectArgumentException {
        boolean listChanged = false;
        ListIterator<IslandTile> it = islands.listIterator();
        IslandTile currentTile;
        IslandTile nextTile;
        while (it.hasNext()) {
            currentTile = it.next();
            if (it.hasNext()) nextTile = it.next();
            else nextTile = islands.getFirst();
            if (nextTile.getOwner().equals(currentTile.getOwner())) {
                currentTile.sumStudentsUnification(nextTile.getStudents());
                currentTile.sumTowersUnification(nextTile.getTowers());
                islands.remove(nextTile);
                listChanged = true;
            }
        }
        if (listChanged) checkUnificationIslands();
    }

    public boolean isGameOver() throws IncorrectArgumentException {
        if (!bag.hasEnoughStudents(numDrawnStudents) || islands.size() <= 3 || numRounds >= 9) return true;
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
