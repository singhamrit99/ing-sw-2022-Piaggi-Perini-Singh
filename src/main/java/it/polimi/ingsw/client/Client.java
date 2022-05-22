package it.polimi.ingsw.client;

import it.polimi.ingsw.server.Server;

import java.beans.PropertyChangeEvent;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
public class Client implements Remote {
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
            Server server = (Server) registry.lookup("server");
            server.registerUser("test");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
