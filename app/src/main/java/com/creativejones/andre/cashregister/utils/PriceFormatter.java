package com.creativejones.andre.cashregister.utils;


import java.text.NumberFormat;
import java.util.Locale;

public class PriceFormatter {

    public static String formattPrice(double price) {
        NumberFormat format = getCostFormat();

        return format.format(price);
    }

    private static NumberFormat getCostFormat() {
        NumberFormat costFormat = NumberFormat.getCurrencyInstance(Locale.US);
        costFormat.setMinimumFractionDigits( 2 );
        costFormat.setMaximumFractionDigits( 2 );

        return costFormat;
    }
}
