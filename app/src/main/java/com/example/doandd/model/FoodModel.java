package com.example.doandd.model;

import java.util.HashMap;
import java.util.Map;

public class FoodModel {
    String id;
    String name;
    String category;
    String description;
    String image;
    double price;
    int discountPercentage;
    double rate;

    public FoodModel(String name, String category, String description, String image,
                     double price, int discountPercentage, double rate){
        this.name = name;
        this.category = category;
        this.description = description;
        this.image = image;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.rate = rate;
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
    public int getDiscountPercentage(){
        return discountPercentage;
    }
    public String getCategory(){
        return category;
    }
    public double getRate(){
        return rate;
    }
    public String getDescription(){
        return description;
    }
    public String getImage() {return  image;}

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("price", price);
        data.put("discountPercentage", discountPercentage);
        data.put("image", image);
        data.put("category", category);
        data.put("rate", rate);
        data.put("description", description);
        return data;
    }
}
