package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.serverStub;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerApp {
    /**
     * Starts the server application.
     * @param args jar startup arguments.
     * @throws RemoteException Thrown in case of a network error
     * @throws AlreadyBoundException Thrown if the socket the server is connecting to is already bound.
     */
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        Server server = new Server();
        serverStub stub = server;
        Registry registry = LocateRegistry.createRegistry(23023);
        registry.bind("server", stub);
        new Thread(server).start();
    }
}