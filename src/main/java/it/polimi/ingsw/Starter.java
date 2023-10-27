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
    /**
     * The class needed to start the generated jar file.
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            if (args.length < 3) {
                if (args.length > 1) {
                    if (args[0].equals("-cli")) {
                        Client client = new Client(args[1], 23023);
                        client.run();
                        CLI cli = new CLI(client);
                        cli.Start();
                    } else if (args[0].equals("-gui")) {
                        Client client = new Client(args[1], 23023);
                        client.run();
                        GUI gui = new GUI(client);
                        gui.start();
                    } else {
                        System.out.println("Incorrect parameters inserted");
                        System.exit(0);
                    }
                } else {
                    if (args[0].equals("-server")) {
                        try {
                            Server server = new Server();
                            Registry registry = LocateRegistry.createRegistry(23023);
                            registry.bind("server", server);
                            new Thread(server).start();
                            System.out.println("Server is up, waiting for connections");
                        } catch (AlreadyBoundException e) {
                            System.out.println("Chosen socket is already bound");
                        } catch (IOException e) {
                            System.out.println("IO exception");
                        }
                    } else {
                        System.out.println("Incorrect parameters inserted");
                        System.exit(0);
                    }
                }
            } else {
                System.out.println("Too many parameters inserted");
                System.exit(0);
            }
        } else {
            System.out.println("No parameters inserted");
            System.exit(0);
        }
    }
}
