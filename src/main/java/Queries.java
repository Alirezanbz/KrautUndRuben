import javax.swing.plaf.nimbus.State;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Queries extends MariaDBConnection {

    public ArrayList<ArrayList<String>> selectStringQuery(String[] columns, String table, String whereClause) {

        ArrayList<ArrayList<String>> result = new ArrayList<>();
        //String[][] result;

        //String[] columnsArray = columns.split(",");

        String selectQuery = "SELECT " + columns.toString() + " FROM " + table + " ";

        try{

            if (whereClause != null) {
                selectQuery += " " + whereClause;
            }

            selectQuery += ";";
            System.out.println(selectQuery);

            Statement selectStatement = connection.createStatement();
            ResultSet selectResult = selectStatement.executeQuery(selectQuery);

            while (selectResult.next()){
                ArrayList<String> values = new ArrayList<>();
                for (String col : columns) {
                    values.add(selectResult.getString(col));
                }
                result.add(values);
            }

            selectStatement.close();
            selectResult.close();

        } catch (Exception exception){
            System.err.println("Couldn't run SELECT query: " + exception.getMessage());
        }

        return result;
    }

    public ArrayList<ArrayList<String>> selectStringQuery(String columns, String table, String whereClause) {
        return selectStringQuery(columns.split(","), table, whereClause);
    }

    public ArrayList<Integer> selectIntegerQuery(String columns, String table, String whereClause) {

        ArrayList<Integer> result = new ArrayList<>();

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

                result.add(selectResult.getInt(columns));
            }

            selectStatement.close();
            selectResult.close();

        } catch (Exception exception){
            System.err.println("Couldn't run SELECT query: " + exception.getMessage());
        }

        return result;
    }

    /*
    public HashMap<String, Integer> getZutatenNachRezept(int rezeptNr) {

        HashMap<String, Integer> zutatenMap = new HashMap<String, Integer>();

        ArrayList<String> zutatBezeichnungen = selectStringQuery(
                "zutat.bezeichnung,menge",
                "rezept_zutat",
                "LEFT JOIN zutat ON rezept_zutat.zutatNr = zutat.zutatNr WHERE rezept_zutat.RezeptNr = " + rezeptNr);

        ArrayList<Integer> zutatMenge = selectIntegerQuery(
                "menge",
                "rezept_zutat",
                "LEFT JOIN zutat ON rezept_zutat.zutatNr = zutat.zutatNr WHERE rezept_zutat.RezeptNr = " + rezeptNr);

        for (int i = 0; i < zutatBezeichnungen.size(); i++) {
            zutatenMap.put(zutatBezeichnungen.get(i), zutatMenge.get(i));
        }

        return zutatenMap;
    }
     */

    public ArrayList<ArrayList<String>> getRezeptNachZutat(int zutatNr) {
        return selectStringQuery(
                "rezeptname",
                "rezept",
                "JOIN rezept_zutat rz on rezept.RezeptNr = rz.RezeptNr WHERE zutatNr = " + zutatNr);
    }

    public ArrayList<ArrayList<String>> getRezeptNachKategorie(int ernaehrungskategorieNr) {
        return selectStringQuery(
                "rezeptname",
                "rezept",
                "JOIN rezept_kategorie rk on rk.RezeptNr = rezept.RezeptNr WHERE KatNr = " + ernaehrungskategorieNr);
    }

    public ArrayList<ArrayList<String>> getZutatNachBeschraenkung(int beschraenkungNr) {
        return selectStringQuery(
                "z.bezeichnung",
                "beschraenkung_zutat",
                "JOIN zutat z on beschraenkung_zutat.zutatNr = z.zutatNr WHERE Allnr = " + beschraenkungNr);
    }

    public void postQuery(String columns, String table, String values){

        String postQuery = "INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ");";
        System.out.println(postQuery);

        try{

            Statement postStatement = connection.createStatement();
            postStatement.executeQuery(postQuery);

            postStatement.close();

        } catch (Exception exception) {
            System.err.println("Couldn't run POST query: " + exception.getMessage());
            System.err.println(exception);
        }
    }

    public void createOrder(Integer kdNr, Basket basket){

        String date = getDate();
        Integer lfNr = randomNrGenerator();
        Integer rechnungsBetrag = getTotalPrice(basket.rezepte, basket.zutaten);

        String postValues = "'" + date + "', " + rechnungsBetrag + ", " + kdNr + ", " + lfNr;

        postQuery("datum, rechnungsbetrag, KdNr, LfNr", "Bestellung", postValues);

        rezept_Bestellung(getLatestBestellungNr(kdNr), basket.rezepte);
        zutat_Bestellung(getLatestBestellungNr(kdNr), basket.zutaten);
    }

    private void rezept_Bestellung(int latestBestellungNr, ArrayList<ArrayList<Integer>> rezepte) {

        for (ArrayList<Integer> rezept : rezepte) {
            postQuery("BestellNr,RezeptNr,menge", "rezept_bestellung", latestBestellungNr + ", " + rezept.get(0) + ", " + rezept.get(1));
        }
    }

    private void zutat_Bestellung(int latestBestellungNr, ArrayList<ArrayList<Integer>> zutaten) {

        for (ArrayList<Integer> zutat : zutaten) {
            postQuery("BestellNr,ZutatNr,menge", "zutat_bestellung", latestBestellungNr + ", " + zutat.get(0) + ", " + zutat.get(1));
        }
    }

    private int getLatestBestellungNr(Integer kdNr) {

        ArrayList<Integer> bestellungen = selectIntegerQuery("BestellNr", "bestellung", "WHERE KdNr = " + kdNr + " ORDER BY BestellNr DESC");
        return bestellungen.get(0);
    }

    private Integer getTotalPrice(ArrayList<ArrayList<Integer>> rezepte, ArrayList<ArrayList<Integer>> zutaten) {

        Integer totalPrice = 0;

        if (rezepte != null) {
            totalPrice += getTotalRezeptePrice(rezepte);
        }

        if (zutaten != null) {
            totalPrice += getTotalZutatenPrice(zutaten);
        }

        return totalPrice;
    }

    private Integer getTotalZutatenPrice(ArrayList<ArrayList<Integer>> zutaten) {

        Integer zutatenPrice = 0;

        for (ArrayList<Integer> zutat : zutaten) {

            String getPriceQuery = "SELECT preis " +
                    "FROM Zutat " +
                    "WHERE zutatNr = " + zutat.get(0);

            System.out.println(getPriceQuery);

            try {
                Statement getPriceStatement = connection.createStatement();
                ResultSet getPriceResult = getPriceStatement.executeQuery(getPriceQuery);

                while (getPriceResult.next()) {
                    zutatenPrice += getPriceResult.getInt("preis") * zutat.get(1);
                }

            } catch (Exception exception) {
                System.err.println("Couldn't retreive price: " + exception.getMessage());
            }
        }

        return zutatenPrice;
    }

    private Integer getTotalRezeptePrice(ArrayList<ArrayList<Integer>> rezepte) {

        Integer rezeptPrice = 0;

        for (ArrayList<Integer> rezept : rezepte) {

            String getPriceQuery = "SELECT SUM(preis * menge) " +
                    "FROM Zutat " +
                    "INNER JOIN Rezept_Zutat ON Rezept_Zutat.zutatNr = Zutat.zutatNr " +
                    "WHERE RezeptNr = " + rezept.get(0);

            System.out.println(getPriceQuery);

            try {
                Statement getPriceStatement = connection.createStatement();
                ResultSet getPriceResult = getPriceStatement.executeQuery(getPriceQuery);

                while (getPriceResult.next()) {
                    rezeptPrice += getPriceResult.getInt("SUM(preis * menge)") * rezept.get(1);
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
        return  ThreadLocalRandom.current().nextInt(0,15 + 1);
    }
}