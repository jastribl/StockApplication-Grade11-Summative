package stockapplication;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
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
import static stockapplication.StockApplication.listener;
import static stockapplication.StockApplication.mainFrame;
import static stockapplication.StockApplication.reportFrame;
import static stockapplication.StockApplication.reportOptionsFrame;
import static stockapplication.StockApplication.stockFrame;
import static stockapplication.StockApplication.warningWindow;

public class ReportFrame extends JFrame {

    private final DefaultTableModel reportModel = new DefaultTableModel(new String[][]{{"", "", "", "", ""}}, new String[]{"Stock", "Year", "Month", "Day", "Capital Gains / Losses"}) {
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
        printMenuItem.addActionListener((ActionListener) listener);
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
        reportTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
        reportTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "Tab");
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
                    reportFrame.setVisible(false);
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
        setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - getSize().height) / 2));
        setVisible(true);
    }

    public boolean reportFull() {
        if (datesGood()) {
            JDate startDate = reportOptionsFrame.getStartDate(), endDate = reportOptionsFrame.getEndDate();
            clearReportTable();
            DecimalFormat money = new DecimalFormat("$#,##0.00;$-#,##0.00");
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
                        JDate tempDate = new JDate(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
                        if (temp[4].equals("S") && (tempDate.compareTo(startDate) >= 0 && tempDate.compareTo(endDate) <= 0)) {
                            reportModel.addRow(new String[]{"", temp[0], temp[1], temp[2], money.format(Double.parseDouble(temp[11]))});
                            totals[i] += Double.parseDouble(money.format(Double.parseDouble(temp[11])).replace("$", "").replaceAll(",", ""));
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
            reportModel.addRow(new String[]{});
            mainFrame.setVisible(false);
            display(false, "Full");
            return true;
        } else {
            warningWindow.displayWarning("The \"Start Date\" must come before the \"End Date\".", 1);
        }
        return false;
    }

    public boolean reportStock() {
        if (datesGood()) {
            JDate startDate = reportOptionsFrame.getStartDate(), endDate = reportOptionsFrame.getEndDate();
            clearReportTable();
            DecimalFormat money = new DecimalFormat("$#,##0.00;$-#,##0.00");
            Double total = 0.0;
            try (BufferedReader stockReader = new BufferedReader(new FileReader(stockFrame.getTitle() + ".txt"))) {
                reportModel.addRow(new String[]{stockFrame.getTitle(), "", "", "", ""});
                stockReader.readLine();
                while (stockReader.ready()) {
                    String[] temp = stockReader.readLine().split(" ");
                    JDate tempDate = new JDate(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
                    if (temp[4].equals("S") && (tempDate.compareTo(startDate) >= 0 && tempDate.compareTo(endDate) <= 0)) {
                        reportModel.addRow(new String[]{"", temp[0], temp[1], temp[2], money.format(Double.parseDouble(temp[11]))});
                        total += Double.parseDouble(money.format(Double.parseDouble(temp[11])).replace("$", "").replace(",", ""));
                    }
                }
                reportModel.addRow(new String[]{});
                reportModel.addRow(new String[]{"", "", "", "", "Total: " + money.format(total)});
            } catch (IOException ex) {
            }
            stockFrame.setVisible(false);
            display(true, stockFrame.getTitle());
            return true;
        } else {
            warningWindow.displayWarning("The \"Start Date\" must come before the \"End Date\".", 1);
        }
        return false;
    }

    private boolean datesGood() {
        return reportOptionsFrame.getStartDate().compareTo(reportOptionsFrame.getEndDate()) <= 0;
    }

    public void clearReportTable() {
        reportModel.setRowCount(0);
    }

    public void print() {
        try {
            reportTable.print();
        } catch (PrinterException ex) {
            warningWindow.displayWarning("Printing error.  Sorry for the inconvenience!", 0);
        }
    }
}
