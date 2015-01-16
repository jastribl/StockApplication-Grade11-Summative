package stockapplication;

import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DateBox extends JPanel {

    private final JComboBox[] dateBox;

    public DateBox() {
        dateBox = new JComboBox[3];
        setLayout(new GridLayout(2, 3));
        for (int i = 0; i < 3; i++) {
            dateBox[i] = new JComboBox();
        }
        for (int i = 1; i < 13; i++) {
            dateBox[1].addItem(i);
        }
        for (int i = 1; i < 32; i++) {
            dateBox[2].addItem(i);
        }
        for (int i = 2000; i <= new JDate().getYear() + 1; i++) {
            dateBox[0].addItem(i);
        }
        add(new JLabel("Year"));
        add(new JLabel("Month"));
        add(new JLabel("Day"));
        for (int j = 0; j < 3; j++) {
            add(dateBox[j]);
        }
    }

    public final void setToToday() {
        JDate date = new JDate();
        setYear(date.getYear());
        setMonth(date.getMonth());
        setDay(date.getDay());
    }

    public final JDate getDate() {
        return new JDate(getYear(), getMonth(), getDay());
    }

    public final void setYear(int year) {
        dateBox[0].setSelectedItem(year);
    }

    public final void setMonth(int month) {
        dateBox[1].setSelectedItem(month);
    }

    public final void setDay(int day) {
        dateBox[2].setSelectedItem(day);
    }

    public final int getYear() {
        return (int) dateBox[0].getSelectedItem();
    }

    public final int getMonth() {
        return (int) dateBox[1].getSelectedItem();
    }

    public final int getDay() {
        return (int) dateBox[2].getSelectedItem();
    }
}
