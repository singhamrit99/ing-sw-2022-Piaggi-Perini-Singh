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
import java.lang.reflect.Array;
import java.util.*;

public class Game {
    private boolean expertMode;
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
    public void loadCharacters(){

        characterDeckBuilder.newDeck(listOfCharacters);

    }
    public void pickCharacter(Player player) {
        int answer;
        boolean getnewinput= true;
        System.out.println("Choose your character!");

        for(CharacterCard c: listOfCharacters)
        {
                System.out.println("Power number" + c.getCharacterID() + ":" + c.getPowerDescription() + "\n");
            }
        System.out.println("Choose a power 1-12!\n");
        answer= reader.nextInt();

        }

    public Game(boolean expertMode, int numOfPlayer){
        this.expertMode = expertMode;
        this.numOfPlayer = numOfPlayer;
    }

    public void initializeGame() throws IncorrectArgumentException, IncorrectStateException {
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

        // place MotherNature on a random island
        motherNaturePosition = (int) Math.random() * numOfPlayer;
        islands.get(motherNaturePosition).moveMotherNature();

        // create Bag and students
        HashMap<StudentDisc,Integer> students = new HashMap<StudentDisc,Integer>();
        for(int i = 0; i < Colors.values().length -3 ; i++ ){  //3 are the colors of the towers
            StudentDisc student = new StudentDisc(Colors.getColor(i));
            students.put(student,2); //for the 'placing Islands phase'
        }
        bag = new Bag(students);

        //calculate opposite MotherNature's Island
        int oppositeMotherNaturePos = 0;
        if(motherNaturePosition>=islands.size()/2)oppositeMotherNaturePos=motherNaturePosition+islands.size()-1;
        else oppositeMotherNaturePos=motherNaturePosition-islands.size()+1;
        // placing students except MotherNature's Island and the opposite one
        IslandTile islandOppositeMN = islands.get(oppositeMotherNaturePos);
        for(IslandTile island : islands){
            if(!island.hasMotherNature() && !(island.getName().equals(islandOppositeMN.getName()))){
                island.addStudents(bag.drawStudents(1));
            }
        }

        //populate the Bag after 'placing Islands and Students phase'
        students = new HashMap<StudentDisc,Integer>();
        for(int i = 0; i < Colors.values().length -3 ; i++ ){  //3 are the colors of the towers
            StudentDisc student = new StudentDisc(Colors.getColor(i));
            students.put(student,24); //total students are 26 - 2 (that are on the islands) for each color
        }
        bag = new Bag(students);

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
        currentPlayer.moveStudents(students,destinations);

        //If there are students directed to the islands I check that that the islands string arraylist has the right size
        if (studentsToMoveToIsland.size() != 0 && studentsToMoveToIsland.size() == islandDestinations.size() ){
            boolean islandNameFound = true;
            for (String dest : islandDestinations){
                if(islandNameFound) { //I check that the Island that I was searching in the last iteration it's found
                    islandNameFound = false;
                    for (IslandTile island : islands) {
                        if (island.getName().equals(dest)) {
                            islandNameFound = true;
                            island.addStudents(studentsToMoveToIsland);
                            break;
                        }
                    }
                }
                else{
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

    public void checkAndPlaceProfessor() throws IncorrectArgumentException{
        int max = 0;
        Player maxPlayer = players.getFirst();
        for(int i=0; i< Colors.values().length; i++){
            for(Player p : players){
                if(p.getStudentsByColor(Colors.getColor(i))>max){
                    maxPlayer = p;
                }
            }
            maxPlayer.moveProfessor(Colors.getColor(i)); //Maybe Amrit will change the methods like this
        }
        //Based on AMrit changes I will have to tell to each player if is not the max one to remove the professor with that color
    }

    private void checkAndPlaceTower(IslandTile island) throws IncorrectArgumentException {
        //ask to the island the students that it stores
        HashMap<StudentDisc,Integer> students = island.getStudents();
        // initialize the array that counts the students influence
        ArrayList<Integer> playersInfluence = new ArrayList<Integer>(numOfPlayer);
        // check the towerowner and the number of towers , increase the array
        //players.indexOf()
        //ask to the player the array the contains all the colors of the professor that it controls
        // find the influence of each player counting the students

        //if there isn't a tie! you have found the player with the most influence
        // if the player MAX == towerOwner do nothing else replace the towers, change the towerOwner



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
        for(Player p: players){
            if(p.getPlayerTowers()==0)winner=p;
        }

    }

}
