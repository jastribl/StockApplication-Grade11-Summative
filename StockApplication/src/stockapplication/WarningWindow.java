package stockapplication;

import javax.swing.JOptionPane;

public class WarningWindow {

    private final String ends[] = {"  Please review your entries!"};

    public final void displayWarning(String body) {
        JOptionPane.showMessageDialog(null, body, "Warning!", JOptionPane.WARNING_MESSAGE);
    }

    public void displayWarning(String body, int end) {
        JOptionPane.showMessageDialog(null, body + ends[end], "Warning!", JOptionPane.WARNING_MESSAGE);
    }
}
