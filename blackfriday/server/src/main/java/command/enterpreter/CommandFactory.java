package command.enterpreter;

import command.enterpreter.interfaces.CommandInterpreter;
import command.enterpreter.interfaces.Executable;
import commands.*;
import connection.Connection;
import database.ProductDatabase;
import database.PurchaseDatabase;
import database.UserDatabase;
import store.Store;
import store.services.EarningsService;
import store.services.ProductService;
import store.services.PurchaseService;
import store.services.UserService;

import java.sql.SQLException;
import java.util.HashMap;

public class CommandFactory implements CommandInterpreter {

    private static final String COMMAND_DIRECTORY = "commands.";
    private Store store;
    private Connection connection;
    private EarningsService earningsService;
    private UserService userService;
    private ProductService productService;
    private PurchaseService purchaseService;

    public CommandFactory(Connection connection, java.sql.Connection dbConnection) throws SQLException {
        ProductDatabase productDatabase = new ProductDatabase(dbConnection);
        PurchaseDatabase purchaseDatabase = new PurchaseDatabase(dbConnection);
        UserDatabase userDatabase = new UserDatabase(dbConnection);
        this.store = new Store(productDatabase);
        this.earningsService = new EarningsService(purchaseDatabase);
        this.userService = new UserService(userDatabase);
        this.productService = new ProductService(productDatabase);
        this.purchaseService = new PurchaseService(purchaseDatabase);
        this.connection = connection;
    }

    /**
     * Creates executable based on the given name (@code data)
     *
     * @param data the executable name
     * @return the created executable with populated dependencies
     */
    @Override
    public Executable interpretCommand(String data) {
        HashMap<String, Executable> commands = new HashMap<>();
        fillHashMapWithCommands(commands);
        String command = getCorrectClassName(data);
        return commands.get(command);
    }

    private void fillHashMapWithCommands(HashMap<String, Executable> commands) {
        commands.put("BuyProduct", new BuyProduct(connection, userService, productService, earningsService));
        commands.put("ChangeAge", new ChangeAge(connection, userService));
        commands.put("ChangeFirstName", new ChangeFirstName(connection, userService));
        commands.put("ChangeLastName", new ChangeLastName(connection, userService));
        commands.put("ChangePassword", new ChangePassword(connection, userService));
        commands.put("ChangeProductDescription", new ChangeProductDescription(connection, userService, productService));
        commands.put("ChangeProductDiscountPercent", new ChangeProductDiscountPercent(userService, connection, productService));
        commands.put("ChangeProductImage", new ChangeProductImage(connection, userService, productService));
        commands.put("ChangeProductMinimumPrice", new ChangeProductMinimumPrice(connection, store, userService, productService));
        commands.put("ChangeProductName", new ChangeProductName(connection, userService, productService));
        commands.put("ChangeProductPrice", new ChangeProductPrice(connection, userService, productService));
        commands.put("ChangeProductQuantity", new ChangeProductQuantity(connection, userService, productService));
        commands.put("ChangeProductSize", new ChangeProductSize(connection, userService, productService));
        commands.put("ChangeUsername", new ChangeUsername(connection, userService));
        commands.put("CreateProduct", new CreateProduct(connection, userService, productService));
        commands.put("DeleteClientByUsername", new DeleteClientByUsername(connection, userService));
        commands.put("DeleteProduct", new DeleteProduct(connection, userService, productService));
        commands.put("DeleteUser", new DeleteUser(connection, userService));
        commands.put("EarningsDate", new EarningsDate(connection, userService, earningsService));
        commands.put("EarningsMonth", new EarningsMonth(connection, userService, earningsService));
        commands.put("EarningsPeriod", new EarningsPeriod(connection, userService, earningsService));
        commands.put("EarningsYear", new EarningsYear(connection, userService, earningsService));
        commands.put("ExistProductOptions", new ExistProductOptions(productService));
        commands.put("GetAllClientsInformation", new GetAllClientsInformation(connection, userService, purchaseService));
        commands.put("GetClientDiscountedProducts", new GetClientDiscountedProducts(connection, userService, productService));
        commands.put("GetClientProducts", new GetClientProducts(connection, userService, productService));
        commands.put("GetLoggedInUser", new GetLoggedInUser(connection, userService));
        commands.put("GetProductByName", new GetProductByName(connection, userService, productService));
        commands.put("GetStaffDiscountedProducts", new GetStaffDiscountedProducts(connection, userService, productService));
        commands.put("GetStaffProducts", new GetStaffProducts(connection, userService, productService));
        commands.put("IsBlackFriday", new IsBlackFriday(store, connection, userService));
        commands.put("Login", new Login(connection, userService));
        commands.put("Logout", new Logout(userService));
        commands.put("ProductExists", new ProductExists(connection, userService, productService));
        commands.put("RegisterUser", new RegisterUser(connection, userService));
        commands.put("SearchClientAllProducts", new SearchClientAllProducts(connection, userService, productService));
        commands.put("SearchClientDiscountedProducts", new SearchClientDiscountedProducts(connection, userService, productService));
        commands.put("SearchQuantityControl", new SearchQuantityControl(connection, userService, productService));
        commands.put("SearchStaffAllProducts", new SearchStaffAllProducts(connection, userService, productService));
        commands.put("SearchStaffDiscountedProducts", new SearchStaffDiscountedProducts(connection, userService, productService));
        commands.put("SetProduct", new SetProduct(connection, userService, productService));
        commands.put("StartBlackFriday", new StartBlackFriday(store, userService));
        commands.put("StopBlackFriday", new StopBlackFriday(store, userService));

    }

    /**
     * Parses command name to class name
     *
     * @param data the command in plain text
     * @return the command as class name
     */
    private String getCorrectClassName(String data) {
        StringBuilder commandName = new StringBuilder();
        String[] tokens = data.split("\\s+");
        for (String token : tokens) {
            commandName.append(Character.toUpperCase(token.charAt(0)))
                    .append(token.substring(1));
        }
        return commandName.toString();
    }
}
