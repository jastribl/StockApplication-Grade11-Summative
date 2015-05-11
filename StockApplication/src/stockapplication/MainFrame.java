package stockapplication;

import java.awt.event.*;
import javax.swing.*;
import static stockapplication.StockApplication.addStockFrame;
import static stockapplication.StockApplication.confirmWindow;
import static stockapplication.StockApplication.reportOptionsFrame;
import static stockapplication.StockApplication.stockFrame;
import static stockapplication.StockApplication.stocks;
import static stockapplication.StockApplication.warningWindow;
import static stockapplication.StockApplication.helpFrame;

public class MainFrame extends JFrame {

    public final DefaultListModel mainListModel = new DefaultListModel();
    public final JList mainList = new JList(mainListModel);

    public MainFrame() {
        mainList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainList.setVisibleRowCount(20);
        mainList.setPrototypeCellValue("                                                                     ");
        JScrollPane mainScrollPane = new JScrollPane(mainList);
        mainList.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                int key = ke.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    stockFrame.display(mainList.getSelectedIndex());
                } else if (key == KeyEvent.VK_DELETE) {
                    removeStock();
                } else if (key == KeyEvent.VK_N && ke.isControlDown()) {
                    addStockFrame.display(true);
                } else if (key == KeyEvent.VK_R && ke.isControlDown()) {
                    reportOptionsFrame.displayForQuickCapitalGainsReport("ALL", true);
                } else if (key == KeyEvent.VK_T && ke.isControlDown()) {
                    reportOptionsFrame.displayForTaxReport(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });
        mainScrollPane.setWheelScrollingEnabled(true);
        setTitle("Stock Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        JButton mainAddButton = new JButton("New Stock");
        mainAddButton.setFocusable(false);
        JButton mainRemoveButton = new JButton("Remove Stock");
        mainRemoveButton.setFocusable(false);
        JButton mainFunctionButton = new JButton("Edit / View Stock");
        mainFunctionButton.setFocusable(false);
        add(mainPanel);
        mainPanel.add(mainScrollPane);
        mainPanel.add(mainAddButton);
        mainPanel.add(mainRemoveButton);
        mainPanel.add(mainFunctionButton);
        JMenuBar mainMenuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
        mainMenuBar.add(fileMenu);
        JMenu reportMenu = new JMenu("Reports");
        JMenuItem fullReportItem = new JMenuItem("Full Report");
        JMenuItem taxReportItem = new JMenuItem("Tax Report");
        reportMenu.add(fullReportItem);
        reportMenu.add(taxReportItem);
        mainMenuBar.add(reportMenu);
        JMenu helpMenu = new JMenu("Help");
        JMenuItem stockHelpItem = new JMenuItem("Stock Help");
        helpMenu.add(stockHelpItem);
        mainMenuBar.add(helpMenu);
        setJMenuBar(mainMenuBar);
        mainAddButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                addStockFrame.display(true);
            }
        });
        mainRemoveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                removeStock();
            }
        });
        mainFunctionButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                stockFrame.display(mainList.getSelectedIndex());
            }
        });
        exitItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
        fullReportItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                reportOptionsFrame.displayForQuickCapitalGainsReport("ALL", true);
            }
        });
        taxReportItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                reportOptionsFrame.displayForTaxReport(true);
            }
        });
        stockHelpItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                helpFrame.display(0);
            }
        });
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public final void display() {
        refreshMainFrame();
        setVisible(true);
    }

    private void refreshMainFrame() {
        mainListModel.clear();
        for (Stock stock : stocks) {
            mainListModel.addElement(stock.getName());
        }
    }

    public final boolean addStock(String name, int totalStocks, double ACBTotal) {
        if (!mainListModel.contains(name)) {
            if (!name.matches("")) {
                stocks.addStock(new Stock(name, totalStocks, ACBTotal));
                refreshMainFrame();
                mainList.setSelectedValue(name, true);
                return true;
            } else {
                warningWindow.displayWarning("You must provide the Stock Symbol!");
            }
        } else {
            warningWindow.displayWarning("You cannot enter this stock again!");
        }
        return false;
    }

    public final void removeStock() {
        if (mainList.getSelectedIndex() >= 0) {
            String name = mainList.getSelectedValue().toString();
            if (confirmWindow.displayConfirmation("Are you sure you would like to remove the stock \"" + name + "\"")) {
                int index = mainList.getSelectedIndex();
                mainListModel.removeElementAt(index);
                stocks.removeStockAt(index);
                if (index >= mainListModel.size()) {
                    index--;
                }
                mainList.setSelectedIndex(index);
            }
        } else {
            warningWindow.displayWarning("You have not selected anything!");
        }
    }
}
