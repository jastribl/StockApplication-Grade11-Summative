package stockapplication;

import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JFormattedTextField;
import javax.swing.text.*;

public class MoneyField extends JFormattedTextField {

    public MoneyField(double minimum) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.CANADA);
        NumberFormatter formatter = new NumberFormatter(format);
        format.setMaximumFractionDigits(10);
        formatter.setMinimum(0.0);
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        DefaultFormatterFactory moneyFactory = new DefaultFormatterFactory(formatter);
        setFormatterFactory(moneyFactory);
        setValue(0);
    }
}
