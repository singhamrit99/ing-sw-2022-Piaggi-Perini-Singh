package it.polimi.ingsw.model;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.sun.org.apache.xerces.internal.parsers.IntegratedParserConfiguration;
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
import jdk.internal.org.jline.utils.Colors;


import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Game {
    private boolean expertMode;
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
    private IslandTile[] importingIslands;
    private CloudTile[] importingClouds;

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

    public Game(boolean expertMode, int numOfPlayer) {
        this.expertMode = expertMode;
        this.numOfPlayer = numOfPlayer;
    }

    private void importingIslandsFromJson() {
        //Loading IslandTiles Json file
        Gson gson = new Gson();
        try {
            InputStreamReader streamReader = new InputStreamReader(FillDeck.class.getResourceAsStream(GetPaths.ISLAND_TILES_LOCATION), StandardCharsets.UTF_8);
            JsonReader jsonReader = new JsonReader(streamReader);
            fileContent = new String(Files.readAllBytes(Paths.get(GetPaths.ISLAND_TILES_LOCATION)));
        } catch (Exception FileNotFound) {
            FileNotFound.printStackTrace();
        }
        importingIslands = gson.fromJson(fileContent, IslandTile[].class);


        // initialization clouds;
        try { //Loading CloudTiles JSON file
            InputStreamReader streamReader = new InputStreamReader(FillDeck.class.getResourceAsStream(GetPaths.CLOUD_TILES_LOCATION), StandardCharsets.UTF_8);
            JsonReader jsonReader = new JsonReader(streamReader);
            fileContent = new String(Files.readAllBytes(Paths.get(GetPaths.ISLAND_TILES_LOCATION)));
        } catch (Exception FileNotFound) {
            FileNotFound.printStackTrace();
        }
        importingClouds = gson.fromJson(fileContent, CloudTile[].class);
    }

    public void initializeGame() throws IncorrectArgumentException, IncorrectStateException {
        numRounds = 0;
        counter = numOfPlayer - 1;
        if (numOfPlayer == 3) numDrawnStudents = 4;
        else numDrawnStudents = 3;

        importingIslandsFromJson();

        //Initialization clouds
        clouds = new CloudTile[numOfPlayer];
        for (int i = 0; i < numOfPlayer; i++) {
            CloudTile cloud = new CloudTile(importingClouds[i].name);
            clouds[i] = cloud;
        }

        // initialization islands;
        islands = new LinkedList<>(islands);
        for (int i = 0; i < 12; i++) {
            IslandTile island = new IslandTile(importingIslands[i].getName());
            islands.add(island);
        }

        // place MotherNature on a random island
        motherNaturePosition = (int) (Math.random() * numOfPlayer);
        islands.get(motherNaturePosition).moveMotherNature();

        // create Bag and students
        EnumMap<Students, Integer> students = new EnumMap(Students.class);
        for (Students studentColor : Students.values()) {
            students.put(studentColor, 2);
        }
        bag = new Bag(students);

        //calculate opposite MotherNature's Island
        int oppositeMotherNaturePos = 0;
        if (motherNaturePosition >= islands.size() / 2)
            oppositeMotherNaturePos = motherNaturePosition + islands.size() - 1;
        else oppositeMotherNaturePos = motherNaturePosition - islands.size() + 1;
        // placing students except MotherNature's Island and the opposite one
        IslandTile islandOppositeMN = islands.get(oppositeMotherNaturePos);
        for (IslandTile island : islands) {
            if (!island.hasMotherNature() && !(island.getName().equals(islandOppositeMN.getName()))) {
                island.addStudents(bag.drawStudents(1));
            }
        }

        //Re-populate the Bag after 'placing Islands and Students phase'
        students = new EnumMap(Students.class);
        for (Students studentColor : Students.values()) {
            students.put(studentColor, 24); //26  (total discStudents) -2 (used before) for each color
        }
        bag = new Bag(students);

        //initialization LinkedList<Player>
        playerIterator = players.listIterator();
        firstPlayerPlanPhase = players.get((int) Math.random() * numOfPlayer); //random init player
        playerIterator.set(firstPlayerPlanPhase);
        playerDrawnOut = false;
        state = State.PLANNINGPHASE;
        orderPlayers = new PriorityQueue<>(numOfPlayer);
    }

    public void drawFromBag(Player playerCaller) throws IncorrectArgumentException {
        if (state == State.PLANNINGPHASE && playerCaller.getNickname().equals(currentPlayer.getNickname()) && !playerDrawnOut) {
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

    public void takeStudentsFromCloud(String nicknameCaller, int index) throws IncorrectStateException, IncorrectPlayerException, IncorrectArgumentException {
        if (state == State.ACTIONPHASE) {
            if (nicknameCaller.equals(currentPlayer.getNickname())) {
                currentPlayer.addStudents(clouds[index].removeStudents());
            } else throw new IncorrectPlayerException();
        } else {
            throw new IncorrectStateException();
        }
    }

    //0 dining room , 1 to island tile
    public void moveStudents(EnumMap<Students, Integer> students, ArrayList<Integer> destinations, ArrayList<String> islandDestinations) throws IncorrectArgumentException {
        int DestCounter = 0;
        EnumMap<Students, Integer> studentsToMoveToIsland = new EnumMap<>(Students.class);
        if (students.size() == destinations.size()) {
            for (Map.Entry<Students, Integer> set : students.entrySet()) {
                if (destinations.get(DestCounter) == 1) {
                    studentsToMoveToIsland.put(set.getKey(), set.getValue());
                }
                DestCounter++;
            }
        } else {
            throw new IncorrectArgumentException();
        }
        //Sending ALL the students (including the islands ones) so that Player remove them from the entrance
        currentPlayer.moveStudents(students, destinations);

        if (!studentsToMoveToIsland.isEmpty() && studentsToMoveToIsland.size() == islandDestinations.size()) {
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
                checkAndPlaceTower(islands.get(destinationMotherNature));
                checkUnificationIslands();

            } else {
                throw new IncorrectArgumentException();
            }
        } else {
            throw new MotherNatureLostException();
        }
    }

    public void checkAndPlaceProfessor() throws IncorrectArgumentException {
        int max = 0;
        Player maxPlayer = null;
        for (Students studentColor : Students.values()) {
            for (Player player : players) {
                if (player.getNumOfStudent(studentColor) > max) {
                    maxPlayer = player;
                    max = player.getNumOfStudent(studentColor);
                } else if (player.getNumOfStudent(studentColor) == max) {
                    maxPlayer = null; //in case of ties noone should have assign the professor
                }
            }
            if (maxPlayer != null) {
                for (Player player : players) { //eventually remove all the players that had that professor
                    if (player.hasProfessorOfColor(studentColor)) player.removeProfessor(studentColor);
                }
                maxPlayer.addProfessor(studentColor);
            }
        }
    }

    private void checkAndPlaceTower(IslandTile island) throws IncorrectArgumentException {
        HashMap<Player, Integer> influenceScores = new HashMap<>();
        for (Player p : players) {
            influenceScores.put(p, 0);
        }

        EnumMap<Students, Integer> students = island.getStudents();

        for (Students studentColor : Students.values()) {
            if (students.get(studentColor) != 0) {
                //find the player with that professor
                for (Player p : players) {
                    if (p.hasProfessorOfColor(studentColor)) {
                        influenceScores.replace(p, influenceScores.get(p) + students.get(studentColor));
                        //If it's the island owner I also add the towers number
                        if (p.getNickname().equals(island.getOwner().getNickname())) {
                            influenceScores.replace(p, influenceScores.get(p) + island.getNumOfTowers());
                        }
                    }
                }
            }

            Player maxScorePlayer = null; //new owner
            int maxScore = 0;
            for (Player p : players) {
                if (influenceScores.get(p) > maxScore) {
                    maxScorePlayer = p;
                    maxScore = influenceScores.get(p);
                } else if (influenceScores.get(p) == maxScore) {
                    maxScorePlayer = null; // there is a TIE!
                }
            }

            if (maxScorePlayer != null) { //there is a max player
                if (island.getOwner() == null) {
                    island.setOwner(maxScorePlayer);
                    maxScorePlayer.moveTowers(-1);
                    island.sumTowers(1);
                    checkUnificationIslands();
                } else if ((!maxScorePlayer.getNickname().equals(island.getOwner().getNickname()))) { //the player with max influence is not the owner
                    //I tell the owner to remove his towers
                    int towersRemoved = island.getNumOfTowers();
                    island.getOwner().moveTowers(towersRemoved);
                    // Taking the same amount of towers from the new owner
                    maxScorePlayer.moveTowers(towersRemoved);
                    // Finally declaring the new owner
                    island.setOwner(maxScorePlayer);
                    checkUnificationIslands();
                }
            }
        }
    }

    // I make an array[numOfPlayer] in that array for each player I check all the students in that island


    //I need to know towers number from player, if 0 -> wins
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
        //for(Player p: players) {
           // if p.tower
        //}

        if (!bag.hasEnoughStudents(numDrawnStudents) || islands.size() <= 3 || numRounds >= 9 ) return true;
        else return false;
    }

    public void checkWinner() {
        for (Player p : players) {
            if (p.getPlayerTowers() == 0) winner = p;
        }

    }

}
