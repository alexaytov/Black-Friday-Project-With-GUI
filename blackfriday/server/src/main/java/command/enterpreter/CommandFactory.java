package command.enterpreter;

import command.enterpreter.interfaces.CommandInterpreter;
import command.enterpreter.interfaces.Executable;
import command.enterpreter.interfaces.Inject;
import connection.Connection;
import database.ProductDatabase;
import database.PurchaseDatabase;
import database.UserDatabase;
import store.Store;
import store.services.EarningsService;
import store.services.ProductService;
import store.services.PurchaseService;
import store.services.UserService;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

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
        Executable executable = null;
        String command = getCorrectClassName(data);
        try {
            Class<?> commandClass = Class.forName(COMMAND_DIRECTORY + command);
            Constructor<?> declaredConstructor = commandClass.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            executable = (Executable) declaredConstructor.newInstance();
            populateDependencies(executable);
        } catch (ClassNotFoundException |
                NoSuchMethodException |
                IllegalAccessException |
                InstantiationException |
                InvocationTargetException e
        ) {
            e.printStackTrace();
        }
        return executable;
    }

    /**
     * Checks if all the fields in the command
     * have (@code Injection) annotation
     * and if they do it sets their value
     *
     * @param executable the executable command
     */
    private void populateDependencies(Executable executable) {
        Field[] executableFields = executable.getClass().getDeclaredFields();
        Field[] currentClassFields = this.getClass().getDeclaredFields();

        for (Field executableField : executableFields) {
            try {
                executableField.getAnnotation(Inject.class);
            } catch (ClassCastException ex) {
                continue;
            }
            for (Field currentClassField : currentClassFields) {
                if (currentClassField.getType().equals(executableField.getType())) {
                    try {
                        executableField.setAccessible(true);
                        executableField.set(executable, currentClassField.get(this));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
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
