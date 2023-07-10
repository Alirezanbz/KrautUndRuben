import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomePageStatements extends Queries {

    protected final Set<String> currentFilters = new HashSet<>();


    protected List<String[]> getRezepts() {

        //ArrayList<String> rezepts = selectStringQuerySingle("rezeptname", "rezept", "");
        List<String[]> rowList = new ArrayList<>();
        /*
        for (int i = 0; i < rezepts.size(); i++) {
            String[] row = new String[3];
            row[0] = rezepts.get(i);
            row[1] = "";
            row[2] = "";
            rowList.add(row);
        }
        */

        ArrayList<ArrayList<String>> rezepts = selectStringQuery("rezeptname,kategoriename,sum(preis*menge),rezept.RezeptNr", "rezept", "LEFT JOIN rezept_kategorie ON rezept.RezeptNr = rezept_kategorie.RezeptNr\n" +
                "LEFT JOIN ernaehrungskategorie ON rezept_kategorie.KatNr = ernaehrungskategorie.KatNr\n" +
                "LEFT JOIN rezept_zutat ON rezept.RezeptNr = rezept_zutat.RezeptNr\n" +
                "LEFT JOIN zutat ON rezept_zutat.zutatNr = zutat.zutatNr\n" +
                "GROUP BY rezept.RezeptNr");

        for (ArrayList<String> rezept : rezepts) {
            String[] row = new String[4];
            row[0] = rezept.get(0);
            row[1] = getKategorieNachRezept(Integer.parseInt(rezept.get(3)));
            row[2] = rezept.get(2);
            row[3] = rezept.get(3);
            rowList.add(row);
        }

        return rowList;
    }

    protected ArrayList<String> getKategories(){

        ArrayList<Integer> RezeptNrs = selectIntegerQuery("RezeptNr", "rezept","");
        ArrayList<String> kategories = new ArrayList<>();

        for (int i = 0; i < RezeptNrs.size(); i++){

            kategories.add(getKategorieNachRezept(i+1));
        }



        return kategories;
    }
    protected List<String> getBeschraenkungs() {
        return selectStringQuerySingle("beschraenkungsname", "beschraenkung", "");
    }

    protected boolean isRecipeRestricted(String recipe, String beschraenkung) {
        ArrayList<Integer> beschraenkungNrs = selectIntegerQuery("beschraenkungNr", "beschraenkung", "WHERE beschraenkungsname = '" + beschraenkung + "'");
        ArrayList<Integer> zutatNrs = selectIntegerQuery("zutatNr", "beschraenkung_zutat", "WHERE Allnr = " + beschraenkungNrs.get(0));
        for (Integer zutatNr : zutatNrs) {
            ArrayList<String> recipes = getRezeptNachZutat(zutatNr);
            if (recipes.contains(recipe)) {
                return true;
            }
        }
        return false;
    }

    protected boolean matchesFilters(String recipe) {
        for (String filter : currentFilters) {
            if (isRecipeRestricted(recipe, filter)) {
                return true;
            }
        }
        return false;
    }
    protected void updateTable(DefaultTableModel model) {
        model.setRowCount(0);
        List<String[]> rows = getRezepts();
        ArrayList<String> kategories = getKategories();
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            row[1] = kategories.get(i);
            if (currentFilters.isEmpty()) {
                model.addRow(row);
            } else if (!matchesFilters(row[0])) {
                model.addRow(row);
            }
        }


    }


}
