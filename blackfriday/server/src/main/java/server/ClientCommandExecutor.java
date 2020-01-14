package server;

import command.enterpreter.interfaces.CommandInterpreter;
import command.enterpreter.interfaces.Executable;
import commonMessages.ExceptionMessages;
import connection.Connection;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

import static java.util.Objects.requireNonNull;

public class ClientCommandExecutor implements Runnable {

    private Connection clientConnection;
    private CommandInterpreter commandInterpreter;


    public ClientCommandExecutor(Connection clientConnection, CommandInterpreter commandInterpreter) {
        setClientConnection(clientConnection);
        this.setCommandInterpreter(commandInterpreter);

    }

    public void setCommandInterpreter(CommandInterpreter commandInterpreter) {
        requireNonNull(commandInterpreter);
        this.commandInterpreter = commandInterpreter;
    }

    public void setClientConnection(Connection clientConnection) {
        Validator.requireNonNull(clientConnection, ExceptionMessages.CONNECTION_NULL);
        this.clientConnection = clientConnection;
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
