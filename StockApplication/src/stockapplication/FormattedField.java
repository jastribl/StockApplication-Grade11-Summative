package stockapplication;

import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class FormattedField extends JFormattedTextField {

    private final NumberFormat format;
    private final NumberFormatter formatter;

    public FormattedField(int minimum) {
        format = NumberFormat.getIntegerInstance();
        formatter = new NumberFormatter(format);
        format.setGroupingUsed(false);
        formatter.setMinimum(minimum);
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter);
        setFormatterFactory(factory);
        setValue(0);
    }

    public FormattedField(double minimum) {
        format = NumberFormat.getCurrencyInstance(Locale.CANADA);
        formatter = new NumberFormatter(format);
        format.setMaximumFractionDigits(10);
        formatter.setMinimum(0.0);
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        DefaultFormatterFactory moneyFactory = new DefaultFormatterFactory(formatter);
        setFormatterFactory(moneyFactory);
        setValue(0);
    }
}
