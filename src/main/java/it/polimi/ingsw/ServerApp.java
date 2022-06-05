package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.serverStub;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerApp {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
            Server server = new Server();
            serverStub stub = server;
            System.out.println("Binding server implementation to registry...");
            Registry registry = LocateRegistry.createRegistry(23023);
            registry.bind("server", stub);
            System.out.println("Server has started");
            System.out.println("Waiting for invocations from clients...");
            new Thread(server).start();
    }
}