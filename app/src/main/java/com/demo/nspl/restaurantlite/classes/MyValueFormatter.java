package com.demo.nspl.restaurantlite.classes;

/**
 * Created by Desktop on 5/8/2018.
 */
import com.github.mikephil.charting.utils.ValueFormatter;

public class MyValueFormatter implements ValueFormatter {

    private static String[] SUFFIX = new String[] {
            "", "K", "M", "B", "T"
    };

    private static int MAX_LENGTH = 4;

    private java.text.DecimalFormat mFormat;

    public MyValueFormatter() {

        mFormat = new java.text.DecimalFormat("###E0");
    }

    @Override
    public String getFormattedValue(float value) {
        return makePretty(value);
    }

    /**
     * Formats each number properly. Special thanks to Roman Gromov
     * (https://github.com/romangromov) for this piece of code.
     */
    private String makePretty(double number) {

        String r = mFormat.format(number);

        r = r.replaceAll("E[0-9]", SUFFIX[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);

        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
        }

        return r;
    }

}
