package stockapplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class StockApplication implements ActionListener {

    public static StockApplication listener = new StockApplication();
    public static MainFrame mainFrame = new MainFrame();
    public static StockFrame stockFrame = new StockFrame();
    public static AddStockFrame addStockFrame = new AddStockFrame();
    private static final AddEntryFrame addEntryFrame = new AddEntryFrame();
    public static EditStartingValuesFrame editStartingValuesFrame = new EditStartingValuesFrame();
    public static WarningFrame warningFrame = new WarningFrame();
    private static final ReportFrame reportFrame = new ReportFrame();
    public static ReportOptionsFrame reportOptionsFrame = new ReportOptionsFrame();
    private static final HelpFrame helpFrame = new HelpFrame();

    public static void main(String[] args) throws IOException {
        try {
            try (BufferedReader stockReader = new BufferedReader(new FileReader("stocks.txt"))) {
                while (stockReader.ready()) {
                    mainFrame.mainListModel.addElement(stockReader.readLine());
                }
            }
        } catch (FileNotFoundException ex) {
            try (PrintWriter stockWriter = new PrintWriter(new FileWriter("stocks.txt"))) {
                stockWriter.print("");
            }
        }
        mainFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonName = e.getActionCommand();
        switch (buttonName) {
            case "New Stock":
                addStockFrame.display();
                break;
            case "Remove Stock":
                mainFrame.removeStock();
                break;
            case "Edit / View Stock":
                stockFrame.display();
                break;
            case "New Entry":
                addEntryFrame.display("New Entry");
                break;
            case "Remove Entry":
                stockFrame.removeEntry();
                break;
            case "Edit / View Entry":
                addEntryFrame.display("Edit / View Entry");
                break;
            case "Add Stock":
                mainFrame.addStock(addStockFrame.addStockField.getText().toUpperCase(), addStockFrame.addStockNumField.getText(), addStockFrame.addStockAdjCostBaseField.getText());
                break;
            case "Add Entry":
                addEntryFrame.checkAddEditEntrys("add");
                break;
            case "Edit Entry":
                addEntryFrame.checkAddEditEntrys("edit");
                break;
            case "Apply":
                editStartingValuesFrame.editStartingValues();
                break;
            case "OK":
                warningFrame.setVisible(false);
                break;
            case "Exit":
                System.exit(0);
                break;
            case "Full Report":
                reportOptionsFrame.display("Full");
                break;
            case "Report This Stock":
                reportOptionsFrame.display("Stock");
                break;
            case "Get Full - All":
                reportFrame.reportAll("all");
                break;
            case "Get Full - Range":
                reportFrame.reportAll("range");
                break;
            case "Get Stock - All":
                reportFrame.reportOne("all");
                break;
            case "Get Stock - Range":
                reportFrame.reportOne("range");
                break;
            case "Print":
                reportFrame.print();
                break;
            case "Stock Help":
                helpFrame.display(buttonName, "STOCK HELP\n\nFrom the main screen, you can:\n\t--Add a stock via the \"New Stock\" button\n\t--Remove a stock via the \"Remove Stock\" button\n\t--View further details about a stock via the \"Edit / View Stock\" button\n\t--Exit the program via File\"Exit\"\n\t--View a stock report including all stocks ranging from:\n\t\t--All dates\n\t\t--A Date Range selected by you\n\n\t--Get help via:\n\t\t--Help\"Stock Help\"\n\t\t--Help\"Entry Help\"\n\n\tWhile creating a new stock or creating a full Capital gains report, if any of your entries are entered incorrectly, you will be prompted with errors / suggestions regarding the problem.  Some errors will not let you proceed with entering your stock, while others may allow you to proceed with entering your stock but will advise you to change your entry.\n\nNote  -  ACB  Adjusted Cost Base ");
                break;
            case "Entry Help":
                helpFrame.display(buttonName, "ENTRY HELP\n\nFrom the individual stock screen, you can:\n\t--Add an entry via the \"New Entry\" button\n\t--Remove an entry via the \"Remove Entry\" button\n\t--View editable fields for the selected entry via the \"Edit / View Entry\" button\n\t--View an individual stock report ranging from:\n\t\t--All dates\n\t\t--A Date Range selected by you\n\n\t--Get help via:\n\t\t--Help\"Stock Help\"\n\t\t--Help\"Entry Help\"\n\n\tWhile creating a new entry or creating an individual Capital gains report, if any of your entries are entered incorrectly, you will be prompted with errors / suggestions regarding the problem. Some errors will not let you proceed with your entry, while others may allow you to proceed with your entry but will advise you to change your entry.\n\nNote  -  ACB  Adjusted Cost Base ");
                break;
            default:
                warningFrame.displayWarning("Unsupported Button!  Consult the programmer.");
                break;
        }
    }
}
