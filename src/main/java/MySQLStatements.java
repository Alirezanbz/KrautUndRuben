import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MySQLStatements extends MySQLConnection{

    String rezeptTable = "rezept";



public ArrayList<String> getRezepts(){

    ArrayList<String> rezepts = new ArrayList<>();

    try {

        /*
        String retrieveRezeptsQuery = "SELECT * FROM " + rezeptTable;
        Statement retrieveRezeptsStatement = connection.createStatement();
        ResultSet retrieveRezeptsresult =retrieveRezeptsStatement.executeQuery(retrieveRezeptsQuery);

        while (retrieveRezeptsresult.next()){

            String rezeptName =retrieveRezeptsresult.getString("rezeptname");
            rezepts.add(rezeptName);


        }
        */

        Queries queries = new Queries();
        rezepts = queries.selectQuery("rezeptname,RezeptNr", rezeptTable, "");




    }catch (Exception e){
        System.out.println(e.getMessage());

    }
    return rezepts;


}


}
