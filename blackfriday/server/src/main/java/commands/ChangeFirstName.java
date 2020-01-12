package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.ServerClientConnection;
import store.Store;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class ChangeFirstName implements Executable {

    @Inject
    private Store store;

    @Inject
    private ServerClientConnection clientConnection;

    /**
     * Changes logged in user first name
     * and return to client if operation was successful
     *
     * @throws IOException            if IO error occurs
     * @throws SQLException           if SQL error occurs
     * @throws ClassNotFoundException if class read by
     *                                (@code clientConnection) is not found
     */
    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException {
        Validator.requireNonNull(this.store.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        boolean isChangeFirstNameSuccessful = this.store.changeUserFirstName(this.store.getLoggedInUser().getUsername(), this.clientConnection.read());
        this.clientConnection.write(isChangeFirstNameSuccessful);
    }
}
