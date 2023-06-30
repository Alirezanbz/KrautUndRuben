import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {


    Connection connection = connectToDataBase();
    public static Connection connectToDataBase() {

        Connection connection = null;

        String jdbcURL = "jdbc:mysql://127.0.0.1:3306/kraut_und_ruben_db";
        String username = "root";
        String password = "root";

        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);

            return connection;

        } catch (SQLException exception) {
            System.err.println("Connection failed. Error message: " + exception.getMessage());
        }

        return null;
    }
}
