package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 23023;
    final private ServerSocket serverSocket;
    final private ExecutorService executor = Executors.newFixedThreadPool(128);
    private HashMap<ClientConnection,String> users;
    private HashMap<String, Lobby> rooms;
    private int connections = 0;
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    public void run(){
        users = new HashMap<>();
        rooms = new HashMap<>();
        System.out.println("Server has started");
        while(true){
            try {
                Socket newSocket = serverSocket.accept();
                connections++;
                ClientConnection socketConnection = new ClientConnection(newSocket, this);
                System.out.println("Just connected a new dude! #" + connections);
                executor.submit(socketConnection);
            } catch (IOException e) { System.out.println("Connection Error!");
            }
        }
    }

    public synchronized void registerUser(ClientConnection c, String name){
        users.put(c,name);
        System.out.println("user '"+ name + "' is in Waiting List");
    }

    public ArrayList<String> getUserNames(){
        return new ArrayList<>(users.values());
    }

    public ArrayList<String> getRoomsList(){return new ArrayList<>(rooms.keySet());   }


    public synchronized void deregisterConnection(ClientConnection c){
        System.out.println("Client deregistered");
        users.remove(c);
    }


    public synchronized void createRoom(String roomName, ClientConnection user){
        ArrayList<ClientConnection> userInNewRoom = new ArrayList<>();
        userInNewRoom.add(user);
        Lobby newlobby = new Lobby(user.getPlayerName(),roomName,userInNewRoom);
        rooms.put(newlobby.getRoomName(), newlobby);
        System.out.println("User "+ users.get(user) +" just created "+roomName+"\n");
    }


   /* public synchronized void createRoom(String roomName, ClientConnection user){
        ArrayList<ClientConnection> userInNewRoom = new ArrayList<>();
        userInNewRoom.add(user);
        rooms.put(roomName,userInNewRoom);
        System.out.println("User "+ users.get(user) +" just created "+roomName+"\n");
    }*/

    public synchronized void joinRoom(String roomName, ClientConnection user){
        if(rooms.containsKey(roomName)) {
            Lobby newUsers;
            newUsers = rooms.get(roomName);
            if(user.getClientRoom()!=null)
            {
                Lobby oldLobby;
                oldLobby= rooms.get(user.getClientRoom());
                oldLobby.removeUser(user);
                System.out.println("Player " + user.getPlayerName()+" in lobby "+ user.getClientRoom()+ " changed lobby\n");
            }
            newUsers.addUser(user);
            rooms.replace(roomName, newUsers);
        }
    }

    public synchronized ArrayList<ClientConnection> getUserNamesInRoom(String roomName) {

        return  rooms.get(roomName).getPlayerList();

    }



}

