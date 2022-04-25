package it.polimi.ingsw.server;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientConnection implements Runnable {

    final private Socket socket;
    private ObjectOutputStream out;
    private Scanner in;
    final private Server server;

    private boolean active = true;

    public ClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    private synchronized boolean isActive(){
        return active;
    }

    @Override
    public void run(){
        String name;
        try{
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            sendString("Welcome!\nWhat is your name?");
            name = in.nextLine();

            while(server.getUserNames().contains(name)){
                sendString("Ops, there is another client with the same username! Choose another one please. \n");
                name = in.nextLine();
            }
            server.registerUser(this, name);

            sendString("Possible options: \n JOIN to join a room; \n CREATE to create a new room;");

            String command;
            while(true){
                command = in.nextLine();
                switch(command){
                    case "join":
                        requestRoomJoin();
                        break;
                    case "create":
                        requestRoomCreation();
                        break;
                    default:
                        sendString("Command not recognized");
                        break;
                }
            }
        }catch (IOException | NoSuchElementException e) {
            System.err.println("Error from client, " + e.getMessage());
            closeConnection();
        }
    }

    public synchronized void requestRoomCreation(){
        sendString("Insert room name: \n");
        String nameRoom;
        nameRoom = in.nextLine();
        while(server.getRoomsList().contains(nameRoom)){
            sendString("Ops, there is another room with the same name! Choose another one please. \n");
            nameRoom = in.nextLine();
        }
        server.createRoom(nameRoom,this);
    }

    public synchronized void requestRoomJoin(){
        sendString("Select the room: \n");
        if(server.getRoomsList().isEmpty()) sendString("There aren't rooms, you can only create a new one");
        else{
            sendArrayList(server.getRoomsList());
            String nameRoomToJoin;
            nameRoomToJoin = in.nextLine();
            while(!server.getRoomsList().contains(nameRoomToJoin)){
                sendString("Ops, there aren't rooms with the same name\n");
                nameRoomToJoin = in.nextLine();
            }
            server.joinRoom(nameRoomToJoin,this);
            sendString("You are entered room "+nameRoomToJoin+ " successfully \n");
            sendString("In this room there are:");
            ArrayList<String> userNamesInRoom = server.getUserNamesInRoom(nameRoomToJoin);
            if(!userNamesInRoom.isEmpty())sendArrayList(userNamesInRoom);
        }
    }

    public synchronized void closeConnection() {
        sendString("Connection closed!");
        try {
            socket.close();
            server.deregisterConnection(this);
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
        }
        active = false;
    }


    public synchronized void sendString(String message) {
        try {
            out.reset();
            out.writeObject(message);
            out.flush();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    }
    public synchronized void sendArrayList(ArrayList<String> messageArray) {
        for(String message : messageArray)sendString(message);
    }

}
