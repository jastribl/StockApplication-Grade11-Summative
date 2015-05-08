package stockapplicationfileconverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import static stockapplicationfileconverter.StockApplicationFileConverter.*;

public class Stock {

    private final String name;
    private int startingNum;
    private double startingACBTotal;
    private double startingACBPerUnit;
    private final ArrayList<Entry> entries;

    public Stock(String name, int startingNum, double startingACBTotal) {
        this.name = name;
        this.startingNum = startingNum;
        this.startingACBTotal = startingACBTotal;
        if (startingNum == 0) {
            startingACBPerUnit = 0;
        } else {
            startingACBPerUnit = startingACBTotal / startingNum;
        }
        entries = new ArrayList();
    }

    public final String getName() {
        return name;
    }

    public final int getStartingNum() {
        return startingNum;
    }

    public final double getStartingACBTotal() {
        return startingACBTotal;
    }

    public final double getStartingACBPerUnit() {
        return startingACBPerUnit;
    }

    public final ArrayList<Entry> getEntries() {
        return entries;
    }

    public final void addEntry(Entry entry) {
        entries.add(entry);
        sortEntries();
        calulateEntries(true);
    }

    public final void removeEntry(int index) {
        entries.remove(index);
        calulateEntries(true);
    }

    public final void setStartingValues(int num, double acbTotal, double acbPerUnit) {
        startingNum = num;
        startingACBTotal = acbTotal;
        startingACBPerUnit = acbPerUnit;
        calulateEntries(true);
    }

    public final void editEntry(int index, Entry entry) {
        entries.set(index, entry);
        sortEntries();
        calulateEntries(true);
    }

    private void sortEntries() {
        Collections.sort(entries, new Comparator<Entry>() {
            @Override
            public int compare(Entry entry1, Entry entry2) {
                if (entry1.getYear() == entry2.getYear()) {
                    if (entry1.getMonth() == entry2.getMonth()) {
                        if (entry1.getDay() == entry2.getDay()) {
                            if (entry1.getTradeNum() == entry2.getTradeNum()) {
                                return 0;
                            } else {
                                return entry1.getTradeNum() > entry2.getTradeNum() ? 1 : -1;
                            }
                        } else {
                            return entry1.getDay() > entry2.getDay() ? 1 : -1;
                        }
                    } else {
                        return entry1.getMonth() > entry2.getMonth() ? 1 : -1;
                    }
                } else {
                    return entry1.getYear() > entry2.getYear() ? 1 : -1;
                }
            }
        });
    }

    public final void calulateEntries(boolean displayProblems) {
        if (entries.size() > 0) {
            int firstBadRow = -1;
            Entry firstEntry = entries.get(0);
            char tempp = firstEntry.getBS();
            if (tempp == 'B') {
                firstEntry.setTotalQuantity(startingNum + firstEntry.getQuantity());
                firstEntry.setACBTotal(startingACBTotal + (firstEntry.getPrice() * firstEntry.getQuantity()) + firstEntry.getCommission());
                firstEntry.setACBIndevidual(firstEntry.getACBTotal() / firstEntry.getTotalQuantity());
            } else if (tempp == 'S') {
                firstEntry.setTotalQuantity(startingNum - firstEntry.getQuantity());
                if (firstEntry.getTotalQuantity() < 0) {
                    if (displayProblems) {
                        warningWindow.displayWarning("You cannot sell stocks that you do not have.", 0);
                        firstBadRow = 0;
                    } else {
                        warningWindow.displayWarning("You have errors in: " + getName());
                    }
                }
                if (firstEntry.getTotalQuantity() == 0) {
                    firstEntry.setACBTotal(0);
                    firstEntry.setACBIndevidual(0);
                } else {
                    firstEntry.setACBTotal(getStartingACBTotal() - (firstEntry.getQuantity() * getStartingACBTotal() / getStartingNum()));
                    firstEntry.setACBIndevidual(firstEntry.getACBTotal() / firstEntry.getTotalQuantity());
                }
                firstEntry.setCapitalGain(((firstEntry.getPrice() * firstEntry.getQuantity()) - firstEntry.getCommission()) - (getStartingACBPerUnit() * firstEntry.getQuantity()));
            }
            entries.set(0, firstEntry);
            for (int i = 1; i < entries.size(); i++) {
                Entry entry = entries.get(i), previousEntry = entries.get(i - 1);
                char temp = entry.getBS();
                if (temp == 'B') {
                    entry.setTotalQuantity(previousEntry.getTotalQuantity() + entry.getQuantity());
                    entry.setACBTotal(previousEntry.getACBTotal() + (entry.getPrice() * entry.getQuantity()) + entry.getCommission());
                    entry.setACBIndevidual(entry.getACBTotal() / entry.getTotalQuantity());
                } else if (temp == 'S') {
                    entry.setTotalQuantity(previousEntry.getTotalQuantity() - entry.getQuantity());
                    if (entry.getTotalQuantity() < 0 && firstBadRow == -1) {
                        if (displayProblems) {
                            warningWindow.displayWarning("You cannot sell stocks that you do not have.", 0);
                            firstBadRow = i;
                        } else {
                            warningWindow.displayWarning("You have errors in: " + getName());
                        }
                    }
                    if (entry.getTotalQuantity() == 0) {
                        entry.setACBTotal(0);
                        entry.setACBIndevidual(0);
                    } else {
                        entry.setACBTotal(previousEntry.getACBTotal() - (entry.getQuantity() * previousEntry.getACBTotal() / previousEntry.getTotalQuantity()));
                        entry.setACBIndevidual(entry.getACBTotal() / entry.getTotalQuantity());
                    }
                    entry.setCapitalGain(((entry.getPrice() * entry.getQuantity()) - entry.getCommission()) - (previousEntry.getACBIndevidual() * entry.getQuantity()));
                }
                entries.set(i, entry);
            }
            stocks.saveStocks();
            if (firstBadRow != -1 && displayProblems) {
                warningWindow.displayWarning("THERE WAS AN ERROR", 0);
            }
        }
    }
}
