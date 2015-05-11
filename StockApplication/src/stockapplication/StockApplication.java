package stockapplication;

import java.text.DecimalFormat;

public class StockApplication {

    public static final Stocks stocks = new Stocks();
    public static final MainFrame mainFrame = new MainFrame();
    public static final StockFrame stockFrame = new StockFrame();
    public static final AddStockFrame addStockFrame = new AddStockFrame();
    public static final AddEntryFrame addEntryFrame = new AddEntryFrame();
    public static final EditStartingValuesFrame editStartingValuesFrame = new EditStartingValuesFrame();
    public static final WarningWindow warningWindow = new WarningWindow();
    public static final ConfirmWindow confirmWindow = new ConfirmWindow();
    public static final ReportFrame reportFrame = new ReportFrame();
    public static final ReportOptionsFrame reportOptionsFrame = new ReportOptionsFrame();
    public static final HelpFrame helpFrame = new HelpFrame();
    public static final DecimalFormat moneyFormat = new DecimalFormat("$#,##0.00;$-#,##0.00");

    public final static void main(String[] args) {
        stocks.readStocksFromFile();
        mainFrame.display();
    }
}
