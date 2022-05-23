package it.polimi.ingsw.server;
import java.io.IOException;
public class ClientConnection{
    private String clientRoom = null;
    private String nickname;
    private boolean inGame;
    public ClientConnection(String nickname) {
        this.nickname = nickname;
        inGame = false;
    }
    public String getNickname() {
        return nickname;
    }
    public String getRoom() {
        return clientRoom;
    }

    public void setRoom(String room) {
        this.clientRoom = room;
    }
    public boolean inGame(){ return inGame;}

    public void setInGame(boolean isPlaying){
        inGame = isPlaying;
    }
}
