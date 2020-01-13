package commands;

import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.Connection;
import store.Store;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeProductSize implements Executable {

    @Inject
    private Store store;

    @Inject
    private Connection clientConnection;

    /**
     * Changes chosen product size
     *
     * @throws IOException  if IO error occurs
     * @throws SQLException if SQL error occurs
     */
    @Override
    public void execute() throws IOException, SQLException {
        Validator.requireNonNull(this.store.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        String newSize = this.clientConnection.read().toString();
        this.store.changeProductSize(newSize);
        this.clientConnection.write(true);
    }
}
