package stockapplication;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static stockapplication.StockApplication.mainFrame;
import static stockapplication.StockApplication.reportFrame;
import static stockapplication.StockApplication.warningWindow;

public class ReportOptionsFrame {

    private final JPanel reportOptionsPanel = new JPanel();
    private final DateBox startDateBox = new DateBox();
    private final DateBox endDateBox = new DateBox();

    public ReportOptionsFrame() {
        reportOptionsPanel.add(startDateBox);
        reportOptionsPanel.add(new JLabel("          to          "));
        reportOptionsPanel.add(endDateBox);
    }

    public void display(String option, boolean reset) {
        if (reset) {
            JDate date = new JDate();
            startDateBox.setYear(2000);
            startDateBox.setMonth(1);
            startDateBox.setDay(1);
            endDateBox.setYear(date.getYear());
            endDateBox.setMonth(date.getMonth());
            endDateBox.setDay(date.getDay());
        }
        if (option.equals("Full") && mainFrame.mainListModel.getSize() < 1) {
            warningWindow.displayWarning("You must have at least one stock to view a report!", 0);
        } else {
            boolean good;
            if (JOptionPane.showConfirmDialog(null, reportOptionsPanel, "Captital Gains Report Options - " + option, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                good = option.equals("Full") ? reportFrame.reportFull() : reportFrame.reportStock();
                if (!good) {
                    display(option, false);
                }
            }
        }
    }

    public JDate getStartDate() {
        return startDateBox.getDate();
    }

    public JDate getEndDate() {
        return endDateBox.getDate();
    }
}
