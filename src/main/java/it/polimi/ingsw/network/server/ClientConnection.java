package it.polimi.ingsw.network.server;

public class ClientConnection{
    private String clientRoom = null;
    private String nickname;

    private  boolean up;
    private boolean inGame;
    public ClientConnection(String nickname) {
        this.nickname = nickname;
        inGame = false;
        up = true;
    }
    public synchronized String getNickname() {
        return nickname;
    }
    public synchronized String getRoom() {
        return clientRoom;
    }
    public synchronized  void setRoom(String room) {
        this.clientRoom = room;
    }
    public synchronized boolean inGame(){ return inGame;}
    public synchronized void setInGame(boolean isPlaying){
        inGame = isPlaying;
    }
    public synchronized boolean isUp(){
        return up;
    }
    public synchronized void setUp(){
        up = true;
    }
    public synchronized void setDown(){
        up = false;
    }
}
