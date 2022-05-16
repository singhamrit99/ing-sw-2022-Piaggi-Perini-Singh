package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.stripped.*;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.NegativeValueException;
import it.polimi.ingsw.model.tiles.Cloud;
import it.polimi.ingsw.model.tiles.Island;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedList;

public class Room implements PropertyChangeListener {
    private final String roomName;
    private final ArrayList<ClientConnection> players;
    private boolean expertMode;
    final private Controller controller;

    public Room(String roomName, ArrayList<ClientConnection> playerList) {
        this.roomName = roomName;
        this.players = playerList;
        expertMode = false;
        controller = new Controller();
    }

    public void setExpertmode(boolean expertmode) {
        this.expertMode = expertmode;
    }

    public boolean getExpertMode() {
        return expertMode;
    }

    public String getRoomName() {
        return roomName;
    }

    public ArrayList<ClientConnection> getPlayers() {
        return players;
    }

    public void addUser(ClientConnection user) {
        players.add(user);
    }

    public void removeUser(ClientConnection user) {
        players.remove(user);
    }

    public void startGame() throws NegativeValueException, IncorrectArgumentException {
        ArrayList<String> nicknames = new ArrayList<>();
        for (ClientConnection cl : players) {
            nicknames.add(cl.getNickname());
        }
        Game newGame = controller.initializeGame(this, expertMode, players.size(), nicknames);
        buildStrippedModel(newGame.getPlayers(), newGame.getCharacterCards(),
                newGame.getClouds(), newGame.getIslands());
        for (ClientConnection c : players) {
            c.startView(controller);
        }
    }

    private void buildStrippedModel(ArrayList<Player> players, ArrayList<CharacterCard> charactersCard, ArrayList<Cloud> clouds, LinkedList<Island> islands) {
        ArrayList<StrippedBoard> strippedBoards = new ArrayList<>();
        ArrayList<StrippedCharacter> strippedCharacters = new ArrayList<>();
        ArrayList<StrippedCloud> strippedClouds = new ArrayList<>();
        ArrayList<StrippedIsland> strippedIslands = new ArrayList<>();

        for (Player p : players) {
            StrippedBoard newStrippedBoard = new StrippedBoard(p);
            strippedBoards.add(newStrippedBoard);
        }
        //This should be able to provide strippedCharacters with the correct id for controller calls
        int i=0;
        for (CharacterCard c : charactersCard) {
            StrippedCharacter newStrippedCharCard = new StrippedCharacter(c);
            newStrippedCharCard.setCharacterID(i);
            strippedCharacters.add(newStrippedCharCard);
            i++;
        }

        for (Cloud c : clouds) {
            StrippedCloud newStrippedCloud = new StrippedCloud(c.getName(), c.getStudents());
            strippedClouds.add(newStrippedCloud);
        }

        for (Island s : islands) {
            StrippedIsland newStrippedIsland = new StrippedIsland(s);
            strippedIslands.add(newStrippedIsland);
        }
        StrippedModel strippedModel = new StrippedModel(strippedBoards, strippedCharacters, strippedClouds, strippedIslands);
        SourceEvent modelInitSource = new SourceEvent(getRoomName(), "init");
        PropertyChangeEvent evtInitialGame = new PropertyChangeEvent(modelInitSource, "init", null, strippedModel);
        broadcast(evtInitialGame);
    }

    public void commandInvoker(Command command) {
        command.execute(controller);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("error")) sendErrorEvent(evt);
        else broadcast(evt);
    }

    private void broadcast(PropertyChangeEvent event) {
        for (ClientConnection client : players) {
            client.sendEvent(event);
        }
    }

    private void sendErrorEvent(PropertyChangeEvent error) {
        SourceEvent src = (SourceEvent) error.getSource();
        String nickname = src.getWho();
        for (ClientConnection client : players) {
            if (client.getNickname().equals(nickname)) {
                client.sendEvent(error);
                break;
            }
        }
    }
}
