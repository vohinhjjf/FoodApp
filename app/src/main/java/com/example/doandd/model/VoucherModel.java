package com.example.doandd.model;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class VoucherModel {
    String id;
    boolean active;
    String title;
    double discount;
    double condition;
    String date;

    public VoucherModel(String id, boolean active, String title, double discount, double condition, String date){
        this.id = id;
        this.active = active;
        this.title = title;
        this.discount = discount;
        this.condition = condition;
        this.date = date;
    }
    public String getId() {return id;}
    public String getTitle(){
        return title;
    }
    public String getDate(){
        return date;
     }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("active", active);
        data.put("title", title);
        data.put("discount", discount);
        data.put("condition", condition);
        data.put("datetime", date);
        return data;
    }
}
