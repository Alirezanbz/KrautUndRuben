import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDBConnection {

    Connection connection = connectToDataBase();
    public static Connection connectToDataBase() {

        Connection connection = null;

        String jdbcURL = "jdbc:mariadb://172.16.5.15:3306/kraut_und_ruben_db";
        String username = "admin";
        String password = "admin";

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);

            return connection;

        } catch (SQLException exception) {
            System.err.println("Connection failed. Error message: " + exception.getMessage());
        }

        return connection;
    }
}