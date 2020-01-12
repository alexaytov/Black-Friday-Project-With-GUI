package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import connection.ServerClientConnection;
import store.Store;

import java.io.IOException;
import java.sql.SQLException;

public class SearchClientAllProducts implements Executable {

    @Inject
    private ServerClientConnection clientConnection;

    @Inject
    private Store store;

    /**
     * Searches all products for users with permission
     * client
     *
     * @throws IOException            if IO error occurs
     * @throws SQLException           if SQL error occurs
     * @throws ClassNotFoundException if read class by (@code clientConnection) is not found
     */
    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException {
        String searchedAllProductsName = this.clientConnection.read().toString();
        this.clientConnection.write(this.store.searchAllProducts(searchedAllProductsName));
    }
}