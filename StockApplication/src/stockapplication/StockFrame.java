package stockapplication;

import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import static stockapplication.StockApplication.addEntryFrame;
import static stockapplication.StockApplication.confirmWindow;
import static stockapplication.StockApplication.mainFrame;
import static stockapplication.StockApplication.reportOptionsFrame;
import static stockapplication.StockApplication.stocks;
import static stockapplication.StockApplication.warningWindow;
import static stockapplication.StockApplication.helpFrame;

public class StockFrame extends JFrame {

    private final DefaultTableModel entriesModel;
    public final JTable entriesTable;
    private int currentStockIndex = 0;

    public StockFrame() {
        Object data[][] = {{"", "", "", "", "", "", "", "", "", "", "", ""}};
        String[] headers = {"Year", "Month", "Day", "Trade #", "Buy / Sell", "Quantity", "Price", "Commission", "Total Shares", "ACB (Per Unit)", "ACB (Total)", "Capital Gains / Losses"};
        entriesModel = new DefaultTableModel(data, headers) {
            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        entriesTable = new JTable(entriesModel);
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
        JMenuItem entryHelpItem = new JMenuItem("Entry Help");
        helpMenu.add(entryHelpItem);
        stockMenuBar.add(helpMenu);
        setJMenuBar(stockMenuBar);
        add(stockPanel);
        indevidualReportItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                reportOptionsFrame.displayForQuickCapitalGainsReport("ONE", true);
            }
        });
        stockAddButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                addEntryFrame.display("New Entry");
            }
        });
        stockRemoveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                removeEntry();
            }
        });
        stockFunctionButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                addEntryFrame.display("Edit / View Entry");
            }
        });
        entryHelpItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                helpFrame.display(1);
            }
        });
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
                    reportOptionsFrame.displayForQuickCapitalGainsReport("ONE", true);
                    CTRRisDown = false;
                } else if (key == KeyEvent.VK_ESCAPE && ESCisDown) {
                    ESCisDown = false;
                    setVisible(false);
                    mainFrame.setVisible(true);
                }
            }
        });
    }

    public void display(int index) {
        currentStockIndex = index;
        if (currentStockIndex >= 0) {
            setTitle(stocks.get(currentStockIndex).getName());
            refreshStockFrame();
            entriesTable.setRowSelectionInterval(0, 0);
            mainFrame.setVisible(false);
            setVisible(true);
        } else {
            warningWindow.displayWarning("You have not selected anything!");
        }
    }

    public final void refreshStockFrame() {
        clearStocksTable();
        Stock stock = stocks.get(currentStockIndex);
        entriesModel.addRow(new Object[]{"", "", "", "", "", "", "", "", stock.getStartingNum(), stock.getStartingACBPerUnit(), stock.getStartingACBTotal(), "",});
        for (int i = 0; i < stock.getEntries().size(); i++) {
            entriesModel.addRow(stock.getEntries().get(i).getValues());
        }
        pack();
        setLocationRelativeTo(null);
    }

    public final void removeEntry() {
        if (entriesTable.getSelectedRow() > 0) {
            if (confirmWindow.displayConfirmation("Are you sure you would like to remove this entrie?")) {
                int index = entriesTable.getSelectedRow();
                entriesModel.removeRow(index);
                stocks.get(currentStockIndex).removeEntry(index - 1);
                refreshStockFrame();
                if (index >= entriesTable.getRowCount()) {
                    index--;
                }
                entriesTable.setRowSelectionInterval(0, index);
            }
        } else if (entriesTable.getSelectedRow() < 0) {
            warningWindow.displayWarning("You have not selected anything!");
        } else {
            warningWindow.displayWarning("You can not delete this entry!");
        }
    }

    private void clearStocksTable() {
        entriesModel.setRowCount(0);
    }
}
