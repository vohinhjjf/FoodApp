package com.example.doandd.model;

import java.util.HashMap;
import java.util.Map;

public class CartModel {
    String id;
    String name;
    double price;
    double discountPercentage;
    String image;
    double rate;
    double amount;
    boolean checkbuy;

    public CartModel(String id, String name, double price, double discountPercentage,
                     String image, double rate, double amount, boolean checkbuy){
        this.id = id;
        this.name = name;
        this.price= price;
        this.amount= amount;
        this.discountPercentage = discountPercentage;
        this.image= image;
        this.rate= rate;
        this.checkbuy = checkbuy;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public double getPrice(){
        return price;
    }
    public double getDiscountPercentage(){
        return discountPercentage;
    }
    public String getImage(){
        return image;
    }
    public double getRate(){
        return rate;
    }
    public double getAmount(){
        return amount;
    }
    public boolean getCheckbuy(){
        return checkbuy;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("price", price);
        data.put("discountPercentage", discountPercentage);
        data.put("image", image);
        data.put("rate", rate);
        data.put("amount", amount);
        data.put("checkbuy", checkbuy);
        return data;
    }
}
