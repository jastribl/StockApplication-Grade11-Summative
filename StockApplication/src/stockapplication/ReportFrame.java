package stockapplication;

import java.awt.Dimension;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import static stockapplication.StockApplication.listener;
import static stockapplication.StockApplication.mainFrame;
import static stockapplication.StockApplication.reportOptionsFrame;
import static stockapplication.StockApplication.stockFrame;
import static stockapplication.StockApplication.warningFrame;

public class ReportFrame extends JFrame {

    public final DefaultTableModel reportModel = new DefaultTableModel(new String[][]{{"", "", "", "", ""}}, new String[]{"Stock", "Year", "Month", "Day", "Capital Gains / Losses"}) {
        @Override
        public Class<?> getColumnClass(int column) {
            return getValueAt(0, column).getClass();
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    public final JTable reportTable = new JTable(reportModel);

    public ReportFrame() {
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
        setJMenuBar(reportMenuBar);
        printMenuItem.addActionListener(listener);
        add(reportPanel);
        setResizable(false);
    }

    public void display() {
        pack();
        setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - getSize().height) / 2));
        setVisible(true);
    }

    public void reportAll(String option) {
        if ((option.equals("all")) || (option.equals("range") && checkDates())) {
            reportOptionsFrame.setVisible(false);
            Date startDate = new Date(), endDate = new Date();
            if (option.equals("range")) {
                startDate = reportOptionsFrame.getStartDate();
                endDate = reportOptionsFrame.getEndDate();
            }
            clearReportTable();
            NumberFormat money = NumberFormat.getCurrencyInstance();
            Double fullTotal = 0.0;
            Double totals[] = new Double[mainFrame.mainListModel.size()];
            for (int i = 0; i < totals.length; i++) {
                totals[i] = 0.0;
            }
            for (int i = 0; i < mainFrame.mainListModel.size(); i++) {
                try (BufferedReader stockReader = new BufferedReader(new FileReader(mainFrame.mainListModel.get(i).toString() + ".txt"))) {
                    reportModel.addRow(new String[]{mainFrame.mainListModel.get(i).toString(), "", "", "", ""});
                    stockReader.readLine();
                    while (stockReader.ready()) {
                        String[] temp = stockReader.readLine().split(" ");
                        Date tempDate = new Date(Integer.parseInt(temp[0]) - 1900, Integer.parseInt(temp[1]) - 1, Integer.parseInt(temp[2]));
                        if (temp[4].equals("S") && ((option.equals("all")) || (tempDate.compareTo(startDate) >= 0 && tempDate.compareTo(endDate) <= 0))) {
                            reportModel.addRow(new String[]{"", temp[0], temp[1], temp[2], money.format(Double.parseDouble(temp[11]))});
                            totals[i] += Double.parseDouble(money.format(Double.parseDouble(temp[11])).replace("$", "").replace(",", ""));
                        }
                    }
                    fullTotal += totals[i];
                    reportModel.addRow(new String[]{});
                    reportModel.addRow(new String[]{"", "", "", "", "Total: " + money.format(totals[i])});
                    reportModel.addRow(new String[]{});
                    reportModel.addRow(new String[]{});
                } catch (IOException ex) {
                }
            }
            reportModel.addRow(new String[]{});
            reportModel.addRow(new String[]{"", "", "", "", "Final Total: " + money.format(fullTotal)});
            display();
        }
    }

    public void reportOne(String option) {
        if ((option.equals("all")) || (option.equals("range") && checkDates())) {
            reportOptionsFrame.setVisible(false);
            Date startDate = new Date(), endDate = new Date();
            if (option.equals("range")) {
                startDate = reportOptionsFrame.getStartDate();
                endDate = reportOptionsFrame.getEndDate();
            }
            clearReportTable();
            NumberFormat money = NumberFormat.getCurrencyInstance();
            Double total = 0.0;
            try (BufferedReader stockReader = new BufferedReader(new FileReader(stockFrame.getTitle() + ".txt"))) {
                reportModel.addRow(new String[]{stockFrame.getTitle(), "", "", "", ""});
                stockReader.readLine();
                while (stockReader.ready()) {
                    String[] temp = stockReader.readLine().split(" ");
                    Date tempDate = new Date(Integer.parseInt(temp[0]) - 1900, Integer.parseInt(temp[1]) - 1, Integer.parseInt(temp[2]));
                    if (temp[4].equals("S") && ((option.equals("all")) || (tempDate.compareTo(startDate) >= 0 && tempDate.compareTo(endDate) <= 0))) {
                        reportModel.addRow(new String[]{"", temp[0], temp[1], temp[2], money.format(Double.parseDouble(temp[11]))});
                        total += Double.parseDouble(money.format(Double.parseDouble(temp[11])).replace("$", "").replace(",", ""));
                    }
                }
                reportModel.addRow(new String[]{});
                reportModel.addRow(new String[]{"", "", "", "", "Total: " + money.format(total)});
            } catch (IOException ex) {
            }
            display();
        }
    }

    private boolean checkDates() {
        if (reportOptionsFrame.getStartDate().compareTo(reportOptionsFrame.getEndDate()) > 0) {
            warningFrame.displayWarning("The \"Start Date\" must come before the \"End Date\".  Please review your entries!");
            return false;
        } else {
            return true;
        }
    }

    public void clearReportTable() {
        reportModel.setRowCount(0);
    }

    public void print() {
        try {
            reportTable.print();
        } catch (PrinterException ex) {
            warningFrame.displayWarning("Printing error.  Sorry for the inconvenience!");
        }
    }
}
