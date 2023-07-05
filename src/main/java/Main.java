import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        //MySQLStatements statements = new MySQLStatements();
        Queries queries = new Queries();

        // System.out.println(statements.getRezepts());

        System.out.println(queries.getZutatenNachRezept(6));
        System.out.println(queries.getRezeptNachZutat(7));
        System.out.println(queries.getZutatNachBeschraenkung(47));
    }
}
