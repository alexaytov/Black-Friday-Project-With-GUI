package server;

import command.enterpreter.CommandFactory;
import commonMessages.ConstantMessages;
import connection.Connection;
import connection.TCPConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server {

    private final int PORT;
    private final java.sql.Connection DB_CONNECTION;
    private static final int THREAD_POOL_SIZE = 10;

    public Server(int PORT, java.sql.Connection DB_CONNECTION) {
        this.PORT = PORT;
        this.DB_CONNECTION = DB_CONNECTION;
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
                CommandFactory commandFactory = new CommandFactory(clientConnection, DB_CONNECTION);

                // create thread to handle this connection
                ClientCommandExecutor clientCommandExecutor = new ClientCommandExecutor(clientConnection, commandFactory);
                // execute thread
                threadPool.execute(clientCommandExecutor);
                System.out.println(ConstantMessages.THREAD_STARTED);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }
}
