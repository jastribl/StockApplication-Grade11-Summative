package stockapplication;

import java.util.*;

public class JDate implements Comparable {

    private int year, month, day;

    public JDate() {
        setToToday();
    }

    public JDate(int gYear, int gMonth, int gDay) {
        year = gYear;
        month = gMonth;
        day = gDay;
    }

    private void setToToday() {
        Calendar date = Calendar.getInstance(Locale.CANADA);
        year = date.get(Calendar.YEAR);
        month = date.get(Calendar.MONTH) + 1;
        day = date.get(Calendar.DAY_OF_MONTH);
    }

    public final int getYear() {
        return year;
    }

    public final int getMonth() {
        return month;
    }

    public final int getDay() {
        return day;
    }

    @Override
    public int compareTo(Object d) {
        int tY = ((JDate) d).getYear();
        int tM = ((JDate) d).getMonth();
        int tD = ((JDate) d).getDay();
        if (year == tY) {
            if (month == tM) {
                if (day == tD) {
                    return 0;
                } else {
                    return day > tD ? 1 : -1;
                }
            } else {
                return month > tM ? 1 : -1;
            }
        } else {
            return year > tY ? 1 : -1;
        }
    }
}
