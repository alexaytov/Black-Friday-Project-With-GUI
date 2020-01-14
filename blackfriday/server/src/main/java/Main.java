import server.Server;

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
            Server server = new Server(port, DBConnection);
            server.launch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
