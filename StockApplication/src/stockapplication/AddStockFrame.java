package stockapplication;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static stockapplication.StockApplication.mainFrame;

public class AddStockFrame {

    private final JPanel addStockPanel = new JPanel();
    private final JTextField addStockField = new JTextField();
    private final FormattedField addStockNumField = new FormattedField(0);
    private final FormattedField addStockAdjCostBaseField = new FormattedField(0.0);

    public AddStockFrame() {
        addStockPanel.setLayout(new GridLayout(2, 4));
        addStockPanel.add(new JLabel("Stock Symbol"));
        addStockPanel.add(new JLabel("Num of Existing Stocks"));
        addStockPanel.add(new JLabel("Adjusted Cost Base (total) "));
        addStockPanel.add(addStockField);
        addStockPanel.add(addStockNumField);
        addStockPanel.add(addStockAdjCostBaseField);
    }

    public void display(boolean reset) {
        if (reset) {
            addStockField.setText("");
            addStockNumField.setValue(0);
            addStockAdjCostBaseField.setValue(0.00);
        }
        if (new InputFrame(addStockPanel, mainFrame, "New Stock", new Object[]{addStockField, addStockNumField, addStockAdjCostBaseField}, addStockField).getInput()) {
            if (!mainFrame.addStock(addStockField.getText().replaceAll(" ", "_").toUpperCase(), (int) addStockNumField.getValue(), (double) addStockAdjCostBaseField.getValue())) {
                display(false);
            }
        }
    }
}
