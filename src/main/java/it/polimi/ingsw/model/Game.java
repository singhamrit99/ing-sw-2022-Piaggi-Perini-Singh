package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.IncorrectPlayerException;
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
    private boolean playerDrawnOut;
    private ListIterator<Player> playerIterator;

    public void pickCharacter(Player player) {

    }

    public void initializeGame() throws IncorrectArgumentException, IncorrectStateException {
        expertMode = 0;
        motherNaturePosition = 0;
        numRounds = 0;
        counter = numOfPlayer - 1;
        if (numOfPlayer == 3) numDrawnStudents = 4;
        else numDrawnStudents = 3;

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

        //initialization LinkedList<Player>
        playerIterator = players.listIterator();
        firstPlayerPlanPhase = players.get((int) Math.random() * numOfPlayer); //random init player
        playerIterator.set(firstPlayerPlanPhase);
        playerDrawnOut = false;
        state = State.PLANNINGPHASE;

    }

    public void drawFromBag(Player playerCaller) throws IncorrectArgumentException {
        if (state == State.PLANNINGPHASE && playerCaller.equals(currentPlayer) && !playerDrawnOut) {
            for (CloudTile cloud : clouds) {
                cloud.addStudents(bag.drawStudents(numDrawnStudents));
            }
            playerDrawnOut = true;
        } else throw new IncorrectArgumentException();
    }

    public void playAssistantCard(Player player, int indexCard) throws IncorrectPlayerException, IncorrectStateException, IncorrectArgumentException {
        if (state == State.PLANNINGPHASE) {
            if (player.equals(currentPlayer) && playerDrawnOut) {  //playerDrawnOut = player has drawn from bag
                currentPlayer.playAssistantCard(indexCard);
                nextPlayer(currentPlayer);
            } else {
                throw new IncorrectPlayerException();
            }
        } else {
            throw new IncorrectStateException();
        }
    }

    private void nextPlayer(Player callerPlayer) throws IncorrectPlayerException, IncorrectArgumentException, IncorrectStateException {
        if (callerPlayer.equals(currentPlayer)) { //TODO Player equals method
            if (state == State.PLANNINGPHASE) {
                if (counter > 0) {
                    counter--;
                    if (playerIterator.hasNext()) currentPlayer = playerIterator.next();
                    else playerIterator.set(players.getFirst());
                    playerDrawnOut = false;
                } else {
                    state = State.ACTIONPHASE;
                    firstPlayerPlanPhase = orderPlayers.peek();
                }
            } else if (state == State.ACTIONPHASE) {
                if (!orderPlayers.isEmpty()) currentPlayer = orderPlayers.poll();
                else {
                    state = State.ENDTURN;
                    nextRound();
                }
            } else {
                throw new IncorrectStateException();
            }
        } else {
            throw new IncorrectPlayerException();
        }
    }

    public void nextRound() throws IncorrectArgumentException, IncorrectStateException {
        if (state == State.ENDTURN) {
            if (isGameOver()) {
                state = State.END;
                checkWinner();
            } else {
                state = State.PLANNINGPHASE;
                currentPlayer = firstPlayerPlanPhase;
                counter = numOfPlayer - 1;
            }
        } else throw new IncorrectStateException();
    }

    public boolean isGameOver() throws IncorrectArgumentException {
        if (!bag.hasEnoughStudents(numDrawnStudents) || islands.size() <= 3 || numRounds >= 9) return true;
        else return false;
    }


    public void takeStudentsFromCloud(Player playerCaller, int index) throws IncorrectStateException, IncorrectPlayerException {
        if (state == State.ACTIONPHASE) {
            if (playerCaller.equals(currentPlayer)) {
                //currentPlayer.moveStudents(clouds[index].removeStudents() waiting Amrit
            } else throw new IncorrectPlayerException();
        } else {
            throw new IncorrectStateException();
        }
    }


    public void moveStudents() {
        //currentPlayer.moveStudents();
    }


    public void moveMotherNature(int distanceChoosen) throws IncorrectArgumentException, MotherNatureLostException {
        int destinationMotherNature = motherNaturePosition + distanceChoosen;
        if (islands.get(motherNaturePosition).hasMotherNature()) {
            if (currentPlayer.moveMotherNature(distanceChoosen)) {
                islands.get(motherNaturePosition).removeMotherNature();
                islands.get(destinationMotherNature).moveMotherNature();
                //islands.get(destinationMotherNature).getStudents(); islands.getTowers() and islands.getOwner()
                //check which player has that color professor and eventually place the tower in that islands using checkAndPlaceTower
            } else {
                throw new IncorrectArgumentException();
            }
        } else {
            throw new MotherNatureLostException();
        }
    }

    public void checkAndPlaceProfessor() {
        //ask to each player how many students has
        // and based on that placing professor using Player Method
    }

    private void checkAndPlaceTower() throws IncorrectArgumentException { // UML this method became private
        // I need to decrement towers
        //I need to know towers number from player, if 0 -> wins
        checkUnificationIslands();
    }

    private void checkUnificationIslands() throws IncorrectArgumentException { // UML this method became private
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


    public void checkWinner() {
        //I need to know towers number from player, if 0 -> wins
    }
}
