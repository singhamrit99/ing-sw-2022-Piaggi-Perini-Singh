package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.CharacterCard;
import it.polimi.ingsw.model.cards.FillCharacterDeck;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.IncorrectPlayerException;
import it.polimi.ingsw.model.exceptions.IncorrectStateException;
import it.polimi.ingsw.model.exceptions.MotherNatureLostException;
import it.polimi.ingsw.model.tiles.CloudTile;
import it.polimi.ingsw.model.tiles.IslandTile;


import java.awt.*;
import java.util.*;

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
    private Player winner;
    private ArrayList<CharacterCard> listOfCharacters;
    private FillCharacterDeck characterDeckBuilder;
    private Scanner reader = new Scanner(System.in);

    public void loadCharacters() {

        characterDeckBuilder.newDeck(listOfCharacters);

    }

    public void pickCharacter(Player player) {
        int answer;
        boolean getnewinput = true;
        System.out.println("Choose your character!");

        for (CharacterCard c : listOfCharacters) {
            System.out.println("Power number" + c.getCharacterID() + ":" + c.getPowerDescription() + "\n");
        }
        while (getnewinput) {
            System.out.println("Choose a power 1-12!\n");
            answer = reader.nextInt();
            if (answer > 12 || answer < 1) {
                System.out.println("Invalid character number! Try again\n");
            }
            else {
                getnewinput = false;
            }

        }
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

        //initialization PriorityQueue<Player>
        orderPlayers = new PriorityQueue<>(numOfPlayer);
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

            } else {
                throw new IncorrectPlayerException();
            }
        } else {
            throw new IncorrectStateException();
        }
        orderPlayers.add(currentPlayer);
        nextPlayer(currentPlayer);
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

    //0 dining room , 1 to island tile
    public void moveStudents(HashMap<StudentDisc, Integer> students, ArrayList<Integer> destinations, ArrayList<String> islandDestinations) throws IncorrectArgumentException {
        int i = 0;
        HashMap<StudentDisc, Integer> studentsToMoveToIsland = new HashMap<>();
        if (students.size() == destinations.size()) {
            for (Map.Entry<StudentDisc, Integer> set : students.entrySet()) {
                if (destinations.get(i) == 1) {
                    studentsToMoveToIsland.put(set.getKey(), set.getValue());

                } else {
                    throw new IncorrectArgumentException();

                }
                i++;
            }
        } else {
            throw new IncorrectArgumentException();
        }

        currentPlayer.moveStudents(students, destinations);
        /*
        if (studentsToMoveToIsland.size() != 0 && studentsToMoveToIsland.size() == islandDestinations.size()){
            boolean found = false;
            for (String dest : islandDestinations){
                for(IslandTile island : islands ){
                    if(island.getName().equals){
                        found = true;
                        //island.addStudents(studentsToMoveToIsland;
                        break;
                    }
                }
            }
        }
        */

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

    public void checkAndPlaceProfessor() throws IncorrectArgumentException {
        int max = 0;
        Player maxPlayer = players.getFirst();
        for (int i = 0; i < Colors.values().length; i++) {
            for (Player p : players) {
                if (p.getStudentsByColor(Colors.getColor(i)) > max) {
                    maxPlayer = p;
                }
            }
            maxPlayer.moveProfessor(Colors.getColor(i)); //Maybe Amrit will change the methods like this
        }
        //Based on AMrit changes I will probably have to tell to each player if is not the max one to remove the professor with that color
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
                currentTile.addStudents(nextTile.getStudents());
                currentTile.sumTowersUnification(nextTile.getTowers());
                islands.remove(nextTile);
                listChanged = true;
            }
        }
        if (listChanged) checkUnificationIslands();
    }


    public void checkWinner() {
        for (Player p : players) {
            if (p.getPlayerTowers() == 0) winner = p;
        }

    }

}
