import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        MySQLStatements statements = new MySQLStatements();
        HomePageFrame homePageFrame = new HomePageFrame();
        homePageFrame.openHomePage();
        Queries queries = new Queries();


        //System.out.println(queries.selectQuery("nachname", "kunden", "where kunden.kdnr = 1001"));

        System.out.println(statements.getRezepts());
    }
}
