package stockapplication;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import static stockapplication.StockApplication.addEntryFrame;
import static stockapplication.StockApplication.confirmWindow;
import static stockapplication.StockApplication.listener;
import static stockapplication.StockApplication.mainFrame;
import static stockapplication.StockApplication.reportOptionsFrame;
import static stockapplication.StockApplication.stockFrame;
import static stockapplication.StockApplication.warningWindow;

public class StockFrame extends JFrame {

    private final String data[][] = {{"", "", "", "", "", "", "", "", "", "", "", ""}};
    private final String[] headers = {"Year", "Month", "Day", "Trade #", "Buy / Sell", "Quantity", "Price", "Commission", "Total Shares", "ACB (Per Unit)", "ACB (Total)", "Capital Gains / Losses"};
    public final DefaultTableModel entriesModel = new DefaultTableModel(data, headers) {
        @Override
        public Class<?> getColumnClass(int column) {
            return getValueAt(0, column).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    public final JTable entriesTable = new JTable(entriesModel);

    public StockFrame() {
        entriesTable.setCellSelectionEnabled(false);
        entriesTable.setRowSelectionAllowed(true);
        entriesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        entriesTable.getTableHeader().setReorderingAllowed(false);
        entriesTable.getTableHeader().setResizingAllowed(false);
        int[] widths = {34, 42, 31, 51, 59, 53, 53, 79, 77, 88, 71, 135};
        for (int i = 0; i < 12; i++) {
            entriesTable.getColumnModel().getColumn(i).setMinWidth(widths[i]);
            entriesTable.getColumnModel().getColumn(i).setMaxWidth(widths[i] + 1);
        }
        entriesTable.setPreferredScrollableViewportSize(new Dimension(773, 373));
        JPanel stockPanel = new JPanel();
        JScrollPane entriesScrollPane = new JScrollPane(entriesTable);
        JButton stockAddButton = new JButton("New Entry");
        stockAddButton.setFocusable(false);
        JButton stockRemoveButton = new JButton("Remove Entry");
        stockRemoveButton.setFocusable(false);
        JButton stockFunctionButton = new JButton("Edit / View Entry");
        stockFunctionButton.setFocusable(false);
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
        setJMenuBar(stockMenuBar);
        indevidualReportItem.addActionListener(listener);
        add(stockPanel);
        stockAddButton.addActionListener(listener);
        stockRemoveButton.addActionListener(listener);
        stockFunctionButton.addActionListener(listener);
        stockHelpItem.addActionListener(listener);
        entryHelpItem.addActionListener(listener);
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                mainFrame.setVisible(true);
            }
        });
        entriesTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        entriesTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "Tab");
        entriesTable.addKeyListener(new KeyListener() {
            boolean ENTERisDown = false, DELETEisDown = false, CTRNisDown = false, CTRRisDown = false, ESCisDown = false;

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
                } else if (key == KeyEvent.VK_ESCAPE) {
                    ESCisDown = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                int key = ke.getKeyCode();
                if (key == KeyEvent.VK_ENTER && ENTERisDown) {
                    ENTERisDown = false;
                    addEntryFrame.display("Edit / View Entry");
                } else if (key == KeyEvent.VK_DELETE && DELETEisDown) {
                    DELETEisDown = false;
                    removeEntry();
                } else if (key == KeyEvent.VK_N && CTRNisDown) {
                    CTRNisDown = false;
                    addEntryFrame.display("New Entry");
                } else if (key == KeyEvent.VK_R && CTRRisDown) {
                    reportOptionsFrame.display("Stock", true);
                    CTRRisDown = false;
                } else if (key == KeyEvent.VK_ESCAPE && ESCisDown) {
                    ESCisDown = false;
                    stockFrame.setVisible(false);
                    mainFrame.setVisible(true);
                }
            }
        });
    }

    public void display() {
        if (mainFrame.mainList.getSelectedIndex() >= 0) {
            setTitle(mainFrame.mainList.getSelectedValue().toString());
            clearStocksTable();
            try (BufferedReader stockReader = new BufferedReader(new FileReader(mainFrame.mainList.getSelectedValue().toString() + ".txt"))) {
                while (stockReader.ready()) {
                    entriesModel.addRow(stockReader.readLine().split(" "));
                }
            } catch (IOException ex) {
            }
            pack();
            setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - getSize().height) / 2));
            mainFrame.setVisible(false);
            entriesTable.setRowSelectionInterval(0, 0);
            setVisible(true);
        } else {
            warningWindow.displayWarning("You have not selected anything!", 0);
        }
    }

    public void removeEntry() {
        if (entriesTable.getSelectedRow() > 0) {
            if (confirmWindow.displayConfirmation("Are you sure you would like to remove this entrie?")) {
                int index = entriesTable.getSelectedRow();
                entriesModel.removeRow(index);
                sortCalculateSaveEntries();
                if (index >= entriesTable.getRowCount()) {
                    index--;
                }
                entriesTable.setRowSelectionInterval(0, index);
            }
        } else if (entriesTable.getSelectedRow() < 0) {
            warningWindow.displayWarning("You have not selected anything!", 0);
        } else {
            warningWindow.displayWarning("You can not delete this entry!", 0);
        }
    }

    public void clearStocksTable() {
        entriesModel.setRowCount(0);
    }

    public void sortCalculateSaveEntries() {
        if (entriesModel.getRowCount() > 1) {
            String all[][] = getNumsFromTable();
            JDate currentDate = new JDate();
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
            clearStocksTable();
            entriesModel.addRow(all[0]);
            for (int year = minYear; year <= currentDate.getYear(); year++) {
                for (int month = 0; month <= 12; month++) {
                    for (int day = 0; day <= 31; day++) {
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
            all = getNumsFromTable();
            int firstBadRow = -1;
            for (int i = 1; i < all.length; i++) {
                String temp = all[i][4].toUpperCase();
                if (temp.matches("B")) {
                    all[i][8] = String.valueOf(Integer.valueOf(all[i - 1][8]) + Integer.valueOf(all[i][5]));
                    all[i][10] = String.valueOf(Double.valueOf(all[i - 1][10]) + (Double.valueOf(all[i][6]) * Double.valueOf(all[i][5])) + Double.valueOf(all[i][7]));
                    all[i][9] = String.valueOf(Double.valueOf(all[i][10]) / Double.valueOf(all[i][8]));
                } else if (temp.matches("S")) {
                    all[i][8] = String.valueOf(Integer.valueOf(all[i - 1][8]) - Integer.valueOf(all[i][5]));
                    if (Integer.valueOf(all[i][8]) < 0 && firstBadRow == -1) {
                        warningWindow.displayWarning("You cannot sell stocks that you do not have.", 1);
                        firstBadRow = i;
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
            clearStocksTable();
            try (PrintWriter stockWriter = new PrintWriter(new FileWriter(getTitle() + ".txt"))) {
                for (int i = 0; i < all.length; i++) {
                    entriesModel.addRow(all[i]);
                    for (int j = 0; j < 12; j++) {
                        stockWriter.print(entriesTable.getValueAt(i, j).toString() + " ");
                    }
                    stockWriter.println();
                }
            } catch (IOException ex) {
            }
            if (firstBadRow != -1) {
                entriesTable.setRowSelectionInterval(0, firstBadRow);
                addEntryFrame.display("Edit / View Entry");
            }
        }
    }

    public String[][] getNumsFromTable() {
        String[][] all = new String[entriesModel.getRowCount()][12];
        for (int i = 0; i < all.length; i++) {
            for (int j = 0; j < 12; j++) {
                all[i][j] = entriesModel.getValueAt(i, j).toString();
            }
        }
        return (all);
    }
}
