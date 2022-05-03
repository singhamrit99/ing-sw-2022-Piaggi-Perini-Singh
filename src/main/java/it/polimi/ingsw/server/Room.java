package it.polimi.ingsw.server;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.NegativeValueException;

import java.util.ArrayList;

public class Room {
    private final String roomName;
    private final ArrayList<ClientConnection> players;
    private boolean expertMode;

    public Room(String roomName, ArrayList<ClientConnection> playerList) {
        this.roomName = roomName;
        this.players = playerList;
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
        Game game = new Game(expertMode, players.size(), nicknames);
        for(ClientConnection c: players)
        {
            c.startView();
        }
    }
}
