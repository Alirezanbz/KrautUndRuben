public class Main {
    public static void main(String[] args) {
        //MySQLStatements statements = new MySQLStatements();
        Queries queries = new Queries();

        // System.out.println(statements.getRezepts());

        System.out.println(queries.selectQuery("email", "Kunden", "WHERE KdNr = ''"));
    }
}
