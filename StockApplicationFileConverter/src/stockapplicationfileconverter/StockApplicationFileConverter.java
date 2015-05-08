package stockapplicationfileconverter;

import java.io.*;

public class StockApplicationFileConverter {

    public static final Stocks stocks = new Stocks();
    public static final WarningWindow warningWindow = new WarningWindow();

    public static void main(String[] args) throws IOException {
        try {
            try (BufferedReader stockReader = new BufferedReader(new FileReader("stocks.txt"))) {
                while (stockReader.ready()) {
                    String name = stockReader.readLine();
                    try (BufferedReader indiStockReader = new BufferedReader(new FileReader(name + ".txt"))) {
                        if (indiStockReader.ready()) {
                            Stock stock;
                            String[] stockThing = indiStockReader.readLine().split(" ");
                            stock = new Stock(name, Integer.valueOf(stockThing[8]), Double.valueOf(stockThing[10]));
                            while (indiStockReader.ready()) {
                                String[] a = indiStockReader.readLine().split(" ");
                                for (int i = 0; i < a.length; i++) {
                                    System.out.print(a[i] + " ");
                                }
                                System.out.println("");
                                Entry entry = new Entry(Integer.valueOf(a[0]), Integer.valueOf(a[1]), Integer.valueOf(a[2]), Integer.valueOf(a[3]), a[4].charAt(0), Integer.valueOf(a[5]), Double.valueOf(a[6]), Double.valueOf(a[7]));
                                stock.addEntry(entry);
                            }
                            stocks.add(stock);
                        }
                    } catch (IOException ex) {
                    }
                }
            }
            stocks.saveStocks();
        } catch (FileNotFoundException ex) {
        }
    }
}
