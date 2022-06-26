package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.serverStub;
import it.polimi.ingsw.view.CLI.CLI;
import it.polimi.ingsw.view.GUI.GUI;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author Amrit
 * This class is the starter class
 */
public class Starter {
    public static void main(String[] args) throws InterruptedException, AlreadyBoundException, IOException {
        String serverClient = args[0];
        switch (serverClient) {
            case "-server":
                try {
                    Server server = new Server();
                    serverStub stub = server;
                    System.out.println("Binding server implementation to registry...");
                    Registry registry = LocateRegistry.createRegistry(23023);
                    registry.bind("server", stub);
                    System.out.println("Server has started");
                    System.out.println("Waiting for invocations from clients...");
                    new Thread(server).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "-cli": {
                Client client = new Client("localhost", 23023);
                client.run();
                CLI cli = new CLI(client);
                cli.Start();
                break;
            }
            case "-gui": {
                Client client = new Client("localhost", 23023);
                client.run();
                GUI gui = new GUI(client);
                gui.start();
                break;
            }
            default:
                System.out.println("Parameters not correct");
                System.exit(0);
        }
    }
}
