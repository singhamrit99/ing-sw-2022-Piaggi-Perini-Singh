package it.polimi.ingsw.model;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.cards.AssistantCard;
import it.polimi.ingsw.model.cards.CharacterCard;
import it.polimi.ingsw.model.cards.FillDeck;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.IncorrectPlayerException;
import it.polimi.ingsw.model.exceptions.IncorrectStateException;
import it.polimi.ingsw.model.exceptions.MotherNatureLostException;
import it.polimi.ingsw.model.tiles.CloudTile;
import it.polimi.ingsw.model.tiles.IslandTile;


import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Game {
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
    private Scanner reader = new Scanner(System.in);
    private String fileContent;
    private ArrayList<IslandTile> importingIslands;
    private ArrayList<CloudTile> importingClouds;
    private ArrayList<CharacterCard> listOfCharacters;
    private String jsoncontent;

    public ArrayList<IslandTile> getimportingIslands(){
        return importingIslands;
    }
    public ArrayList<CloudTile> getImportingClouds(){
        return importingClouds;
    }
    public Game(int numOfPlayer, ArrayList<String> nicknames) throws IncorrectArgumentException{
        this.numOfPlayer = numOfPlayer;
        numRounds = 0;
        counter = numOfPlayer - 1; //used to 'count' during the Planning Phase
        if (numOfPlayer == 3) numDrawnStudents = 4;
        else numDrawnStudents = 3;

        //Initialization Players
        players = new LinkedList<Player>();
        int indexColorTeam = 1;
        for (String nickname : nicknames) {
            int colorTeam;
            if (numOfPlayer == 3) {
                colorTeam = indexColorTeam;
            } else {
                if (indexColorTeam % 2 == 1) {
                    colorTeam = 1; //black tower
                } else {
                    colorTeam = 3; //white tower
                }
            }
            Player newPlayer = new Player(nickname, Towers.values()[colorTeam], numOfPlayer);
            players.add(newPlayer);
            indexColorTeam++;
        }

        importingTilesJson();

        //Initialization clouds
        clouds = new CloudTile[numOfPlayer];
        for (int i = 0; i < numOfPlayer; i++) {
            CloudTile cloud = new CloudTile(importingClouds.get(i).getName());
            clouds[i] = cloud;
        }

        // initialization islands;
        islands = new LinkedList<>(islands);
        for (int i = 0; i < 12; i++) {
            IslandTile island = new IslandTile(importingIslands.get(i).getName());
            islands.add(island);
        }

        // place MotherNature on a random island
        motherNaturePosition = (int) (Math.random() * numOfPlayer);
        islands.get(motherNaturePosition).moveMotherNature();

        // create Bag and students
        EnumMap<Colors, Integer> students = new EnumMap(Colors.class);
        for (Colors studentColor : Colors.values()) {
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
        students = new EnumMap(Colors.class);
        for (Colors studentColor : Colors.values()) {
            students.put(studentColor, 24); //26  (total discStudents) -2 (used before) for each color
        }
        bag = new Bag(students);

        //initialization LinkedList<Player>
        playerIterator = players.listIterator();
        firstPlayerPlanPhase = players.get((int)(Math.random() * numOfPlayer)); //random init player
        playerIterator.set(firstPlayerPlanPhase);
        playerDrawnOut = false;
        state = State.PLANNINGPHASE;
        orderPlayers = new PriorityQueue<>(numOfPlayer);
    }

    public void importingCharactersJson(){
        Gson gson = new Gson();
        try {
            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(AssistantCard.class.getResourceAsStream(FilePaths.CHARACTER_CARDS_LOCATION)), StandardCharsets.UTF_8);
            Scanner s = new Scanner(streamReader).useDelimiter("\\A");
             jsoncontent = s.hasNext() ? s.next() : "";
        } catch (Exception FileNotFound) {
            FileNotFound.printStackTrace();
        }
        listOfCharacters = gson.fromJson(jsoncontent, new TypeToken<List<IslandTile>>() {}.getType());

    }

    public void pickCharacter(Player player) {
        int answer = 0;
        char response = 0;
        boolean getnewinput = true;
        importingCharactersJson();
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

    public void importingTilesJson() {
        //Loading IslandTiles Json file
        Gson gson = new Gson();
        try {
            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(AssistantCard.class.getResourceAsStream(FilePaths.ISLAND_TILES_LOCATION)), StandardCharsets.UTF_8);
            Scanner s = new Scanner(streamReader).useDelimiter("\\A");
             jsoncontent = s.hasNext() ? s.next() : "";
        } catch (Exception FileNotFound) {
            FileNotFound.printStackTrace();
        }
        importingIslands = gson.fromJson(jsoncontent, new TypeToken<List<IslandTile>>() {}.getType());


        // initialization clouds;
        try { //Loading CloudTiles JSON file
            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(FillDeck.class.getResourceAsStream(GetPaths.CLOUD_TILES_LOCATION)), StandardCharsets.UTF_8);
            Scanner s = new Scanner(streamReader).useDelimiter("\\A");
            jsoncontent = s.hasNext() ? s.next() : "";
        } catch (Exception FileNotFound) {
            FileNotFound.printStackTrace();
        }
        importingClouds = gson.fromJson(jsoncontent, new TypeToken<List<CloudTile>>() {}.getType());
    }

    public void drawFromBag(String nicknameCaller) throws IncorrectArgumentException {
        if (state == State.PLANNINGPHASE && nicknameCaller.equals(currentPlayer.getNickname()) && !playerDrawnOut) {
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
        nextPlayer(currentPlayer.getNickname());
    }

    private void nextPlayer(String nicknameCaller) throws IncorrectPlayerException, IncorrectArgumentException, IncorrectStateException {
        if (nicknameCaller.equals(currentPlayer.getNickname())) { //TODO Player equals method
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
    public void moveStudents(EnumMap<Colors, Integer> students, ArrayList<Integer> destinations, ArrayList<String> islandDestinations) throws IncorrectArgumentException {
        int DestCounter = 0;
        EnumMap<Colors, Integer> studentsToMoveToIsland = new EnumMap<>(Colors.class);
        if (students.size() == destinations.size()) {
            for (Map.Entry<Colors, Integer> set : students.entrySet()) {
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

        checkAndPlaceProfessor(); //maybe some students have arrived in the dining table
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
        for (Colors studentColor : Colors.values()) {
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
        HashMap<Towers, Integer> influenceScores = new HashMap<>();
        influenceScores.put(Towers.BLACK, 0);
        influenceScores.put(Towers.WHITE, 0);
        if (numOfPlayer % 2 == 1) influenceScores.put(Towers.GREY, 0);

        EnumMap<Colors, Integer> students = island.getStudents();

        for (Colors studentColor : Colors.values()) {
            if (students.get(studentColor) != 0){
                for (Player p : players) {
                    if (p.hasProfessorOfColor(studentColor)){ //find the player with that professor
                        Towers teamColor = p.getTowerColor();
                        influenceScores.replace(teamColor, influenceScores.get(teamColor) + students.get(studentColor));
                        if (teamColor.equals(island.getTowersColor())) { //counting the towers if team owns the island
                            influenceScores.replace(teamColor, influenceScores.get(teamColor) + island.getNumOfTowers());
                        }
                    }
                }
            }
        }

        Towers newTeamOwner = null; //new owner
        int maxScore = 0;
        for (Map.Entry<Towers, Integer> team : influenceScores.entrySet()) {
            if (team.getValue() > maxScore) {
                newTeamOwner = team.getKey();
                maxScore = team.getValue();
            } else if (team.getValue() == maxScore) {
                newTeamOwner = null; // there is a TIE! -> no new Team owner
            }
        }

        if (newTeamOwner != null) {
            ArrayList<Player> newTeam = findPlayerFromTeam(newTeamOwner);
            if (island.getTowersColor() == null){// The island was empty
                moveTowersFromTeam(newTeam, -island.getNumOfTowers());
            } else if (newTeamOwner != island.getTowersColor()) { //it means that there is a switch from team
                int switchedTowers = island.getNumOfTowers();
                moveTowersFromTeam(newTeam, -switchedTowers); //removing towers from new team player
                ArrayList<Player> oldTeam = findPlayerFromTeam(island.getTowersColor()); //oldTeamOwnerShip
                moveTowersFromTeam(oldTeam, switchedTowers); //adding towers to old team
                island.setTowersColor(newTeamOwner); //set ownership
            }
        }
    }

    public ArrayList<Player> findPlayerFromTeam(Towers teamColor) {
        Player firstPlayer = null;
        Player secondPlayer = null; // in case of 4 players I have to check both players of the team
        for (Player p : players) {
            if (p.getTowerColor() == teamColor) if (firstPlayer == null) {
                firstPlayer = p;
                if (numOfPlayer != 4) break;
            } else {
                secondPlayer = p;
            }
        }
        ArrayList<Player> returnedPlayers = new ArrayList<Player>();
        returnedPlayers.add(firstPlayer);
        if (secondPlayer != null) returnedPlayers.add(secondPlayer);

        return returnedPlayers;
    }

    public void moveTowersFromTeam(ArrayList<Player> team, int amount) {
        int numbersOfIterations = Math.abs(amount);
        int oneTowerSigned;
        if (amount < 0) oneTowerSigned = -1;
        else oneTowerSigned = 1;

        if (team.get(1) != null) { //It means we are 4 players game
            while (numbersOfIterations > 0) {
                if (oneTowerSigned > 0) { //this so that I always add/remove from the correct player
                    if (team.get(0).getPlayerTowers() <= team.get(1).getPlayerTowers())
                        team.get(0).moveTowers(oneTowerSigned);
                    else team.get(1).moveTowers(oneTowerSigned);
                    numbersOfIterations--;
                } else {
                    if (team.get(0).getPlayerTowers() >= team.get(1).getPlayerTowers())
                        team.get(0).moveTowers(oneTowerSigned);
                    else team.get(1).moveTowers(oneTowerSigned);
                    numbersOfIterations--;
                }
            }
        } else team.get(0).moveTowers(amount);
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
            if (nextTile.getTowersColor().equals(currentTile.getTowersColor())) {
                currentTile.addStudents(nextTile.getStudents());
                currentTile.sumTowers(nextTile.getNumOfTowers());
                islands.remove(nextTile);
                listChanged = true;
            }
        }
        if (listChanged) checkUnificationIslands();
    }

    public boolean isGameOver() throws IncorrectArgumentException {
        for (Player p : players) {
            if (p.getPlayerTowers() <= 0) return true;
        }
        if (!bag.hasEnoughStudents(numDrawnStudents) || islands.size() <= 3 || numRounds >= 9) return true;
        else return false;
    }

    public void checkWinner() {
        for (Player p : players) {
            if (p.getPlayerTowers() == 0) winner = p;
        }

    }

}
