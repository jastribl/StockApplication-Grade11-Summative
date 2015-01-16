package stockapplication;

import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static stockapplication.StockApplication.editStartingValuesFrame;
import static stockapplication.StockApplication.stockFrame;
import static stockapplication.StockApplication.warningWindow;

public class AddEntryFrame {

    private final JPanel addEntryPanel = new JPanel();
    private final DateBox dateBox = new DateBox();
    private final FormattedField tradeNumBox = new FormattedField(1);
    private final JComboBox BSBox = new JComboBox();
    private final FormattedField quantityBox = new FormattedField(1);
    private final FormattedField priceBox = new FormattedField(0.0);
    private final FormattedField commissionBox = new FormattedField(0.0);

    public AddEntryFrame() {
        JPanel boxPanel = new JPanel(new GridLayout(2, 5));
        String[] labels = {"Trade #", "Buy / Sell", "Quantity", "Price", "Commission"};
        for (String label : labels) {
            boxPanel.add(new JLabel(label));
        }
        addEntryPanel.add(dateBox);
        boxPanel.add(tradeNumBox);
        BSBox.addItem("B");
        BSBox.addItem("S");
        boxPanel.add(BSBox);
        boxPanel.add(quantityBox);
        boxPanel.add(priceBox);
        commissionBox.setValue(9.99);
        boxPanel.add(commissionBox);
        addEntryPanel.add(boxPanel);
    }

    public void display(String title) {
        if (title.equals("Edit / View Entry") && stockFrame.entriesTable.getSelectedRow() < 0) {
            warningWindow.displayWarning("You have not selected anything!", 0);
        } else if (title.equals("Edit / View Entry") && stockFrame.entriesTable.getSelectedRow() == 0) {
            editStartingValuesFrame.display();
        } else {
            if (title.equals("New Entry")) {
                dateBox.setToToday();
                String all[][] = stockFrame.getNumsFromTable();
                int maxTradeNum = 0;
                for (int i = 1; i < all.length; i++) {
                    if (Integer.valueOf(all[i][3]) > maxTradeNum) {
                        maxTradeNum = Integer.valueOf(all[i][3]);
                    }
                }
                tradeNumBox.setValue(maxTradeNum + 1);
                BSBox.setSelectedItem("B");
                quantityBox.setValue(1);
                priceBox.setValue(0);
                commissionBox.setValue(9.99);
            } else {
                String[] labels = stockFrame.getNumsFromTable()[stockFrame.entriesTable.getSelectedRow()];
                dateBox.setYear(Integer.valueOf(labels[0]));
                dateBox.setMonth(Integer.valueOf(labels[1]));
                dateBox.setDay(Integer.valueOf(labels[2]));
                tradeNumBox.setValue(Integer.valueOf(labels[3]));
                BSBox.setSelectedItem(labels[4]);
                quantityBox.setValue(Integer.valueOf(labels[5]));
                priceBox.setValue(Double.valueOf(labels[6]));
                commissionBox.setValue(Double.valueOf(labels[7]));
            }
            if (new InputFrame(addEntryPanel, stockFrame, title, new Object[]{dateBox, tradeNumBox, BSBox, quantityBox, priceBox, commissionBox}, quantityBox).getInput()) {
                if (!checkAndAddEntrys(title)) {
                    display(title);
                }
            }
        }
    }

    private boolean checkAndAddEntrys(String kind) {
        try {
            String all[][] = stockFrame.getNumsFromTable();
            boolean duplicate = false;
            for (int i = 1; i < all.length; i++) {
                if (Integer.valueOf(all[i][0]) == dateBox.getYear() && Integer.valueOf(all[i][1]) == dateBox.getMonth() && Integer.valueOf(all[i][2]) == dateBox.getDay()) {
                    if (Integer.valueOf(all[i][3]) == (int) tradeNumBox.getValue()) {
                        switch (kind) {
                            case "New Entry":
                                duplicate = true;
                                i = all.length;
                                break;
                            case "Edit / View Entry":
                                if (i != stockFrame.entriesTable.getSelectedRow()) {
                                    duplicate = true;
                                    i = all.length;
                                }
                                break;
                        }
                    }
                }
            }
            if (duplicate == false) {
                if (kind.equals("Edit / View Entry")) {
                    stockFrame.entriesModel.removeRow(stockFrame.entriesTable.getSelectedRow());
                }
                String year = String.valueOf(dateBox.getYear()), month = String.valueOf(dateBox.getMonth()), day = String.valueOf(dateBox.getDay()), tradeNum = String.valueOf(tradeNumBox.getValue());
                stockFrame.entriesModel.addRow(new String[]{year, month, day, tradeNum, BSBox.getSelectedItem().toString(), String.valueOf(quantityBox.getValue()), String.valueOf(priceBox.getValue()), String.valueOf(commissionBox.getValue()), "-", "-", "-", "-"});
                stockFrame.sortCalculateSaveEntries();
                for (int i = 0; i < stockFrame.entriesTable.getRowCount(); i++) {
                    if (stockFrame.entriesTable.getValueAt(i, 0).equals(year)) {
                        if (stockFrame.entriesTable.getValueAt(i, 1).equals(month)) {
                            if (stockFrame.entriesTable.getValueAt(i, 2).equals(day)) {
                                if (stockFrame.entriesTable.getValueAt(i, 3).equals(tradeNum)) {
                                    stockFrame.entriesTable.setRowSelectionInterval(0, i);
                                }
                            }
                        }
                    }
                }
                return true;
            } else {
                warningWindow.displayWarning("Duplicate \"Trade #\".", 1);
            }
        } catch (NumberFormatException ex) {
            if (ex.toString().contains("empty String") || ex.toString().contains("For input string: \"\"")) {
                warningWindow.displayWarning("Empty Entry(s).", 2);
            } else if (ex.toString().contains("multiple points")) {
                warningWindow.displayWarning("Entries can only contain one decimal point!", 0);
            } else if (ex.toString().contains("For input string:")) {
                warningWindow.displayWarning("Invalid Character(s). [" + ex.toString().substring(52, ex.toString().length() - 1) + "]", 1);
            } else {
                warningWindow.displayWarning("Unknown error.", 1);
            }
        }
        return false;
    }
}
