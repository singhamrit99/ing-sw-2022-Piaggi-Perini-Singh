package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.cards.charactercard.Ability;
import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.cards.charactercard.CharacterCardFactory;
import it.polimi.ingsw.model.deck.FileJSONPath;
import it.polimi.ingsw.model.deck.characterdeck.CharacterCardDeck;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.exceptions.ControllerExceptions;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.tiles.Cloud;
import it.polimi.ingsw.model.tiles.Island;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Game {
    private State state;
    private Bag bag;
    private final boolean expertMode;
    private final int numOfPlayer;
    private Player currentPlayer;
    private int playerPlanPhase;
    private final ArrayList<Player> players;
    private PriorityQueue<Player> orderPlayers;
    private LinkedList<Island> islands;
    private ArrayList<Cloud> clouds;
    private int motherNaturePosition;
    private int numRounds;
    private int numDrawnStudents;
    private int counterPlanningPhase;
    private boolean playerDrawnOut;
    private ArrayList<String> importingIslands;
    private ArrayList<String> importingClouds;
    private ArrayList<CharacterCard> characterCards;
    private String JSONContent;

    /**
     * Constructor it initializes everything following the rules of the game. It finishes initialize the first (random)
     * player of the Plan Phase, initializing the specific counters for the phase like 'counter' and 'playerPlanPhase'
     *
     * @throws IncorrectArgumentException in case of bad arguments
     */
    public Game(boolean expertMode, int numOfPlayer, ArrayList<String> nicknames) throws IncorrectArgumentException, NegativeValueException {
        this.expertMode = expertMode;
        this.numOfPlayer = numOfPlayer;
        numRounds = 0;
        characterCards = new ArrayList<>();

        players = new ArrayList<>();
        initializationPlayers(nicknames);
        initializationTilesBag();

        if (expertMode) {
            importingCharacterCards();
        }
    }

    private void importingCharacterCards() throws IncorrectArgumentException, NegativeValueException {
        CharacterCardDeck characterCardDeck = new CharacterCardDeck();
        characterCardDeck.fillDeck();
        CharacterCardFactory factory = new CharacterCardFactory();
        int index;

        //pick three random cards
        CharacterCard card;
        for (int i = 0; i < 3; i++) {
            index = (int) Math.floor(Math.random() * characterCardDeck.getDeck().size());
            card = characterCardDeck.get(index);
            characterCards.add(factory.getCard(card.getImageName(), card.getPrice(), card.getDescription(), card.getType(), card.getAbility(), card.getRequirements()));
            characterCardDeck.discardCard(index);
        }
    }

    private boolean buyCharacterCard(int index) {
        if ((getCurrentState().equals(State.ACTIONPHASE_1) || getCurrentState().equals(State.ACTIONPHASE_2) || getCurrentState().equals(State.ACTIONPHASE_3)) && expertMode) //TODO ACTIONPHASE CE NE SONO TRE, CHIEDERE A TINO
            if (characterCards.get(index).getPrice() <= currentPlayer.getCoins()) {
                characterCards.get(index).setStatus(1);
                currentPlayer.setPlayedCharacterCard(characterCards.get(index));
                return true;
            }
        return false;
    }

    public void activateCharacterCard(int index) throws NotEnoughCoinsException, NegativeValueException, ProfessorNotFoundException, IncorrectArgumentException {
        if (buyCharacterCard(index) && expertMode) {
            currentPlayer.getPlayedCharacterCard().activate(this);
        } else {
            throw new NotEnoughCoinsException();
        }
        if (currentPlayer.getPlayedCharacterCard().getStatus() == 2) {
            currentPlayer.removeCoins(currentPlayer.getPlayedCharacterCard().getPrice());
            characterCards.get(index).increasePrice();
            characterCards.get(index).setStatus(0);
            currentPlayer.setPlayedCharacterCard(null);
        }
    }

    public void activateCharacterCharacter(int index, int choice) throws NotEnoughCoinsException, NegativeValueException, ProfessorNotFoundException, IncorrectArgumentException {
        if (buyCharacterCard(index) && expertMode) {
            currentPlayer.getPlayedCharacterCard().setChoiceIndex(choice);
            currentPlayer.getPlayedCharacterCard().activate(this);
        } else {
            throw new NotEnoughCoinsException();
        }
        if (currentPlayer.getPlayedCharacterCard().getStatus() == 2) {
            currentPlayer.removeCoins(currentPlayer.getPlayedCharacterCard().getPrice());
            characterCards.get(index).increasePrice();
            characterCards.get(index).setStatus(0);
            currentPlayer.setPlayedCharacterCard(null);
        }
    }

    public void activateCharacterCard(int index, EnumMap<Colors, Integer> students1, EnumMap<Colors, Integer> students2) throws NotEnoughCoinsException, NegativeValueException, ProfessorNotFoundException, IncorrectArgumentException {
        if (buyCharacterCard(index) && expertMode) {
            currentPlayer.getPlayedCharacterCard().setEnums(students1, students2);
            currentPlayer.getPlayedCharacterCard().activate(this);
        } else {
            throw new NotEnoughCoinsException();
        }
        if (currentPlayer.getPlayedCharacterCard().getStatus() == 2) {
            currentPlayer.removeCoins(currentPlayer.getPlayedCharacterCard().getPrice());
            characterCards.get(index).increasePrice();
            characterCards.get(index).setStatus(0);
            currentPlayer.setPlayedCharacterCard(null);
        }
    }

    public void activateCharacterCard(int index, int student, int island) throws NotEnoughCoinsException, NegativeValueException, ProfessorNotFoundException, IncorrectArgumentException {
        if (buyCharacterCard(index) && expertMode) {
            currentPlayer.getPlayedCharacterCard().setChoices(student, island);
            currentPlayer.getPlayedCharacterCard().activate(this);
        } else {
            throw new NotEnoughCoinsException();
        }
        if (currentPlayer.getPlayedCharacterCard().getStatus() == 2) {
            currentPlayer.removeCoins(currentPlayer.getPlayedCharacterCard().getPrice());
            characterCards.get(index).increasePrice();
            characterCards.get(index).setStatus(0);
            currentPlayer.setPlayedCharacterCard(null);
        }
    }

    private void initializationPlayers(ArrayList<String> nicknames) {
        //Initialization Players
        int indexColorTeam = 0;
        for (String nickname : nicknames) {
            int colorTeam;
            if (numOfPlayer == 3) {
                colorTeam = indexColorTeam;
            } else {
                if (indexColorTeam % 2 == 1) {
                    colorTeam = 0; //black tower
                } else {
                    colorTeam = 2; //white tower
                }
            }
            Player newPlayer = new Player(nickname, Towers.values()[colorTeam], numOfPlayer);
            players.add(newPlayer);
            indexColorTeam++;
        }

        //initialization LinkedList<Player>
        playerPlanPhase = (int) (Math.random() * numOfPlayer - 1); //random init player
        counterPlanningPhase = numOfPlayer - 1; //used to 'count' during the Planning Phase
        playerDrawnOut = false; //used on drawBag and playAssistantCard
        state = State.PLANNINGPHASE;
        orderPlayers = new PriorityQueue<>(numOfPlayer);
        currentPlayer = players.get(playerPlanPhase);

        if (numOfPlayer == 3) numDrawnStudents = 4;
        else numDrawnStudents = 3;
    }

    private void initializationTilesBag() throws NegativeValueException {
        importingTilesJson();

        //Initialization clouds
        clouds = new ArrayList<>();
        for (int i = 0; i < numOfPlayer; i++) {
            Cloud cloud = new Cloud(importingClouds.get(i)); //LORE:
            clouds.add(cloud);
        }

        // initialization islands;
        islands = new LinkedList<>();
        for (int i = 0; i < 12; i++) {
            Island island = new Island(importingIslands.get(i));
            islands.add(island);
        }

        // place MotherNature on a random island
        motherNaturePosition = (int) (Math.random() * numOfPlayer);
        islands.get(motherNaturePosition).moveMotherNature();

        // create Bag and students
        EnumMap<Colors, Integer> students = new EnumMap<>(Colors.class);
        for (Colors studentColor : Colors.values()) {
            students.put(studentColor, 26);
        }
        bag = Bag.getInstance();
        bag.setStudents(students);

        //calculate opposite MotherNature's Island
        int oppositeMotherNaturePos;
        if (motherNaturePosition >= islands.size() / 2)
            oppositeMotherNaturePos = motherNaturePosition - islands.size() / 2 + 1;
        else oppositeMotherNaturePos = motherNaturePosition + islands.size() / 2 - 1;
        // placing students except MotherNature's Island and the opposite one
        Island islandOppositeMN = islands.get(oppositeMotherNaturePos);
        for (Island island : islands) {
            if (!island.hasMotherNature() && !(island.getName().equals(islandOppositeMN.getName()))) {
                island.addStudents(bag.drawStudents(1));
            }
        }

        //Re-populate the Bag after 'placing Islands and Students phase'
        students = new EnumMap<>(Colors.class);
        for (Colors studentColor : Colors.values()) {
            students.put(studentColor, 24); //26  (total discStudents) -2 (used before) for each color
        }
        bag.setStudents(students);

        //Insertion in each SchoolBoard 7 students drawn from Bag
        for (Player p : players) {
            p.addStudents(bag.drawStudents(7));
        }
    }

    /**
     * Method use to import the textures (that we used as name id) of clouds and islands from the Json
     */
    public void importingTilesJson() {
        Gson gson = new Gson();

        //Loading IslandTiles Json file
        try {
            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(Island.class.getResourceAsStream(FileJSONPath.ISLAND_TILES_LOCATION)), StandardCharsets.UTF_8);
            Scanner s = new Scanner(streamReader).useDelimiter("\\A");
            JSONContent = s.hasNext() ? s.next() : "";
        } catch (Exception FileNotFound) {
            FileNotFound.printStackTrace();
        }
        importingIslands = gson.fromJson(JSONContent, new TypeToken<List<String>>() {
        }.getType());

        // initialization clouds;
        try { //Loading CloudTiles JSON file
            InputStreamReader streamReader = new InputStreamReader(Objects.requireNonNull(Cloud.class.getResourceAsStream(FileJSONPath.CLOUD_TILES_LOCATION)), StandardCharsets.UTF_8);
            Scanner s = new Scanner(streamReader).useDelimiter("\\A");
            JSONContent = s.hasNext() ? s.next() : "";
        } catch (Exception FileNotFound) {
            FileNotFound.printStackTrace();
        }
        importingClouds = gson.fromJson(JSONContent, new TypeToken<List<String>>() {
        }.getType());
    }

    /**
     * the first action of the PlanPhase, it takes a randomize set of students from bag
     * numDrawnStudent is a variable that depends on the rules and it changes according to the number of
     * players. playerDrawnOut is a boolean variable that it's necessary to block possible calls from
     * the callerPlayer to the method 'playAssistantCard' without drawing from bag before. playerDrawnOut it's
     * initialized in the Game() constructor
     */
    public void drawFromBag(String nicknameCaller) throws IncorrectArgumentException, IncorrectPlayerException, NegativeValueException {
        if (state == State.PLANNINGPHASE && !playerDrawnOut) {
            if (nicknameCaller.equals(currentPlayer.getNickname())) {
                for (Cloud cloud : clouds) {
                    cloud.addStudents(bag.drawStudents(numDrawnStudents));
                }
                playerDrawnOut = true;
            } else throw new IncorrectPlayerException();
        } else throw new IncorrectArgumentException();
    }

    /**
     * Action used to play a card. Notice that it calls 'nextPlayer() at the end and creates
     * * the correct order of player for the ActionPhase. It is based using a PriorityQueue and
     * taking advantage of the comparable interface of Player
     */
    public void playAssistantCard(String nicknameCaller, int indexCard) throws IncorrectPlayerException, IncorrectStateException, IncorrectArgumentException {
        if (state == State.PLANNINGPHASE) {
            if (nicknameCaller.equals(currentPlayer.getNickname()) && playerDrawnOut) {  //playerDrawnOut = player has drawn from bag
                currentPlayer.playAssistantCard(indexCard);

            } else {
                throw new IncorrectPlayerException();
            }
        } else {
            throw new IncorrectStateException();
        }
        orderPlayers.add(currentPlayer); //adding to priority queue the player in the correct order
        nextPlayer();
    }

    /**
     * This is the function that correctly switch the player during the phases. It is called at the end
     * of the last action of each phase: so at the end of playAssistantCard for the Planning Phase and
     * at the end of takeStudentsFromCloud for the Action Phase. It also switch the state of the game.
     */
    public void nextPlayer() throws IncorrectStateException {
        if (state == State.PLANNINGPHASE) {
            if (counterPlanningPhase > 0) {
                counterPlanningPhase--;
                if (playerPlanPhase >= numOfPlayer - 1) {
                    playerPlanPhase = -1;
                }
                playerPlanPhase++;
                currentPlayer = players.get(playerPlanPhase);
                playerDrawnOut = false;
            } else {
                state = State.ACTIONPHASE_1;
                playerPlanPhase = players.indexOf(orderPlayers.peek());
                currentPlayer = orderPlayers.poll(); //first player of Action Phase
            }
        } else if (state == State.ACTIONPHASE_3) { //Last player did the 3 step of Action Phase
            if (!orderPlayers.isEmpty()) {
                currentPlayer = orderPlayers.poll();
                state = State.ACTIONPHASE_1;
            } else {
                state = State.ENDTURN;
                nextRound();
            }
        } else {
            throw new IncorrectStateException();
        }
    }

    /**
     * Supporting method to nextPlayer() , it's call at the end of the turn of the last player of the action
     * phase. It checks if there is a gameOver and a winner, otherwise it starts the planning phase assigning
     * the correct currentPlayer, initializing the counter for the PlanningPhase and increasing the num of rounds
     * counter (that is one of the gameOver conditions.
     */
    private void nextRound() throws IncorrectStateException {
        if (state == State.ENDTURN) {
            if (isGameOver()) {
                state = State.END;
                //TODO incrementa valore carte e vedi altro se c'è da fare
                checkWinner();
            } else {
                state = State.PLANNINGPHASE;
                numRounds++;
                currentPlayer = players.get(playerPlanPhase); //This is decided with the Assistant Card values and is assign in nextPlayer()
                counterPlanningPhase = numOfPlayer - 1;
            }
        } else throw new IncorrectStateException();
    }

    /**
     * It sends the student to one of the destination: 0 for the dining room, 1 to the islands.
     * It makes a split of the students, checking which of them go to islands or to the Player that controls the dining room
     * In case the destination is the islands, it is used an array of islandDestination that is used by the game to send
     * the students in the correct place (the array uses the unique name of each island).
     */
    public void moveStudents(String playerCaller, EnumMap<Colors, ArrayList<String>> students) throws IncorrectArgumentException, IncorrectStateException, IncorrectPlayerException, NegativeValueException, ProfessorNotFoundException {
        if (currentPlayer.getNickname().equals(playerCaller)) {
            if (state == State.ACTIONPHASE_1) {
                int numOfStudents;
                if (numOfPlayer == 3) numOfStudents = 4;
                else numOfStudents = 3;

                for (Colors c : Colors.values()) {
                    if (!students.get(c).isEmpty()) {
                        int i = 0;
                        while (i < students.get(c).size()) {
                            numOfStudents--;
                            i++;
                        }
                    }
                }
                if (numOfStudents != 0) throw new IncorrectArgumentException("Numbers of students is wrong");

                EnumMap<Colors, Integer> studentsToDining = new EnumMap<>(Colors.class);
                EnumMap<Colors, Integer> studentsToRemove = new EnumMap<>(Colors.class);

                for (Colors c : Colors.values()) {
                    studentsToDining.put(c, 0);
                    studentsToRemove.put(c, 0);
                }

                for (Colors c : Colors.values()) {
                    if (!students.get(c).isEmpty()) {
                        int i = 0;
                        while (i < students.get(c).size()) {
                            if (students.get(c).get(i).equals("dining")) {
                                studentsToDining.put(c, studentsToDining.get(c) + 1);
                            } else {
                                studentsToRemove.put(c, studentsToRemove.get(c) + 1);
                                String dest = students.get(c).get(i);
                                int islandsIndex = 0;
                                while (islandsIndex < islands.size()) {
                                    if (islands.get(islandsIndex).getName().equals(dest)) {
                                        EnumMap<Colors, Integer> tmp = new EnumMap<>(Colors.class);
                                        for (Colors color : Colors.values()) {
                                            tmp.put(color, 0);
                                        }
                                        tmp.put(c, 1);
                                        islands.get(islandsIndex).addStudents(tmp);
                                    }
                                    islandsIndex++;
                                }
                            }
                            i++;
                        }
                    }
                }
                currentPlayer.moveStudents(studentsToDining, studentsToRemove);
                state = State.ACTIONPHASE_2; //so that the Player can move MotherNature
                checkAndPlaceProfessor(); //maybe some students have arrived in the dining table
            } else throw new IncorrectStateException();
        } else throw new IncorrectPlayerException();
    }

    /**
     * It takes the students from a cloud and throw them to the entrance using a method of the player. It checks
     * that is the correct moment and the correct player to perform the action.
     */
    public void takeStudentsFromCloud(String nicknameCaller, int index) throws IncorrectStateException, IncorrectPlayerException, IncorrectArgumentException, NegativeValueException {
        if (state == State.ACTIONPHASE_3) {
            if (nicknameCaller.equals(currentPlayer.getNickname())) {
                currentPlayer.addStudents(clouds.get(index).drawStudents());
                nextPlayer();
            } else throw new IncorrectPlayerException();
        } else {
            throw new IncorrectStateException();
        }
    }

    /**
     * It uses a method in player to check if the distance choosen by the player is legal. After the
     * control it moves the Mother Nature and it eventually moves the towers and unify islands.
     */
    public void moveMotherNature(String playerCaller, int distanceChosen) throws
            IncorrectPlayerException, IncorrectArgumentException, MotherNatureLostException, IncorrectStateException, NegativeValueException {
        if (playerCaller.equals(currentPlayer.getNickname())) {
            if (state == State.ACTIONPHASE_2) {
                int destinationMotherNature = motherNaturePosition + distanceChosen;
                if (islands.get(motherNaturePosition).hasMotherNature()) {
                    if (distanceChosen <= getMaxMotherNatureMove()) {
                        islands.get(motherNaturePosition).removeMotherNature();
                        islands.get(destinationMotherNature).moveMotherNature();
                        motherNaturePosition = destinationMotherNature;

                        resolveMotherNature(destinationMotherNature);
                        state = State.ACTIONPHASE_3;
                    } else {
                        throw new IncorrectArgumentException();
                    }
                } else {
                    throw new MotherNatureLostException();
                }
            } else throw new IncorrectStateException();
        } else throw new IncorrectPlayerException();
    }

    public void resolveMotherNature(int island) throws NegativeValueException {
        checkAndPlaceTower(islands.get(island));
        checkUnificationIslands();
    }

    /**
     * It is called if new students are added to the dining room and it checks if new professor are placed.
     * It is only called in moveStudents method in the Action Phase.
     */
    public void checkAndPlaceProfessor() throws ProfessorNotFoundException {
        int max = 0;
        Player maxPlayer = null;
        for (Colors studentColor : Colors.values()) {
            for (Player player : players) {
                if (player.getNumOfStudent(studentColor) > max) {
                    maxPlayer = player;
                    max = player.getNumOfStudent(studentColor);
                } else if (player.getNumOfStudent(studentColor) == max) {
                    CharacterCard cardPlayed = player.getPlayedCharacterCard();
                    if (cardPlayed != null) {
                        if (cardPlayed.getAbility().getAction().equals(Ability.Actions.TAKE_PROFESSORS) && cardPlayed.getStatus() >= 1) {
                            maxPlayer = currentPlayer;
                            currentPlayer.getPlayedCharacterCard().setStatus(2);
                        } else {
                            maxPlayer = null;
                        }
                    } else {
                        maxPlayer = null; //in case of ties no one should have assign the professor
                    }
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

    /**
     * It computes the influence of each team on a given island. If it finds a team with more influence
     * than another assign the ownership and a new tower only if the island hasn't any owner or has an owner
     * different from the new team. It works with 2 players, 3 players and 4 players.
     */
    private void checkAndPlaceTower(Island island) {
        HashMap<Towers, Integer> influenceScores = new HashMap<>();
        influenceScores.put(Towers.BLACK, 0);
        influenceScores.put(Towers.WHITE, 0);
        CharacterCard cardPlayed = currentPlayer.getPlayedCharacterCard();

        if (numOfPlayer % 2 == 1) influenceScores.put(Towers.GREY, 0);

        EnumMap<Colors, Integer> students = island.getStudents();

        for (Colors studentColor : Colors.values()) {
            if (cardPlayed != null) {
                if (cardPlayed.getAbility().getAction().equals(Ability.Actions.AVOID_COLOR_INFLUENCE) && cardPlayed.getStatus() >= 1) {
                    if (studentColor.getIndex() == cardPlayed.getAbility().getValue()) {
                        currentPlayer.getPlayedCharacterCard().setStatus(2);
                        continue;
                    }
                }
            }

            if (students.get(studentColor) != 0) {
                for (Player p : players) {
                    if (p.hasProfessorOfColor(studentColor)) { //find the player with that professor
                        Towers teamColor = p.getTowerColor();
                        influenceScores.replace(teamColor, influenceScores.get(teamColor) + students.get(studentColor));

                        if (cardPlayed != null) {
                            if (cardPlayed.getAbility().getAction().equals(Ability.Actions.TOWER_INFLUENCE) && cardPlayed.getStatus() >= 1) {
                                currentPlayer.getPlayedCharacterCard().setStatus(2);
                                influenceScores.replace(teamColor, influenceScores.get(teamColor));
                                currentPlayer.getPlayedCharacterCard().setStatus(2);
                            } else if (cardPlayed.getAbility().getAction().equals(Ability.Actions.ADD_POINTS) && cardPlayed.getStatus() >= 1) {
                                currentPlayer.getPlayedCharacterCard().setStatus(2);
                                influenceScores.replace(teamColor, influenceScores.get(teamColor) + island.getNumOfTowers() + cardPlayed.getAbility().getValue());
                            } else {
                                if (teamColor.equals(island.getTowersColor())) { //counting the towers if team owns the island
                                    influenceScores.replace(teamColor, influenceScores.get(teamColor) + island.getNumOfTowers());
                                }
                            }
                        } else {
                            if (teamColor.equals(island.getTowersColor())) { //counting the towers if team owns the island
                                influenceScores.replace(teamColor, influenceScores.get(teamColor) + island.getNumOfTowers());
                            }
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
            if (island.getTowersColor() == null) {// The island was empty
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
        ArrayList<Player> returnedPlayers = new ArrayList<>();
        if (firstPlayer != null) returnedPlayers.add(firstPlayer);
        if (secondPlayer != null) returnedPlayers.add(secondPlayer);
        return returnedPlayers;
    }

    /**
     * It is used from checkAndPlaceTowers to add or remove towers from a team. The towers must be
     * removed from each player ONCE at time each. For example if playerA and playerB of same team have 3 and 4 towers
     * and 3 towers will be removed, it will leave this configuration: 2 and 2.
     */
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
                } else {
                    if (team.get(0).getPlayerTowers() >= team.get(1).getPlayerTowers())
                        team.get(0).moveTowers(oneTowerSigned);
                    else team.get(1).moveTowers(oneTowerSigned);
                }
                numbersOfIterations--;
            }
        } else team.get(0).moveTowers(amount);
    }

    public void checkUnificationIslands() throws NegativeValueException {
        boolean listChanged = false;
        ListIterator<Island> it = islands.listIterator();
        Island currentTile;
        Island nextTile;
        while (it.hasNext()) {
            currentTile = it.next();
            if (it.hasNext()) nextTile = it.next();
            else nextTile = islands.getFirst();
            if (nextTile.getNumOfTowers() != 0 && currentTile.getNumOfTowers() != 0
                    && (nextTile.getTowersColor().equals(currentTile.getTowersColor()))) {
                currentTile.addStudents(nextTile.getStudents());
                currentTile.sumTowers(nextTile.getNumOfTowers());
                islands.remove(nextTile);
                listChanged = true;
            }
        }
        if (listChanged) checkUnificationIslands();
    }

    /**
     * Check the game over returning true if it is
     */
    public boolean isGameOver() {
        for (Player p : players) {
            if (p.getPlayerTowers() <= 0) return true;
        }
        return !bag.hasEnoughStudents(numDrawnStudents) || islands.size() <= 3 || numRounds >= 9 || checkWinner() != null;
    }

    /**
     * It checks the winner and it is used as supporting method inside isGameOver() . It return the
     * winning team using an arrayList of Player (that could have size of 1 or 2 depending of numOfPLayers).
     * If it return a null team there isn't a winning team.
     */
    public ArrayList<Player> checkWinner() {
        ArrayList<Player> team1 = findPlayerFromTeam(Towers.WHITE);
        ArrayList<Player> team2 = findPlayerFromTeam(Towers.BLACK);
        ArrayList<ArrayList<Player>> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        if (numOfPlayer % 2 == 1) {
            ArrayList<Player> team3 = findPlayerFromTeam(Towers.GREY);
            teams.add(team3);
        }

        for (ArrayList<Player> team : teams) {
            boolean teamWin = false;
            for (Player p : team) {
                teamWin = p.getPlayerTowers() == 0;
            }
            if (teamWin) return team;
        }

        return null;  //no win
    }

    /**
     * Primitive method used to count the number of students inside an enumMap.
     */
    public int valueOfEnum(EnumMap<Colors, Integer> map) {
        int sum = 0;
        for (Colors c : Colors.values()) {
            sum += map.get(c);
        }
        return sum;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public State getCurrentState() {
        return state;
    }

    public boolean getPlayerDrawnOut() {
        return playerDrawnOut;
    }

    public Cloud getCloudTile(int index) {
        return clouds.get(index);
    }

    public int getMotherNaturePosition() {
        return motherNaturePosition;
    }

    public int getMaxMotherNatureMove() {
        CharacterCard cardPlayed = currentPlayer.getPlayedCharacterCard();
        if (cardPlayed != null) {
            if (cardPlayed.getAbility().getAction().equals(Ability.Actions.MOVE_MOTHER_NATURE) && cardPlayed.getStatus() >= 1) {
                currentPlayer.getPlayedCharacterCard().setStatus(2);
                return currentPlayer.getPlayedAssistantCard().getMove() + currentPlayer.getPlayedCharacterCard().getAbility().getValue();
            }
        }
        return currentPlayer.getPlayedAssistantCard().getMove();
    }

    public void error(ControllerExceptions error) {
        //TODO GUARDARE ANCHE QUESTO E SISTEMARE
    }

    public Island getIsland(int index) {
        return islands.get(index);
    }

    public void returnStudentsEffect(int choiceIndex) throws NegativeValueException {
        EnumMap<Colors, Integer> enumMap = new EnumMap<>(Colors.class);
        for (Player player : players) {
            if (player.getSchoolBoard().getStudentsByColor(Colors.getStudent(choiceIndex)) < 3) {
                enumMap.put(Colors.getStudent(choiceIndex), player.getSchoolBoard().getStudentsByColor(Colors.getStudent(choiceIndex)));
            } else {
                enumMap.put(Colors.getStudent(choiceIndex), 3);
            }
            player.getSchoolBoard().removeDiningStudents(enumMap);
        }
    }
}
