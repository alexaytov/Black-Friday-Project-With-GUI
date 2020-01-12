package commands;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.ServerClientConnection;
import store.Store;
import validator.Validator;

import java.io.IOException;
import java.sql.SQLException;

public class SearchStaffDiscountedProducts implements Executable {

    @Inject
    private ServerClientConnection clientConnection;

    @Inject
    private Store store;

    /**
     * Searches products for users with permission admin
     *
     * @throws IOException            if IO error occurs
     * @throws SQLException           if SQL error occurs
     * @throws ClassNotFoundException if read class by (@code clientConnection) is not found
     */
    @Override
    public void execute() throws IOException, SQLException, ClassNotFoundException {
        Validator.requireNonNull(this.store.getLoggedInUser(), ExceptionMessages.USER_MUST_BE_LOGGED_IN);
        String searchedDiscountedProductsName = this.clientConnection.read().toString();
        this.clientConnection.write(this.store.searchDiscountedStaffProducts(searchedDiscountedProductsName));
    }
}
