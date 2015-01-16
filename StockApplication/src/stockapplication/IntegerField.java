package stockapplication;

import java.text.NumberFormat;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class IntegerField extends JFormattedTextField {

    public IntegerField(int minimum) {
        NumberFormat format = NumberFormat.getIntegerInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        format.setGroupingUsed(false);
        formatter.setMinimum(minimum);
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter);
        setFormatterFactory(factory);
        setValue(0);
    }
}
