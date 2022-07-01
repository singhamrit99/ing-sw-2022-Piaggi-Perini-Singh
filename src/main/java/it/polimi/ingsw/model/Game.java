package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.cards.charactercard.CharacterCardFactory;
import it.polimi.ingsw.model.deck.FileJSONPath;
import it.polimi.ingsw.model.deck.characterdeck.CharacterCardDeck;
import it.polimi.ingsw.model.enumerations.Actions;
import it.polimi.ingsw.model.enumerations.Colors;
import it.polimi.ingsw.model.enumerations.State;
import it.polimi.ingsw.model.enumerations.Towers;
import it.polimi.ingsw.model.tiles.Cloud;
import it.polimi.ingsw.model.tiles.Island;
import it.polimi.ingsw.network.server.Room;
import it.polimi.ingsw.network.server.stripped.StrippedCharacter;
import it.polimi.ingsw.network.server.stripped.StrippedCloud;
import it.polimi.ingsw.network.server.stripped.StrippedIsland;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
    final private PropertyChangeListener gameListener;
    private String firstPlayer;
    private int selectedCharacterIndex;
    private int moveStudentCounter;

    /**
     * Constructor of game
     *
     * @param room        necessary for the event notification
     * @param expertMode  for toggle expert mode
     * @param numOfPlayer is the number of players
     * @param nicknames   is the nicknames of the clients in the room
     * @throws IncorrectArgumentException in case of bad arguments
     * @throws NegativeValueException     in case of negative arguments
     */
    public Game(Room room, boolean expertMode, int numOfPlayer, ArrayList<String> nicknames) throws IncorrectArgumentException, NegativeValueException {
        gameListener = room;
        this.expertMode = expertMode;
        this.numOfPlayer = numOfPlayer;
        numRounds = 0;
        moveStudentCounter = 0;
        characterCards = new ArrayList<>();

        players = new ArrayList<>();
        initializationPlayers(nicknames);
        initializationTilesBag();

        if (expertMode) {
            importingCharacterCards();
        }
    }

    /**
     * Method used to load character cards from their JSON file
     *
     * @throws IncorrectArgumentException thrown when fillDeck method can't find JSON file.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     */
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

    /**
     * Method used to buy a character card.
     *
     * @param index Index of the character card.
     * @return whether the card can be bought or not according to the player's coins.
     */
    private boolean buyCharacterCard(int index) {
        if ((getCurrentState().equals(State.ACTIONPHASE_1) || getCurrentState().equals(State.ACTIONPHASE_2) || getCurrentState().equals(State.ACTIONPHASE_3)) && expertMode) {
            if (characterCards.get(index).getPrice() <= currentPlayer.getCoins()) {
                characterCards.get(index).setStatus(1);
                currentPlayer.setPlayedCharacterCard(characterCards.get(index));
                selectedCharacterIndex = index;
                return true;
            }
        }
        return false;
    }

    /**
     * Method implemented locally through Factory method in each specific Character Card.
     * This is the simplest type of card as it requires no additional resources or information for activation.
     *
     * @param index The index of the called card.
     * @throws NotEnoughCoinsException    Thrown when the player that tried to perform this character call doesn't have enough coins to complete it.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws ProfessorNotFoundException If the character power causes a professor gain or loss and that generates an error this exception is thrown.
     * @throws IncorrectArgumentException Thrown if the index or any of the parameters used for card activation are invalid.
     */
    public void activateCharacterCard(int index) throws NotEnoughCoinsException, NegativeValueException, ProfessorNotFoundException, FullDiningException, CardPlayedInTurnException, IncorrectArgumentException {
        if (currentPlayer.getPlayedCharacterCard() != null) {
            throw new CardPlayedInTurnException();
        } else {
            if (buyCharacterCard(index) && expertMode) {
                currentPlayer.getPlayedCharacterCard().activate(this);
            } else {
                characterCards.get(index).setStatus(0);
                currentPlayer.setPlayedCharacterCard(null);
                selectedCharacterIndex = -1;
                throw new NotEnoughCoinsException();
            }
            if (currentPlayer.getPlayedCharacterCard().getStatus() == 2) increaseCharacterPrice(index);
        }
    }

    /**
     * After a successful character activation the card's price is increased by 1 through this method.
     *
     * @param index The index of the character card to increment the price of.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException Thrown if the coins field is invalid.
     */
    public void increaseCharacterPrice(int index) throws NegativeValueException, IncorrectArgumentException {
        CharacterCard updatedCard = characterCards.get(index);
        int coinsRemoved = currentPlayer.getPlayedCharacterCard().getPrice();
        currentPlayer.removeCoins(coinsRemoved);
        int coins = currentPlayer.getCoins();
        //notify coins changed
        PropertyChangeEvent coinsEvt =
                new PropertyChangeEvent(this, "coins", currentPlayer.getNickname(), coins);
        gameListener.propertyChange(coinsEvt);
        updatedCard.increasePrice();
        updatedCard.setStatus(0);
        notifyCharacterEvent(updatedCard);
    }

    /**
     * Method used to notify the client of character card event.
     *
     * @param updatedCard The card that was modified.
     */
    public void notifyCharacterEvent(CharacterCard updatedCard) {
        StrippedCharacter strippedCard = new StrippedCharacter(updatedCard);
        PropertyChangeEvent cardEvent = new PropertyChangeEvent(this, "character", null, strippedCard);
        gameListener.propertyChange(cardEvent);
    }

    /**
     * Methods implemented locally through Strategy pattern in each specific Character Card.
     * This type of card requires an additional integer for activation (a color, a student or an island).
     *
     * @param index  The index of the character card to be called.
     * @param choice The choice the card power acts upon.
     * @throws NotEnoughCoinsException    Thrown if the player that tried to play the card doesn't have enough coins to buy it.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws ProfessorNotFoundException If the character power causes a professor gain or loss and that generates an error this exception is thrown.
     * @throws IncorrectArgumentException Thrown if the index or any of the parameters used for card activation are invalid.
     */
    public void activateCharacterCard(int index, int choice) throws NotEnoughCoinsException, NegativeValueException, ProfessorNotFoundException, IncorrectArgumentException, FullDiningException, CardPlayedInTurnException {
        if (currentPlayer.getPlayedCharacterCard() != null) {
            throw new CardPlayedInTurnException();
        } else {
            if (buyCharacterCard(index) && expertMode) {
                currentPlayer.getPlayedCharacterCard().setChoiceIndex(choice);
                currentPlayer.getPlayedCharacterCard().activate(this);
            } else {
                characterCards.get(index).setStatus(0);
                currentPlayer.setPlayedCharacterCard(null);
                selectedCharacterIndex = -1;
                throw new NotEnoughCoinsException();
            }
            if (currentPlayer.getPlayedCharacterCard().getStatus() == 2) increaseCharacterPrice(index);
        }
    }

    /**
     * Methods implemented locally through Strategy pattern in each specific Character Card.
     * This card requires two sets of students to operate.
     *
     * @param index     Card number.
     * @param students1 Students EnumMap 1
     * @param students2 Students EnumMap 2
     * @throws NotEnoughCoinsException    Thrown if the player that tried to play the card doesn't have enough coins to buy it.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws ProfessorNotFoundException If the character power causes a professor gain or loss and that generates an error this exception is thrown.
     * @throws IncorrectArgumentException Thrown if the index or any of the parameters used for card activation are invalid.
     */
    public void activateCharacterCard(int index, EnumMap<Colors, Integer> students1, EnumMap<Colors, Integer> students2) throws NotEnoughCoinsException, NegativeValueException, ProfessorNotFoundException, IncorrectArgumentException, FullDiningException, CardPlayedInTurnException {
        if (currentPlayer.getPlayedCharacterCard() != null) {
            throw new CardPlayedInTurnException();
        } else {
            if (buyCharacterCard(index) && expertMode) {
                currentPlayer.getPlayedCharacterCard().setEnums(students1, students2);
                currentPlayer.getPlayedCharacterCard().activate(this);
            } else {
                characterCards.get(index).setStatus(0);
                currentPlayer.setPlayedCharacterCard(null);
                selectedCharacterIndex = -1;
                throw new NotEnoughCoinsException();
            }
            if (currentPlayer.getPlayedCharacterCard().getStatus() == 2) increaseCharacterPrice(index);
        }
    }

    /**
     * Method implemented locally through Factory method in each specific Character Card.
     * This one requires a student and an island to work, both coded as an integer for convenience.
     *
     * @param index   Card Number.
     * @param student The color of student chosen by the player.
     * @param island  The island chosen by the player.z
     * @throws NotEnoughCoinsException    Thrown if the player that tried to play the card doesn't have enough coins to buy it.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws ProfessorNotFoundException If the character power causes a professor gain or loss and that generates an error this exception is thrown.
     * @throws IncorrectArgumentException Thrown if the index or any of the parameters used for card activation are invalid.
     */
    public void activateCharacterCard(int index, int student, int island) throws NotEnoughCoinsException, NegativeValueException, ProfessorNotFoundException, IncorrectArgumentException, FullDiningException, CardPlayedInTurnException {
        if (currentPlayer.getPlayedCharacterCard() != null) {
            throw new CardPlayedInTurnException();
        } else {
            if (buyCharacterCard(index) && expertMode) {
                currentPlayer.getPlayedCharacterCard().setChoices(student, island);
                currentPlayer.getPlayedCharacterCard().activate(this);
            } else {
                characterCards.get(index).setStatus(0);
                currentPlayer.setPlayedCharacterCard(null);
                selectedCharacterIndex = -1;
                throw new NotEnoughCoinsException();
            }
            if (currentPlayer.getPlayedCharacterCard().getStatus() == 2) increaseCharacterPrice(index);
        }
    }

    /**
     * Method used to initialize players at the beginning of the game.
     *
     * @param nicknames The nicknames of every player in the game
     */
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
        firstPlayer = currentPlayer.getNickname();

        if (numOfPlayer == 3) numDrawnStudents = 4;
        else numDrawnStudents = 3;
    }

    /**
     * Method used to initialize tiles (islands and clouds), pulling from the JSON file.
     *
     * @throws NegativeValueException As always, this game has no negative values, and any found are automatically incorrect.
     */
    private void initializationTilesBag() throws NegativeValueException, IncorrectArgumentException {
        importingTilesJson();

        //Initialization clouds
        clouds = new ArrayList<>();
        for (int i = 0; i < numOfPlayer; i++) {
            Cloud cloud = new Cloud(importingClouds.get(i));
            clouds.add(cloud);
        }

        // initialization islands;
        islands = new LinkedList<>();
        for (int i = 0; i < 12; i++) {
            Island island = new Island(importingIslands.get(i));
            islands.add(island);
        }

        // place MotherNature on a random island
        motherNaturePosition = (int) (Math.random() * 11.99999f);
        islands.get(motherNaturePosition).moveMotherNature();

        // create Bag and students
        EnumMap<Colors, Integer> students = new EnumMap<>(Colors.class);
        for (Colors studentColor : Colors.values()) {
            students.put(studentColor, 26);
        }
        bag = Bag.getInstance();
        bag.setStudents(students);

        //calculate opposite MotherNature's Island
        int oppositeMotherNaturePos = (motherNaturePosition + 6) % islands.size();
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

        //Insertion in each SchoolBoard 7 or 9 students drawn from Bag
        for (Player p : players) {
            if (numOfPlayer == 3) {
                p.addStudents(bag.drawStudents(9));
            } else {
                p.addStudents(bag.drawStudents(7));
            }
        }
    }

    /**
     * Method used to import the textures, which double as IDs, of clouds and islands from related JSON files.
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
     * the first action of the Planning Phase, it takes a randomized set of students from the Bag and puts it on the Clouds.
     * The number of students drawn varies depending on how many players are in the game.
     * PlayerDrawnOut is a boolean variable that is used to prevent the current player from playing Assistant Cards before
     * drawing from the bag.
     *
     * @param nicknameCaller The player that needs to draw from the bag.
     * @throws IncorrectArgumentException Thrown if any of the parameters used in the methods are incorrect.
     * @throws IncorrectPlayerException   Thrown if the player that called the method isn't the current player.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     */
    public void drawFromBag(String nicknameCaller) throws IncorrectArgumentException, IncorrectPlayerException, NegativeValueException {
        if (state == State.PLANNINGPHASE && !playerDrawnOut) {
            if (nicknameCaller.equals(currentPlayer.getNickname())) {
                for (Cloud cloud : clouds) {
                    cloud.setStudents(bag.drawStudents(numDrawnStudents)); //it was addStudents
                    //notify cloud
                    StrippedCloud newCloud = new StrippedCloud(cloud);
                    PropertyChangeEvent evt =
                            new PropertyChangeEvent(this, "cloud", null, newCloud);
                    gameListener.propertyChange(evt);
                }
                playerDrawnOut = true;
            } else throw new IncorrectPlayerException();
        } else throw new IncorrectArgumentException();
    }


    /**
     * Method used to play a card. Notice that it calls 'nextPlayer() at the end and creates
     * the correct order of player for the ActionPhase. It is based using a PriorityQueue and
     * taking advantage of the comparable interface of Player
     *
     * @param nicknameCaller The player that plays the assistant card.
     * @param nameCard       the name of the assistant card that's being played.
     * @throws IncorrectPlayerException       Thrown if the player that called the method isn't the current player.
     * @throws IncorrectStateException        Thrown if the method is called in an illegal phase (in this case, anything but Planning Phase)
     * @throws IncorrectArgumentException     Thrown if any of the parameters used by the method are invalid.
     * @throws AssistantCardNotFoundException Thrown if the assistant card name provided doesn't correspond to any card.
     */
    public void playAssistantCard(String nicknameCaller, String nameCard) throws IncorrectPlayerException, IncorrectStateException, IncorrectArgumentException, AssistantCardNotFoundException, NegativeValueException, AssistantCardAlreadyPlayed {
        if (!playerDrawnOut) throw new IncorrectStateException();
        if (state == State.PLANNINGPHASE) {
            if (nicknameCaller.equals(currentPlayer.getNickname())) {  //playerDrawnOut = player has drawn from bag
                for (Player p : players) {
                    if (p.hasPlayedAssistantInThisTurn()) {
                        if (p.getPlayedAssistantCard().getImageName().equals(nameCard)) {
                            throw new AssistantCardAlreadyPlayed();
                        }
                    }
                }
                currentPlayer.playAssistantCard(nameCard); //notice that when a card is played, is removed from the deck of the player
                //notify deck change and assistant
                PropertyChangeEvent assistantDeckUpdateEvent =
                        new PropertyChangeEvent(this, "assistant", nameCard, currentPlayer.getAssistantCardDeck());
                gameListener.propertyChange(assistantDeckUpdateEvent);
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
     * at the end of takeStudentsFromCloud for the Action Phase. It also switches the state of the game.
     *
     * @throws IncorrectStateException Thrown if the method is called in an illegal phase
     */
    public void nextPlayer() throws IncorrectStateException, IncorrectArgumentException, NegativeValueException {
        if (state == State.PLANNINGPHASE) {
            if (counterPlanningPhase > 0) {
                counterPlanningPhase--;
                if (playerPlanPhase >= numOfPlayer - 1) {
                    playerPlanPhase = -1;
                }
                playerPlanPhase++;
                currentPlayer = players.get(playerPlanPhase);
            } else {
                for (Player p : players)
                    p.forgetAssistantCard(); //necessary to always play the AssistantCards that hasn't played by any other players during the SAME turn (for the Planning Phase)
                state = State.ACTIONPHASE_1;
                playerPlanPhase = players.indexOf(orderPlayers.peek());
                currentPlayer = orderPlayers.poll(); //first player of Action Phase
                PropertyChangeEvent phaseChange = null;
                if (currentPlayer != null) {
                    phaseChange = new PropertyChangeEvent(this, "change-phase", state, currentPlayer.getNickname());
                }
                gameListener.propertyChange(phaseChange);
            }
        } else if (state == State.ACTIONPHASE_3) { //Last player did the 3 step of Action Phase
            if (!orderPlayers.isEmpty()) {
                currentPlayer = orderPlayers.poll();
                state = State.ACTIONPHASE_1;
                PropertyChangeEvent phaseChange =
                        new PropertyChangeEvent(this, "change-phase", state, currentPlayer.getNickname());
                gameListener.propertyChange(phaseChange);
            } else {
                state = State.ENDTURN;
                for (Player player : players) {
                    if (player.getPlayedCharacterCard() != null) {
                        player.setPlayedCharacterCard(null);
                    }
                }

                nextRound();
                PropertyChangeEvent phaseChange =
                        new PropertyChangeEvent(this, "change-phase", state, currentPlayer.getNickname());
                gameListener.propertyChange(phaseChange);
            }
        } else {
            throw new IncorrectStateException();
        }

        //notify current player
        if (currentPlayer.getNickname() != null) {
            PropertyChangeEvent changeCurrentPlayer =
                    new PropertyChangeEvent(this, "current-player", null, currentPlayer.getNickname());
            gameListener.propertyChange(changeCurrentPlayer);
        }
    }

    /**
     * Supporting method to nextPlayer() , it's call at the end of the turn of the last player of the action
     * phase. It checks if there is a gameOver and a winner, otherwise it starts the planning phase assigning
     * the correct currentPlayer, initializing the counter for the PlanningPhase and increasing the num of rounds
     * counter (that is one of the gameOver conditions.
     *
     * @throws IncorrectStateException Thrown if the method is called in an illegal phase
     */
    private void nextRound() throws IncorrectStateException {
        if (state == State.ENDTURN) {
            if (isGameOver()) {
                state = State.END;
                String teamWinner = checkWinner();
                //notify game-over
                PropertyChangeEvent gameOverEvt =
                        new PropertyChangeEvent(this, "game-finished", null, teamWinner);
                gameListener.propertyChange(gameOverEvt);
            } else {
                state = State.PLANNINGPHASE;
                playerDrawnOut = false;
                PropertyChangeEvent phaseChange =
                        new PropertyChangeEvent(this, "change-phase", state, null);
                gameListener.propertyChange(phaseChange);
                numRounds++;
                firstPlayer = players.get(playerPlanPhase).getNickname();

                PropertyChangeEvent firstPlayerChange =
                        new PropertyChangeEvent(this, "first-player-change", firstPlayer, firstPlayer);
                gameListener.propertyChange(firstPlayerChange);

                currentPlayer = players.get(playerPlanPhase); //This is decided with the Assistant Card values and is assign in nextPlayer()
                counterPlanningPhase = numOfPlayer - 1;
            }
        } else throw new IncorrectStateException();
    }

    /**
     * It sends the student to one of the destination: 0 for the dining room, 1 to the islands.
     * It makes a split of the students, checking which of them go to islands or to the Player that controls the dining room
     * In case the destination is the islands, an array of islandDestination is used by the game to send
     * the students in the correct place (the array uses the unique name of each island).
     *
     * @param playerCaller the player that called the MoveStudents method
     * @throws IncorrectArgumentException Thrown when either of the parameters are incorrect.
     * @throws IncorrectStateException    Thrown when this method is called in an invalid phase of the game.
     * @throws IncorrectPlayerException   Thrown when the playerCaller is not the CurrentPlayer.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws ProfessorNotFoundException This is thrown if the move produces a professor change and that creates an error.
     */
    public void moveStudents(String playerCaller, Colors color, String dest) throws IncorrectArgumentException, IncorrectStateException, IncorrectPlayerException, NegativeValueException, ProfessorNotFoundException {
        EnumMap<Colors, Integer> studentToMove = StudentManager.createEmptyStudentsEnum();
        if (currentPlayer.getNickname().equals(playerCaller)) {
            if (state == State.ACTIONPHASE_1) {
                studentToMove.put(color, 1);
                if (dest.equals("dining")) {
                    int oldCoins = currentPlayer.getCoins(), newCoins;
                    currentPlayer.moveStudents(studentToMove, StudentManager.createEmptyStudentsEnum());
                    checkAndPlaceProfessor(); //check and eventually modifies and notifies
                    //notify dining AND entrance AND possible coins change
                    newCoins = currentPlayer.getCoins();
                    if (newCoins != oldCoins) {
                        PropertyChangeEvent coinsAddedEvent =
                                new PropertyChangeEvent(this, "coins", currentPlayer.getNickname(), newCoins);
                        gameListener.propertyChange(coinsAddedEvent);
                    }
                    EnumMap<Colors, Integer> newDining = currentPlayer.getSchoolBoard().getDining();
                    PropertyChangeEvent event =
                            new PropertyChangeEvent(this, "entrance", currentPlayer.getNickname(), currentPlayer.getSchoolBoard().getEntrance());
                    gameListener.propertyChange(event);
                    PropertyChangeEvent evt =
                            new PropertyChangeEvent(this, "dining", currentPlayer.getNickname(), newDining);
                    gameListener.propertyChange(evt);
                } else {
                    for (Island islandToChange : islands) {
                        if (islandToChange.getName().equals(dest)) {
                            //notify island change and modify
                            StrippedIsland oldIsland = new StrippedIsland(islandToChange);
                            islandToChange.addStudents(studentToMove); //adding students
                            StrippedIsland changedIsland = new StrippedIsland(islandToChange);
                            currentPlayer.moveStudents(StudentManager.createEmptyStudentsEnum(), studentToMove);

                            PropertyChangeEvent evt =
                                    new PropertyChangeEvent(this, "island", oldIsland, changedIsland);
                            gameListener.propertyChange(evt);

                            PropertyChangeEvent event =
                                    new PropertyChangeEvent(this, "entrance", currentPlayer.getNickname(), currentPlayer.getSchoolBoard().getEntrance());
                            gameListener.propertyChange(event);
                        }
                    }
                }
                moveStudentCounter++;
                int numOfStudents;
                if (numOfPlayer == 3) numOfStudents = 4;
                else numOfStudents = 3;

                if (moveStudentCounter == numOfStudents) {
                    state = State.ACTIONPHASE_2; //so that the Player can move MotherNature
                    moveStudentCounter = 0;
                    PropertyChangeEvent phaseChange =
                            new PropertyChangeEvent(this, "change-phase", state, currentPlayer.getNickname());
                    gameListener.propertyChange(phaseChange);
                }
            } else throw new IncorrectStateException();
        } else throw new IncorrectPlayerException();
    }

    /**
     * It takes the students from a cloud and throw them to the entrance using a method of the player. It checks
     * that is the correct moment and the correct player to perform the action.
     *
     * @param nicknameCaller The player that called this method
     * @param name           The name of the cloud.
     * @throws IncorrectStateException    Thrown if the method is called in an illegal phase (in this case any state but ActionPhase_3)
     * @throws IncorrectPlayerException   Thrown if the player that called the method isn't the current player.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException Thrown if any of the parameters used by the method are invalid.
     */
    public void takeStudentsFromCloud(String nicknameCaller, String name) throws IncorrectStateException, IncorrectPlayerException, NegativeValueException, IncorrectArgumentException {
        int index = -1;
        if (state == State.ACTIONPHASE_3) {
            if (nicknameCaller.equals(currentPlayer.getNickname())) {
                for (int i = 0; i < clouds.size(); i++) {
                    if (clouds.get(i).getName().equals(name)) {
                        index = i;
                    }
                }
                if (index == -1) {
                    throw new IncorrectArgumentException();
                }
                currentPlayer.addStudents(clouds.get(index).drawStudents());

                //notify cloud change and entrance change
                StrippedCloud changedCloud = new StrippedCloud(clouds.get(index));
                PropertyChangeEvent evtCloud =
                        new PropertyChangeEvent(this, "cloud", null, changedCloud);
                gameListener.propertyChange(evtCloud);
                PropertyChangeEvent event =
                        new PropertyChangeEvent(this, "entrance", currentPlayer.getNickname(), currentPlayer.getSchoolBoard().getEntrance());
                gameListener.propertyChange(event);
                nextPlayer();
            } else throw new IncorrectPlayerException();
        } else {
            throw new IncorrectStateException();
        }
    }


    /**
     * It uses a method in player to check if the distance chosen by the player is legal. After the
     * control it moves the Mother Nature, and it eventually moves the towers and unify islands.
     *
     * @param playerCaller   The player that called this method
     * @param distanceChosen Distance chosen for MN movement.
     * @throws IncorrectPlayerException   Thrown if the player that called the method isn't the current player.
     * @throws IncorrectArgumentException Thrown if any of the parameters used by the method are invalid.
     * @throws MotherNatureLostException  Thrown if after movement the game cannot calculate Mother Nature's position.
     * @throws IncorrectStateException    Thrown if the method is called in an illegal phase (Such as PlanningPhase)
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     */
    public void moveMotherNature(String playerCaller, int distanceChosen) throws
            IncorrectPlayerException, IncorrectArgumentException, MotherNatureLostException, IncorrectStateException, NegativeValueException {

        if (playerCaller.equals(currentPlayer.getNickname())) {
            if (state == State.ACTIONPHASE_2) {
                motherNaturePosition = findMotherNature();
                int destinationMotherNature = (motherNaturePosition + distanceChosen) % islands.size();
                CharacterCard cardPlayed = currentPlayer.getPlayedCharacterCard();
                int maxMotherNatureMove = currentPlayer.getPlayedAssistantCard().getMove();

                if (cardPlayed != null) {
                    if (cardPlayed.getAbility().getAction().equals(Actions.MOVE_MOTHER_NATURE) && cardPlayed.getStatus() >= 1) {
                        currentPlayer.getPlayedCharacterCard().setStatus(2);
                        try {
                            increaseCharacterPrice(selectedCharacterIndex);
                        } catch (NegativeValueException e) {
                            throw new NegativeValueException();
                        } catch (IncorrectArgumentException e) {
                            throw new IncorrectArgumentException();
                        }

                        maxMotherNatureMove = currentPlayer.getPlayedAssistantCard().getMove() + cardPlayed.getAbility().getValue();
                    }
                }
                if (distanceChosen <= maxMotherNatureMove) {
                    //notify oldIsland
                    StrippedIsland oldIsland = new StrippedIsland(islands.get(motherNaturePosition)); //saving oldIsland source
                    islands.get(motherNaturePosition).removeMotherNature();
                    StrippedIsland changedIsland = new StrippedIsland(islands.get(motherNaturePosition));
                    PropertyChangeEvent evt = new PropertyChangeEvent(this, "island", oldIsland, changedIsland);
                    gameListener.propertyChange(evt);
                    //notify destination island
                    StrippedIsland oldIslandDest = new StrippedIsland(islands.get(destinationMotherNature)); //saving oldIsland destination
                    islands.get(destinationMotherNature).moveMotherNature();
                    //notify new newIslandDest (changedIsland)
                    changedIsland = new StrippedIsland(islands.get(destinationMotherNature));
                    PropertyChangeEvent evtDest = new PropertyChangeEvent(this, "island", oldIslandDest, changedIsland);
                    gameListener.propertyChange(evtDest);
                    //ended notifications
                    motherNaturePosition = destinationMotherNature;
                    resolveMotherNature(destinationMotherNature);
                    state = State.ACTIONPHASE_3;
                    PropertyChangeEvent phaseChange =
                            new PropertyChangeEvent(this, "change-phase", state, currentPlayer.getNickname());
                    gameListener.propertyChange(phaseChange);
                } else {
                    throw new IncorrectArgumentException();
                }
            } else throw new IncorrectStateException(state.toString());
        } else throw new IncorrectPlayerException();
    }

    /**
     * Method used to find Mother Nature after a movement.
     *
     * @return Mother Nature's position.
     * @throws MotherNatureLostException Thrown if after movement the game cannot calculate Mother Nature's position.
     */
    private int findMotherNature() throws MotherNatureLostException {
        int i = 0;
        for (Island is : islands) {
            if (is.hasMotherNature()) {
                return i;
            }
            i++;
        }
        throw new MotherNatureLostException();
    }

    /**
     * Method called to check for influence calculations on the island MN ended up on.
     *
     * @param island The island to check.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException in case of wrong island name
     */
    public void resolveMotherNature(int island) throws NegativeValueException, IncorrectArgumentException {
        if (islands.get(island).hasNoEntryTile()) {
            StrippedIsland oldIsland = new StrippedIsland(islands.get(island));
            islands.get(island).setHasNoEntryTile(false);
            for (CharacterCard card : characterCards) {
                if (card.getAbility().getAction().equals(Actions.NO_ENTRY_TILE)) {
                    card.incrementNoTileNumber();

                    notifyCharacterEvent(card);
                    StrippedIsland changedIsland = new StrippedIsland(islands.get(island));
                    PropertyChangeEvent evt =
                            new PropertyChangeEvent(this, "island", oldIsland, changedIsland);
                    gameListener.propertyChange(evt);
                }
            }
        } else {
            checkAndPlaceTower(islands.get(island));
            checkUnificationIslands();
        }
    }

    public void setNoEntryTile(int index) {
        if (!islands.get(index).hasNoEntryTile()) {
            StrippedIsland oldIsland = new StrippedIsland(islands.get(index));

            islands.get(index).setHasNoEntryTile(true);
            characterCards.get(selectedCharacterIndex).decrementNoTileNumber();
            characterCards.get(selectedCharacterIndex).setStatus(2);

            StrippedIsland changedIsland = new StrippedIsland(islands.get(index));
            PropertyChangeEvent evt =
                    new PropertyChangeEvent(this, "island", oldIsland, changedIsland);
            gameListener.propertyChange(evt);
        }
    }

    /**
     * Method called if new students are added to the dining room and checks if new professor need to be placed.
     * It is only called in moveStudents method in the Action Phase and by the CharactersCard.
     * It returns a boolean to signal if it has been a change in the professor table.
     *
     * @throws ProfessorNotFoundException If there's an issue in professor placement this exception is thrown.
     */
    public void checkAndPlaceProfessor() throws ProfessorNotFoundException {
        Player maxPlayer = null;
        for (Colors color : Colors.values()) {
            int max = 0;
            for (Player player : players) {
                if (player.getNumOfStudent(color) > max) {
                    maxPlayer = player;
                    max = player.getNumOfStudent(color);
                } else if (player.getNumOfStudent(color) == max) {
                    CharacterCard cardPlayed = player.getPlayedCharacterCard();
                    if (cardPlayed != null) {
                        if (max != 0 && cardPlayed.getAbility().getAction().equals(Actions.TAKE_PROFESSORS) && cardPlayed.getStatus() >= 1 && player.getNickname().equals(currentPlayer.getNickname())) {
                            maxPlayer = currentPlayer;
                            currentPlayer.getPlayedCharacterCard().setStatus(2);
                        } else if (cardPlayed.getAbility().getAction().equals(Actions.RETURN_STUDENT) && cardPlayed.getStatus() >= 1 && player.getNickname().equals(currentPlayer.getNickname())) {
                            maxPlayer = null;
                            for (Player p : players) { //eventually remove the player who had that professor
                                if (p.hasProfessorOfColor(color)) {
                                    p.removeProfessor(color);
                                    //notify the removed professors
                                    ArrayList<Colors> professors = p.getSchoolBoard().getProfessorsTable();
                                    PropertyChangeEvent evt =
                                            new PropertyChangeEvent(this, "professorTable", p.getNickname(), professors);
                                    gameListener.propertyChange(evt);
                                }
                            }
                        } else if (cardPlayed.getAbility().getAction().equals(Actions.SWAP_ENTRANCE_DINING) && cardPlayed.getStatus() >= 1 && player.getNickname().equals(currentPlayer.getNickname())) {
                            maxPlayer = null;
                            for (Player p : players) { //eventually remove the player who had that professor
                                if (p.hasProfessorOfColor(color)) {
                                    p.removeProfessor(color);
                                    //notify the removed professors
                                    ArrayList<Colors> professors = p.getSchoolBoard().getProfessorsTable();
                                    PropertyChangeEvent evt =
                                            new PropertyChangeEvent(this, "professorTable", p.getNickname(), professors);
                                    gameListener.propertyChange(evt);
                                }
                            }
                        } else {
                            maxPlayer = null;
                        }
                    } else {
                        maxPlayer = null; //in case of ties no one should have assign the professor
                    }
                }
            }
            if (maxPlayer != null) {
                for (Player player : players) { //eventually remove the player who had that professor
                    if (player.hasProfessorOfColor(color)) {
                        player.removeProfessor(color);
                        //notify the removed professors
                        ArrayList<Colors> professors = player.getSchoolBoard().getProfessorsTable();
                        PropertyChangeEvent evt =
                                new PropertyChangeEvent(this, "professorTable", player.getNickname(), professors);
                        gameListener.propertyChange(evt);
                    }
                }
                maxPlayer.addProfessor(color);
                //notify the added prof
                ArrayList<Colors> professors = maxPlayer.getSchoolBoard().getProfessorsTable();
                PropertyChangeEvent evt =
                        new PropertyChangeEvent(this, "professorTable", maxPlayer.getNickname(), professors);
                gameListener.propertyChange(evt);
            }
        }

    }

    /**
     * It computes the influence of each team on a given island. If it finds a team with more influence
     * than another assign the ownership and a new tower only if the island hasn't any owner or has an owner
     * different from the new team. It works with 2 players, 3 players and 4 players.
     *
     * @param island the island the tower needs to be placed on.
     * @throws IncorrectArgumentException if island name is wrong
     * @throws NegativeValueException     if selectedCharacterIndex is wrong
     */
    private void checkAndPlaceTower(Island island) throws IncorrectArgumentException, NegativeValueException {
        EnumMap<Colors, Integer> students = island.getStudents();
        HashMap<Towers, Integer> influenceScores = new HashMap<>();
        influenceScores.put(Towers.BLACK, 0);
        influenceScores.put(Towers.WHITE, 0);
        if (numOfPlayer % 2 == 1) influenceScores.put(Towers.GREY, 0);

        CharacterCard cardPlayed = currentPlayer.getPlayedCharacterCard();
        for (Colors studentColor : Colors.values()) {
            if (cardPlayed != null) {
                if (cardPlayed.getAbility().getAction().equals(Actions.AVOID_COLOR_INFLUENCE) && cardPlayed.getStatus() >= 1) {
                    if (studentColor.getIndex() == Colors.getStudent(cardPlayed.getChoiceIndex()).getIndex()) {
                        currentPlayer.getPlayedCharacterCard().setStatus(2);
                        increaseCharacterPrice(selectedCharacterIndex);
                        continue;
                    }
                }
            }

            if (students.get(studentColor) != 0) {
                for (Player p : players) {
                    Towers teamColor = p.getTowerColor();
                    if (p.hasProfessorOfColor(studentColor)) { //find the player with that professor
                        influenceScores.replace(teamColor, influenceScores.get(teamColor) + students.get(studentColor));

                        if (cardPlayed != null) {
                            if (cardPlayed.getAbility().getAction().equals(Actions.TOWER_INFLUENCE) && cardPlayed.getStatus() >= 1 && p.getNickname().equals(currentPlayer.getNickname())) {
                                influenceScores.replace(teamColor, influenceScores.get(teamColor));
                                currentPlayer.getPlayedCharacterCard().setStatus(2);
                                increaseCharacterPrice(selectedCharacterIndex);
                            } else {
                                if (!cardPlayed.getAbility().getAction().equals(Actions.ADD_POINTS) && teamColor.equals(island.getTowersColor())) { //counting the towers if team owns the island
                                    influenceScores.replace(teamColor, influenceScores.get(teamColor) + island.getNumOfTowers());
                                }
                            }
                        } else {
                            if (teamColor.equals(island.getTowersColor())) { //counting the towers if team owns the island
                                influenceScores.replace(teamColor, influenceScores.get(teamColor) + island.getNumOfTowers());
                            }
                        }
                    }

                    if (cardPlayed != null && cardPlayed.getAbility().getAction().equals(Actions.ADD_POINTS) && cardPlayed.getStatus() >= 1 && p.getNickname().equals(currentPlayer.getNickname())) {
                        influenceScores.replace(teamColor, influenceScores.get(teamColor) + cardPlayed.getAbility().getValue());
                        if (teamColor.equals(island.getTowersColor())) { //counting the towers if team owns the island
                            influenceScores.replace(teamColor, influenceScores.get(teamColor) + island.getNumOfTowers());
                        }
                        currentPlayer.getPlayedCharacterCard().setStatus(2);
                        increaseCharacterPrice(selectedCharacterIndex);
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

        //substitution + notify islands
        if (newTeamOwner != null) {
            ArrayList<Player> newTeam = findPlayerFromTeam(newTeamOwner);
            if (island.getNumOfTowers() == 0) {// The island was empty
                //notify prep
                StrippedIsland oldIsland = new StrippedIsland(island);
                //set ownership
                island.setTowersColor(newTeamOwner);
                try {
                    island.sumTowers(1);
                } catch (NegativeValueException ignored) {
                }

                //notify island change
                StrippedIsland islandChangedStripped = new StrippedIsland(island);
                PropertyChangeEvent evtConquest =
                        new PropertyChangeEvent(this, "island-conquest", oldIsland, islandChangedStripped);
                gameListener.propertyChange(evtConquest);

                moveTowersFromTeam(newTeam, -1);

            } else if (newTeamOwner != island.getTowersColor()) { //it means that there is a switch from team
                int switchedTowers = island.getNumOfTowers();
                moveTowersFromTeam(newTeam, -switchedTowers); //removing towers from new team player
                ArrayList<Player> oldTeam = findPlayerFromTeam(island.getTowersColor()); //oldTeamOwnerShip
                moveTowersFromTeam(oldTeam, switchedTowers); //adding towers to old team
                //notify prep
                StrippedIsland oldIsland = new StrippedIsland(island);
                //set ownership
                island.setTowersColor(newTeamOwner);
                //notify island change
                StrippedIsland islandChangedStripped = new StrippedIsland(island);
                PropertyChangeEvent evtConquest =
                        new PropertyChangeEvent(this, "island-conquest", oldIsland, islandChangedStripped);
                gameListener.propertyChange(evtConquest);
            }
        }
    }

    /**
     * Finds player from the relative team. Supporting method.
     *
     * @param teamColor the color of the team that is being checked.
     * @return the players in the team.
     */
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
     *
     * @param team   the team that the towers need to be moved from
     * @param amount the number of towers that need to be moved.
     */
    public void moveTowersFromTeam(ArrayList<Player> team, int amount) {
        int numbersOfIterations = Math.abs(amount);
        int oneTowerSigned;
        if (amount < 0) oneTowerSigned = -1;
        else oneTowerSigned = 1;

        if (team.size() == 2) { //It means we are 4 players game
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

        for (Player teamMember : team) {
            int changedTowers = teamMember.getSchoolBoard().getTowers();
            //notify towers
            PropertyChangeEvent towersEvent =
                    new PropertyChangeEvent(this, "towers", teamMember.getNickname(), changedTowers);
            gameListener.propertyChange(towersEvent);
        }
    }

    /**
     * Method that checks if the condition for the unification of two or more islands are met.
     *
     * @throws NegativeValueException As always, this game has no negative values, and any found are automatically incorrect.
     */
    public void checkUnificationIslands() throws NegativeValueException {
        boolean listChanged = false;
        Island currentTile;
        Island prevTile;
        int islandToDestroy = -1;

        for (int i = 0; i < islands.size() && !listChanged && islands.size() > 1; i++) {
            prevTile = islands.get(i);
            currentTile = islands.get((i + 1) % islands.size());

            if (prevTile.getNumOfTowers() != 0 && currentTile.getNumOfTowers() != 0
                    && (prevTile.getTowersColor().equals(currentTile.getTowersColor()))) {
                currentTile.addStudents(prevTile.getStudents());
                currentTile.sumTowers(prevTile.getNumOfTowers());
                if (prevTile.hasMotherNature() || currentTile.hasMotherNature()) currentTile.moveMotherNature();
                islandToDestroy = i;
                listChanged = true;
                StrippedIsland mergedTile = new StrippedIsland(currentTile); //notify currentTile
                StrippedIsland deletedTile = new StrippedIsland(prevTile); //notify tile to deleted
                //notifications
                PropertyChangeEvent islandMergeEvent =
                        new PropertyChangeEvent(this, "island", mergedTile, mergedTile);
                gameListener.propertyChange(islandMergeEvent);

                //notify Island merged (island that will be hidden)
                PropertyChangeEvent islandDeletedEvent =
                        new PropertyChangeEvent(this, "island-merged", deletedTile, null);
                gameListener.propertyChange(islandDeletedEvent);
            }
        }

        if (listChanged) {
            if (islandToDestroy >= 0) {
                islands.remove(islandToDestroy);
                checkUnificationIslands();
            }
        }
    }

    /**
     * Check the game over returning true if it is
     */
    public boolean isGameOver() {
        return !bag.hasEnoughStudents(numDrawnStudents) || islands.size() <= 3 || numRounds >= 9 || isTeamWithZeroTowers();
    }

    /**
     * It checks the winner is used as supporting method inside isGameOver() . It returns the
     * winning team using an arrayList of Player (that could have size of 1 or 2 depending on numOfPLayers).
     * If it returns null it's a tie.
     */

    public boolean isTeamWithZeroTowers() {
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
            boolean teamWin = true;
            for (Player p : team) {
                if (p.getPlayerTowers() != 0) {
                    teamWin = false;
                }
            }
            if (teamWin) return true;
        }

        return false;
    }

    public String checkWinner() {
        ArrayList<Player> team1 = findPlayerFromTeam(Towers.WHITE);
        ArrayList<Player> team2 = findPlayerFromTeam(Towers.BLACK);
        ArrayList<ArrayList<Player>> teams = new ArrayList<>();
        teams.add(team1);
        teams.add(team2);
        if (numOfPlayer % 2 == 1) {
            ArrayList<Player> team3 = findPlayerFromTeam(Towers.GREY);
            teams.add(team3);
        }


        //Find winner team in case of fewer towers (or 0 towers)
        int minTowers = 42;
        ArrayList<ArrayList<Player>> teamsTie = new ArrayList<>();
        ArrayList<Player> teamMinTowers = null;

        for (ArrayList<Player> team : teams) {
            int teamTowers = 0;
            for (Player p : team) {
                teamTowers += p.getPlayerTowers();
            }
            if (teamTowers <= minTowers) {
                if (teamTowers == minTowers) { //there is a tie of towers between teams
                    minTowers = 42;
                    teamsTie.add(teamMinTowers);
                    teamsTie.add(team);
                } else {
                    minTowers = teamTowers;
                    teamMinTowers = team;
                }
            }
        }


        if (minTowers != 42) {
            return teamMinTowers.get(0).getTowerColor().toString();
        } else if (teamsTie.size() > 1) { //there is a tie between teams
            ArrayList<Player> winningTeam = null;
            int professorsOfTeam = 0;
            int maxProfessors = -1;
            for (ArrayList<Player> team : teamsTie) {

                for (Player p : team) {
                    for (Colors colorProf : Colors.values()) {
                        if (p.hasProfessorOfColor(colorProf)) professorsOfTeam += 1;
                    }
                }

                if (professorsOfTeam >= maxProfessors) {
                    if (professorsOfTeam == maxProfessors) {
                        winningTeam = null;
                    } else {
                        maxProfessors = professorsOfTeam;
                        winningTeam = team;
                    }
                }
            }

            if (winningTeam != null) {
                if (winningTeam.size() > 0) return winningTeam.get(0).getTowerColor().toString();
            }

        }
        return null;  //no win
    }

    /**
     * Updates schoolBoard enumMaps according to Character card action.
     *
     * @param choiceIndex the color chosen.
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws ProfessorNotFoundException If the character power causes a professor gain or loss and that generates an error this exception is thrown.
     */
    public void returnStudentsEffect(int choiceIndex) throws NegativeValueException, ProfessorNotFoundException, IncorrectArgumentException {
        EnumMap<Colors, Integer> enumMap = new EnumMap<>(Colors.class);
        for (Player player : players) {
            if (player.getSchoolBoard().getStudentsByColor(Colors.getStudent(choiceIndex)) < 3) {
                enumMap.put(Colors.getStudent(choiceIndex), player.getSchoolBoard().getStudentsByColor(Colors.getStudent(choiceIndex)));
            } else {
                enumMap.put(Colors.getStudent(choiceIndex), 3);
            }
            player.getSchoolBoard().removeDiningStudents(enumMap);
        }

        checkAndPlaceProfessor(); //check and eventually modifies and notifies
        //notify dining AND entrance AND COINS change
        for (Player player : players) {
            EnumMap<Colors, Integer> newDining = player.getSchoolBoard().getDining();
            PropertyChangeEvent event =
                    new PropertyChangeEvent(this, "entrance", player.getNickname(), player.getSchoolBoard().getEntrance());
            gameListener.propertyChange(event);
            PropertyChangeEvent evt =
                    new PropertyChangeEvent(this, "dining", player.getNickname(), newDining);
            gameListener.propertyChange(evt);
        }
    }

    //getters necessary to build StrippedModel :

    /**
     * Returns the Character cards currently in the game.
     *
     * @return CharacterCards
     */
    public ArrayList<CharacterCard> getCharacterCards() {
        return characterCards;
    }

    /**
     * Returns the clouds.
     *
     * @return clouds.
     */
    public ArrayList<Cloud> getClouds() {
        return clouds;
    }

    /**
     * Island getter.
     *
     * @return islands.
     */
    public LinkedList<Island> getIslands() {
        return islands;
    }

    /**
     * Method used in character card setup and testing
     *
     * @param index         The index of the card to setup for testing
     * @param characterCard the Character card itself.
     */
    protected void setCharacterCards(int index, CharacterCard characterCard) {
        characterCards.set(index, characterCard);
    }

    /**
     * Notifies listener of this class about changes on an island.
     *
     * @param islandToChange The island in which the change occurs.
     * @param studentsToAdd  The students that need to be added.
     * @throws NegativeValueException As always, this game has no negative values, and any found are automatically incorrect.
     */
    public void notifyIsland(Island islandToChange, EnumMap<Colors, Integer> studentsToAdd) throws NegativeValueException {
        //notify island change and modify
        StrippedIsland oldIsland = new StrippedIsland(islandToChange);
        islandToChange.addStudents(studentsToAdd); //adding students
        StrippedIsland changedIsland = new StrippedIsland(islandToChange);
        PropertyChangeEvent evt =
                new PropertyChangeEvent(this, "island", oldIsland, changedIsland);
        gameListener.propertyChange(evt);
    }

    /**
     * Returns the first player.
     *
     * @return First player
     */
    public String getFirstPlayer() {
        return firstPlayer;
    }

    public PropertyChangeListener getGameListener() {
        return gameListener;
    }

    public int getSelectedCharacterIndex() {
        return selectedCharacterIndex;
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

    /**
     * Players getter.
     *
     * @return Players.
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * CurrentPlayer getter.
     *
     * @return CurrentPlayer
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * State getter.
     *
     * @return State.
     */
    public State getCurrentState() {
        return state;
    }

    /**
     * playerDrawnOut getter.
     *
     * @return playerDrawnOut.
     */
    public boolean getPlayerDrawnOut() {
        return playerDrawnOut;
    }

    /**
     * Returns the cloudTIle found through its index.
     *
     * @param index The index of the cloudTile.
     * @return CloudTile.
     */
    public Cloud getCloudTile(int index) {
        return clouds.get(index);
    }

    /**
     * MotherNaturePosition getter.
     *
     * @return MotherNaturePosition.
     */
    public int getMotherNaturePosition() {
        return motherNaturePosition;
    }

    /**
     * Island getter
     *
     * @param index the index of the island to return.
     * @return island.
     */
    public Island getIsland(int index) {
        return islands.get(index);
    }

}
