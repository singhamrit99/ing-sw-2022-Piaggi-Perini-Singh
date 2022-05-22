package it.polimi.ingsw.server;
import java.io.IOException;
public class ClientConnection extends java.rmi.server.UnicastRemoteObject implements Runnable{
    private String clientRoom = null;
    private String nickname;
    public ClientConnection(String nickname) { this.nickname = nickname;}
    public String getNickname() {
        return nickname;
    }
    @Override
    public void run() {

    }

    public String getRoom() {
        return clientRoom;
    }

}
