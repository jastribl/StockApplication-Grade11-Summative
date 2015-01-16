package stockapplication;

import java.awt.Dimension;
import java.awt.Toolkit;
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
import static stockapplication.StockApplication.listener;
import static stockapplication.StockApplication.warningFrame;

public class MainFrame extends JFrame {

    public final DefaultListModel mainListModel = new DefaultListModel();
    public final JList mainList = new JList(mainListModel);

    public MainFrame() {
        mainList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainList.setVisibleRowCount(20);
        mainList.setPrototypeCellValue("                                                                     ");
        JScrollPane mainScrollPane = new JScrollPane(mainList);
        mainScrollPane.setWheelScrollingEnabled(true);
        setTitle("Stock Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        JButton mainAddButton = new JButton("New Stock");
        JButton mainRemoveButton = new JButton("Remove Stock");
        JButton mainFunctionButton = new JButton("Edit / View Stock");
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
        mainAddButton.addActionListener(listener);
        mainRemoveButton.addActionListener(listener);
        mainFunctionButton.addActionListener(listener);
        exitItem.addActionListener(listener);
        fullReportItem.addActionListener(listener);
        stockHelpItem.addActionListener(listener);
        entryHelpItem.addActionListener(listener);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(((int) screenSize.getWidth() - getSize().width) / 2, ((int) screenSize.getHeight() - getSize().height) / 2);
        setResizable(false);
    }

    public void addStock(String name, String totalStocks, String ACBTotal) {
        try {
            if (!mainListModel.contains(name)) {
                if (!name.matches("")) {
                    if (!name.contains(" ")) {
                        if (Integer.valueOf(totalStocks) >= 0) {
                            if (Double.valueOf(ACBTotal) >= 0) {
                                addStockFrame.setVisible(false);
                                mainListModel.addElement(name);
                                sortSaveStocks();
                                File stockFile = new File(name + ".txt");
                                String ACBPerUnit = "0";
                                if (Integer.valueOf(totalStocks) != 0) {
                                    ACBPerUnit = String.valueOf(Double.valueOf(ACBTotal) / Double.valueOf(totalStocks));
                                }
                                try (PrintWriter stockWriter = new PrintWriter(new FileWriter(stockFile))) {
                                    stockWriter.print("- - - - - - - - " + totalStocks + " " + ACBPerUnit + " " + ACBTotal + " - ");
                                }
                            } else {
                                warningFrame.displayWarning("\"Adjusted Cost Base (total)\" must be positive!");
                            }
                        } else {
                            warningFrame.displayWarning("\"Num Of Existing Stocks\" must be positive!");
                        }
                    } else {
                        warningFrame.displayWarning("\"Stock Symbol\" cannot contain spaces!");
                    }
                } else {
                    warningFrame.displayWarning("You must provide the Stock Symbol!");
                }
            } else {
                warningFrame.displayWarning("You cannot enter this stock again!");
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
        } catch (IOException ex) {
        }
    }

    public void removeStock() {
        try {
            if (mainList.getSelectedIndex() >= 0) {
                String name = mainList.getSelectedValue().toString();
                mainListModel.removeElementAt(mainList.getSelectedIndex());
                sortSaveStocks();
                File oldFile = new File(name + ".txt");
                File newFile = new File("trash/" + name + "(trash).txt");
                int nameShifter = 0;
                while (newFile.isFile()) {
                    newFile = new File("trash/" + name + String.valueOf(nameShifter) + "(trash).txt");
                    nameShifter++;
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
            } else {
                warningFrame.displayWarning("You have not selected anything!");
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
