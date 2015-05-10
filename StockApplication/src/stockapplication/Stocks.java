package stockapplication;

import java.io.*;
import java.util.*;

public class Stocks extends ArrayList<Stock> {

    public void readStocksFromFile() {
        try (DataInputStream stockReader = new DataInputStream(new FileInputStream("stocks.StockFile"))) {
            int numberOfStocks = stockReader.readInt();
            for (int i = 0; i < numberOfStocks; i++) {
                Stock stock = new Stock(stockReader.readUTF(), stockReader.readInt(), stockReader.readDouble());
                int numberOfEntries = stockReader.readInt();
                for (int j = 0; j < numberOfEntries; j++) {
                    stock.getEntries().add(new Entry(stockReader.readInt(), stockReader.readInt(), stockReader.readInt(), stockReader.readInt(), stockReader.readChar(), stockReader.readInt(), stockReader.readDouble(), stockReader.readDouble()));
                }
                add(stock);
            }
            sortStocks();
            calculateAllStocks();
        } catch (FileNotFoundException ex) {
            try (DataOutputStream stockWriter = new DataOutputStream(new FileOutputStream("stocks.StockFile"))) {
                stockWriter.writeInt(0);
            } catch (IOException ex2) {
            }
        } catch (IOException ex) {
        }
    }

    public final void saveStocks() {
        try (DataOutputStream stockWriter = new DataOutputStream(new FileOutputStream("stocks.StockFile"))) {
            int numberOfStocks = size();
            stockWriter.writeInt(numberOfStocks);
            for (int i = 0; i < numberOfStocks; i++) {
                Stock stock = get(i);
                stockWriter.writeUTF(stock.getName());
                stockWriter.writeInt(stock.getStartingNum());
                stockWriter.writeDouble(stock.getStartingACBTotal());
                int numberOfEntries = stock.getEntries().size();
                stockWriter.writeInt(numberOfEntries);
                for (int j = 0; j < numberOfEntries; j++) {
                    Entry entry = stock.getEntries().get(j);
                    stockWriter.writeInt(entry.getYear());
                    stockWriter.writeInt(entry.getMonth());
                    stockWriter.writeInt(entry.getDay());
                    stockWriter.writeInt(entry.getTradeNum());
                    stockWriter.writeChar(entry.getBS());
                    stockWriter.writeInt(entry.getQuantity());
                    stockWriter.writeDouble(entry.getPrice());
                    stockWriter.writeDouble(entry.getCommission());
                }
            }
        } catch (IOException ex) {
        }
    }

    public final void addStock(Stock stock) {
        add(stock);
        sortStocks();
        saveStocks();
    }

    public final void removeStockAt(int index) {
        remove(index);
        saveStocks();
    }

    private void sortStocks() {
        Collections.sort(this, new Comparator<Stock>() {
            @Override
            public int compare(Stock stock1, Stock stock2) {
                return stock1.getName().compareTo(stock2.getName());
            }
        });
    }

    private void calculateAllStocks() {
        for (Stock stock : this) {
            stock.calulateEntries(false);
        }
    }
}
