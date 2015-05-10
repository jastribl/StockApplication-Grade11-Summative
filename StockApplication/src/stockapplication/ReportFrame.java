package stockapplication;

import java.awt.Dimension;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.text.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;
import static stockapplication.StockApplication.mainFrame;
import static stockapplication.StockApplication.reportOptionsFrame;
import static stockapplication.StockApplication.stockFrame;
import static stockapplication.StockApplication.stocks;
import static stockapplication.StockApplication.warningWindow;

public class ReportFrame extends JFrame {

    private final JPanel quickReportPanel = new JPanel(), taxReportPanel = new JPanel();
    private final DefaultTableModel quickReportModel = new DefaultTableModel(new String[]{"Stock", "Year", "Month", "Day", "Capital Gains / Losses"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final DefaultTableModel taxReportModel = new DefaultTableModel(new String[]{"Number", "Name", "Bought", "Sold", "Proceeds", "ACB", "Outlays", "Gain / Loss"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    public final JTable quickReportTable = new JTable(quickReportModel);
    public final JTable taxReportTable = new JTable(taxReportModel);

    public ReportFrame() {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        taxReportTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        taxReportTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        taxReportTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        taxReportTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        taxReportTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        taxReportTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        quickReportTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        quickReportTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        quickReportTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        taxReportTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        quickReportTable.getTableHeader().setReorderingAllowed(false);
        quickReportTable.getTableHeader().setResizingAllowed(false);
        taxReportTable.getTableHeader().setReorderingAllowed(false);
        taxReportTable.getTableHeader().setResizingAllowed(false);
        int quickReportWidths[] = {93, 34, 42, 31, 145}, taxReportWidths[] = {60, 45, 80, 80, 75, 75, 55, 75};//{50, 40, 70, 70, 70, 70, 40, 70};
        int quickReportTotalWidth = 0, taxReportTotalWidth = 0;
        for (int i = 0; i < quickReportWidths.length; i++) {
            quickReportTable.getColumnModel().getColumn(i).setMinWidth(quickReportWidths[i]);
            quickReportTotalWidth += quickReportWidths[i];
        }
        for (int i = 0; i < taxReportWidths.length; i++) {
            taxReportTable.getColumnModel().getColumn(i).setMinWidth(taxReportWidths[i]);
            taxReportTotalWidth += taxReportWidths[i];
        }
        quickReportTable.setPreferredScrollableViewportSize(new Dimension(quickReportTotalWidth, 500));
        taxReportTable.setPreferredScrollableViewportSize(new Dimension(taxReportTotalWidth, 500));
        JScrollPane quickReportScrollPane = new JScrollPane(quickReportTable);
        JScrollPane taxReportScrollPane = new JScrollPane(taxReportTable);
        quickReportPanel.add(quickReportScrollPane);
        taxReportPanel.add(taxReportScrollPane);
        JMenu fileMenu = new JMenu("File");
        JMenuItem printMenuItem = new JMenuItem("Print");
        fileMenu.add(printMenuItem);
        JMenuBar reportMenuBar = new JMenuBar();
        reportMenuBar.add(fileMenu);
        setJMenuBar(reportMenuBar);
        printMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                print();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (getTitle().substring(0, 1).equals(" ")) {
                    stockFrame.setVisible(true);
                } else {
                    mainFrame.setVisible(true);
                }
            }
        });
        KeyListener keyListener = new KeyListener() {
            boolean ESCisDown = false;

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    ESCisDown = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                if (ke.getKeyCode() == KeyEvent.VK_ESCAPE && ESCisDown) {
                    ESCisDown = false;
                    setVisible(false);
                    if (getTitle().substring(0, 1).equals(" ")) {
                        stockFrame.setVisible(true);
                    } else {
                        mainFrame.setVisible(true);
                    }
                }
            }
        };
        quickReportTable.addKeyListener(keyListener);
        taxReportTable.addKeyListener(keyListener);
        setResizable(false);
    }

    private void display(boolean option, String title) {
        setTitle((option ? " " : "") + title + " Stock Report");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public boolean quickReportAllStocks() {
        if (datesGood()) {
            JDate startDate = reportOptionsFrame.getStartDate(), endDate = reportOptionsFrame.getEndDate();
            clearReportTable();
            DecimalFormat money = new DecimalFormat("$#,##0.00;$-#,##0.00");
            Double fullTotal = 0.0;
            Double totals[] = new Double[stocks.size()];
            for (int i = 0; i < totals.length; i++) {
                totals[i] = 0.0;
            }
            for (int i = 0; i < stocks.size(); i++) {
                Stock stock = stocks.get(i);
                quickReportModel.addRow(new Object[]{stock.getName()});
                for (int j = 0; j < stock.getEntries().size(); j++) {
                    Entry entry = stock.getEntries().get(j);
                    JDate tempDate = new JDate(entry.getYear(), entry.getMonth(), entry.getDay());
                    if (entry.getBS() == 'S' && (tempDate.compareTo(startDate) >= 0 && tempDate.compareTo(endDate) <= 0)) {
                        quickReportModel.addRow(new Object[]{null, entry.getYear(), entry.getMonth(), entry.getDay(), money.format(entry.getCapitalGain())});
                        totals[i] += entry.getCapitalGain();
                    }
                }
                quickReportModel.addRow(new String[]{});
                quickReportModel.addRow(new String[]{null, null, null, null, "Total: " + money.format(totals[i])});
                quickReportModel.addRow(new String[]{});
                fullTotal += totals[i];
            }
            quickReportModel.addRow(new String[]{});
            quickReportModel.addRow(new String[]{null, null, null, null, "Final Total: " + money.format(fullTotal)});
            quickReportModel.addRow(new String[]{});
            mainFrame.setVisible(false);
            display(false, "Full");
            return true;
        } else {
            warningWindow.displayWarning("The \"Start Date\" must come before the \"End Date\".", 0);
            return false;
        }
    }

    public boolean quickReportOneStock(int index) {
        if (datesGood()) {
            JDate startDate = reportOptionsFrame.getStartDate(), endDate = reportOptionsFrame.getEndDate();
            clearReportTable();
            DecimalFormat money = new DecimalFormat("$#,##0.00;$-#,##0.00");
            Double total = 0.0;
            Stock stock = stocks.get(index);
            quickReportModel.addRow(new Object[]{stock.getName()});
            for (int j = 0; j < stock.getEntries().size(); j++) {
                Entry entry = stock.getEntries().get(j);
                JDate tempDate = new JDate(entry.getYear(), entry.getMonth(), entry.getDay());
                if (entry.getBS() == 'S' && (tempDate.compareTo(startDate) >= 0 && tempDate.compareTo(endDate) <= 0)) {
                    quickReportModel.addRow(new Object[]{null, entry.getYear(), entry.getMonth(), entry.getDay(), money.format(entry.getCapitalGain())});
                    total += entry.getCapitalGain();
                }
            }
            quickReportModel.addRow(new String[]{});
            quickReportModel.addRow(new String[]{null, null, null, null, "Final Total: " + money.format(total)});
            quickReportModel.addRow(new String[]{});
            stockFrame.setVisible(false);
            display(true, stockFrame.getTitle());
            return true;
        } else {
            warningWindow.displayWarning("The \"Start Date\" must come before the \"End Date\".", 0);
            return false;
        }
    }

    public boolean quickReport(String stocksToDo, int index) {
        this.remove(quickReportPanel);
        this.remove(taxReportPanel);
        add(quickReportPanel);
        if (stocksToDo.equals("ALL")) {
            return quickReportAllStocks();
        } else {
            return quickReportOneStock(index);
        }
    }

    public void taxReport() {
        this.remove(quickReportPanel);
        this.remove(taxReportPanel);
        add(taxReportPanel);
        JDate startDate = new JDate(reportOptionsFrame.getTaxYear(), 1, 1), endDate = new JDate(reportOptionsFrame.getTaxYear(), 12, 31);
        clearReportTable();
        DecimalFormat money = new DecimalFormat("$#,##0.00;$-#,##0.00");
        Double totalCapitalGains = 0.0;
        for (Stock stock : stocks) {
            ArrayList<ArrayList> records = new ArrayList();
            boolean foundFirstBuy = false;
            for (int j = 0; j < stock.getEntries().size(); j++) {
                Entry entry = stock.getEntries().get(j);
                if (entry.getBS() == 'S') {
                    if (entry.getDate().compareTo(startDate) >= 0 && entry.getDate().compareTo(endDate) <= 0) {
                        double acb;
                        if (j == 0) {
                            acb = stock.getStartingACBPerUnit();
                        } else {
                            acb = stock.getEntries().get(j - 1).getACBIndevidual();
                        }
                        acb *= entry.getQuantity();
                        JDate dateBought = new JDate();
                        int amount = entry.getQuantity();
                        if (foundFirstBuy) {
                            dateBought = (JDate) records.get(0).get(0);
                            for (int i = 0; i < records.size(); i++) {
                                if ((int) records.get(i).get(1) > amount) {
                                    records.get(i).set(1, (int) records.get(i).get(1) - amount);
                                    break;
                                } else if ((int) records.get(i).get(1) == amount) {
                                    records.remove(0);
                                    break;
                                } else {
                                    amount -= (int) records.get(i).get(1);
                                    records.remove(0);
                                    i--;
                                }
                            }
                        }
                        taxReportModel.addRow(new Object[]{entry.getQuantity(), stock.getName(), dateBought, entry.getDate().toString(), money.format(entry.getQuantity() * entry.getPrice()), money.format(acb), money.format(entry.getCommission()), money.format(entry.getCapitalGain())});
                        totalCapitalGains += entry.getCapitalGain();

                    }
                    if (entry.getTotalQuantity() == 0) {
                        records.clear();
                    }
                } else if (entry.getBS() == 'B') {
                    if (!foundFirstBuy) {
                        foundFirstBuy = true;
                    }
                    boolean found = false;
                    for (ArrayList record : records) {
                        if (((JDate) record.get(0)).compareTo(entry.getDate()) == 0) {
                            record.set(1, (int) record.get(1) + entry.getQuantity());
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        ArrayList newSet = new ArrayList();
                        newSet.add((JDate) entry.getDate());
                        newSet.add((int) entry.getQuantity());
                        records.add(newSet);
                    }
                }
            }
        }
        taxReportModel.addRow(new String[]{});
        taxReportModel.addRow(new String[]{null, null, null, null, null, null, "Total: ", money.format(totalCapitalGains)});
        taxReportModel.addRow(new String[]{});
        mainFrame.setVisible(false);
        display(false, "TAX");
    }

    private boolean datesGood() {
        return reportOptionsFrame.getStartDate().compareTo(reportOptionsFrame.getEndDate()) <= 0;
    }

    private void clearReportTable() {
        quickReportModel.setRowCount(0);
        taxReportModel.setRowCount(0);
    }

    public void print() {
        try {
            MessageFormat header = new MessageFormat("Tax Report");
            MessageFormat footer = new MessageFormat("Page - {0}");
            taxReportTable.print(JTable.PrintMode.NORMAL,header,footer);
        } catch (PrinterException ex) {
            warningWindow.displayWarning("Printing error.  Sorry for the inconvenience!");
        }
    }
}
