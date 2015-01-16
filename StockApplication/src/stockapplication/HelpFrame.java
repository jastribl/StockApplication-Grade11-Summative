package stockapplication;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpFrame {

    private final JPanel helpPanel = new JPanel();
    private final JTextArea helpField = new JTextArea();
    private final String titles[] = new String[]{"Stock Help", "Entry Help"};
    private final String messages[] = new String[]{"STOCK HELP\n\nFrom the main screen, you can:\n\t--Add a stock via the \"New Stock\" button\n\t--Remove a stock via the \"Remove Stock\" button\n\t--View further details about a stock via the \"Edit / View Stock\" button\n\t--Exit the program via File\"Exit\"\n\t--View a stock report including all stocks ranging from:\n\t\t--All dates\n\t\t--A Date Range selected by you\n\n\t--Get help via:\n\t\t--Help\"Stock Help\"\n\t\t--Help\"Entry Help\"\n\n\tWhile creating a new stock or creating a full Capital gains report, if any of your entries are entered incorrectly, you will be prompted with errors / suggestions regarding the problem.  Some errors will not let you proceed with entering your stock, while others may allow you to proceed with entering your stock but will advise you to change your entry.\n\nNote  -  ACB  Adjusted Cost Base ", "ENTRY HELP\n\nFrom the individual stock screen, you can:\n\t--Add an entry via the \"New Entry\" button\n\t--Remove an entry via the \"Remove Entry\" button\n\t--View editable fields for the selected entry via the \"Edit / View Entry\" button\n\t--View an individual stock report ranging from:\n\t\t--All dates\n\t\t--A Date Range selected by you\n\n\t--Get help via:\n\t\t--Help\"Stock Help\"\n\t\t--Help\"Entry Help\"\n\n\tWhile creating a new entry or creating an individual Capital gains report, if any of your entries are entered incorrectly, you will be prompted with errors / suggestions regarding the problem. Some errors will not let you proceed with your entry, while others may allow you to proceed with your entry but will advise you to change your entry.\n\nNote  -  ACB  Adjusted Cost Base "};

    public HelpFrame() {
        helpField.setRows(25);
        helpField.setColumns(60);
        helpField.setEditable(false);
        helpField.setLineWrap(true);
        helpField.setTabSize(3);
        helpField.setWrapStyleWord(true);
        helpPanel.add(new JScrollPane(helpField));
    }

    public final void display(int message) {
        helpField.setText(messages[message]);
        JOptionPane.showConfirmDialog(null, helpPanel, titles[message], JOptionPane.DEFAULT_OPTION);
    }
}
