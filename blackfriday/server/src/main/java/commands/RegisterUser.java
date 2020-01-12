package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import exceptions.DataAlreadyExistsException;
import store.Store;
import user.User;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterUser implements Executable {

    @Inject
    private ServerClientConnection clientConnection;

    @Inject
    private Store store;

    /**
     * Registers new user in store
     *
     * @throws IOException            if IO error occurs
     * @throws SQLException           if SQL error occurs
     * @throws ClassNotFoundException if read class by (@code clientConnection) is not found
     */
    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException {
        User user = this.clientConnection.read();
        try {
            this.store.registerUser(user);
            this.clientConnection.write(true);
        } catch (DataAlreadyExistsException ex) {
            this.clientConnection.write(false);
        }
    }
}
