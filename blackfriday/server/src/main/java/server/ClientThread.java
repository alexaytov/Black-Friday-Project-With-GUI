package server;

import commandEnterpreter.interfaces.CommandInterpreter;
import commandEnterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import connection.ServerClientConnection;
import store.Store;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

import static java.util.Objects.requireNonNull;

public class ClientThread implements Runnable {

    private ServerClientConnection clientConnection;
    private CommandInterpreter commandInterpreter;


    public ClientThread(ServerClientConnection clientConnection, Store store, CommandInterpreter commandInterpreter) throws IOException {
        setClientConnection(clientConnection);
        this.setStore(store);
        this.setCommandInterpreter(commandInterpreter);

    }

    public void setCommandInterpreter(CommandInterpreter commandInterpreter) {
        requireNonNull(commandInterpreter);
        this.commandInterpreter = commandInterpreter;
    }

    public void setClientConnection(ServerClientConnection clientConnection) {
        Validator.requireNonNull(clientConnection, ExceptionMessages.CONNECTION_NULL);
        this.clientConnection = clientConnection;
    }

    public void setStore(Store store) {
        requireNonNull(store);
    }

    public void run() {
        try {
            while (true) {
                // read command from client
                String command = this.clientConnection.read();
                // get command from command interpreter
                Executable executable = this.commandInterpreter.interpretCommand(command);
                // execute command
                executable.execute();
            }
        } catch (IOException ex) {
            System.out.println("Client thread: " + Thread.currentThread().getName() + " ended!!!");
        } catch (ClassNotFoundException | CloneNotSupportedException | SQLException e) {
            e.printStackTrace();
        }
    }

}
