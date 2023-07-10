import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomePageFrame extends HomePageStatements {

    public void openHomePage() {
        final JFrame frame = new JFrame("Home Page");
        frame.setLayout(new GridBagLayout());
        frame.getContentPane().setBackground(new Color(79, 94, 92));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(79, 94, 92));
        gbc.weighty = 0.1; // Top panel doesn't need as much space
        frame.add(topPanel, gbc);

        JLabel filterTitle = new JLabel("Beschränkung:");
        filterTitle.setForeground(new Color(197, 235, 230));
        filterTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        topPanel.add(filterTitle, BorderLayout.NORTH);

        String[] columnNames = {"Name", "Kategorie", "Preis"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
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
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(RezeptsTable);
        scrollPane.setPreferredSize(new Dimension(460, 400));
        scrollPane.getViewport().setBackground(new Color(79, 94, 92));
        List<String[]> rows = getRezepts();
        ArrayList<String> kategories = getKategories();
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            row[1] = kategories.get(i);
            model.addRow(row);
        }
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new GridLayout(2, 3));
        checkBoxPanel.setBackground(new Color(79, 94, 92));
        Set<String> uniqueConstraints = new HashSet<>(getBeschraenkungs());
        for (String constraint : uniqueConstraints) {
            JCheckBox checkBox = new JCheckBox(constraint);
            checkBox.setForeground(new Color(197, 235, 230));
            checkBox.setBackground(new Color(79, 94, 92));
            checkBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    currentFilters.add(constraint);
                } else {
                    currentFilters.remove(constraint);
                }
                updateTable(model);
            });
            checkBoxPanel.add(checkBox);
        }

        topPanel.add(checkBoxPanel, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setBackground(new Color(79, 94, 92));

        JLabel searchLabel = new JLabel("Suche:");
        searchLabel.setForeground(new Color(197, 235, 230));
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        searchPanel.add(searchLabel, BorderLayout.WEST);

        JTextField searchField = new JTextField(15);
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String val = searchField.getText();
                TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(model);
                RezeptsTable.setRowSorter(rowSorter);
                rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + val));
            }
        });
        searchPanel.add(searchField, BorderLayout.CENTER);

        topPanel.add(searchPanel, BorderLayout.SOUTH);

        JPanel tableTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tableTitlePanel.setBackground(new Color(79, 94, 92));
        JLabel tableTitel = new JLabel("Alle Rezepte:");
        tableTitel.setForeground(new Color(197, 235, 230));
        tableTitel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitlePanel.add(tableTitel);

        gbc.gridy = 1;
        gbc.weighty = 0.1;
        frame.add(tableTitlePanel, gbc);

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
                int row = target.getSelectedRow();

                if (e.getClickCount() == 2) {
                    String rezeptName = (String) target.getValueAt(row, 3);
                    RezeptDetailsPageFrame rezeptDetailsPageFrame = new RezeptDetailsPageFrame();
                    rezeptDetailsPageFrame.openRezeptDetailsFrame(rezeptName);

                }
                ArrayList<ArrayList<String>> zRecords = selectStringQuery("bezeichnung,menge,sum(preis*menge),kalorien", "rezept_zutat", "LEFT JOIN zutat ON zutat.zutatNr = rezept_zutat.zutatNr\n" +
                        "WHERE RezeptNr = '" + (String) target.getValueAt(row, 0) + "'\n" +
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
        RezeptsTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.8;
        gbc.fill = GridBagConstraints.BOTH;
        frame.add(scrollPane, gbc);

        JTable ZutatenTable = new JTable(zModel);

        JTableHeader zHeader = ZutatenTable.getTableHeader();
        zHeader.setBackground(new Color(79, 94, 92));
        zHeader.setForeground(new Color(197, 235, 230));
        zHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane zScrollPane = new JScrollPane(ZutatenTable);
        zScrollPane.setPreferredSize(new Dimension(460, 400));
        zScrollPane.getViewport().setBackground(new Color(79, 94, 92));

        ZutatenTable.setBackground(new Color(79, 94, 92));
        ZutatenTable.setForeground(new Color(197, 235, 230));
        ZutatenTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        gbc.gridy = 3;
        frame.add(zScrollPane, gbc);

        frame.setSize(1200,700);
        frame.getContentPane().setBackground(new Color(79, 94, 92));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        frame.pack();
    }



}
