import javax.swing.*;
import java.awt.*;

public class SampleApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SampleApp::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Sample App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JLabel label = new JLabel("Example distributed app.", JLabel.CENTER);
        frame.add(label);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}