package it.polimi.ingsw.client;

import it.polimi.ingsw.server.serverStub;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class Client{
    final private String ip;
    final private int port;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        connect();
    }

    public void connect() {
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(ip,port);
            // Looking up the registry for the remote object
            serverStub server = (serverStub) registry.lookup("server");
            System.out.println("connection done");
            server.registerUser("Cooper");
            //server.registerUser("test");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
