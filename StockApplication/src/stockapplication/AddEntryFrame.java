package stockapplication;

import java.awt.GridLayout;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static stockapplication.StockApplication.editStartingValuesFrame;
import static stockapplication.StockApplication.listener;
import static stockapplication.StockApplication.mainFrame;
import static stockapplication.StockApplication.stockFrame;
import static stockapplication.StockApplication.warningFrame;

public class AddEntryFrame extends JFrame {

    public final JTextField addEntryBoxes[] = new JTextField[8];
    public final JButton addEntryButton = new JButton("Add Entry");

    public AddEntryFrame() {
        JPanel addEntryPanel = new JPanel();
        addEntryPanel.setLayout(new GridLayout(2, 8));
        String[] labels = {"Year", "Month", "Day", "Trade #", "Buy / Sell", "Quantity", "Price", "Commission", ""};
        for (String label : labels) {
            addEntryPanel.add(new JLabel(label));
        }
        for (int i = 0; i < 8; i++) {
            addEntryBoxes[i] = new JTextField();
            addEntryPanel.add(addEntryBoxes[i]);
        }
        addEntryPanel.add(addEntryButton);
        add(addEntryPanel);
        addEntryButton.addActionListener(listener);
        setResizable(false);
        pack();
        setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - getSize().height) / 2));
    }

    public void display(String title) {
        setTitle(title);
        if (title.equals("New Entry")) {
            Date date = new Date();
            String[] labels = {String.valueOf(date.getYear() + 1900), String.valueOf(date.getMonth() + 1), String.valueOf(date.getDate()), "1", "", "", "", "9.99"};
            for (int i = 0; i < labels.length; i++) {
                addEntryBoxes[i].setText(labels[i]);
            }
            addEntryButton.setText("Add Entry");
            addEntryBoxes[4].requestFocus();
            setVisible(true);
        } else if (stockFrame.entriesTable.getSelectedRow() > 0) {
            String all[][] = stockFrame.getNumsFromTable();
            for (int i = 0; i < 8; i++) {
                addEntryBoxes[i].setText(all[stockFrame.entriesTable.getSelectedRow()][i]);
            }
            addEntryButton.setText("Edit Entry");
            addEntryBoxes[4].requestFocus();
            setVisible(true);
        } else if (stockFrame.entriesTable.getSelectedRow() < 0) {
            warningFrame.displayWarning("You have not selected anything!");
        } else if (stockFrame.entriesTable.getSelectedRow() == 0) {
            editStartingValuesFrame.display();
        }
    }

    public void checkAddEditEntrys(String kind) {
        try {
            Date date = new Date();
            if (Integer.valueOf(addEntryBoxes[0].getText()) >= 2000 && Integer.valueOf(addEntryBoxes[0].getText()) <= date.getYear() + 1900) {
                if (Integer.valueOf(addEntryBoxes[1].getText()) >= 1 && Integer.valueOf(addEntryBoxes[1].getText()) <= 12) {
                    if (!addEntryBoxes[1].getText().substring(0, 1).equals("0")) {
                        if (Integer.valueOf(addEntryBoxes[2].getText()) >= 1 && Integer.valueOf(addEntryBoxes[2].getText()) <= 31) {
                            if (!addEntryBoxes[2].getText().substring(0, 1).equals("0")) {
                                if (Integer.valueOf(addEntryBoxes[3].getText()) >= 1) {
                                    if (addEntryBoxes[4].getText().toLowerCase().matches("b") || addEntryBoxes[4].getText().toLowerCase().matches("s")) {
                                        if (Integer.valueOf(addEntryBoxes[5].getText()) > 0) {
                                            if (Double.valueOf(addEntryBoxes[6].getText()) > 0) {
                                                if (Double.valueOf(addEntryBoxes[7].getText()) >= 0) {
                                                    String all[][] = stockFrame.getNumsFromTable();
                                                    boolean duplicate = false;
                                                    for (int i = 1; i < all.length; i++) {
                                                        if (all[i][0].equals(addEntryBoxes[0].getText()) && all[i][1].equals(addEntryBoxes[1].getText()) && all[i][2].equals(addEntryBoxes[2].getText()) && all[i][3].equals(addEntryBoxes[3].getText())) {
                                                            switch (kind) {
                                                                case "add":
                                                                    duplicate = true;
                                                                    i = all.length;
                                                                    break;
                                                                case "edit":
                                                                    if (i != stockFrame.entriesTable.getSelectedRow()) {
                                                                        duplicate = true;
                                                                        i = all.length;
                                                                    }
                                                                    break;
                                                            }
                                                        }
                                                    }
                                                    if (duplicate == false) {
                                                        setVisible(false);
                                                        if (kind.equals("edit")) {
                                                            stockFrame.entriesModel.removeRow(stockFrame.entriesTable.getSelectedRow());
                                                        }
                                                        stockFrame.entriesModel.addRow(new String[]{addEntryBoxes[0].getText(), addEntryBoxes[1].getText(), addEntryBoxes[2].getText(), addEntryBoxes[3].getText(), addEntryBoxes[4].getText().toUpperCase(), addEntryBoxes[5].getText(), addEntryBoxes[6].getText(), addEntryBoxes[7].getText(), "-", "-", "-", "-"});
                                                        stockFrame.sortCalculateSaveEntries();
                                                    } else {
                                                        warningFrame.displayWarning("Duplicate \"Trade #\".  Please review your entries!");
                                                    }
                                                } else {
                                                    warningFrame.displayWarning("\"Commission\" out of range.  Please review your entries!");
                                                }
                                            } else {
                                                warningFrame.displayWarning("\"Price\" out of range.  Please review your entries!");
                                            }
                                        } else {
                                            warningFrame.displayWarning("\"Quantity\" out of range.  Please review your entries!");
                                        }
                                    } else {
                                        warningFrame.displayWarning("Invalid character(s) in \"Buy / Sell\".  Please review your entries!");
                                    }
                                } else {
                                    warningFrame.displayWarning("\"Trade #\" out of range.  Please review your entries!");
                                }
                            } else {
                                warningFrame.displayWarning("No leading Zero's in \"Day\".  Please review your entries!");
                            }
                        } else {
                            warningFrame.displayWarning("\"Day\" out of range.  Please review your entries!");
                        }
                    } else {
                        warningFrame.displayWarning("No leading Zero's in \"Month\".  Please review your entries!");
                    }
                } else {
                    warningFrame.displayWarning("\"Month\" out of range.  Please review your entries!");
                }
            } else {
                warningFrame.displayWarning("\"Year\" out of range.  Please review your entries!");
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
