package com.example.doandd.utils;

import java.text.ChoiceFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Format {
    public String currency(double money) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(money);
    }
    public String number(String money){
        NumberFormat format = new DecimalFormat("####");
        return format.format(money);
    }
}
