package stockapplication;

import javax.swing.*;
import static stockapplication.StockApplication.mainFrame;
import static stockapplication.StockApplication.reportFrame;
import static stockapplication.StockApplication.stockFrame;
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
        for (int i = 2011; i <= new JDate().getYear(); i++) {
            yearBox.addItem(i);
        }
    }

    public final void displayForQuickCapitalGainsReport(String stocksToDo, boolean resetFields) {
        reportOptionsPanel.removeAll();
        reportOptionsPanel.add(startDateBox);
        reportOptionsPanel.add(new JLabel("          to          "));
        reportOptionsPanel.add(endDateBox);
        if (resetFields) {
            startDateBox.setYear(2011);
            startDateBox.setMonth(1);
            startDateBox.setDay(1);
            endDateBox.setToToday();
        }
        if (stocksToDo.equals("ALL") && mainFrame.mainListModel.getSize() < 1) {
            warningWindow.displayWarning("You must have at least one stock to view a report!");
        } else {
            if (new InputFrame(reportOptionsPanel, stocksToDo.equals("ALL") ? mainFrame : stockFrame, "Quick Captital Gains Report Options - " + stocksToDo, new Object[]{startDateBox, endDateBox, new JLabel("          to          ")}, 0).getInput()) {
                if (!(stocksToDo.equals("ALL") ? reportFrame.quickReport("ALL", 0) : reportFrame.quickReport("ONE", mainFrame.mainList.getSelectedIndex()))) {
                    displayForQuickCapitalGainsReport(stocksToDo, false);
                }
            }
        }
    }

    public final void displayForTaxReport(boolean resetFields) {
        reportOptionsPanel.removeAll();
        reportOptionsPanel.add(yearBox);
        if (resetFields) {
            yearBox.setSelectedItem(new JDate().getYear());
        }
        if (mainFrame.mainListModel.getSize() < 1) {
            warningWindow.displayWarning("You must have at least one stock to view a report!");
        } else {
            if (new InputFrame(reportOptionsPanel, mainFrame, "Tax Report Options", new Object[]{yearBox}, 0).getInput()) {
                reportFrame.taxReport();
            }
        }
    }

    public final JDate getStartDate() {
        return startDateBox.getDate();
    }

    public final JDate getEndDate() {
        return endDateBox.getDate();
    }

    public final int getTaxYear() {
        return (int) yearBox.getSelectedItem();
    }
}
