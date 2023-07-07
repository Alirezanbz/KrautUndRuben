import java.util.ArrayList;
import java.util.List;

public class HomePageStatements extends Queries {


    protected List<String[]> getRezepts() {
        ArrayList<String> rezepts = selectStringQuery("rezeptname", "rezept", "");
        List<String[]> rowList = new ArrayList<>();

        for (int i = 0; i < rezepts.size(); i++) {
            String[] row = new String[3];
            row[0] = rezepts.get(i);
            row[1] = "";
            row[2] = "";
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


}
