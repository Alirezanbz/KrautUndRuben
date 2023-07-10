import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LoginPage extends JFrame {
    private JTextField emailField;
    private JButton submitButton;
    private JLabel emailLabel;
    private JPanel panel;


    public LoginPage() {
        setTitle("Login Page");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(79, 94, 92));

        panel = new JPanel();
        panel.setBackground(new Color(79, 94, 92));
        emailLabel = new JLabel("Email: ");
        emailLabel.setForeground(new Color(197, 235, 230));
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        emailField = new JTextField(20);
        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitButton.setBackground(new Color(197, 235, 230));
        submitButton.setForeground(new Color(79, 94, 92));

        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(submitButton);


        add(panel);
        setLocationRelativeTo(null);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Queries db = new Queries();

                // Check if email exists in the database
                ArrayList<String> emails = db.selectStringQuerySingle("email", "kunden", "WHERE email='" + emailField.getText() + "'");
                ArrayList<String> kdnrList = db.selectStringQuerySingle("kdnr", "kunden", "WHERE email='" + emailField.getText() + "'");

                // If email exists, open the Home Page Frame, else show an error
                if(emails.size() > 0) {
                    // Open Home Page
                    HomePageFrame homePageFrame = new HomePageFrame(kdnrList.get(0));
                    System.out.println(kdnrList.get(0));
                    homePageFrame.openHomePage();
                    dispose(); // Close login page
                } else {
                    // Show an error
                    JOptionPane.showMessageDialog(null,"Email does not exist. Please try again.");
                }
            }
        });
    }

}
