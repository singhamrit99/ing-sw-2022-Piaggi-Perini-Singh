package it.polimi.ingsw.network.server;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.deck.assistantcard.AssistantCardDeck;
import it.polimi.ingsw.network.server.stripped.*;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.server.commands.Command;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.tiles.Cloud;
import it.polimi.ingsw.model.tiles.Island;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Room implements PropertyChangeListener {
    private final String roomName;
    private final ArrayList<ClientConnection> players;
    private boolean expertMode;
    final private Controller controller;
    private boolean inGame;
    private HashMap<ClientConnection, ArrayList<PropertyChangeEvent>> eventsBuffer;

    public Room(String roomName, ArrayList<ClientConnection> playerList) {
        this.roomName = roomName;
        this.players = playerList;
        expertMode = false;
        controller = new Controller();
    }

    public synchronized void setExpertmode(boolean expertmode) {
        this.expertMode = expertmode;
    }

    public synchronized boolean getExpertMode() {
        return expertMode;
    }

    public synchronized String getRoomName() {
        return roomName;
    }

    public synchronized ArrayList<ClientConnection> getPlayers() {
        return players;
    }

    public synchronized void addUser(ClientConnection user) {
        players.add(user);
    }

    public synchronized void removeUser(ClientConnection user) {
        players.remove(user);
    }

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
        PropertyChangeEvent firstPlayerEvt=
                new PropertyChangeEvent(this,"first-player",null,newGame.getCurrentPlayer().getNickname());
        addEventToBuffer(firstPlayerEvt);
    }

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
        //This should be able to provide strippedCharacters with the correct id for controller calls
        int i = 0;
        for (CharacterCard c : charactersCard) {
            System.out.println("Building stripped character card\n");
            StrippedCharacter newStrippedCharCard = new StrippedCharacter(c);
            newStrippedCharCard.setCharacterID(i);
            strippedCharacters.add(newStrippedCharCard);
            i++;
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
        System.out.println("Adding strippedModel send to buffer\n");
        addEventToBuffer(evtInitialGame);
    }

    public synchronized void commandInvoker(Command command) throws MotherNatureLostException, NegativeValueException, AssistantCardNotFoundException, ProfessorNotFoundException, IncorrectPlayerException, IncorrectArgumentException, NotEnoughCoinsException, IncorrectStateException {
        command.execute(controller);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        addEventToBuffer(evt);
    }
    private void addEventToBuffer(PropertyChangeEvent event) {
        for (ClientConnection clientBufferEvents : players) {
            ArrayList<PropertyChangeEvent> eventsQueue = eventsBuffer.get(clientBufferEvents);
            eventsQueue.add(event);
            eventsBuffer.replace(clientBufferEvents, eventsQueue);
        }
    }
    public synchronized ArrayList<PropertyChangeEvent> getBuffer(ClientConnection asker){
        ArrayList<PropertyChangeEvent> buffer = new ArrayList<>();
        for (PropertyChangeEvent event: eventsBuffer.get(asker)){
            buffer.add(event);
        }
        eventsBuffer.replace(asker,new ArrayList<>()); //flushing the buffer of Events on Server side
        return buffer;
    }
    public synchronized void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
    public synchronized boolean isInGame() {
        return inGame;
    }
}