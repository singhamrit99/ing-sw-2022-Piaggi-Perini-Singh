package it.polimi.ingsw.server;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.NegativeValueException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class Room implements PropertyChangeListener {
    private final String roomName;
    private final ArrayList<ClientConnection> players;
    private boolean expertMode;
    final private Controller controller;


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
    public void commandInvoker(Command command){
        command.execute(controller);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt){
        switch ((String)evt.getSource()){
            case "boards": break;
            case "characters": break;
            case "clouds": break;
            case "islands":
                String islandName = evt.getPropertyName();
                break;
            case "players": break;
        }
    }
}
