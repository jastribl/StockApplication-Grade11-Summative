package stockapplication;

import java.util.ArrayList;

public class Stock {

    private final String name;
    private int startingNum;
    private double startingACB;
    private ArrayList<Entry> entries;

    public Stock(String name, int startingNum, int startingACB) {
        this.name = name;
        this.startingNum = startingNum;
        this.startingACB = startingACB;
        entries = new ArrayList();
    }

    public final String getName() {
        return name;
    }

    public final int getStartingNum() {
        return startingNum;
    }

    public final double getStartingACB() {
        return startingACB;
    }

    public final ArrayList<Entry> getEntries() {
        return entries;
    }

    public final void setStartingNum(int num) {
        startingNum = num;
    }

    public final void setStartingACB(double acb) {
        startingACB = acb;
    }

    public final void setEntries(ArrayList<Entry> ents) {
        entries = ents;
    }

    public void addEntrie(Entry entry) {
        entries.add(entry);
    }
}
