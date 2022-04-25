package it.polimi.ingsw.server;

import java.util.ArrayList;

public class Lobby {
    private String leader;
    private String roomName;
    private ArrayList<ClientConnection> playerList;

    public String getLeader() {
        return leader;
    }

    public String getRoomName() {
        return roomName;
    }

    public ArrayList<ClientConnection> getPlayerList() {
        return playerList;
    }

    public Lobby(String leader, String roomName, ArrayList<ClientConnection> playerList) {
        this.leader = leader;
        this.roomName = roomName;
        this.playerList = playerList;
    }

    public void addUser(ClientConnection user)
    {
        playerList.add(user);
    }
    public void removeUser(ClientConnection user)
    {
        playerList.remove(user);
    }
}
