package stockapplication;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
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
import static stockapplication.StockApplication.listener;
import static stockapplication.StockApplication.reportOptionsFrame;
import static stockapplication.StockApplication.stockFrame;
import static stockapplication.StockApplication.warningWindow;

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
                    stockFrame.display();
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
        JMenuItem entryHelpItem = new JMenuItem("Entry Help");
        helpMenu.add(stockHelpItem);
        helpMenu.add(entryHelpItem);
        mainMenuBar.add(helpMenu);
        setJMenuBar(mainMenuBar);
        mainAddButton.addActionListener((ActionListener) listener);
        mainRemoveButton.addActionListener((ActionListener) listener);
        mainFunctionButton.addActionListener((ActionListener) listener);
        exitItem.addActionListener((ActionListener) listener);
        fullReportItem.addActionListener((ActionListener) listener);
        stockHelpItem.addActionListener((ActionListener) listener);
        entryHelpItem.addActionListener((ActionListener) listener);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public boolean addStock(String name, int totalStocks, double ACBTotal) {
        try {
            if (!mainListModel.contains(name)) {
                if (!name.matches("")) {
                    mainListModel.addElement(name);
                    sortSaveStocks();
                    File stockFile = new File(name + ".txt");
                    double ACBPerUnit = 0;
                    if (totalStocks != 0) {
                        ACBPerUnit = ACBTotal / totalStocks;
                    }
                    try (PrintWriter stockWriter = new PrintWriter(new FileWriter(stockFile))) {
                        stockWriter.print("- - - - - - - - " + totalStocks + " " + ACBPerUnit + " " + ACBTotal + " - ");
                    }
                    mainList.setSelectedValue(name, true);
                    return true;
                } else {
                    warningWindow.displayWarning("You must provide the Stock Symbol!", 0);
                }
            } else {
                warningWindow.displayWarning("You cannot enter this stock again!", 0);
            }
        } catch (IOException ex) {
        }
        return false;
    }

    public void removeStock() {
        try {
            if (mainList.getSelectedIndex() >= 0) {
                if (confirmWindow.displayConfirmation("Are you sure you would like to remove the stock \"" + mainList.getSelectedValue() + "\"")) {
                    int index = mainList.getSelectedIndex();
                    String name = mainList.getSelectedValue().toString();
                    mainListModel.removeElementAt(index);
                    sortSaveStocks();
                    if (index >= mainListModel.size()) {
                        index--;
                    }
                    mainList.setSelectedIndex(index);
                    File trash = new File("trash");
                    if (!trash.exists()) {
                        trash.mkdir();
                    }
                    File oldFile = new File(name + ".txt");
                    File newFile = new File("trash/" + name + "(trash).txt");
                    int nameShifter = 0;
                    while (newFile.isFile()) {
                        newFile = new File("trash/" + name + String.valueOf(nameShifter++) + "(trash).txt");
                    }
                    PrintWriter stockWriter;
                    try (BufferedReader stockReader = new BufferedReader(new FileReader(oldFile))) {
                        stockWriter = new PrintWriter(new FileWriter(newFile));
                        while (stockReader.ready()) {
                            stockWriter.println(stockReader.readLine());
                        }
                    }
                    stockWriter.close();
                    oldFile.delete();
                }
            } else {
                warningWindow.displayWarning("You have not selected anything!", 0);
            }
        } catch (IOException ex) {
        }
    }

    public void sortSaveStocks() {
        String[] allStocks = new String[mainListModel.size()];
        for (int i = 0; i < mainListModel.size(); i++) {
            allStocks[i] = mainListModel.elementAt(i).toString();
        }
        Arrays.sort(allStocks);
        mainListModel.removeAllElements();
        try (PrintWriter stockWriter = new PrintWriter(new FileWriter("stocks.txt"))) {
            for (int i = 0; i < allStocks.length; i++) {
                mainListModel.addElement(allStocks[i]);
                stockWriter.print(mainListModel.elementAt(i).toString() + "\n");
            }
        } catch (IOException ex) {
        }
    }
}
