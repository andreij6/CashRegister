package com.creativejones.andre.cashregister;


import com.creativejones.andre.cashregister.utils.PriceFormatter;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void priceFormatsCorrectly() throws Exception{
        double price = 2.2;
        String expected = "$2.20";

        String actual = PriceFormatter.formattPrice(price);

        assertEquals(expected, actual);
    }

    @Test
    public void priceFormatChangeCorrectly() throws Exception{
        double price = 0.15;
        String expected = "$0.15";

        String actual = PriceFormatter.formattPrice(price);

        assertEquals(expected, actual);
    }

    @Test
    public void priceFormatChangeMultipleDecimalPlacesCorrectly() throws Exception{
        double price = 0.1586221662;
        String expected = "$0.16";

        String actual = PriceFormatter.formattPrice(price);

        assertEquals(expected, actual);
    }

    @Test
    public void priceFormatsLargeNumbersCorrectly() throws Exception{
        double price = 222222.2;
        String expected = "$222,222.20";

        String actual = PriceFormatter.formattPrice(price);

        assertEquals(expected, actual);
    }
}

