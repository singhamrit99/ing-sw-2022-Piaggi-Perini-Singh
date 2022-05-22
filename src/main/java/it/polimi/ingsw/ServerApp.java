package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ServerApp {
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server();
            System.out.println("Binding server implementation to registry...");
            Registry registry = LocateRegistry.createRegistry(23023);
            registry.bind("server", server);
            System.out.println("Server has started");
            System.out.println("Waiting for invocations from clients...");
        } catch (IOException | AlreadyBoundException e) {
            System.err.println("Impossible to initialize the server.");
        }
    }
}
