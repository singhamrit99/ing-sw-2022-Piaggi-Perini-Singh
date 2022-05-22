package it.polimi.ingsw.server;
import java.io.IOException;
public class ClientConnection{
    private String clientRoom = null;
    private String nickname;
    private boolean isPlaying;
    public ClientConnection(String nickname) {
        this.nickname = nickname;
        isPlaying = false;
    }
    public String getNickname() {
        return nickname;
    }
    public String getRoom() {
        return clientRoom;
    }
    public boolean isPlaying(){ return isPlaying;}
}
