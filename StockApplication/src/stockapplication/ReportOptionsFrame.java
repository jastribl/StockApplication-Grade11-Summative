package stockapplication;

import java.awt.GridLayout;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static stockapplication.StockApplication.listener;
import static stockapplication.StockApplication.mainFrame;
import static stockapplication.StockApplication.warningFrame;

public class ReportOptionsFrame extends JFrame {

    public final JButton reportOptionsButton1 = new JButton("Get Full - Range");
    public final JButton reportOptionsButton2 = new JButton("Get Stock - Range");
    public final JComboBox[] reportOptionsBox = new JComboBox[6];

    public ReportOptionsFrame() {
        setTitle("Captital Gains Report Options");
        setLayout(new GridLayout(4, 4));
        JPanel reportOptionsPenel1 = new JPanel();
        reportOptionsPenel1.setLayout(new GridLayout(1, 3));
        for (int i = 0; i < 6; i++) {
            reportOptionsBox[i] = new JComboBox();
        }
        for (int i = 1; i < 13; i++) {
            reportOptionsBox[1].addItem(i);
            reportOptionsBox[4].addItem(i);
        }
        for (int i = 1; i < 32; i++) {
            reportOptionsBox[2].addItem(i);
            reportOptionsBox[5].addItem(i);
        }
        Date date = new Date();
        for (int i = date.getYear() - 100; i > -1; i--) {
            reportOptionsBox[0].addItem(date.getYear() + 1900 - i);
            reportOptionsBox[3].addItem(date.getYear() + 1900 - i);
        }
        reportOptionsBox[0].setSelectedItem(date.getYear() + 1900);
        reportOptionsBox[1].setSelectedItem(date.getMonth() + 1);
        reportOptionsBox[2].setSelectedItem(date.getDate());
        reportOptionsBox[3].setSelectedItem(date.getYear() + 1900);
        reportOptionsBox[4].setSelectedItem(date.getMonth() + 1);
        reportOptionsBox[5].setSelectedItem(date.getDate());
        reportOptionsPenel1.add(reportOptionsBox[0]);
        reportOptionsPenel1.add(reportOptionsBox[1]);
        reportOptionsPenel1.add(reportOptionsBox[2]);
        JPanel reportOptionsPenel2 = new JPanel();
        reportOptionsPenel2.setLayout(new GridLayout(1, 3));
        reportOptionsPenel2.add(reportOptionsBox[3]);
        reportOptionsPenel2.add(reportOptionsBox[4]);
        reportOptionsPenel2.add(reportOptionsBox[5]);
        add(new JLabel("                                  Start"));
        add(new JLabel("                     to"));
        add(new JLabel("End"));
        add(reportOptionsButton1);
        for (int i = 0; i < 4; i++) {
            add(new JLabel("----------------------------------"));
        }
        for (int i = 0; i < 2; i++) {
            add(new JLabel("Year       Month    Day"));
            add(new JLabel(""));
        }
        add(reportOptionsPenel1);
        add(new JLabel("                     to"));
        add(reportOptionsPenel2);
        add(reportOptionsButton2);
        reportOptionsButton1.addActionListener(listener);
        reportOptionsButton2.addActionListener(listener);
        setResizable(false);
    }

    public void display(String option) {
        if (option.equals("Full") && mainFrame.mainListModel.getSize() < 1) {
            warningFrame.displayWarning("You must have at least one stock to view a report!");
        } else {
            reportOptionsButton1.setText("Get " + option + " - All");
            reportOptionsButton2.setText("Get " + option + " - Range");
            pack();
            setLocation(mainFrame.getLocation().x + ((mainFrame.getSize().width - getSize().width) / 2), mainFrame.getLocation().y + ((mainFrame.getSize().height - getSize().height) / 2));
            setVisible(true);
        }
    }

    public Date getStartDate() {
        return new Date(Integer.valueOf(reportOptionsBox[0].getSelectedItem().toString()) - 1900, Integer.valueOf(reportOptionsBox[1].getSelectedItem().toString()) - 1, Integer.valueOf(reportOptionsBox[2].getSelectedItem().toString()));
    }

    public Date getEndDate() {
        return new Date(Integer.valueOf(reportOptionsBox[3].getSelectedItem().toString()) - 1900, Integer.valueOf(reportOptionsBox[4].getSelectedItem().toString()) - 1, Integer.valueOf(reportOptionsBox[5].getSelectedItem().toString()));
    }
}
