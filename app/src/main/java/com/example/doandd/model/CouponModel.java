package com.example.doandd.model;

public class CouponModel {
    String title;
    String date;

    public CouponModel(String title, String date){
        this.title = title;
        this.date = date;
    }

    public String getTitle(){
        return title;
    }
     public String getDate(){
        return date;
     }
}
