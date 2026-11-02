package com.foodease.util;

import java.text.NumberFormat;
import java.util.Locale;

public final class CurrencyFormatter {

    private CurrencyFormatter() {
    }

    public static String formatINR(double amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        return nf.format(amount);
    }
}
