package server;

import connection.Connection;
import connection.TCPConnection;
import store.Store;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {

    private final Store STORE;
    private final int PORT;

    public Server(Store store, int PORT) {
        this.STORE = store;
        this.PORT = PORT;
    }

    /**
     * Starts the server
     */
    public void launch() {

        ServerSocket serverSocket;
        Executor threadPool = Executors.newFixedThreadPool(10);
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket socket;
                socket = serverSocket.accept();
                Connection clientConnection = new TCPConnection(socket);
                ClientThread clientThread = new ClientThread(clientConnection, STORE);
                Thread thread = new Thread(clientThread);
                threadPool.execute(thread);
                System.out.println("Thread with id " + thread.getId() + " started!!!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
