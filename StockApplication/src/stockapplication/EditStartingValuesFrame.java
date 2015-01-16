package stockapplication;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static stockapplication.StockApplication.stockFrame;

public class EditStartingValuesFrame {

    private final JPanel editStartingValuesPanel = new JPanel();
    private final FormattedField editStartingNumField = new FormattedField(0);
    private final FormattedField editStartingABCField = new FormattedField(0.0);

    public EditStartingValuesFrame() {
        editStartingValuesPanel.setLayout(new GridLayout(2, 2));
        editStartingValuesPanel.add(new JLabel("Num Of Existing Stocks"));
        editStartingValuesPanel.add(new JLabel("Adjusted Cost Base (total)"));
        editStartingValuesPanel.add(editStartingNumField);
        editStartingValuesPanel.add(editStartingABCField);
    }

    public void display() {
        editStartingNumField.setValue(Integer.valueOf(stockFrame.entriesModel.getValueAt(0, 8).toString()));
        editStartingABCField.setValue(Double.valueOf(stockFrame.entriesModel.getValueAt(0, 10).toString()));
        if (new InputFrame(editStartingValuesPanel, stockFrame, "Edit Starting Values", new Object[]{editStartingNumField, editStartingABCField}, editStartingNumField).getInput()) {
            editStartingValues();
        }
    }

    private void editStartingValues() {
        double ACBPerUnit = 0;
        if ((int) editStartingNumField.getValue() != 0) {
            ACBPerUnit = (double) editStartingABCField.getValue() / (int) editStartingNumField.getValue();
        }
        String all[][] = stockFrame.getNumsFromTable();
        all[0] = new String[]{"-", "-", "-", "-", "-", "-", "-", "-", String.valueOf(editStartingNumField.getValue()), String.valueOf(ACBPerUnit), String.valueOf(editStartingABCField.getValue()), "-"};
        stockFrame.clearStocksTable();
        for (Object[] all1 : all) {
            stockFrame.entriesModel.addRow(all1);
        }
        stockFrame.sortCalculateSaveEntries();
        stockFrame.entriesTable.setRowSelectionInterval(0, 0);
    }
}
