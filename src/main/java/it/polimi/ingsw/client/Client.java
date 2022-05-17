package it.polimi.ingsw.client;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

public class Client {

    final private String ip;
    final private int port;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    private Thread readFromSocket(final ObjectInputStream socketIn) {
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    Object inputObject = socketIn.readObject();
                    System.out.println((PropertyChangeEvent) inputObject);
                }
            } catch (Exception e) {
                System.out.println("Error while reading from socket");
            }
        });
        t.start();
        return t;
    }

    private Thread writeToSocket(final ObjectInputStream inputStream, final ObjectOutputStream socketOut) {
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    Object obj = inputStream.readObject();
                    socketOut.writeObject(obj);
                    socketOut.flush();
                }
            } catch (Exception e) {
                System.out.println("Error while writing to socket");
            }
        });
        t.start();
        return t;
    }

    public void startView()
    {
    }
    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream stdin = new ObjectInputStream(System.in);
        try {
            Thread t0 = readFromSocket(socketIn);
            Thread t1 = writeToSocket(stdin, socketOut);
            t0.join();
            t1.join();
            startView();
        } catch (InterruptedException | NoSuchElementException e) {
            System.out.println("Connection closed from the client side");
        } finally {
            stdin.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }
}
