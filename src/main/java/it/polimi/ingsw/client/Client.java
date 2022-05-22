package it.polimi.ingsw.client;

import it.polimi.ingsw.server.serverStub;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client implements Runnable {
    final private String ip;
    final private int port;

    private serverStub server;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        connect();
        run();
    }

    public void connect() {
        try {
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(ip, port);
            // Looking up the registry for the remote object
            server = (serverStub) registry.lookup("server");
            System.out.println("connection done");
            server.registerUser("Cooper");

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(server.testPing());
                Thread.sleep(1000);
            } catch (RemoteException | InterruptedException e) {
                System.err.println("Remote exception: " + e.toString());
            }
        }
    }
}
