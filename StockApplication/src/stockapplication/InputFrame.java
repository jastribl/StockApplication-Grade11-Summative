package stockapplication;

import java.awt.event.*;
import java.beans.*;
import javax.swing.*;

public class InputFrame {

    private final JPanel panel;
    private final String title;
    private final JFrame hostFrame;
    private final Object inputFields[];
    private final int firstToFocus;

    public InputFrame(JPanel gPanel, JFrame gHostFrame, String gTitle, Object gInputFields[], int gFirstToFocus) {
        panel = gPanel;
        hostFrame = gHostFrame;
        title = gTitle;
        inputFields = gInputFields;
        firstToFocus = gFirstToFocus;
    }

    public final boolean getInput() {
        final JOptionPane pane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION);
        pane.setValue(JOptionPane.CANCEL_OPTION);
        final JDialog dialog = new JDialog(hostFrame, title, true);
        dialog.setContentPane(pane);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        pane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (dialog.isVisible() && (e.getSource() == pane) && (e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY))) {
                    dialog.setVisible(false);
                }
            }
        });
        KeyListener keyListener = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                int key = ke.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    pane.setValue(JOptionPane.YES_OPTION);
                    dialog.setVisible(false);
                } else if (key == KeyEvent.VK_ESCAPE) {
                    pane.setValue(JOptionPane.CANCEL_OPTION);
                    dialog.setVisible(false);
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        };
        for (Object inputField : inputFields) {
            ((JComponent) inputField).addKeyListener(keyListener);
        }
        if (((JComponent) inputFields[firstToFocus]) instanceof DateBox) {
            ((DateBox) inputFields[firstToFocus]).getYearBox().requestFocus();
        } else {
            ((JComponent) inputFields[firstToFocus]).requestFocus();
        }
        dialog.setVisible(true);
        return (int) pane.getValue() == JOptionPane.YES_OPTION;
    }
}
