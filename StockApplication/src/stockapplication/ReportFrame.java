package stockapplication;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import static stockapplication.StockApplication.mainFrame;
import static stockapplication.StockApplication.reportOptionsFrame;
import static stockapplication.StockApplication.stockFrame;
import static stockapplication.StockApplication.stocks;
import static stockapplication.StockApplication.warningWindow;

public class ReportFrame extends JFrame {

    private final DefaultTableModel reportModel = new DefaultTableModel(new String[]{"Stock", "Year", "Month", "Day", "Capital Gains / Losses"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    public final JTable reportTable = new JTable(reportModel);

    public ReportFrame() {
        reportTable.getTableHeader().setReorderingAllowed(false);
        reportTable.getTableHeader().setResizingAllowed(false);
        int[] widths = {93, 34, 42, 31, 145};
        int total = 0;
        for (int i = 0; i < 5; i++) {
            reportTable.getColumnModel().getColumn(i).setMinWidth(widths[i]);
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
        printMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                print();
            }
        });
        add(reportPanel);
        setResizable(false);
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
        reportTable.addKeyListener(new KeyListener() {
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
        });
    }

    private void display(boolean option, String title) {
        setTitle((!option ? "" : " ") + title + " Stock Report");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public boolean reportAllStocks() {
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
                reportModel.addRow(new Object[]{stock.getName()});
                for (int j = 0; j < stock.getEntries().size(); j++) {
                    Entry entry = stock.getEntries().get(j);
                    JDate tempDate = new JDate(entry.getYear(), entry.getMonth(), entry.getDay());
                    if (entry.getBS() == 'S' && (tempDate.compareTo(startDate) >= 0 && tempDate.compareTo(endDate) <= 0)) {
                        reportModel.addRow(new Object[]{null, entry.getYear(), entry.getMonth(), entry.getDay(), money.format(entry.getCapitalGain())});
                        totals[i] += entry.getCapitalGain();
                    }
                }
                reportModel.addRow(new String[]{});
                reportModel.addRow(new String[]{null, null, null, null, "Total: " + money.format(totals[i])});
                reportModel.addRow(new String[]{});
                fullTotal += totals[i];
            }
            reportModel.addRow(new String[]{});
            reportModel.addRow(new String[]{null, null, null, null, "Final Total: " + money.format(fullTotal)});
            reportModel.addRow(new String[]{});
            mainFrame.setVisible(false);
            display(false, "Full");
            return true;
        } else {
            warningWindow.displayWarning("The \"Start Date\" must come before the \"End Date\".", 0);
            return false;
        }
    }

    public boolean reportOneStock(int index) {
        if (datesGood()) {
            JDate startDate = reportOptionsFrame.getStartDate(), endDate = reportOptionsFrame.getEndDate();
            clearReportTable();
            DecimalFormat money = new DecimalFormat("$#,##0.00;$-#,##0.00");
            Double total = 0.0;
            Stock stock = stocks.get(index);
            reportModel.addRow(new Object[]{stock.getName()});
            for (int j = 0; j < stock.getEntries().size(); j++) {
                Entry entry = stock.getEntries().get(j);
                JDate tempDate = new JDate(entry.getYear(), entry.getMonth(), entry.getDay());
                if (entry.getBS() == 'S' && (tempDate.compareTo(startDate) >= 0 && tempDate.compareTo(endDate) <= 0)) {
                    reportModel.addRow(new Object[]{null, entry.getYear(), entry.getMonth(), entry.getDay(), money.format(entry.getCapitalGain())});
                    total += entry.getCapitalGain();
                }
            }
            reportModel.addRow(new String[]{});
            reportModel.addRow(new String[]{null, null, null, null, "Final Total: " + money.format(total)});
            reportModel.addRow(new String[]{});
            stockFrame.setVisible(false);
            display(true, stockFrame.getTitle());
            return true;
        } else {
            warningWindow.displayWarning("The \"Start Date\" must come before the \"End Date\".", 0);
            return false;
        }
    }

    private boolean datesGood() {
        return reportOptionsFrame.getStartDate().compareTo(reportOptionsFrame.getEndDate()) <= 0;
    }

    private void clearReportTable() {
        reportModel.setRowCount(0);
    }

    public void print() {
        try {
            reportTable.print();
        } catch (PrinterException ex) {
            warningWindow.displayWarning("Printing error.  Sorry for the inconvenience!");
        }
    }
}
