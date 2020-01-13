import server.Server;
import store.Store;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        String url = System.getenv("DATABASE_URL");
        String user = System.getenv("DATABASE_USER");
        String password = System.getenv("DATABASE_PASSWORD");
        final int port = Integer.parseInt(System.getenv("PORT"));
        try (Connection DBConnection = DriverManager.getConnection(url, user, password)) {
            Store store = new Store(DBConnection);
            Server server = new Server(store, port);
            server.launch();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
