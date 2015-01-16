package stockapplication;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpFrame {

    private final JPanel helpPanel = new JPanel();
    private final JTextArea helpField = new JTextArea();

    public HelpFrame() {
        helpField.setRows(25);
        helpField.setColumns(60);
        helpField.setEditable(false);
        helpField.setLineWrap(true);
        helpField.setTabSize(3);
        helpField.setWrapStyleWord(true);
        helpPanel.add(new JScrollPane(helpField));
    }

    public void display(String title, String message) {
        helpField.setText(message);
        JOptionPane.showConfirmDialog(null, helpPanel, title, JOptionPane.DEFAULT_OPTION);
    }
}
