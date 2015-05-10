package stockapplication;

import java.awt.GridLayout;
import javax.swing.*;
import static stockapplication.StockApplication.editStartingValuesFrame;
import static stockapplication.StockApplication.mainFrame;
import static stockapplication.StockApplication.stockFrame;
import static stockapplication.StockApplication.stocks;
import static stockapplication.StockApplication.warningWindow;

public class AddEntryFrame {

    private final JPanel addEntryPanel = new JPanel();
    private final DateBox dateBox = new DateBox();
    private final IntegerField tradeNumBox = new IntegerField(1);
    private final JComboBox BSBox = new JComboBox();
    private final IntegerField quantityBox = new IntegerField(1);
    private final MoneyField priceBox = new MoneyField(0.0);
    private final MoneyField commissionBox = new MoneyField(0.0);
    private int currentStockIndex = 0, currentEntrieIndex = 0;

    public AddEntryFrame() {
        JPanel boxPanel = new JPanel(new GridLayout(2, 5));
        String[] labels = {"Trade #", "Buy / Sell", "Quantity", "Price", "Commission"};
        for (String label : labels) {
            boxPanel.add(new JLabel(label));
        }
        addEntryPanel.add(dateBox);
        boxPanel.add(tradeNumBox);
        BSBox.addItem('B');
        BSBox.addItem('S');
        boxPanel.add(BSBox);
        boxPanel.add(quantityBox);
        boxPanel.add(priceBox);
        commissionBox.setValue(9.99);
        boxPanel.add(commissionBox);
        addEntryPanel.add(boxPanel);
    }

    public final void display(String title) {
        currentStockIndex = mainFrame.mainList.getSelectedIndex();
        currentEntrieIndex = stockFrame.entriesTable.getSelectedRow() - 1;
        if (title.equals("Edit / View Entry") && currentEntrieIndex < -1) {
            warningWindow.displayWarning("You have not selected anything!");
        } else if (title.equals("Edit / View Entry") && currentEntrieIndex == -1) {
            editStartingValuesFrame.display(mainFrame.mainList.getSelectedIndex());
        } else {
            if (title.equals("New Entry")) {
                dateBox.setToToday();
                tradeNumBox.setValue(1);
                BSBox.setSelectedIndex(0);
                quantityBox.setValue(1);
                priceBox.setValue(0.0);
                commissionBox.setValue(9.99);
            } else {
                Entry entry = stocks.get(currentStockIndex).getEntries().get(currentEntrieIndex);
                dateBox.setYear(entry.getYear());
                dateBox.setMonth(entry.getMonth());
                dateBox.setDay(entry.getDay());
                tradeNumBox.setValue(entry.getTradeNum());
                BSBox.setSelectedItem(entry.getBS());
                quantityBox.setValue(entry.getQuantity());
                priceBox.setValue(entry.getPrice());
                commissionBox.setValue(entry.getCommission());
            }
            if (new InputFrame(addEntryPanel, stockFrame, title, new Object[]{dateBox, tradeNumBox, BSBox, quantityBox, priceBox, commissionBox}, 3).getInput()) {
                for (int i = 0; i < stocks.get(currentStockIndex).getEntries().size(); i++) {
                    Entry entry = stocks.get(currentStockIndex).getEntries().get(i);
                    if (entry.getYear() == dateBox.getYear() && entry.getMonth() == dateBox.getMonth() && entry.getDay() == dateBox.getDay() && entry.getTradeNum() == (int) tradeNumBox.getValue() && ((title.equals("Edit / View Entry") && i != currentEntrieIndex) || title.equals("New Entry"))) {
                        while (entry.getTradeNum() == (int) tradeNumBox.getValue()) {
                            tradeNumBox.setValue((int) tradeNumBox.getValue() + 1);
                        }
                    }
                }
                Entry newEntry = new Entry(dateBox.getYear(), dateBox.getMonth(), dateBox.getDay(), (int) tradeNumBox.getValue(), (char) BSBox.getSelectedItem(), (int) quantityBox.getValue(), (double) priceBox.getValue(), (double) commissionBox.getValue());
                switch (title) {
                    case "Edit / View Entry":
                        stocks.get(currentStockIndex).editEntry(currentEntrieIndex, newEntry);
                        break;
                    case "New Entry":
                        stocks.get(currentStockIndex).addEntry(newEntry);
                        break;
                }
                Stock currentStock = stocks.get(currentStockIndex);
                for (int i = 0; i < stocks.get(currentStockIndex).getEntries().size(); i++) {
                    if (currentStock.getEntries().get(i).compareTo(newEntry) == 0) {
                        stockFrame.entriesTable.setRowSelectionInterval(0, i + 1);
                    }
                }
            }
        }
    }
}
