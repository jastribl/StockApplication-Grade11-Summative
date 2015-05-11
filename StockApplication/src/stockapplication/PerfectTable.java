package stockapplication;

import java.awt.*;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;
import static stockapplication.StockApplication.warningWindow;

public class PerfectTable extends JPanel {

    private final DefaultTableModel tableModel = new DefaultTableModel() {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public PerfectTable() {
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        add(scrollPane);
    }

    public final JTable getTable() {
        return table;
    }

    public final void setHeader(String[] headers) {
        resetCellRendering();
        tableModel.setColumnIdentifiers(headers);
    }

    public final void fillData(ArrayList<Object[]> data) {
        for (Object[] r : data) {
            tableModel.addRow(r);
        }
    }

    public final void resize() {
        int tableHeight = table.getRowHeight() * (table.getRowCount());
        if (tableHeight > 500) {
            tableHeight = 500;
        }
        table.setPreferredScrollableViewportSize(new Dimension(setAndReturnTableWidths(), tableHeight));
    }

    private int setAndReturnTableWidths() {
        int total = 0;
        for (int i = 0; i < table.getColumnCount(); i++) {
            int width = table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, table.getColumnModel().getColumn(i).getHeaderValue(), false, false, -1, i).getPreferredSize().width;
            for (int j = 0; j < table.getRowCount(); j++) {
                TableCellRenderer renderer = table.getCellRenderer(j, i);
                Component comp = table.prepareRenderer(renderer, j, i);
                width = Math.max(comp.getPreferredSize().width, width);
            }
            width += 10;
            table.getColumnModel().getColumn(i).setPreferredWidth(width);
            total += width;
        }
        return total;
    }

    private void resetCellRendering() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.LEFT);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(r);
        }
    }

    public final void clearTable() {
        tableModel.setRowCount(0);
    }

    public final void print() {
        try {
            table.print(JTable.PrintMode.NORMAL, new MessageFormat("Tax Report"), new MessageFormat("Page - {0}"));
        } catch (PrinterException ex) {
            warningWindow.displayWarning("Printing error.  Sorry for the inconvenience!");
        }
    }

}
