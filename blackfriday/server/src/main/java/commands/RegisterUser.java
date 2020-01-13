package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import connection.Connection;
import exceptions.DataAlreadyExistsException;
import store.Store;
import user.User;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterUser implements Executable {

    @Inject
    private Connection clientConnection;

    @Inject
    private Store store;

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
            this.store.registerUser(user);
            this.clientConnection.write(true);
        } catch (DataAlreadyExistsException ex) {
            this.clientConnection.write(false);
        }
    }
}
