import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomePageFrame extends HomePageStatements{


    public void openHomePage(){
        final JFrame frame = new JFrame("Home Page");


        JLabel transactionsLabel = new JLabel("Alle Rezepte:");
        transactionsLabel.setForeground(new Color(197, 235, 230));
        transactionsLabel.setFont(new Font("Segoe UI" , Font.BOLD, 18));
        transactionsLabel.setBounds(10,120,400, 30);

        String[] columnNames = {"Name", "Kategorie", "Preis", "Nr"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        ArrayList<ArrayList<String>> records = selectStringQuery("rezeptname,kategoriename,sum(preis*menge),rezept.RezeptNr", "rezept", "LEFT JOIN rezept_kategorie ON rezept.RezeptNr = rezept_kategorie.RezeptNr\n" +
                "LEFT JOIN ernaehrungskategorie ON rezept_kategorie.KatNr = ernaehrungskategorie.KatNr\n" +
                "LEFT JOIN rezept_zutat ON rezept.RezeptNr = rezept_zutat.RezeptNr\n" +
                "LEFT JOIN zutat ON rezept_zutat.zutatNr = zutat.zutatNr\n" +
                "GROUP BY rezept.RezeptNr");

        System.out.println(records);
        String name="",kategorie="",preis="",nr="";

        for (ArrayList<String> record : records) {
            name = record.get(0);
            kategorie = record.get(1);
            preis = record.get(2);
            nr = record.get(3);
            model.addRow(new Object[]{name,kategorie,preis,nr});
        }

        JTable RezeptsTable = new JTable(model);


        JTableHeader header = RezeptsTable.getTableHeader();
        header.setBackground(new Color(79, 94, 92));
        header.setForeground(new Color(197, 235, 230));
        header.setFont(new Font("Segoe UI" , Font.BOLD, 14));


        JScrollPane scrollPane = new JScrollPane(RezeptsTable);
        scrollPane.setBounds(10, 150, 460, 400);
        scrollPane.setBackground(new Color(79, 94, 92));




        String[] zColumnNames = {"Name", "Menge", "Preis"};
        DefaultTableModel zModel = new DefaultTableModel(zColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };


        RezeptsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JTable target = (JTable) e.getSource();
                int row = target.getSelectedRow(); // get the selected row
                if (e.getClickCount() == 2) { // Detect double click events
                    String rezeptName = (String) target.getValueAt(row, 0); // get the Rezept name from the selected row

                    // Call a method to open the new frame/page and pass the Rezept name
                    RezeptDetailsPageFrame rezeptDetailsPageFrame = new RezeptDetailsPageFrame();
                    rezeptDetailsPageFrame.openRezeptDetailsFrame(rezeptName);
                }

                ArrayList<ArrayList<String>> zRecords = selectStringQuery("bezeichnung,menge,sum(preis*menge),kalorien", "rezept_zutat", "LEFT JOIN zutat ON zutat.zutatNr = rezept_zutat.zutatNr\n" +
                        "WHERE RezeptNr = '" + (String) target.getValueAt(row, 3) + "'\n" +
                        "GROUP BY zutat.ZutatNr");

                System.out.println(zRecords);
                while(zModel.getRowCount()>0) zModel.removeRow(0);
                String name = "",menge="",preis = "";
                for (ArrayList<String> record : zRecords) {
                    name = record.get(0);
                    menge = record.get(1);
                    preis = record.get(2);
                    zModel.addRow(new Object[]{name,menge,preis});
                }
            }
        });






        RezeptsTable.setBackground(new Color(79, 94, 92));
        RezeptsTable.setForeground(new Color(197, 235, 230));
        RezeptsTable.setFont(new Font("Segoe UI" , Font.PLAIN, 13));






        JTable ZutatenTable = new JTable(zModel);

        JTableHeader zHeader = ZutatenTable.getTableHeader();
        zHeader.setBackground(new Color(79, 94, 92));
        zHeader.setForeground(new Color(197, 235, 230));
        zHeader.setFont(new Font("Segoe UI" , Font.BOLD, 14));

        JScrollPane zScrollPane = new JScrollPane(ZutatenTable);
        zScrollPane.setBounds(500, 150, 460, 400);
        zScrollPane.setBackground(new Color(79, 94, 92));

        ZutatenTable.setBackground(new Color(79, 94, 92));
        ZutatenTable.setForeground(new Color(197, 235, 230));
        ZutatenTable.setFont(new Font("Segoe UI" , Font.PLAIN, 13));

        frame.add(transactionsLabel);
        frame.add(scrollPane);
        frame.add(zScrollPane);
        frame.setSize(1200,700);
        frame.getContentPane().setBackground(new Color(79, 94, 92));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}

