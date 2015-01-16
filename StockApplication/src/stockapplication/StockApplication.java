//Justin Stribling
//Date Started: Thursday, May 16, 2013
//Date Compleated: Sunday, June 9, 2013
//Purpose: An Application designed to keep track of sells and buys for individual stocks and to calculate and create Capital Gains / Losses reports
package stockapplication;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StockApplication extends JFrame implements ActionListener {

    public static StockApplication mainFrame = new StockApplication();
    public final static DefaultListModel mainListModel = new DefaultListModel();
    public final static JList mainList = new JList(mainListModel);
    //--------------------------------------------------------------------------------------------
    public static StockApplication stockFrame = new StockApplication();
    public final static String data[][] = {{"", "", "", "", "", "", "", "", "", "", "", ""}};
    public final static String[] headers = {"Year", "Month", "Day", "Trade #", "Buy / Sell", "Quantity", "Price", "Commission", "Total Shares", "ACB (Per Unit)", "ACB (Total)", "Capital Gains / Losses"};
    public final static DefaultTableModel entriesModel = new DefaultTableModel(data, headers) {
        @Override
        public Class<?> getColumnClass(int column) {
            return getValueAt(0, column).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    public final static JTable entriesTable = new JTable(entriesModel);
    //--------------------------------------------------------------------------------------------
    public static StockApplication addStockFrame = new StockApplication();
    public static JTextField addStockField = new JTextField();
    public static JTextField addStockNumField = new JTextField();
    public static JTextField addStockAdjCostBaseField = new JTextField();
    //--------------------------------------------------------------------------------------------
    public static StockApplication addEntryFrame = new StockApplication();
    public static JTextField addEntryBoxes[] = new JTextField[8];
    public static JButton addEntryButton = new JButton("Add Entry");
    //--------------------------------------------------------------------------------------------
    public static StockApplication editStartingValuesFrame = new StockApplication();
    public static JTextField editStartingNumField = new JTextField();
    public static JTextField editStartingACBTotalField = new JTextField();
    //--------------------------------------------------------------------------------------------
    public static StockApplication warningFrame = new StockApplication();
    public static JLabel warningLabel = new JLabel();
    //--------------------------------------------------------------------------------------------
    public static StockApplication reportFrame = new StockApplication();
    public final static String reportData[][] = {{"", "", "", "", ""}};
    public final static String[] reportHeaders = {"Stock", "Year", "Month", "Day", "Capital Gains / Losses"};
    public final static DefaultTableModel reportModel = new DefaultTableModel(reportData, reportHeaders) {
        @Override
        public Class<?> getColumnClass(int column) {
            return getValueAt(0, column).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    public final static JTable reportTable = new JTable(reportModel);
    //--------------------------------------------------------------------------------------------
    public static StockApplication reportOptionsFrame = new StockApplication();
    public static JButton reportOptionsButton1 = new JButton("Get Full - Range");
    public static JButton reportOptionsButton2 = new JButton("Get Stock - Range");
    public static JTextField reportOptionsStartYearField = new JTextField("");
    public static JTextField reportOptionsStartMonthField = new JTextField("");
    public static JTextField reportOptionsStartDayField = new JTextField("");
    public static JTextField reportOptionsEndYearField = new JTextField("");
    public static JTextField reportOptionsEndMonthField = new JTextField("");
    public static JTextField reportOptionsEndDayField = new JTextField("");
    //--------------------------------------------------------------------------------------------
    public static StockApplication helpFrame = new StockApplication();
    public static JTextArea helpField = new JTextArea();

    //Calls all the set-up methods
    public static void main(String[] args) throws FileNotFoundException, ParseException, IOException {
        makeMainFrame();
        setUpFromFile();
        makeStockFrame();
        makeAddStockFrame();
        makeAddEntryFrame();
        makeEditStartingValuesFrame();
        makeWarningFrame();
        makeReportFrame();
        makeReportOptionsFrame();
        makeHelpFrame();
        mainFrame.setVisible(true);
    }

    //creates the main window of the application and adds required addActionListener's
    public static void makeMainFrame() {
        mainList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainList.setVisibleRowCount(20);
        mainList.setPrototypeCellValue("                                                                     ");
        JScrollPane mainScrollPane = new JScrollPane(mainList);
        mainScrollPane.setWheelScrollingEnabled(true);
        mainFrame.setTitle("Stock Application");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        JButton mainAddButton = new JButton("New Stock");
        JButton mainRemoveButton = new JButton("Remove Stock");
        JButton mainFunctionButton = new JButton("Edit / View Stock");
        mainFrame.add(mainPanel);
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
        mainFrame.setJMenuBar(mainMenuBar);
        mainAddButton.addActionListener(mainFrame);
        mainRemoveButton.addActionListener(mainFrame);
        mainFunctionButton.addActionListener(mainFrame);
        exitItem.addActionListener(mainFrame);
        fullReportItem.addActionListener(mainFrame);
        stockHelpItem.addActionListener(mainFrame);
        entryHelpItem.addActionListener(mainFrame);
        mainFrame.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation(((int) screenSize.getWidth() - mainFrame.getSize().width) / 2, ((int) screenSize.getHeight() - mainFrame.getSize().height) / 2);
        mainFrame.setResizable(false);
    }

    //Reads the names of the stock currently in holding from the file
    //If the file does not exist, it is created
    public static void setUpFromFile() throws IOException {
        try {
            BufferedReader stockReader = new BufferedReader(new FileReader("stocks.txt"));
            while (stockReader.ready()) {
                mainListModel.addElement(stockReader.readLine());
            }
            stockReader.close();
        } catch (FileNotFoundException ex) {
            PrintWriter stockWriter = new PrintWriter(new FileWriter("stocks.txt"));
            stockWriter.print("");
            stockWriter.close();
        }
    }

    //creates the secondary window of the application and adds required addActionListener's
    public static void makeStockFrame() throws FileNotFoundException, IOException {
        entriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        entriesTable.getTableHeader().setReorderingAllowed(false);
        entriesTable.getTableHeader().setResizingAllowed(false);
        int[] widths = {34, 42, 31, 51, 59, 53, 53, 79, 77, 88, 71, 135};
        int total = 0;
        for (int i = 0; i < 12; i++) {
            entriesTable.getColumnModel().getColumn(i).setMinWidth(widths[i]);
            entriesTable.getColumnModel().getColumn(i).setMaxWidth(widths[i] + 1);
            total += widths[i];
        }
        entriesTable.setPreferredScrollableViewportSize(new Dimension(total, 373));
        JPanel stockPanel = new JPanel();
        JScrollPane entriesScrollPane = new JScrollPane(entriesTable);
        JButton stockAddButton = new JButton("New Entry");
        JButton stockRemoveButton = new JButton("Remove Entry");
        JButton stockFunctionButton = new JButton("Edit / View Entry");
        stockPanel.add(entriesScrollPane);
        stockPanel.add(stockAddButton);
        stockPanel.add(stockRemoveButton);
        stockPanel.add(stockFunctionButton);
        JMenu reportMenu = new JMenu("Reports");
        JMenuItem indevidualReportItem = new JMenuItem("Report This Stock");
        reportMenu.add(indevidualReportItem);
        JMenuBar stockMenuBar = new JMenuBar();
        stockMenuBar.add(reportMenu);
        JMenu helpMenu = new JMenu("Help");
        JMenuItem stockHelpItem = new JMenuItem("Stock Help");
        JMenuItem entryHelpItem = new JMenuItem("Entry Help");
        helpMenu.add(stockHelpItem);
        helpMenu.add(entryHelpItem);
        stockMenuBar.add(helpMenu);
        stockFrame.setJMenuBar(stockMenuBar);
        indevidualReportItem.addActionListener(stockFrame);
        stockFrame.add(stockPanel);
        stockAddButton.addActionListener(stockFrame);
        stockRemoveButton.addActionListener(stockFrame);
        stockFunctionButton.addActionListener(stockFrame);
        stockHelpItem.addActionListener(mainFrame);
        entryHelpItem.addActionListener(mainFrame);
        stockFrame.setResizable(false);
    }

    //creates the Add Stock window of the application and adds required addActionListener's
    public static void makeAddStockFrame() throws ParseException {
        addStockFrame.setTitle("New Stock");
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
        addStockFrame.add(addStockPanel);
        addStockButton.addActionListener(addStockFrame);
        addStockFrame.setResizable(false);
        addStockFrame.pack();
        addStockFrame.setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - addStockFrame.getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - addStockFrame.getSize().height) / 2));
    }

    //creates the Add Entry window of the application and adds required addActionListener's
    public static void makeAddEntryFrame() throws ParseException {
        JPanel addEntryPanel = new JPanel();
        addEntryPanel.setLayout(new GridLayout(2, 8));
        addEntryPanel.add(new JLabel("Year"));
        addEntryPanel.add(new JLabel("Month"));
        addEntryPanel.add(new JLabel("Day"));
        addEntryPanel.add(new JLabel("Trade #"));
        addEntryPanel.add(new JLabel("Buy / Sell"));
        addEntryPanel.add(new JLabel("Quantity"));
        addEntryPanel.add(new JLabel("Price"));
        addEntryPanel.add(new JLabel("Commission"));
        addEntryPanel.add(new JLabel(""));
        for (int i = 0; i < 8; i++) {
            addEntryBoxes[i] = new JTextField();
            addEntryPanel.add(addEntryBoxes[i]);
        }
        addEntryPanel.add(addEntryButton);
        addEntryFrame.add(addEntryPanel);
        addEntryButton.addActionListener(addEntryFrame);
        addEntryFrame.setResizable(false);
        addEntryFrame.pack();
        addEntryFrame.setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - addEntryFrame.getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - addEntryFrame.getSize().height) / 2));
    }

    //creates the Edit Starting Values window of the application and adds required addActionListener's
    public static void makeEditStartingValuesFrame() {
        editStartingValuesFrame.setTitle("Edit Starting Values");
        JPanel editStartingValuesPanel = new JPanel();
        JButton editStartingValuesButton = new JButton("Apply");
        editStartingValuesPanel.setLayout(new GridLayout(2, 3));
        editStartingValuesPanel.add(new JLabel("Num Of Existing Stocks"));
        editStartingValuesPanel.add(new JLabel("Adjusted Cost Base (total)"));
        editStartingValuesPanel.add(new JLabel(""));
        editStartingValuesPanel.add(editStartingNumField);
        editStartingValuesPanel.add(editStartingACBTotalField);
        editStartingValuesPanel.add(editStartingValuesButton);
        editStartingValuesFrame.add(editStartingValuesPanel);
        editStartingValuesButton.addActionListener(editStartingValuesFrame);
        editStartingValuesFrame.setResizable(false);
        editStartingValuesFrame.pack();
        editStartingValuesFrame.setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - editStartingValuesFrame.getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - editStartingValuesFrame.getSize().height) / 2));
    }

    //creates the Warning window of the application and adds required addActionListener's
    public static void makeWarningFrame() {
        warningFrame.setTitle("Error / Warning");
        JPanel warningPanel = new JPanel();
        JButton warningButton = new JButton("OK");
        warningPanel.add(warningLabel);
        warningPanel.add(warningButton);
        warningFrame.add(warningPanel);
        warningButton.addActionListener(warningFrame);
        warningFrame.setResizable(false);
    }

    //creates the Capital Gains / Losses window of the application and adds required addActionListener's
    public static void makeReportFrame() {
        reportTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportTable.getTableHeader().setReorderingAllowed(false);
        reportTable.getTableHeader().setResizingAllowed(false);
        int[] widths = {70, 34, 42, 31, 135};
        int total = 0;
        for (int i = 0; i < 5; i++) {
            reportTable.getColumnModel().getColumn(i).setMinWidth(widths[i]);
            reportTable.getColumnModel().getColumn(i).setMaxWidth(widths[i] + 1);
            total += widths[i];
        }
        reportTable.setPreferredScrollableViewportSize(new Dimension(total, 500));
        JPanel reportPanel = new JPanel();
        JScrollPane reportScrollPane = new JScrollPane(reportTable);
        reportPanel.add(reportScrollPane);
        JMenu fileMenu = new JMenu("File");
        JMenuItem printMenuItem = new JMenuItem("Print");
        fileMenu.add(printMenuItem);
        JMenuBar reportMenuBar = new JMenuBar();
        reportMenuBar.add(fileMenu);
        reportFrame.setJMenuBar(reportMenuBar);
        printMenuItem.addActionListener(reportFrame);
        reportFrame.add(reportPanel);
        reportFrame.setResizable(false);
    }

    //creates the Capital Gains / Losses Options window of the application and adds required addActionListener's
    public static void makeReportOptionsFrame() {
        reportOptionsFrame.setTitle("Captital Gains Report Options");
        reportOptionsFrame.setLayout(new GridLayout(4, 4));
        JPanel reportOptionsPenel1 = new JPanel();
        reportOptionsPenel1.setLayout(new GridLayout(1, 3));
        reportOptionsPenel1.add(reportOptionsStartYearField);
        reportOptionsPenel1.add(reportOptionsStartMonthField);
        reportOptionsPenel1.add(reportOptionsStartDayField);
        JPanel reportOptionsPenel2 = new JPanel();
        reportOptionsPenel2.setLayout(new GridLayout(1, 3));
        reportOptionsPenel2.add(reportOptionsEndYearField);
        reportOptionsPenel2.add(reportOptionsEndMonthField);
        reportOptionsPenel2.add(reportOptionsEndDayField);
        reportOptionsFrame.add(new JLabel("                                  Start"));
        reportOptionsFrame.add(new JLabel("                     to"));
        reportOptionsFrame.add(new JLabel("End"));
        reportOptionsFrame.add(reportOptionsButton1);
        for (int i = 0; i < 4; i++) {
            reportOptionsFrame.add(new JLabel("----------------------------------"));
        }
        for (int i = 0; i < 2; i++) {
            reportOptionsFrame.add(new JLabel("Year       Month    Day"));
            reportOptionsFrame.add(new JLabel(""));
        }
        reportOptionsFrame.add(reportOptionsPenel1);
        reportOptionsFrame.add(new JLabel("                     to"));
        reportOptionsFrame.add(reportOptionsPenel2);
        reportOptionsFrame.add(reportOptionsButton2);
        reportOptionsButton1.addActionListener(reportOptionsFrame);
        reportOptionsButton2.addActionListener(reportOptionsFrame);
        reportOptionsFrame.setResizable(false);
    }

    //creates the Help window of the application and adds required addActionListener's
    public static void makeHelpFrame() {
        JPanel helpPanel = new JPanel();
        helpFrame.add(helpPanel);
        helpField.setRows(30);
        helpField.setColumns(60);
        helpField.setSize(600, WIDTH);
        helpField.setEditable(false);
        helpField.setLineWrap(true);
        helpField.setTabSize(3);
        helpField.setWrapStyleWord(true);
        JScrollPane helpScrollPane = new JScrollPane(helpField);
        helpScrollPane.setWheelScrollingEnabled(true);
        helpPanel.add(helpScrollPane);
        helpFrame.pack();
        helpFrame.setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - helpFrame.getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - helpFrame.getSize().height) / 2));
        helpFrame.setResizable(false);
    }

    //Displays the Warning window with a given message
    public static void displayWarning(String message) {
        warningLabel.setText(message);
        warningFrame.pack();
        warningFrame.setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - warningFrame.getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - warningFrame.getSize().height) / 2));
        warningFrame.setVisible(true);
    }

    //Adds a new stock to the mainStockList and creates a file for the stock with the starting values in it
    //Only does so if there are no problems with the fields
    //Displays warnings if any problems occur
    public static void addStock(String name, String totalStocks, String ACBTotal) throws IOException {
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
                                PrintWriter stockWriter = new PrintWriter(new FileWriter(stockFile));
                                stockWriter.print("- - - - - - - - " + totalStocks + " " + ACBPerUnit + " " + ACBTotal + " - ");
                                stockWriter.close();
                            } else {
                                displayWarning("\"Adjusted Cost Base (total)\" must be positive!");
                            }
                        } else {
                            displayWarning("\"Num Of Existing Stocks\" must be positive!");
                        }
                    } else {
                        displayWarning("\"Stock Symbol\" cannot contain spaces!");
                    }
                } else {
                    displayWarning("You must provide the Stock Symbol!");
                }
            } else {
                displayWarning("You cannot enter this stock again!");
            }
        } catch (NumberFormatException ex) {
            if (ex.toString().contains("empty String") || ex.toString().contains("For input string: \"\"")) {
                displayWarning("Empty Entry(s)...Please Check All Fields!");
            } else if (ex.toString().contains("multiple points")) {
                displayWarning("Entries can only contain one decimal point!");
            } else if (ex.toString().contains("For input string:")) {
                displayWarning("Invalid Character(s)... [" + ex.toString().substring(52, ex.toString().length() - 1) + "]  ...Please Review your Entries!");
            } else {
                displayWarning("Unknown error.  Please review your entries!");
            }
        }
    }

    //Removes a selected stock from the mainStockList and moves the stock's file to the trash folder within the program 
    public static void removeStock(String name) throws IOException {
        mainListModel.removeElementAt(mainList.getSelectedIndex());
        sortSaveStocks();
        File oldFile = new File(name + ".txt");
        File newFile = new File("trash/" + name + "(trash).txt");
        int nameShifter = 0;
        while (newFile.isFile()) {
            newFile = new File("trash/" + name + String.valueOf(nameShifter) + "(trash).txt");
            nameShifter++;
        }
        BufferedReader stockReader = new BufferedReader(new FileReader(oldFile));
        PrintWriter stockWriter = new PrintWriter(new FileWriter(newFile));
        while (stockReader.ready()) {
            stockWriter.println(stockReader.readLine());
        }
        stockReader.close();
        stockWriter.close();
        oldFile.delete();
    }

    //Checks the fields required to Add or Edit an entry within an individual stock
    //If "Add" is sent, it adds the stock
    //If "Edit" is sent, it replaces the entry with the updated fields
    public static void checkAddEditEntrys(String kind) throws IOException {
        try {
            Date date = new Date();
            if (Integer.valueOf(addEntryBoxes[0].getText()) >= 2000 && Integer.valueOf(addEntryBoxes[0].getText()) <= date.getYear() + 1900) {
                if (Integer.valueOf(addEntryBoxes[1].getText()) >= 1 && Integer.valueOf(addEntryBoxes[1].getText()) <= 12) {
                    if (!addEntryBoxes[1].getText().substring(0, 1).equals("0")) {
                        if (Integer.valueOf(addEntryBoxes[2].getText()) >= 1 && Integer.valueOf(addEntryBoxes[2].getText()) <= 31) {
                            if (!addEntryBoxes[2].getText().substring(0, 1).equals("0")) {
                                if (Integer.valueOf(addEntryBoxes[3].getText()) >= 1) {
                                    if (addEntryBoxes[4].getText().toLowerCase().matches("b") || addEntryBoxes[4].getText().toLowerCase().matches("s")) {
                                        if (Integer.valueOf(addEntryBoxes[5].getText()) > 0) {
                                            if (Double.valueOf(addEntryBoxes[6].getText()) > 0) {
                                                if (Double.valueOf(addEntryBoxes[7].getText()) >= 0) {
                                                    String all[][] = getNumsFromTable();
                                                    boolean duplicate = false;
                                                    for (int i = 1; i < all.length; i++) {
                                                        if (kind.equals("add")) {
                                                            if (all[i][0].equals(addEntryBoxes[0].getText()) && all[i][1].equals(addEntryBoxes[1].getText()) && all[i][2].equals(addEntryBoxes[2].getText()) && all[i][3].equals(addEntryBoxes[3].getText())) {
                                                                duplicate = true;
                                                            }
                                                        } else if (kind.equals("edit")) {
                                                            if (all[i][0].equals(addEntryBoxes[0].getText()) && all[i][1].equals(addEntryBoxes[1].getText()) && all[i][2].equals(addEntryBoxes[2].getText()) && all[i][3].equals(addEntryBoxes[3].getText()) && i != entriesTable.getSelectedRow()) {
                                                                duplicate = true;
                                                            }
                                                        }
                                                    }
                                                    if (duplicate == false) {
                                                        addEntryFrame.setVisible(false);
                                                        if (kind.equals("edit")) {
                                                            entriesModel.removeRow(entriesTable.getSelectedRow());

                                                        }
                                                        entriesModel.addRow(new String[]{addEntryBoxes[0].getText(), addEntryBoxes[1].getText(), addEntryBoxes[2].getText(), addEntryBoxes[3].getText(), addEntryBoxes[4].getText().toUpperCase(), addEntryBoxes[5].getText(), addEntryBoxes[6].getText(), addEntryBoxes[7].getText(), "-", "-", "-", "-"});
                                                        sortEntries();
                                                        calculateEntries();
                                                        saveEntries();
                                                    } else {
                                                        displayWarning("Duplicate \"Trade #\".  Please review your entries!");
                                                    }
                                                } else {
                                                    displayWarning("\"Commission\" out of range.  Please review your entries!");
                                                }
                                            } else {
                                                displayWarning("\"Price\" out of range.  Please review your entries!");
                                            }
                                        } else {
                                            displayWarning("\"Quantity\" out of range.  Please review your entries!");
                                        }
                                    } else {
                                        displayWarning("Invalid character(s) in \"Buy / Sell\".  Please review your entries!");
                                    }
                                } else {
                                    displayWarning("\"Trade #\" out of range.  Please review your entries!");
                                }
                            } else {
                                displayWarning("No leading Zero's in \"Day\".  Please review your entries!");
                            }
                        } else {
                            displayWarning("\"Day\" out of range.  Please review your entries!");
                        }
                    } else {
                        displayWarning("No leading Zero's in \"Month\".  Please review your entries!");
                    }
                } else {
                    displayWarning("\"Month\" out of range.  Please review your entries!");
                }
            } else {
                displayWarning("\"Year\" out of range.  Please review your entries!");
            }
        } catch (NumberFormatException ex) {
            if (ex.toString().contains("empty String") || ex.toString().contains("For input string: \"\"")) {
                displayWarning("Empty Entry(s)...Please Check All Fields!");
            } else if (ex.toString().contains("multiple points")) {
                displayWarning("Entries can only contain one decimal point!");
            } else if (ex.toString().contains("For input string:")) {
                displayWarning("Invalid Character(s)... [" + ex.toString().substring(52, ex.toString().length() - 1) + "]  ...Please Review your Entries!");
            } else {
                displayWarning("Unknown error.  Please review your entries!");
            }
        }
    }

    //Removes a selected entry from the table and calulates and saves the entries
    public static void removeEntry() throws IOException {
        entriesModel.removeRow(entriesTable.getSelectedRow());
        calculateEntries();
        saveEntries();
    }

    //Edits the starting valuse of a stock and calulates and saves the entries
    //Only does so if all fields are correct
    public static void editStartingValues() throws IOException {
        try {
            if (Integer.valueOf(editStartingNumField.getText()) >= 0) {
                if (Double.valueOf(editStartingACBTotalField.getText()) >= 0) {
                    editStartingValuesFrame.setVisible(false);
                    String ACBPerUnit;
                    if (Integer.valueOf(editStartingNumField.getText()) == 0) {
                        ACBPerUnit = "0";
                    } else {
                        ACBPerUnit = String.valueOf(Double.valueOf(editStartingACBTotalField.getText()) / Double.valueOf(editStartingNumField.getText()));
                    }
                    String all[][] = getNumsFromTable();
                    clearTable();
                    entriesModel.addRow(new String[]{"-", "-", "-", "-", "-", "-", "-", "-", editStartingNumField.getText(), ACBPerUnit, editStartingACBTotalField.getText(), "-"});
                    for (int i = 1; i < all.length; i++) {
                        entriesModel.addRow(all[i]);
                    }
                    sortEntries();
                    calculateEntries();
                    saveEntries();
                } else {
                    displayWarning("Invalid character(s) in \"Adjusted Cost Base (total)\".  Please review your entries!");
                }
            } else {
                displayWarning("Invalid Character(s) in \"Num Of Existing Stocks\".  Please review your entries!");
            }
        } catch (NumberFormatException ex) {
            if (ex.toString().contains("empty String") || ex.toString().contains("For input string: \"\"")) {
                displayWarning("Empty Entry(s)...Please Check All Fields!");
            } else if (ex.toString().contains("multiple points")) {
                displayWarning("Entries can only contain one decimal point!");
            } else if (ex.toString().contains("For input string:")) {
                displayWarning("Invalid Character(s)... [" + ex.toString().substring(52, ex.toString().length() - 1) + "]  ...Please Review your Entries!");
            } else {
                displayWarning("Unknown error.  Please review your entries!");
            }
        }
    }

    //Sorts and saves the main lost of stocks to the file
    public static void sortSaveStocks() throws IOException {
        String[] allStocks = new String[mainListModel.size()];
        for (int i = 0; i < mainListModel.size(); i++) {
            allStocks[i] = mainListModel.elementAt(i).toString();
        }
        Arrays.sort(allStocks, String.CASE_INSENSITIVE_ORDER);
        mainListModel.removeAllElements();
        PrintWriter stockWriter = new PrintWriter(new FileWriter("stocks.txt"));
        for (int i = 0; i < allStocks.length; i++) {
            mainListModel.addElement(allStocks[i]);
            stockWriter.print(mainListModel.elementAt(i).toString() + "\n");
        }
        stockWriter.close();
    }

    //Sorts the entries for an individual stock
    public static void sortEntries() {
        if (entriesModel.getRowCount() > 1) {
            String all[][] = getNumsFromTable();
            Date date = new Date();
            int minYear = Integer.valueOf(all[1][0]);
            int maxDayNum = 1;
            for (int i = 1; i < all.length; i++) {
                if (Integer.valueOf(all[i][0]) < minYear) {
                    minYear = Integer.valueOf(all[i][0]);
                }
                if (Integer.valueOf(all[i][3]) > maxDayNum) {
                    maxDayNum = Integer.valueOf(all[i][3]);
                }
            }
            clearTable();
            entriesModel.addRow(all[0]);
            for (int year = minYear; year < (date.getYear() + 1910); year++) {
                for (int month = 0; month < 13; month++) {
                    for (int day = 0; day < 33; day++) {
                        for (int num = 0; num <= maxDayNum; num++) {
                            for (int entry = 1; entry < all.length; entry++) {
                                if (all[entry][0].matches(String.valueOf(year))) {
                                    if (all[entry][1].matches(String.valueOf(month))) {
                                        if (all[entry][2].matches(String.valueOf(day))) {
                                            if (all[entry][3].matches(String.valueOf(num))) {
                                                entriesModel.addRow(all[entry]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Calculates the entries for an individual stock
    public static void calculateEntries() {
        if (entriesModel.getRowCount() > 1) {
            String[][] all = getNumsFromTable();
            ArrayList badRows = new ArrayList();
            for (int i = 1; i < all.length; i++) {
                String temp = all[i][4].toUpperCase();
                if (temp.matches("B")) {
                    all[i][8] = String.valueOf(Integer.valueOf(all[i - 1][8]) + Integer.valueOf(all[i][5]));
                    all[i][10] = String.valueOf(Double.valueOf(all[i - 1][10]) + (Double.valueOf(all[i][6]) * Double.valueOf(all[i][5])) + Double.valueOf(all[i][7]));
                    all[i][9] = String.valueOf(Double.valueOf(all[i][10]) / Double.valueOf(all[i][8]));
                } else if (temp.matches("S")) {
                    all[i][8] = String.valueOf(Integer.valueOf(all[i - 1][8]) - Integer.valueOf(all[i][5]));
                    if (Integer.valueOf(all[i][8]) < 0) {
                        displayWarning("You cannot sell stocks that you do not have.  Please review your entries!");
                        badRows.add(i);
                    }
                    if (Integer.valueOf(all[i][8]) == 0) {
                        all[i][9] = "0";
                        all[i][10] = "0";
                    } else {
                        all[i][10] = String.valueOf(Double.valueOf(all[i - 1][10]) - (Double.valueOf(all[i][5]) * Double.valueOf(all[i - 1][10]) / Double.valueOf(all[i - 1][8])));
                        all[i][9] = String.valueOf(Double.valueOf(all[i][10]) / Integer.valueOf(all[i][8]));
                    }
                    all[i][11] = String.valueOf(((Double.valueOf(all[i][6]) * Double.valueOf(all[i][5])) - Double.valueOf(all[i][7])) - (Double.valueOf(all[i - 1][9]) * Double.valueOf(all[i][5])));
                }
            }
            allToTable(all);
            for (int i = 0; i < badRows.size(); i++) {
                entriesTable.setRowSelectionInterval(Integer.valueOf(badRows.get(0).toString()), Integer.valueOf(badRows.get(i).toString()));
                break;
            }
        }
    }

    //Saves the entries for an individual stock
    public static void saveEntries() throws IOException {
        PrintWriter stockWriter = new PrintWriter(new FileWriter(stockFrame.getTitle() + ".txt"));
        for (int i = 0; i < entriesTable.getRowCount(); i++) {
            for (int j = 0; j < 12; j++) {
                stockWriter.print(entriesTable.getValueAt(i, j).toString() + " ");
            }
            stockWriter.println();
        }
        stockWriter.close();
    }

    //Returns a 2D Array of all the entries in the table for an individual stock
    public static String[][] getNumsFromTable() {
        String[][] all = new String[entriesModel.getRowCount()][12];
        for (int i = 0; i < all.length; i++) {
            for (int j = 0; j < all[i].length; j++) {
                all[i][j] = entriesModel.getValueAt(i, j).toString();
            }
        }
        return (all);
    }

    //Takes a 2D Array and puts it into the table for an individual stock
    public static void allToTable(String all[][]) {
        clearTable();
        for (int i = 0; i < all.length; i++) {
            entriesModel.addRow(all[i]);
        }
    }

    //Clears the Entrys Table
    public static void clearTable() {
        while (entriesModel.getRowCount() > 0) {
            entriesModel.removeRow(0);
        }
    }

    //Clears the Capital Gains / Losses Table
    public static void clearTable2() {
        while (reportModel.getRowCount() > 0) {
            reportModel.removeRow(0);
        }
    }

    //Creates a Capital Gains / Losses report for all stocks, for all dates
    public static void reportAll() throws IOException {
        clearTable2();
        NumberFormat money = NumberFormat.getCurrencyInstance();
        Double totals[] = new Double[mainListModel.size()];
        Double fullTotal = 0.0;
        for (int i = 0; i < totals.length; i++) {
            totals[i] = 0.0;
        }
        for (int i = 0; i < mainListModel.size(); i++) {
            BufferedReader stockReader = new BufferedReader(new FileReader(mainListModel.get(i).toString() + ".txt"));
            reportModel.addRow(new String[]{mainListModel.get(i).toString(), "", "", "", ""});
            String stuff = stockReader.readLine();
            while (stockReader.ready()) {
                String[] temp;
                temp = stockReader.readLine().split(" ");
                if (temp[4].equals("S")) {
                    reportModel.addRow(new String[]{"", temp[0], temp[1], temp[2], money.format(Double.valueOf(temp[11]))});
                    totals[i] += Double.valueOf(money.format(Double.valueOf(temp[11])).replace("$", "").replace(",", ""));
                }
            }
            fullTotal += totals[i];
            reportModel.addRow(new String[]{});
            reportModel.addRow(new String[]{"", "", "", "", "Total: " + money.format(Double.valueOf(totals[i]))});
            reportModel.addRow(new String[]{});
            reportModel.addRow(new String[]{});
            stockReader.close();
        }
        reportModel.addRow(new String[]{});
        reportModel.addRow(new String[]{"", "", "", "", "Final Total: " + money.format(Double.valueOf(fullTotal))});
        reportFrame.pack();
        reportFrame.setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - reportFrame.getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - reportFrame.getSize().height) / 2));
        reportFrame.setVisible(true);
    }

    //Creates a Capital Gains / Losses report for all stocks, for a user defined date range
    public static void reportAll(String startYear, String startMonth, String startDay, String endYear, String endMonth, String endDay) throws IOException {
        if (checkFields(startYear, startMonth, startDay, endYear, endMonth, endDay) == true) {
            reportOptionsFrame.setVisible(false);
            clearTable2();
            NumberFormat money = NumberFormat.getCurrencyInstance();
            Double totals[] = new Double[mainListModel.size()];
            Double fullTotal = 0.0;
            for (int i = 0; i < totals.length; i++) {
                totals[i] = 0.0;
            }
            for (int i = 0; i < mainListModel.size(); i++) {
                BufferedReader stockReader = new BufferedReader(new FileReader(mainListModel.get(i).toString() + ".txt"));
                reportModel.addRow(new String[]{mainListModel.get(i).toString(), "", "", "", ""});
                String stuff = stockReader.readLine();
                while (stockReader.ready()) {
                    String[] temp;
                    temp = stockReader.readLine().split(" ");
                    if (temp[4].equals("S") && Integer.valueOf(temp[0]) >= Integer.valueOf(startYear) && Integer.valueOf(temp[0]) <= Integer.valueOf(endYear) && Integer.valueOf(temp[1]) >= Integer.valueOf(startMonth) && Integer.valueOf(temp[1]) <= Integer.valueOf(endMonth) && Integer.valueOf(temp[2]) >= Integer.valueOf(startDay) && Integer.valueOf(temp[2]) <= Integer.valueOf(endDay)) {
                        reportModel.addRow(new String[]{"", temp[0], temp[1], temp[2], money.format(Double.valueOf(temp[11]))});
                        totals[i] += Double.valueOf(money.format(Double.valueOf(temp[11])).replace("$", "").replace(",", ""));
                    }
                }
                fullTotal += totals[i];
                reportModel.addRow(new String[]{});
                reportModel.addRow(new String[]{"", "", "", "", "Total: " + money.format(Double.valueOf(totals[i]))});
                reportModel.addRow(new String[]{});
                reportModel.addRow(new String[]{});
                stockReader.close();
            }
            reportModel.addRow(new String[]{});
            reportModel.addRow(new String[]{"", "", "", "", "Final Total: " + money.format(Double.valueOf(fullTotal))});
            reportFrame.pack();
            reportFrame.setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - reportFrame.getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - reportFrame.getSize().height) / 2));
            reportFrame.setVisible(true);
        }
    }

    //Creates a Capital Gains / Losses report for one stock, for all dates
    public static void reportOne() throws IOException {
        clearTable2();
        NumberFormat money = NumberFormat.getCurrencyInstance();
        Double total = 0.0;
        BufferedReader stockReader = new BufferedReader(new FileReader(stockFrame.getTitle() + ".txt"));
        reportModel.addRow(new String[]{stockFrame.getTitle(), "", "", "", ""});
        String stuff = stockReader.readLine();
        while (stockReader.ready()) {
            String[] temp;
            temp = stockReader.readLine().split(" ");
            if (temp[4].equals("S")) {
                reportModel.addRow(new String[]{"", temp[0], temp[1], temp[2], money.format(Double.valueOf(temp[11]))});
                total += Double.valueOf(money.format(Double.valueOf(temp[11])).replace("$", "").replace(",", ""));
            }
        }
        reportModel.addRow(new String[]{});
        reportModel.addRow(new String[]{"", "", "", "", "Total: " + money.format(Double.valueOf(total))});
        stockReader.close();
        reportFrame.pack();
        reportFrame.setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - reportFrame.getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - reportFrame.getSize().height) / 2));
        reportFrame.setVisible(true);
    }

    //Creates a Capital Gains / Losses report for one stock, for a user defined date range
    public static void reportOne(String startYear, String startMonth, String startDay, String endYear, String endMonth, String endDay) throws IOException {
        if (checkFields(startYear, startMonth, startDay, endYear, endMonth, endDay) == true) {
            reportOptionsFrame.setVisible(false);
            clearTable2();
            NumberFormat money = NumberFormat.getCurrencyInstance();
            Double total = 0.0;
            BufferedReader stockReader = new BufferedReader(new FileReader(stockFrame.getTitle() + ".txt"));
            reportModel.addRow(new String[]{stockFrame.getTitle(), "", "", "", ""});
            String stuff = stockReader.readLine();
            while (stockReader.ready()) {
                String[] temp;
                temp = stockReader.readLine().split(" ");
                if (temp[4].equals("S") && Integer.valueOf(temp[0]) >= Integer.valueOf(startYear) && Integer.valueOf(temp[0]) <= Integer.valueOf(endYear) && Integer.valueOf(temp[1]) >= Integer.valueOf(startMonth) && Integer.valueOf(temp[1]) <= Integer.valueOf(endMonth) && Integer.valueOf(temp[2]) >= Integer.valueOf(startDay) && Integer.valueOf(temp[2]) <= Integer.valueOf(endDay)) {
                    reportModel.addRow(new String[]{"", temp[0], temp[1], temp[2], money.format(Double.valueOf(temp[11]))});
                    total += Double.valueOf(money.format(Double.valueOf(temp[11])).replace("$", "").replace(",", ""));
                }
            }
            reportModel.addRow(new String[]{});
            reportModel.addRow(new String[]{"", "", "", "", "Total: " + money.format(Double.valueOf(total))});
            stockReader.close();
            reportFrame.pack();
            reportFrame.setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - reportFrame.getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - reportFrame.getSize().height) / 2));
            reportFrame.setVisible(true);
        }
    }

    //Checks the fields for the date range when creating a Capital Gains / Losses report
    public static boolean checkFields(String startYear, String startMonth, String startDay, String endYear, String endMonth, String endDay) {
        boolean good = false;
        try {
            if (Integer.valueOf(startYear) < Integer.valueOf(endYear)) {
                good = true;
            } else if (startYear.equals(endYear)) {
                if (Integer.valueOf(startMonth) < Integer.valueOf(endMonth)) {
                    good = true;
                } else if (startMonth.equals(endMonth)) {
                    if (Integer.valueOf(startDay) <= Integer.valueOf(endDay)) {
                        good = true;
                    }
                }
            }
            if (good == false) {
                displayWarning("The \"START\" date must come before the \"END\" date.  Please review your entries!");
            } else {
                Date date = new Date();
                if (Integer.valueOf(startYear) < 2000 || Integer.valueOf(startYear) > date.getYear() + 1900) {
                    good = false;
                    displayWarning("\"Start Year\" out of range.  Please review your entries!");
                } else if (Integer.valueOf(startMonth) < 1 || Integer.valueOf(startMonth) > 12) {
                    good = false;
                    displayWarning("\"Start Month\" out of range.  Please review your entries!");
                } else if (Integer.valueOf(startDay) < 1 || Integer.valueOf(startDay) > 31) {
                    good = false;
                    displayWarning("\"Start Day\" out of range.  Please review your entries!");
                } else if (Integer.valueOf(endYear) < 2000 || Integer.valueOf(endYear) > date.getYear() + 1900) {
                    good = false;
                    displayWarning("\"End Year\" out of range.  Please review your entries!");
                } else if (Integer.valueOf(endMonth) < 1 || Integer.valueOf(endMonth) > 12) {
                    good = false;
                    displayWarning("\"End Month\" out of range.  Please review your entries!");
                } else if (Integer.valueOf(endDay) < 1 || Integer.valueOf(endDay) > 31) {
                    good = false;
                    displayWarning("\"End Day\" out of range.  Please review your entries!");
                }
            }
        } catch (NumberFormatException ex) {
            if (ex.toString().contains("empty String") || ex.toString().contains("For input string: \"\"")) {
                displayWarning("Empty Entry(s)...Please Check All Fields!");
            } else if (ex.toString().contains("multiple points")) {
                displayWarning("Entries can only contain one decimal point!");
            } else if (ex.toString().contains("For input string:")) {
                displayWarning("Invalid Character(s)... [" + ex.toString().substring(52, ex.toString().length() - 1) + "]  ...Please Review your Entries!");
            } else {
                displayWarning("Unknown error.  Please review your entries!");
            }
        }
        return (good);
    }

    //Called every time a button is presses
    //Does a different function depending on the button presses
    //Some buttons display a new window
    //Some buttons close a window and perform a task
    public void actionPerformed(ActionEvent e) {
        String buttonName = e.getActionCommand();
        if (buttonName.equals("New Stock")) {
            addStockField.setText("");
            addStockNumField.setText("0");
            addStockAdjCostBaseField.setText("0.00");
            addStockField.requestFocus();
            addStockFrame.setVisible(true);
        } else if (buttonName.equals("Remove Stock")) {
            if (mainList.getSelectedIndex() >= 0) {
                try {
                    removeStock(mainList.getSelectedValue().toString());
                } catch (IOException ex) {
                }
            } else {
                displayWarning("You have not selected anything!");
            }
        } else if (buttonName.equals("Edit / View Stock")) {
            if (mainList.getSelectedIndex() >= 0) {
                try {
                    stockFrame.setTitle(mainList.getSelectedValue().toString());
                    clearTable();
                    BufferedReader stockReader = new BufferedReader(new FileReader(mainList.getSelectedValue().toString() + ".txt"));
                    while (stockReader.ready()) {
                        entriesModel.addRow(stockReader.readLine().split(" "));
                    }
                    stockReader.close();
                    stockFrame.pack();
                    stockFrame.setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - stockFrame.getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - stockFrame.getSize().height) / 2));
                    stockFrame.setVisible(true);
                } catch (IOException ex) {
                }
            } else {
                displayWarning("You have not selected anything!");
            }
        } else if (buttonName.equals("New Entry")) {
            addEntryFrame.setTitle(buttonName);
            Date date = new Date();
            addEntryBoxes[0].setText(String.valueOf(date.getYear() + 1900));
            addEntryBoxes[1].setText(String.valueOf(date.getMonth() + 1));
            addEntryBoxes[2].setText(String.valueOf(date.getDate()));
            addEntryBoxes[3].setText("1");
            addEntryBoxes[4].setText("");
            addEntryBoxes[5].setText("");
            addEntryBoxes[6].setText("");
            addEntryBoxes[7].setText("9.99");
            addEntryButton.setText("Add Entry");
            addEntryBoxes[4].requestFocus();
            addEntryFrame.setVisible(true);
        } else if (buttonName.equals("Remove Entry")) {
            if (entriesTable.getSelectedRow() >= 1) {
                try {
                    removeEntry();
                } catch (IOException ex) {
                }
            } else if (entriesTable.getSelectedRow() < 0) {
                displayWarning("You have not selected anything!");
            } else {
                displayWarning("You can not delete this entry!");
            }
        } else if (buttonName.equals("Edit / View Entry")) {
            if (entriesTable.getSelectedRow() > 0) {
                addEntryFrame.setTitle(buttonName);
                String all[][] = getNumsFromTable();
                for (int i = 0; i < addEntryBoxes.length; i++) {
                    addEntryBoxes[i].setText(all[entriesTable.getSelectedRow()][i]);
                }
                addEntryButton.setText("Edit Entry");
                addEntryBoxes[4].requestFocus();
                addEntryFrame.setVisible(true);
            } else if (entriesTable.getSelectedRow() < 0) {
                displayWarning("You have not selected anything!");
            } else if (entriesTable.getSelectedRow() == 0) {
                String all[][] = getNumsFromTable();
                editStartingNumField.setText(all[0][8]);
                editStartingACBTotalField.setText(all[0][10]);
                editStartingValuesFrame.setVisible(true);
            }
        } else if (buttonName.equals("Add Stock")) {
            try {
                addStock(addStockField.getText().toUpperCase(), addStockNumField.getText(), addStockAdjCostBaseField.getText());
            } catch (IOException ex) {
            }
        } else if (buttonName.equals("Add Entry")) {
            try {
                checkAddEditEntrys("add");
            } catch (IOException ex) {
            }
        } else if (buttonName.equals("Edit Entry")) {
            try {
                checkAddEditEntrys("edit");
            } catch (IOException ex) {
            }
        } else if (buttonName.equals("Apply")) {
            try {
                editStartingValues();
            } catch (IOException ex) {
            }
        } else if (buttonName.equals("OK")) {
            warningFrame.setVisible(false);
        } else if (buttonName.equals("Exit")) {
            System.exit(0);
        } else if (buttonName.equals("Full Report")) {
            reportOptionsButton1.setText("Get Full - All");
            reportOptionsButton2.setText("Get Full - Range");
            reportOptionsFrame.pack();
            reportOptionsFrame.setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - reportOptionsFrame.getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - reportOptionsFrame.getSize().height) / 2));
            reportOptionsFrame.setVisible(true);
        } else if (buttonName.equals("Report This Stock")) {
            reportOptionsButton1.setText("Get Stock - All");
            reportOptionsButton2.setText("Get Stock - Range");
            reportOptionsFrame.pack();
            reportOptionsFrame.setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - reportOptionsFrame.getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - reportOptionsFrame.getSize().height) / 2));
            reportOptionsFrame.setVisible(true);
        } else if (buttonName.equals("Print")) {
            try {
                reportTable.print();
            } catch (PrinterException ex) {
            }
        } else if (buttonName.equals("Get Full - All")) {
            if (mainListModel.getSize() >= 1) {
                reportOptionsFrame.setVisible(false);
                try {
                    reportAll();
                } catch (IOException ex) {
                }
            } else {
                displayWarning("You must have at least one stock to view a report!");
            }
        } else if (buttonName.equals("Get Full - Range")) {
            if (mainListModel.getSize() >= 1) {
                try {
                    reportAll(reportOptionsStartYearField.getText(), reportOptionsStartMonthField.getText(), reportOptionsStartDayField.getText(), reportOptionsEndYearField.getText(), reportOptionsEndMonthField.getText(), reportOptionsEndDayField.getText());
                } catch (IOException ex) {
                }
            } else {
                displayWarning("You must have at least one stock to view a report!");
            }
        } else if (buttonName.equals("Get Stock - All")) {
            reportOptionsFrame.setVisible(false);
            try {
                reportOne();
            } catch (IOException ex) {
            }
        } else if (buttonName.equals("Get Stock - Range")) {
            try {
                reportOne(reportOptionsStartYearField.getText(), reportOptionsStartMonthField.getText(), reportOptionsStartDayField.getText(), reportOptionsEndYearField.getText(), reportOptionsEndMonthField.getText(), reportOptionsEndDayField.getText());
            } catch (IOException ex) {
            }
        } else if (buttonName.equals("Stock Help")) {
            helpFrame.setTitle(buttonName);
            helpField.setText("STOCK HELP\n\nFrom the main screen, you can:\n\t--Add a stock via the \"New Stock\" button\n\t--Remove a stock via the \"Remove Stock\" button\n\t--View further details about a stock via the \"Edit / View Stock\" button\n\t--Exit the program via File\"Exit\"\n\t--View a stock report including all stocks ranging from:\n\t\t--All dates\n\t\t--A Date Range selected by you\n\n\t--Get help via:\n\t\t--Help\"Stock Help\"\n\t\t--Help\"Entry Help\"\n\n\tWhile creating a new stock or creating a full Capital gains report, if any of your entries are entered incorrectly, you will be prompted with errors / suggestions regarding the problem.  Some errors will not let you proceed with entering your stock, while others may allow you to proceed with entering your stock but will advise you to change your entry.\n\nNote  -  ACB  Adjusted Cost Base ");
            helpFrame.setVisible(true);
        } else if (buttonName.equals("Entry Help")) {
            helpFrame.setTitle(buttonName);
            helpField.setText("ENTRY HELP\n\nFrame the individual stock screen, you can:\n\t--Add an entry via the \"New Entry\" button\n\t--Remove an entry via the \"Remove Entry\" button\n\t--View editable fields for the selected entry via the \"Edit / View Entry\" button\n\t--View an individual stock report ranging from:\n\t\t--All dates\n\t\t--A Date Range selected by you\n\n\t--Get help via:\n\t\t--Help\"Stock Help\"\n\t\t--Help\"Entry Help\"\n\n\tWhile creating a new entry or creating an individual Capital gains report, if any of your entries are entered incorrectly, you will be prompted with errors / suggestions regarding the problem. Some errors will not let you proceed with your entry, while others may allow you to proceed with your entry but will advise you to change your entry.\n\nNote  -  ACB  Adjusted Cost Base ");
            helpFrame.setVisible(true);
        } else {
            displayWarning("Unsupported Button!  Consult the programmer.");
        }
    }
}