import javax.swing.*;
import java.awt.*;

public class RezeptDetailsPageFrame {

    public void openRezeptDetailsFrame(String rezeptName) {
        JFrame detailFrame = new JFrame(rezeptName); // new frame with rezeptName as title
        JLabel label = new JLabel(rezeptName); // a label to display the rezeptName

        detailFrame.add(label);
        detailFrame.setSize(300, 200);
        detailFrame.setLocationRelativeTo(null);
        detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // close only current frame
        detailFrame.setLayout(new FlowLayout());
        detailFrame.setVisible(true);
    }

}
