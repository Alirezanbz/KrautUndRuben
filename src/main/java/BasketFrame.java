import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BasketFrame extends JFrame {

    private Queries queries = new Queries();
    private JButton orderButton;
    private JButton removeButton;
    private JTable basketTable;
    private DefaultTableModel tableModel;

    public BasketFrame(Integer kdNr, Basket basket) {
        super("Warenkorb");

        tableModel = new DefaultTableModel(new Object[]{"Artikel", "Menge"}, 0);
        basketTable = new JTable(tableModel);

        if (basket.rezepte != null) {
            for (ArrayList<Integer> rezept : basket.rezepte){
                tableModel.addRow(new Object[]{queries.selectStringQuery("rezeptname", "rezept", "WHERE RezeptNr = " + rezept.get(0)).get(0).get(0), rezept.get(1)});
            }
        }

        if (basket.zutaten != null) {
            for (ArrayList<Integer> zutat : basket.zutaten){
                tableModel.addRow(new Object[] {queries.selectStringQuery("bezeichnung", "zutat", "WHERE zutatNr = " + zutat.get(0)).get(0).get(0), zutat.get(1)});
            }
        }

        orderButton = new JButton("Bestellen");
        removeButton = new JButton("Artikel Entwerfen");

        setLayout(new BorderLayout());

        add(new JScrollPane(basketTable), BorderLayout.CENTER);

        // Create a panel for the buttons and add it to the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(orderButton);
        buttonPanel.add(removeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to the buttons
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                queries.createOrder(kdNr, basket);
                System.out.println("Order Placed");
            }
        });

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
