package stockapplication;

import java.awt.event.*;
import static stockapplication.StockApplication.moneyFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;
import static stockapplication.StockApplication.mainFrame;
import static stockapplication.StockApplication.reportOptionsFrame;
import static stockapplication.StockApplication.stockFrame;
import static stockapplication.StockApplication.stocks;
import static stockapplication.StockApplication.warningWindow;

public class ReportFrame extends JFrame {

    private final String[] quickReportHeadings = new String[]{"Stock", "Year", "Month", "Day", "Capital Gains / Losses"};
    private final String[] taxReportHeadings = new String[]{"Number", "Name", "Bought", "Sold", "Proceeds", "ACB", "Outlays", "Gain / Loss"};
    private final PerfectTable reportTable = new PerfectTable();

    public ReportFrame() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem printMenuItem = new JMenuItem("Print");
        fileMenu.add(printMenuItem);
        JMenuBar reportMenuBar = new JMenuBar();
        reportMenuBar.add(fileMenu);
        setJMenuBar(reportMenuBar);
        printMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                reportTable.print();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                goToLastFrame();
            }
        });
        reportTable.getTable().addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
                int key = ke.getKeyCode();
                if (key == KeyEvent.VK_ESCAPE) {
                    setVisible(false);
                    goToLastFrame();
                } else if (key == KeyEvent.VK_P && ke.isControlDown()) {
                    reportTable.print();
                }
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });
        add(reportTable);
        setResizable(false);
    }

    private void goToLastFrame() {
        if (getTitle().contains("Full ") || getTitle().contains("Tax ")) {
            mainFrame.setVisible(true);
        } else {
            stockFrame.setVisible(true);
        }
    }

    private void display(String title) {
        setTitle(title + " Report");
        reportTable.resize();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private boolean quickReportAllStocks() {
        if (datesGood()) {
            JDate startDate = reportOptionsFrame.getStartDate(), endDate = reportOptionsFrame.getEndDate();
            reportTable.clearTable();
            Double fullTotal = 0.0;
            Double totals[] = new Double[stocks.size()];
            for (int i = 0; i < totals.length; i++) {
                totals[i] = 0.0;
            }
            ArrayList<Object[]> data = new ArrayList();
            for (int i = 0; i < stocks.size(); i++) {
                Stock stock = stocks.get(i);
                data.add(new Object[]{stock.getName()});
                for (int j = 0; j < stock.getEntries().size(); j++) {
                    Entry entry = stock.getEntries().get(j);
                    JDate tempDate = new JDate(entry.getYear(), entry.getMonth(), entry.getDay());
                    if (entry.getBS() == 'S' && (tempDate.compareTo(startDate) >= 0 && tempDate.compareTo(endDate) <= 0)) {
                        data.add(new Object[]{null, entry.getYear(), entry.getMonth(), entry.getDay(), moneyFormat.format(entry.getCapitalGain())});
                        totals[i] += entry.getCapitalGain();
                    }
                }
                data.add(new String[]{});
                data.add(new String[]{null, null, null, null, "Total: " + moneyFormat.format(totals[i])});
                data.add(new String[]{});
                fullTotal += totals[i];
            }
            data.add(new String[]{});
            data.add(new String[]{null, null, null, null, "Final Total: " + moneyFormat.format(fullTotal)});
            data.add(new String[]{});
            mainFrame.setVisible(false);
            reportTable.fillData(data);
            display("Full");
            return true;
        } else {
            warningWindow.displayWarning("The \"Start Date\" must come before the \"End Date\".", 0);
            return false;
        }
    }

    private boolean quickReportOneStock(int index) {
        if (datesGood()) {
            JDate startDate = reportOptionsFrame.getStartDate(), endDate = reportOptionsFrame.getEndDate();
            reportTable.clearTable();
            Double total = 0.0;
            Stock stock = stocks.get(index);
            ArrayList<Object[]> data = new ArrayList();
            data.add(new Object[]{stock.getName()});
            for (int j = 0; j < stock.getEntries().size(); j++) {
                Entry entry = stock.getEntries().get(j);
                JDate tempDate = new JDate(entry.getYear(), entry.getMonth(), entry.getDay());
                if (entry.getBS() == 'S' && (tempDate.compareTo(startDate) >= 0 && tempDate.compareTo(endDate) <= 0)) {
                    data.add(new Object[]{null, entry.getYear(), entry.getMonth(), entry.getDay(), moneyFormat.format(entry.getCapitalGain())});
                    total += entry.getCapitalGain();
                }
            }
            data.add(new String[]{});
            data.add(new String[]{null, null, null, null, "Final Total: " + moneyFormat.format(total)});
            data.add(new String[]{});
            stockFrame.setVisible(false);
            reportTable.fillData(data);
            display(stockFrame.getTitle());
            return true;
        } else {
            warningWindow.displayWarning("The \"Start Date\" must come before the \"End Date\".", 0);
            return false;
        }
    }

    public final boolean quickReport(String stocksToDo, int index) {
        reportTable.setHeader(quickReportHeadings);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        reportTable.getTable().getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        reportTable.getTable().getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        reportTable.getTable().getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        if (stocksToDo.equals("ALL")) {
            return quickReportAllStocks();
        } else {
            return quickReportOneStock(index);
        }
    }

    public final void taxReport() {
        reportTable.setHeader(taxReportHeadings);
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        reportTable.getTable().getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        reportTable.getTable().getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        reportTable.getTable().getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        reportTable.getTable().getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        reportTable.getTable().getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        reportTable.getTable().getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        reportTable.getTable().getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        JDate startDate = new JDate(reportOptionsFrame.getTaxYear(), 1, 1), endDate = new JDate(reportOptionsFrame.getTaxYear(), 12, 31);
        reportTable.clearTable();
        Double totalCapitalGains = 0.0;
        ArrayList<Object[]> data = new ArrayList();
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
                        data.add(new Object[]{entry.getQuantity(), stock.getName(), dateBought, entry.getDate().toString(), moneyFormat.format(entry.getQuantity() * entry.getPrice()), moneyFormat.format(acb), moneyFormat.format(entry.getCommission()), moneyFormat.format(entry.getCapitalGain())});
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
        data.add(new String[]{});
        data.add(new String[]{null, null, null, null, null, null, "Total: ", moneyFormat.format(totalCapitalGains)});
        data.add(new String[]{});
        mainFrame.setVisible(false);
        reportTable.fillData(data);
        display("Tax");
    }

    private boolean datesGood() {
        return reportOptionsFrame.getStartDate().compareTo(reportOptionsFrame.getEndDate()) <= 0;
    }
}
