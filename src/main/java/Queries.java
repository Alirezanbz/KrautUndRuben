import java.sql.ResultSet;
import java.sql.Statement;

public class Queries extends DataBaseConnection {
    String selectQuery(String columns, String table, String whereClause) {

        String result = null;

        String selectQuery = "SELECT " + columns + " FROM " + table;

        try{

            if (whereClause != null) {
                selectQuery += whereClause;
            }

            selectQuery += ";";

            Statement selectStatement = connection.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);

            while (selectResult.next()){

                result = selectResult.getString(columns);
            }

            selectStatement.close();
            selectResult.close();

        } catch (Exception exception){
            System.err.println("Couldn't run query: " + exception.getMessage());
        }

        return result;
    }
}
