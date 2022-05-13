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
import it.polimi.ingsw.server.events.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedList;

public class Room implements PropertyChangeListener {
    private final String roomName;
    private final ArrayList<ClientConnection> players;
    private boolean expertMode;
    final private Controller controller;
    private StrippedModel strippedModel;

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
        Game newGame = controller.initializeGame(this,expertMode, players.size(), nicknames);
        buildStrippedModel(newGame.getPlayers(),newGame.getCharacterCards(),
                newGame.getClouds(),newGame.getIslands());
        for(ClientConnection c: players)
        {
            c.startView(controller);
        }
    }

    private void buildStrippedModel(ArrayList<Player> players, ArrayList<CharacterCard> charactersCard, ArrayList<Cloud> clouds, LinkedList<Island> islands){
        ArrayList<StrippedBoard> strippedBoards =new ArrayList<>();
        ArrayList<StrippedCharacter> strippedCharacters = new ArrayList<>();
        ArrayList<StrippedCloud> strippedClouds = new ArrayList<>();
        ArrayList<StrippedIsland> strippedIslands = new ArrayList<>();

        for (Player p: players) {
           StrippedBoard newStrippedBoard = new StrippedBoard(p);
           strippedBoards.add(newStrippedBoard);
        }

        for(CharacterCard c: charactersCard){
            StrippedCharacter newStrippedCharCard = new StrippedCharacter(c);
            strippedCharacters.add(newStrippedCharCard);
        }

        for (Cloud c:clouds) {
            StrippedCloud newStrippedCloud = new StrippedCloud(c.getName(),c.getStudents());
            strippedClouds.add(newStrippedCloud);
        }

        for (Island s: islands) {
            StrippedIsland newStrippedIsland = new StrippedIsland(s);
            strippedIslands.add(newStrippedIsland);
        }
        strippedModel = new StrippedModel(strippedBoards,strippedCharacters,strippedClouds,strippedIslands);
    }

    public void commandInvoker(Command command){
        command.execute(controller);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt){
        switch((String) evt.getPropertyName()){
            case "entrance":
            case "dining":
            case "coins":
            case "professorTable":
                BoardEvent boardEvent = new BoardEvent(evt);
                broadcast(boardEvent);
                break;
            case "character":
                CharacterEvent charCommand = new CharacterEvent(evt);
                broadcast(charCommand);
                break;
            case "cloud":
                CloudEvent cloudEventCommand = new CloudEvent(evt);
                broadcast(cloudEventCommand);
                break;
            case "island":
                IslandEvent islandEventCommand = new IslandEvent(evt);
                broadcast(islandEventCommand);
                break;
            case "message":
                MessageEvent messageEventCommand = new MessageEvent(evt,false);
                broadcast(messageEventCommand);
                break;
            case "error":
                MessageEvent errorEvent = new MessageEvent(evt,true);
                sendErrorEvent(errorEvent);
            default:
                System.out.println("exception da fare in Room"); //todo
                break;
        }
    }

    private void broadcast(Event event){
        for (ClientConnection client: players) {
            client.sendEvent(event);
        }
    }

    private void sendErrorEvent(Event error){
        String nickname = error.getSource().getWho();
        for (ClientConnection client: players) {
            if(client.getNickname().equals(nickname)){
                client.sendEvent(error);
                break;
            }
        }
    }
}
