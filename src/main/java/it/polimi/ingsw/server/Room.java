package it.polimi.ingsw.server;
import it.polimi.ingsw.model.stripped.StrippedModel;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.charactercard.CharacterCard;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.NegativeValueException;
import it.polimi.ingsw.model.stripped.StrippedBoard;
import it.polimi.ingsw.model.stripped.StrippedCharacter;
import it.polimi.ingsw.model.stripped.StrippedClouds;
import it.polimi.ingsw.model.stripped.StrippedIslands;
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
    private StrippedModel localModel;

    public Room(String roomName, ArrayList<ClientConnection> playerList) {
        this.roomName = roomName;
        this.players = playerList;
        expertMode = false;
        controller= new Controller();
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
        controller.initializeGame(expertMode, players.size(), nicknames);
        for(ClientConnection c: players)
        {
            c.startView(controller);
        }
    }

    public void buildLocalModel(ArrayList<Player> players, ArrayList<CharacterCard> charactersCard, ArrayList<Cloud> clouds, LinkedList<Island> islands){
        ArrayList<StrippedBoard> strippedBoards =new ArrayList<>();
        ArrayList<StrippedCharacter> strippedCharacters = new ArrayList<>();

        for (Player p: players) {
           StrippedBoard newStrippedBoard = new StrippedBoard(p);
           strippedBoards.add(newStrippedBoard);
        }

        for(CharacterCard c: charactersCard){
            StrippedCharacter newStrippedCharCard = new StrippedCharacter(c);
            strippedCharacters.add(newStrippedCharCard);
        }

        StrippedClouds strippedClouds = new StrippedClouds(clouds);
        StrippedIslands strippedIslands = new StrippedIslands(islands);

        localModel = new StrippedModel(strippedBoards,strippedCharacters,strippedClouds,strippedIslands);
        //broadcast(); //invio con il Command Pattern del LocalModel a tutti i clients , maybe da sostituire con una richiesta .
    }

    public void commandInvoker(Command command){
        command.execute(controller);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt){
        broadcast(evt);
    }

    private void broadcast(PropertyChangeEvent event){ //todo we have to use Command Pattern and ServerCommand interface
        for (ClientConnection client: players) {
            client.sendEvent(event);
        }
    }
}
