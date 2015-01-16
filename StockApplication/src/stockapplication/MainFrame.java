package stockapplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
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
            boolean ENTERisDown = false, DELETEisDown = false, CTRNisDown = false, CTRRisDown = false;

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                int key = ke.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    ENTERisDown = true;
                } else if (key == KeyEvent.VK_DELETE) {
                    DELETEisDown = true;
                } else if (key == KeyEvent.VK_N && ke.getModifiers() == KeyEvent.CTRL_MASK) {
                    CTRNisDown = true;
                } else if (key == KeyEvent.VK_R && ke.getModifiers() == KeyEvent.CTRL_MASK) {
                    CTRRisDown = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                int key = ke.getKeyCode();
                if (key == KeyEvent.VK_ENTER && ENTERisDown) {
                    stockFrame.display(mainList.getSelectedIndex());
                    ENTERisDown = false;
                } else if (key == KeyEvent.VK_DELETE && DELETEisDown) {
                    removeStock();
                    DELETEisDown = false;
                } else if (key == KeyEvent.VK_N && CTRNisDown) {
                    addStockFrame.display(true);
                    CTRNisDown = false;
                } else if (key == KeyEvent.VK_R && CTRRisDown) {
                    reportOptionsFrame.display("Full", true);
                    CTRRisDown = false;
                }
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
        reportMenu.add(fullReportItem);
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
                reportOptionsFrame.display("Full", true);
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

    public boolean addStock(String name, int totalStocks, double ACBTotal) {
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

    public void removeStock() {
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
