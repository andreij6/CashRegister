package com.creativejones.andre.cashregister.utils;

import android.content.Context;

import java.text.NumberFormat;
import java.util.Locale;

public class PriceFormatter {

    public static String formattPrice(Context context, double price) {
        NumberFormat format = getCostFormat(context);

        return format.format(price);
    }

    private static NumberFormat getCostFormat(Context context) {
        Locale current = context.getResources().getConfiguration().locale;

        NumberFormat costFormat = NumberFormat.getCurrencyInstance(current);
        costFormat.setMinimumFractionDigits( 2 );
        costFormat.setMaximumFractionDigits( 2 );

        return costFormat;
    }
}
