package com.example.otech.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatUtils {
    
    private static final NumberFormat currencyFormat = 
            NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    
    private static final SimpleDateFormat dateFormat = 
            new SimpleDateFormat("dd/MM/yyyy HH:mm", new Locale("vi", "VN"));
    
    public static String formatCurrency(double amount) {
        return currencyFormat.format(amount);
    }
    
    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }
    
    public static String formatDiscount(double percent) {
        return String.format(Locale.getDefault(), "%.0f%%", percent);
    }
}
