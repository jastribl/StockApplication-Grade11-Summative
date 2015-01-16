package stockapplication;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static stockapplication.StockApplication.stockFrame;
import static stockapplication.StockApplication.stocks;

public class EditStartingValuesFrame {

    private final JPanel editStartingValuesPanel = new JPanel();
    private final IntegerField editStartingNumField = new IntegerField(0);
    private final MoneyField editStartingABCField = new MoneyField(0.0);
    private int currentStockIndex = 0;

    public EditStartingValuesFrame() {
        editStartingValuesPanel.setLayout(new GridLayout(2, 2));
        editStartingValuesPanel.add(new JLabel("Num Of Existing Stocks"));
        editStartingValuesPanel.add(new JLabel("Adjusted Cost Base (total)"));
        editStartingValuesPanel.add(editStartingNumField);
        editStartingValuesPanel.add(editStartingABCField);
    }

    public final void display(int stockIndex) {
        currentStockIndex = stockIndex;
        editStartingNumField.setValue(stocks.get(currentStockIndex).getStartingNum());
        editStartingABCField.setValue(stocks.get(currentStockIndex).getStartingACBTotal());
        if (new InputFrame(editStartingValuesPanel, stockFrame, "Edit Starting Values", new Object[]{editStartingNumField, editStartingABCField}, 0).getInput()) {
            editStartingValues();
        }
    }

    private void editStartingValues() {
        double ACBPerUnit = 0;
        if ((int) editStartingNumField.getValue() != 0) {
            ACBPerUnit = (double) editStartingABCField.getValue() / (int) editStartingNumField.getValue();
        }
        stocks.get(currentStockIndex).setStartingValues((int) editStartingNumField.getValue(), (double) editStartingABCField.getValue(), ACBPerUnit);
        stockFrame.entriesTable.setRowSelectionInterval(0, 0);
    }
}
