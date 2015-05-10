package stockapplication;

import java.awt.GridLayout;
import javax.swing.*;
import static stockapplication.StockApplication.mainFrame;
import static stockapplication.StockApplication.reportFrame;
import static stockapplication.StockApplication.warningWindow;

public class ReportOptionsFrame {

    private final JPanel reportOptionsPanel;
    private final DateBox startDateBox;
    private final DateBox endDateBox;
    private final JComboBox yearBox;

    public ReportOptionsFrame() {
        reportOptionsPanel = new JPanel();
        startDateBox = new DateBox();
        endDateBox = new DateBox();
        yearBox = new JComboBox();
        for (int i = 2000; i <= new JDate().getYear() + 1; i++) {
            yearBox.addItem(i);
        }
    }

    public void displayForQuickCapitalGainsReport(String stocksToDo, boolean resetFields) {
        reportOptionsPanel.removeAll();
        reportOptionsPanel.add(startDateBox);
        reportOptionsPanel.add(new JLabel("          to          "));
        reportOptionsPanel.add(endDateBox);
        if (resetFields) {
            startDateBox.setYear(2000);
            startDateBox.setMonth(1);
            startDateBox.setDay(1);
            endDateBox.setToToday();
        }
        if (stocksToDo.equals("ALL") && mainFrame.mainListModel.getSize() < 1) {
            warningWindow.displayWarning("You must have at least one stock to view a report!");
        } else {
            if (JOptionPane.showConfirmDialog(null, reportOptionsPanel, "Quick Captital Gains Report Options - " + stocksToDo, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                if (!(stocksToDo.equals("ALL") ? reportFrame.quickReport("ALL", 0) : reportFrame.quickReport("ONE", mainFrame.mainList.getSelectedIndex()))) {
                    displayForQuickCapitalGainsReport(stocksToDo, false);
                }
            }
        }
    }

    public void displayForTaxReport(boolean resetFields) {
        reportOptionsPanel.removeAll();
        reportOptionsPanel.add(yearBox);
        if (resetFields) {
            yearBox.setSelectedItem(2000);
        }
        if (mainFrame.mainListModel.getSize() < 1) {
            warningWindow.displayWarning("You must have at least one stock to view a report!");
        } else {
            if (JOptionPane.showConfirmDialog(null, reportOptionsPanel, "Tax Report Options", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                reportFrame.taxReport();
            }
        }
    }

    public JDate getStartDate() {
        return startDateBox.getDate();
    }

    public JDate getEndDate() {
        return endDateBox.getDate();
    }

    public final int getTaxYear() {
        return (int) yearBox.getSelectedItem();
    }
}
