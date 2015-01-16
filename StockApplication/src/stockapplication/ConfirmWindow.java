package stockapplication;

import javax.swing.JOptionPane;

public class ConfirmWindow {

    public final boolean displayConfirmation(String body) {
        return JOptionPane.showConfirmDialog(null, body, "Confirm", JOptionPane.YES_OPTION) == JOptionPane.YES_OPTION;
    }
}
