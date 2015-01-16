package stockapplication;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static stockapplication.StockApplication.listener;
import static stockapplication.StockApplication.mainFrame;

public class AddStockFrame extends JFrame {

    public final JTextField addStockField = new JTextField();
    public final JTextField addStockNumField = new JTextField();
    public final JTextField addStockAdjCostBaseField = new JTextField();

    public AddStockFrame() {
        setTitle("New Stock");
        JPanel addStockPanel = new JPanel();
        JButton addStockButton = new JButton("Add Stock");
        addStockPanel.setLayout(new GridLayout(2, 4));
        addStockPanel.add(new JLabel("Stock Symbol"));
        addStockPanel.add(new JLabel("Num of Existing Stocks"));
        addStockPanel.add(new JLabel("Adjusted Cost Base (total) "));
        addStockPanel.add(new JLabel(""));
        addStockPanel.add(addStockField);
        addStockPanel.add(addStockNumField);
        addStockPanel.add(addStockAdjCostBaseField);
        addStockPanel.add(addStockButton);
        add(addStockPanel);
        addStockButton.addActionListener(listener);
        setResizable(false);
        pack();
        setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - getSize().height) / 2));
    }

    public void display() {
        addStockField.setText("");
        addStockNumField.setText("0");
        addStockAdjCostBaseField.setText("0.00");
        addStockField.requestFocus();
        setVisible(true);
    }
}
