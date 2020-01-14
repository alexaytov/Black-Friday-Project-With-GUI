package commands;

import command.enterpreter.interfaces.Executable;
import connection.Connection;
import exceptions.DataAlreadyExistsException;
import store.services.UserService;
import user.User;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterUser implements Executable {

    private Connection clientConnection;
    private UserService userService;

    public RegisterUser(Connection clientConnection, UserService userService) {
        this.clientConnection = clientConnection;
        this.userService = userService;
    }

    /**
     * Registers new user in store
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        User user = this.clientConnection.read();
        try {
            userService.registerUser(user);
            this.clientConnection.write(true);
        } catch (DataAlreadyExistsException ex) {
            this.clientConnection.write(false);
        }
    }
}
