import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDBConnection {

    Connection connection = connectToDataBase();
    public static Connection connectToDataBase() {

        Connection connection = null;

        String jdbcURL = "jdbc:mariadb://localhost:3306/kraut_und_ruben_db";
        String username = "root";
        String password = "Zenbook12";

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);

            return connection;

        } catch (SQLException exception) {
            System.err.println("Connection failed. Error message: " + exception.getMessage());
        }

        return connection;
    }
}