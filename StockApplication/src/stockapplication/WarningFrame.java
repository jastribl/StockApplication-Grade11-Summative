package stockapplication;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static stockapplication.StockApplication.listener;
import static stockapplication.StockApplication.mainFrame;

public class WarningFrame extends JFrame {

    public final JLabel warningLabel = new JLabel();

    public WarningFrame() {
        setTitle("Error / Warning");
        JPanel warningPanel = new JPanel();
        JButton warningButton = new JButton("OK");
        warningPanel.add(warningLabel);
        warningPanel.add(warningButton);
        add(warningPanel);
        warningButton.addActionListener(listener);
        setResizable(false);
    }

    public void displayWarning(String message) {
        warningLabel.setText(message);
        pack();
        setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - getSize().height) / 2));
        setVisible(true);
    }
}
