package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.deck.assistantcard.AssistantCardDeck;
import it.polimi.ingsw.model.enumerations.Actions;
import it.polimi.ingsw.model.tiles.Cloud;
import it.polimi.ingsw.model.tiles.Island;
import it.polimi.ingsw.network.server.commands.Command;
import it.polimi.ingsw.network.server.stripped.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Room implements PropertyChangeListener {
    private final String roomName;
    private ArrayList<ClientConnection> players;
    private boolean expertMode;
    final private Controller controller;
    private boolean inGame;
    private HashMap<ClientConnection, ArrayList<PropertyChangeEvent>> eventsBuffer;

    /**
     * The room's constructor, called when the player calls the Create command in CLI or creates a room in GUI.
     *
     * @param roomName   the given name of the room.
     * @param playerList the Client Connection arraylist, used for server purposes.
     */
    public Room(String roomName, ArrayList<ClientConnection> playerList) {
        this.roomName = roomName;
        this.players = playerList;
        expertMode = false;
        controller = new Controller();
    }

    /**
     * Setter for expert mode.
     *
     * @param expertmode boolean value to set to
     */
    public synchronized void setExpertmode(boolean expertmode) {
        this.expertMode = expertmode;
    }

    /**
     * Getter for expert mode
     *
     * @return boolean value for expert mode.
     */
    public synchronized boolean getExpertMode() {
        return expertMode;
    }

    /**
     * Return the room's name.
     *
     * @return String room name.
     */
    public synchronized String getRoomName() {
        return roomName;
    }

    /**
     * Returns the connections' arraylist.
     *
     * @return players arraylist.
     */
    public synchronized ArrayList<ClientConnection> getPlayers() {
        return players;
    }

    /**
     * Method used to add a connection to the list.
     *
     * @param user the client to add.
     */
    public synchronized void addUser(ClientConnection user) {
        players.add(user);
    }

    /**
     * Method used to remove a connection from the list.
     *
     * @param user the user that needs to be removed.
     */
    public synchronized void removeUser(ClientConnection user) {
        players.remove(user);
    }

    /**
     * The method that is called when a game it started. This is the place where the game class is created.
     *
     * @throws NegativeValueException     As always, this game has no negative values, and any found are automatically incorrect.
     * @throws IncorrectArgumentException Thrown if any of the parameters used by the method are invalid.
     * @throws InterruptedException       exception thrown when this method is interrupted
     */
    public synchronized void startGame() throws NegativeValueException, IncorrectArgumentException, InterruptedException {
        ArrayList<String> nicknames = new ArrayList<>();
        eventsBuffer = new HashMap<>();
        for (ClientConnection cl : players) {
            nicknames.add(cl.getNickname());
            ArrayList<PropertyChangeEvent> bufferArrayEvent = new ArrayList<>();
            eventsBuffer.put(cl, bufferArrayEvent);
        }
        Game newGame = controller.initializeGame(this, expertMode, players.size(), nicknames);
        buildStrippedModel(newGame.getPlayers(), newGame.getCharacterCards(),
                newGame.getClouds(), newGame.getIslands());

        PropertyChangeEvent firstPlayerChange =
                new PropertyChangeEvent(this, "first-player-change", null, newGame.getFirstPlayer());
        addEventToBuffer(firstPlayerChange);

        PropertyChangeEvent firstPlayerEvt =
                new PropertyChangeEvent(this, "current-player", null, newGame.getCurrentPlayer().getNickname());
        addEventToBuffer(firstPlayerEvt);
        setInGame(true);
    }

    /**
     * The method that builds the Stripped model that's used by the client, using each class to build the Stripped Version of itself.
     *
     * @param players        The player ArrayList from Game.
     * @param charactersCard The Character ArrayList from Game.
     * @param clouds         The Clouds ArrayList from Game.
     * @param islands        The Islands ArrayList from Game.
     */
    private synchronized void buildStrippedModel(ArrayList<Player> players, ArrayList<CharacterCard> charactersCard, ArrayList<Cloud> clouds, LinkedList<Island> islands) {
        ArrayList<StrippedBoard> strippedBoards = new ArrayList<>();
        ArrayList<StrippedCharacter> strippedCharacters = new ArrayList<>();
        ArrayList<StrippedCloud> strippedClouds = new ArrayList<>();
        ArrayList<StrippedIsland> strippedIslands = new ArrayList<>();
        ArrayList<AssistantCardDeck> assistantCardDecks = new ArrayList<>();

        for (Player p : players) {
            StrippedBoard newStrippedBoard = new StrippedBoard(p);
            strippedBoards.add(newStrippedBoard);
            assistantCardDecks.add(p.getAssistantCardDeck());
        }


        for (CharacterCard c : charactersCard) {
            StrippedCharacter newStrippedCharCard = new StrippedCharacter(c);
            strippedCharacters.add(newStrippedCharCard);
        }

        for (Cloud c : clouds) {
            StrippedCloud newStrippedCloud = new StrippedCloud(c);
            strippedClouds.add(newStrippedCloud);
        }

        for (Island s : islands) {
            StrippedIsland newStrippedIsland = new StrippedIsland(s);
            strippedIslands.add(newStrippedIsland);
        }
        //notify stripped Model
        StrippedModel strippedModel = new StrippedModel(strippedBoards, strippedCharacters, strippedClouds, strippedIslands, assistantCardDecks);
        PropertyChangeEvent evtInitialGame =
                new PropertyChangeEvent(this, "init", null, strippedModel);
        addEventToBuffer(evtInitialGame);
    }

    /**
     * The command invoker that every command is called by.
     *
     * @param command The command that is created in the View to perform game actions.
     * @throws MotherNatureLostException      Thrown if the game can't calculate the position of Mother Nature.
     * @throws NegativeValueException         As always, this game has no negative values, and any found are automatically incorrect.
     * @throws AssistantCardNotFoundException Thrown if the requested assistant card string can't be found in the deck.
     * @throws ProfessorNotFoundException     If action causes a professor gain or loss and that generates an error this exception is thrown.
     * @throws IncorrectPlayerException       Thrown if the player that called the method isn't the current player.
     * @throws IncorrectArgumentException     Thrown if any of the parameters used by the method are invalid.
     * @throws NotEnoughCoinsException        Thrown if the player that tried to play the card doesn't have enough coins to buy it.
     * @throws IncorrectStateException        Thrown if the method is called in an incorrect phase.
     */
    public synchronized void commandInvoker(Command command) throws MotherNatureLostException, NegativeValueException, AssistantCardNotFoundException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, NotEnoughCoinsException, IncorrectStateException, FullDiningException, CardPlayedInTurnException, AssistantCardAlreadyPlayed {
        command.execute(controller);
    }

    /**
     * Adds event to event buffer
     *
     * @param evt the event that is being added to the buffer.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        addEventToBuffer(evt);
    }

    /**
     * Add event to event buffer
     *
     * @param event the event that is being added.
     */
    private void addEventToBuffer(PropertyChangeEvent event) {
        for (ClientConnection clientBufferEvents : players) {
            ArrayList<PropertyChangeEvent> eventsQueue = eventsBuffer.get(clientBufferEvents);
            eventsQueue.add(event);
            eventsBuffer.replace(clientBufferEvents, eventsQueue);
        }
    }

    /**
     * Returns the event buffer.
     *
     * @param asker The client that requested the event buffer
     * @return the event buffer.
     */
    public synchronized ArrayList<PropertyChangeEvent> getBuffer(ClientConnection asker) {
        ArrayList<PropertyChangeEvent> buffer = new ArrayList<>();
        for (PropertyChangeEvent event : eventsBuffer.get(asker)) {
            buffer.add(event);
        }
        eventsBuffer.replace(asker, new ArrayList<>()); //flushing the buffer of Events on Server side
        return buffer;
    }

    /**
     * Setter for inGame field.
     *
     * @param inGame boolean inGame status.
     */
    public synchronized void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    /**
     * Getter for inGame field.
     *
     * @return inGame value.
     */
    public synchronized boolean isInGame() {
        return inGame;
    }

    /**
     * Adds "user left the game" event to buffer. Needs to be treated differently than the others.
     *
     * @param finalEvent the disconnect event.
     */
    public synchronized void notifyPlayerInGameLeaves(PropertyChangeEvent finalEvent) {
        addEventToBuffer(finalEvent);
    }
}
