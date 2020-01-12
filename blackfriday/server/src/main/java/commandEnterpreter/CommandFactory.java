package commandEnterpreter;

import commandEnterpreter.interfaces.Executable;
import commandEnterpreter.interfaces.Inject;
import commonMessages.ExceptionMessages;
import connection.ServerClientConnection;
import store.Store;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import static validator.Validator.requireNonNull;

public class CommandFactory implements CommandInterpreter {

    private static final String COMMAND_DIRECTORY = "commands.";
    private Store store;
    private ServerClientConnection serverClientConnection;
    public CommandFactory(Store store, ServerClientConnection serverClientConnection){
        this.setStore(store);
        this.setServerClientConnection(serverClientConnection);

    }

    public void setServerClientConnection(ServerClientConnection serverClientConnection) {
        requireNonNull(serverClientConnection, ExceptionMessages.CONNECTION_NULL);
        this.serverClientConnection = serverClientConnection;
    }

    public void setStore(Store store) {
        requireNonNull(store, ExceptionMessages.STORE_NULL);
        this.store = store;
    }

    @Override
    public Executable interpretCommand(String data) {
        Executable executable = null;
        String command = getCorrectClassName(data);
        try {
            Class<?> commandClass = Class.forName(COMMAND_DIRECTORY + command);
            Constructor<?> declaredConstructor = commandClass.getDeclaredConstructor();
            declaredConstructor.setAccessible(true);
            executable = (commandEnterpreter.interfaces.Executable) declaredConstructor.newInstance();
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

    private void populateDependencies(Executable executable){
        Field[] executableFields = executable.getClass().getDeclaredFields();
        Field[] currentClassFields = this.getClass().getDeclaredFields();

        for (Field executableField : executableFields) {
            Inject annotation = null;
            try{
                annotation = executableField.getAnnotation(Inject.class);
            }catch (ClassCastException ex){
                continue;
            }
            for (Field currentClassField : currentClassFields) {
                if(currentClassField.getType().equals(executableField.getType())){
                    try{
                        executableField.setAccessible(true);
                        executableField.set(executable, currentClassField.get(this));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }

    private String getCorrectClassName(String data){
        StringBuilder commandName = new StringBuilder();
        String[] tokens = data.split("\\s+");
        for (String token : tokens) {
            commandName.append(Character.toUpperCase(token.charAt(0)))
                    .append(token.substring(1));
        }
        return commandName.toString();
    }
}
