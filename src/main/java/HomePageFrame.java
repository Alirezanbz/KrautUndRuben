import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class HomePageFrame extends HomePageStatements{


    public void openHomePage(){
        final JFrame frame = new JFrame("Home Page");


        JLabel transactionsLabel = new JLabel("Alle Rezepte:");
        transactionsLabel.setForeground(new Color(197, 235, 230));
        transactionsLabel.setFont(new Font("Segoe UI" , Font.BOLD, 18));
        transactionsLabel.setBounds(10,120,400, 30);

        String[] columnNames = {"Name", "Kategorie", "Preis"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        JTable RezeptsTable = new JTable(model);


        JTableHeader header = RezeptsTable.getTableHeader();
        header.setBackground(new Color(79, 94, 92));
        header.setForeground(new Color(197, 235, 230));
        header.setFont(new Font("Segoe UI" , Font.BOLD, 14));


        JScrollPane scrollPane = new JScrollPane(RezeptsTable);
        scrollPane.setBounds(10, 150, 460, 400);
        scrollPane.setBackground(new Color(79, 94, 92));

        List<String[]> rows = getRezepts();
        ArrayList<String> kategories = getKategories();
        for(int i = 0; i < rows.size(); i++){
            String[] row = rows.get(i);
            row[1] = kategories.get(i); // Here we're setting the category for each recipe
            model.addRow(row);
        }



        RezeptsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Detect double click events
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow(); // get the selected row
                    String rezeptName = (String) target.getValueAt(row, 0); // get the Rezept name from the selected row

                    // Call a method to open the new frame/page and pass the Rezept name
                    RezeptDetailsPageFrame rezeptDetailsPageFrame = new RezeptDetailsPageFrame();
                    rezeptDetailsPageFrame.openRezeptDetailsFrame(rezeptName);
                }
            }
        });






        RezeptsTable.setBackground(new Color(79, 94, 92));
        RezeptsTable.setForeground(new Color(197, 235, 230));
        RezeptsTable.setFont(new Font("Segoe UI" , Font.PLAIN, 13));


        frame.add(transactionsLabel);
        frame.add(scrollPane);
        frame.setSize(500,700);
        frame.getContentPane().setBackground(new Color(79, 94, 92));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}

