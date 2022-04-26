package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.exceptions.IncorrectArgumentException;
import it.polimi.ingsw.model.exceptions.NegativeValueException;

import java.util.ArrayList;

public class Lobby {
    private final String leader;
    private final String roomName;
    private final ArrayList<ClientConnection> playerList;
    private ArrayList<String> playerNicknames;
    private boolean expertMode;

    public Lobby(String leader, String roomName, ArrayList<ClientConnection> playerList) {
        this.leader = leader;
        this.roomName = roomName;
        this.playerList = playerList;
    }

    public void setExpertmode(boolean expertmode) {
        this.expertMode = expertmode;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    public String getLeader() {
        return leader;
    }

    public String getRoomName() {
        return roomName;
    }

    public ArrayList<ClientConnection> getPlayerList() {
        return playerList;
    }

    public void addUser(ClientConnection user) {
        playerList.add(user);
    }

    public void removeUser(ClientConnection user) {
        playerList.remove(user);
    }

    public void startGame() throws NegativeValueException, IncorrectArgumentException {
        for (ClientConnection cl : playerList) {
            playerNicknames.add(cl.getPlayerName());
        }
        Game game = new Game(this.expertMode, this.playerList.size(), this.playerNicknames);

    }
}
