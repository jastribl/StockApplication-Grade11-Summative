package stockapplication;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static stockapplication.StockApplication.listener;
import static stockapplication.StockApplication.mainFrame;
import static stockapplication.StockApplication.stockFrame;
import static stockapplication.StockApplication.warningFrame;

public class EditStartingValuesFrame extends JFrame {

    public final JTextField editStartingNumField = new JTextField();
    public final JTextField editStartingACBTotalField = new JTextField();

    public EditStartingValuesFrame() {
        setTitle("Edit Starting Values");
        JPanel editStartingValuesPanel = new JPanel();
        JButton editStartingValuesButton = new JButton("Apply");
        editStartingValuesPanel.setLayout(new GridLayout(2, 3));
        editStartingValuesPanel.add(new JLabel("Num Of Existing Stocks"));
        editStartingValuesPanel.add(new JLabel("Adjusted Cost Base (total)"));
        editStartingValuesPanel.add(new JLabel(""));
        editStartingValuesPanel.add(editStartingNumField);
        editStartingValuesPanel.add(editStartingACBTotalField);
        editStartingValuesPanel.add(editStartingValuesButton);
        add(editStartingValuesPanel);
        editStartingValuesButton.addActionListener(listener);
        setResizable(false);
        pack();
        setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - getSize().height) / 2));
    }

    public void display() {
        editStartingNumField.setText(stockFrame.entriesModel.getValueAt(0, 8).toString());
        editStartingACBTotalField.setText(stockFrame.entriesModel.getValueAt(0, 10).toString());
        setVisible(true);
    }

    public void editStartingValues() {
        try {
            if (Integer.valueOf(editStartingNumField.getText()) >= 0) {
                if (Double.valueOf(editStartingACBTotalField.getText()) >= 0) {
                    setVisible(false);
                    String ACBPerUnit;
                    if (Integer.valueOf(editStartingNumField.getText()) == 0) {
                        ACBPerUnit = "0";
                    } else {
                        ACBPerUnit = String.valueOf(Double.valueOf(editStartingACBTotalField.getText()) / Double.valueOf(editStartingNumField.getText()));
                    }
                    String all[][] = stockFrame.getNumsFromTable();
                    all[0] = new String[]{"-", "-", "-", "-", "-", "-", "-", "-", editStartingNumField.getText(), ACBPerUnit, editStartingACBTotalField.getText(), "-"};
                    stockFrame.clearStocksTable();
                    for (String[] all1 : all) {
                        stockFrame.entriesModel.addRow(all1);
                    }
                    stockFrame.sortCalculateSaveEntries();
                } else {
                    warningFrame.displayWarning("Invalid character(s) in \"Adjusted Cost Base (total)\".  Please review your entries!");
                }
            } else {
                warningFrame.displayWarning("Invalid Character(s) in \"Num Of Existing Stocks\".  Please review your entries!");
            }
        } catch (NumberFormatException ex) {
            if (ex.toString().contains("empty String") || ex.toString().contains("For input string: \"\"")) {
                warningFrame.displayWarning("Empty Entry(s)...Please Check All Fields!");
            } else if (ex.toString().contains("multiple points")) {
                warningFrame.displayWarning("Entries can only contain one decimal point!");
            } else if (ex.toString().contains("For input string:")) {
                warningFrame.displayWarning("Invalid Character(s)... [" + ex.toString().substring(52, ex.toString().length() - 1) + "]  ...Please Review your Entries!");
            } else {
                warningFrame.displayWarning("Unknown error.  Please review your entries!");
            }
        }
    }
}
