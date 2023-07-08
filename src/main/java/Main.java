import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {


        HomePageFrame homePageFrame = new HomePageFrame();
        homePageFrame.openHomePage();
        HomePageStatements homePageStatements = new HomePageStatements();
        System.out.println(homePageStatements.getKategories());
        System.out.println();


        //System.out.println(queries.selectQuery("nachname", "kunden", "where kunden.kdnr = 1001"));


    }
}
