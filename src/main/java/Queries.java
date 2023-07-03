import javax.swing.plaf.nimbus.State;
import java.sql.ResultSet;
import java.sql.Statement;

public class Queries extends MariaDBConnection {
    String selectQuery(String columns, String table, String whereClause) {

        String result = null;

        String selectQuery = "SELECT " + columns + " FROM " + table;

        try{

            if (whereClause != null) {
                selectQuery += " " + whereClause;
            }

            selectQuery += ";";
            System.out.println(selectQuery);

            Statement selectStatement = connection.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);

            while (selectResult.next()){

                result = selectResult.getString(columns);
            }

            selectStatement.close();
            selectResult.close();

        } catch (Exception exception){
            System.err.println("Couldn't run SELECT query: " + exception.getMessage());
        }

        return result;
    }

    void postQuery(String columns, String table, String values){

        String postQuery = "INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ");";
        System.out.println(postQuery);

        try{

            Statement postStatement = connection.createStatement();
            postStatement.executeQuery(postQuery);

            postStatement.close();

        } catch (Exception exception) {
            System.err.println("Couldn't run POST query: " + exception.getMessage());
        }
    }
}