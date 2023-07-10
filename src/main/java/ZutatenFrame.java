import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ZutatenFrame extends JFrame {

    private Queries queries = new Queries();
    private JButton weiterButton;
    private JTable zutatenTable;
    private DefaultTableModel tableModel;

    public ZutatenFrame(Integer kdNr, Basket basket) {
        super("Zutaten");

        tableModel = new DefaultTableModel(new Object[]{"Artikel"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        zutatenTable = new JTable(tableModel);

        ArrayList<Integer> zutaten = queries.selectIntegerQuery("zutatNr", "zutat", null);

        if (zutaten != null) {
            for (Integer zutatNr : zutaten){
                tableModel.addRow(new Object[]{queries.selectStringQuery("bezeichnung", "zutat", "WHERE zutatNr = " + zutatNr)});
            }
        }

        weiterButton = new JButton("Weiter");

        setLayout(new BorderLayout());

        add(new JScrollPane(zutatenTable), BorderLayout.CENTER);

        // Create a panel for the buttons and add it to the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(weiterButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listener to each row
        ListSelectionModel selectionModel = zutatenTable.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = zutatenTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Handle row selection event here
                        // Add your action here
                        basket.addZutatToBasket(selectedRow, 1);
                        System.out.println("Row " + selectedRow + " selected");
                        System.out.println(basket.zutaten);
                    }
                }
            }
        });

        // Add action listeners to the buttons
        weiterButton.addActionListener(e -> {
            BasketFrame basketFrame = new BasketFrame(kdNr, basket);
            dispose();
        });

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
