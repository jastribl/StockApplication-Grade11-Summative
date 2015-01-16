package stockapplication;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import static stockapplication.StockApplication.mainFrame;

public class HelpFrame extends JFrame {

    public final JTextArea helpField = new JTextArea();

    public HelpFrame() {
        JPanel helpPanel = new JPanel();
        add(helpPanel);
        helpField.setRows(30);
        helpField.setColumns(60);
        helpField.setSize(600, getWidth());
        helpField.setEditable(false);
        helpField.setLineWrap(true);
        helpField.setTabSize(3);
        helpField.setWrapStyleWord(true);
        JScrollPane helpScrollPane = new JScrollPane(helpField);
        helpScrollPane.setWheelScrollingEnabled(true);
        helpPanel.add(helpScrollPane);
        pack();
        setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - getSize().height) / 2));
        setResizable(false);
    }

    public void display(String title, String message) {
        setTitle(title);
        helpField.setText(message);
        setVisible(true);
    }
}
