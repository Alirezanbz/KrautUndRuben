import javax.swing.plaf.nimbus.State;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Queries extends MariaDBConnection {
    ArrayList<String> selectQuery(String columns, String table, String whereClause) {

        ArrayList<String> result = new ArrayList<>();

        String[] columnsArray = columns.split(",");

        String selectQuery = "SELECT " + columns + " FROM " + table + " ";

        try{

            if (whereClause != null) {
                selectQuery += " " + whereClause;
            }

            selectQuery += ";";
            System.out.println(selectQuery);

            Statement selectStatement = connection.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);

            while (selectResult.next()){
                for (String col : columnsArray) {
                    result.add(selectResult.getString(col));
                }
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

    void createOrder(String kdNr, Basket basket){

        String date = getDate();
        Integer lfNr = randomNrGenerator();
        Integer rechnungsBetrag = getTotalPrice(basket.rezepte, basket.zutaten);

        String postValues = date + ", " + rechnungsBetrag + ", " + kdNr + ", " + lfNr;

        postQuery("datum, rechnungsbetrag, KdNr, LfNr", "Bestellung", postValues);

        /*rezept_Bestellung(getBestellungNr(), basket.rezepte);*/
    }

     Integer getTotalPrice(ArrayList<Integer> rezepte, ArrayList<Integer> zutaten) {

        Integer totalPrice = getTotalRezeptePrice(rezepte);

        if (zutaten != null) {
            totalPrice += getTotalZutatenPrice(zutaten);
        }

        return totalPrice;
    }

    private Integer getTotalZutatenPrice(ArrayList<Integer> zutaten) {

        Integer zutatenPrice = 0;

        for (int zutatNr : zutaten) {

            String getPriceQuery = "SELECT preis " +
                    "FROM Zutat " +
                    "WHERE zutatNr = " + zutatNr;

            System.out.println(getPriceQuery);

            try {
                Statement getPriceStatement = connection.createStatement();
                ResultSet getPriceResult = getPriceStatement.executeQuery(getPriceQuery);

                while (getPriceResult.next()) {
                    zutatenPrice += getPriceResult.getInt("preis");
                }

            } catch (Exception exception) {
                System.err.println("Couldn't retreive price: " + exception.getMessage());
            }
        }

        return zutatenPrice;
    }

    private Integer getTotalRezeptePrice(ArrayList<Integer> rezepte) {

        Integer rezeptPrice = 0;

        for (int rezeptNr : rezepte) {

            String getPriceQuery = "SELECT SUM(preis * menge) " +
                    "FROM Zutat " +
                    "INNER JOIN Rezept_Zutat ON Rezept_Zutat.zutatNr = Zutat.zutatNr " +
                    "WHERE RezeptNr = " + rezeptNr;

            System.out.println(getPriceQuery);

            try {
                Statement getPriceStatement = connection.createStatement();
                ResultSet getPriceResult = getPriceStatement.executeQuery(getPriceQuery);

                while (getPriceResult.next()) {
                    rezeptPrice += getPriceResult.getInt("SUM(preis * menge)");
                }

            } catch (Exception exception) {
                System.err.println("Couldn't retreive price: " + exception.getMessage());
            }
        }

        return rezeptPrice;
    }

    private String getDate(){
        LocalDate currentDate = LocalDate.now();
        String dateFormat = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String date = currentDate.format(formatter);
        return date;
    }

    private Integer randomNrGenerator(){
        return ThreadLocalRandom.current().nextInt(0,15 + 1);
    }
}