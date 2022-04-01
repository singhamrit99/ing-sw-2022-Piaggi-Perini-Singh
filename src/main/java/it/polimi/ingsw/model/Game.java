package it.polimi.ingsw.model;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.model.cards.CharacterCard;
import it.polimi.ingsw.model.cards.FillCharacterDeck;
import it.polimi.ingsw.model.cards.FillDeck;
import it.polimi.ingsw.model.enumerations.Students;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.IncorrectPlayerException;
import it.polimi.ingsw.model.exceptions.IncorrectStateException;
import it.polimi.ingsw.model.exceptions.MotherNatureLostException;
import it.polimi.ingsw.model.tiles.CloudTile;
import it.polimi.ingsw.model.tiles.IslandTile;


import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Game {
    private int expertMode = 0;
    private State state;
    private Bag bag;
    private int numOfPlayer;
    private Player currentPlayer;
    private Player firstPlayerPlanPhase;
    private LinkedList<Player> players;
    private ListIterator<Player> playerIterator;
    private PriorityQueue<Player> orderPlayers;
    private LinkedList<IslandTile> islands;
    private CloudTile[] clouds;
    private int motherNaturePosition;
    private int numRounds;
    private int numDrawnStudents;
    private int counter;
    private boolean playerDrawnOut;
    private Player winner;
    private ArrayList<CharacterCard> listOfCharacters;
    private FillCharacterDeck characterDeckBuilder;
    private Scanner reader = new Scanner(System.in);
    private String fileContent;


    public void loadCharacters() {

        characterDeckBuilder.newDeck(listOfCharacters);

    }

    public void pickCharacter(Player player) {
        int answer = 0;
        char response = 0;
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
            } else {
                System.out.println("You chose character number " + answer + ". Are you sure? Y/N \n");
                response = reader.next().charAt(0);
                if (response == 'y' || response == 'Y')
                    getnewinput = false;
            }
        }
        System.out.println("You chose character number " + answer + ".");
        player.setCharacterCard(answer);
    }


    public void initializeGame() throws IncorrectArgumentException, IncorrectStateException {
        expertMode = 0;
        motherNaturePosition = 0;
        numRounds = 0;
        counter = numOfPlayer - 1;
        if (numOfPlayer == 3) numDrawnStudents = 4;
        else numDrawnStudents = 3;

        // initialization islands;
        Gson gson = new Gson(); //Loading IslandTiles Json file
        try {
            InputStreamReader streamReader = new InputStreamReader(FillDeck.class.getResourceAsStream(GetPaths.ISLAND_TILES_LOCATION), StandardCharsets.UTF_8);
            JsonReader jsonReader = new JsonReader(streamReader);
            fileContent = new String(Files.readAllBytes(Paths.get(GetPaths.ISLAND_TILES_LOCATION)));
        } catch (Exception FileNotFound) {
            FileNotFound.printStackTrace();
        }
        IslandTile[] importingIslands = gson.fromJson(fileContent, IslandTile[].class);
        islands = new LinkedList<>(islands);
        for (int i = 0; i < 12; i++) {
            IslandTile island = new IslandTile(importingIslands[i].getName());
            islands.add(island);
        }

        // initialization clouds;
        try { //Loading CloudTiles JSON file
            InputStreamReader streamReader = new InputStreamReader(FillDeck.class.getResourceAsStream(GetPaths.CLOUD_TILES_LOCATION), StandardCharsets.UTF_8);
            JsonReader jsonReader = new JsonReader(streamReader);
            fileContent = new String(Files.readAllBytes(Paths.get(GetPaths.ISLAND_TILES_LOCATION)));
        } catch (Exception FileNotFound) {
            FileNotFound.printStackTrace();
        }
        CloudTile[] importingClouds = gson.fromJson(fileContent, CloudTile[].class);
        clouds = new CloudTile[numOfPlayer];
        for (int i = 0; i < numOfPlayer; i++) {
            CloudTile cloud = new CloudTile(importingClouds[i].name);
            clouds[i] = cloud;
        }

        //initialization LinkedList<Player>
        playerIterator = players.listIterator();
        firstPlayerPlanPhase = players.get((int) Math.random() * numOfPlayer); //random init player
        playerIterator.set(firstPlayerPlanPhase);
        playerDrawnOut = false;
        state = State.PLANNINGPHASE;
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

    /*public void drawFromBag(Player playerCaller, SetupCard setup) throws IncorrectArgumentException
    {
        if (state== State.ACTIONPHASE && playerCaller.equals(currentPlayer)) {



        }


    }*/

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
        int DestCounter = 0;
        HashMap<StudentDisc, Integer> studentsToMoveToIsland = new HashMap<>();
        if (students.size() == destinations.size()) {
            for (Map.Entry<StudentDisc, Integer> set : students.entrySet()) {
                if (destinations.get(DestCounter) == 1) {
                    studentsToMoveToIsland.put(set.getKey(), set.getValue());
                }
                DestCounter++;
            }
        } else {
            throw new IncorrectArgumentException();
        }
        //The game sends to the player also the students that go to the islands because the player has to remove them from the entrance
        currentPlayer.moveStudents(students, destinations);
        //If there are students directed to the islands I check that that the islands string arraylist has the right size
        if (studentsToMoveToIsland.size() != 0 && studentsToMoveToIsland.size() == islandDestinations.size()) {
            boolean islandNameFound = true;
            for (String dest : islandDestinations) {
                if (islandNameFound) { //I check that the Island that I was searching in the last iteration it's found
                    islandNameFound = false;
                    for (IslandTile island : islands) {
                        if (island.getName().equals(dest)) {
                            islandNameFound = true;
                            island.addStudents(studentsToMoveToIsland);
                            break;
                        }
                    }
                } else {
                    throw new IncorrectArgumentException("The island is not found");
                }
            }
        }
    }


    public void moveMotherNature(int distanceChoosen) throws IncorrectArgumentException, MotherNatureLostException {
        int destinationMotherNature = motherNaturePosition + distanceChoosen;
        if (islands.get(motherNaturePosition).hasMotherNature()) {
            if (currentPlayer.moveMotherNature(distanceChoosen)) {
                islands.get(motherNaturePosition).removeMotherNature();
                islands.get(destinationMotherNature).moveMotherNature();
                //check which player has that color professor

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
        for (int i = 0; i < Students.values().length; i++) {
            for (Player p : players) {
                if (p.getStudentsByStudent(Students.getStudent(i)) > max) {
                    maxPlayer = p;
                }
            }
            maxPlayer.moveProfessor(Students.getStudent(i)); //Maybe Amrit will change the methods like this
        }
        //Based on AMrit changes I will probably have to tell to each player if is not the max one to remove the professor with that color
    }

    private void checkAndPlaceTower() throws IncorrectArgumentException {
        // I need to decrement towers
        //I need to know towers number from player, if 0 -> wins
        checkUnificationIslands();
    }

    private void checkUnificationIslands() throws IncorrectArgumentException {
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

    public boolean isGameOver() throws IncorrectArgumentException {
        if (!bag.hasEnoughStudents(numDrawnStudents) || islands.size() <= 3 || numRounds >= 9) return true;
        else return false;
    }

    public void checkWinner() {
        for (Player p : players) {
            if (p.getPlayerTowers() == 0) winner = p;
        }

    }

}
