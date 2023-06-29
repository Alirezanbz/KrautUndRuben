import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    Connection connection = connectToDataBase();
    public static Connection connectToDataBase() {

        Connection connection = null;

        String jdbcURL = "jdbc:mariadb://localhost:8080/DB?user=root&password=SamplePassword";

        try {
            connection = DriverManager.getConnection(jdbcURL);

            return connection;

        } catch (SQLException exception) {
            System.err.println("Connection failed. Error message: " + exception.getMessage());
        }

        return connection;
    }
}
