package server;

import command.enterpreter.CommandFactory;
import commonMessages.ConstantMessages;
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
    private static final int THREAD_POOL_SIZE = 10;

    public Server(Store store, int PORT) {
        this.STORE = store;
        this.PORT = PORT;
    }

    /**
     * Starts the server
     */
    public void launch() {

        ServerSocket serverSocket;
        Executor threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket socket;
                // accept new connection
                socket = serverSocket.accept();
                Connection clientConnection = new TCPConnection(socket);

                // create command factory for this connection
                CommandFactory commandFactory = new CommandFactory(STORE, clientConnection);

                // create thread to handle this connection
                ClientCommandExecutor clientCommandExecutor = new ClientCommandExecutor(clientConnection, STORE, commandFactory);
                // execute thread
                threadPool.execute(clientCommandExecutor);
                System.out.println(ConstantMessages.THREAD_STARTED);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
