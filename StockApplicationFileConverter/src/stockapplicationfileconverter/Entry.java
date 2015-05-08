package stockapplicationfileconverter;

public class Entry implements Comparable {

    private final Object values[];

    public Entry(int year, int month, int day, int tradeNum, char BS, int quantity, double price, double commission) {
        values = new Object[12];
        values[0] = year;
        values[1] = month;
        values[2] = day;
        values[3] = tradeNum;
        values[4] = BS;
        values[5] = quantity;
        values[6] = price;
        values[7] = commission;
        values[8] = 0;
        values[9] = 0.0;
        values[10] = 0.0;
        values[11] = 0.0;
    }

    public final Object[] getValues() {
        return values;
    }

    public final int getYear() {
        return (int) values[0];
    }

    public final int getMonth() {
        return (int) values[1];
    }

    public final int getDay() {
        return (int) values[2];
    }

    public final JDate getDate() {
        return new JDate(getYear(), getMonth(), getDay());
    }

    public final int getTradeNum() {
        return (int) values[3];
    }

    public final char getBS() {
        return (char) values[4];
    }

    public final int getQuantity() {
        return (int) values[5];
    }

    public final double getPrice() {
        return (double) values[6];
    }

    public final double getCommission() {
        return (double) values[7];
    }

    public final int getTotalQuantity() {
        return (int) values[8];
    }

    public final double getACBIndevidual() {
        return (double) values[9];
    }

    public final double getACBTotal() {
        return (double) values[10];
    }

    public final double getCapitalGain() {
        return (double) values[11];
    }

    public final void setYear(int year) {
        values[0] = year;
    }

    public final void setMonth(int month) {
        values[1] = month;
    }

    public final void setDay(int day) {
        values[2] = day;
    }

    public final void setTradeNum(int tradeNum) {
        values[3] = tradeNum;
    }

    public final void setBS(char bs) {
        values[4] = bs;
    }

    public final void setQuantity(int quantity) {
        values[5] = quantity;
    }

    public final void setPrice(double price) {
        values[6] = price;
    }

    public final void setCommission(double commission) {
        values[7] = commission;
    }

    public final void setTotalQuantity(int totalQuantity) {
        values[8] = totalQuantity;
    }

    public final void setACBIndevidual(double acbIndevidual) {
        values[9] = acbIndevidual;
    }

    public final void setACBTotal(double acbTotal) {
        values[10] = acbTotal;
    }

    public final void setCapitalGain(double capitalGains) {
        values[11] = capitalGains;
    }

    @Override
    public int compareTo(Object e) {
        Entry entry2 = (Entry) e;
        if (getYear() == entry2.getYear()) {
            if (getMonth() == entry2.getMonth()) {
                if (getDay() == entry2.getDay()) {
                    if (getTradeNum() == entry2.getTradeNum()) {
                        return 0;
                    } else {
                        return getTradeNum() > entry2.getTradeNum() ? 1 : -1;
                    }
                } else {
                    return getDay() > entry2.getDay() ? 1 : -1;
                }
            } else {
                return getMonth() > entry2.getMonth() ? 1 : -1;
            }
        } else {
            return getYear() > entry2.getYear() ? 1 : -1;
        }
    }
}
