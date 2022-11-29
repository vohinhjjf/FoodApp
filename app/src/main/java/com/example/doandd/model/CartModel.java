package com.example.doandd.model;

public class CartModel {
    String name;
    double price;
    int discountPercentage;
    int image;
    double rate;
    int amount;
    boolean checkbuy;

    public CartModel(String name, double price, int discountPercentage,
            int image, double rate, int amount, boolean checkbuy){
        this.name = name;
        this.price= price;
        this.amount= amount;
        this.discountPercentage = discountPercentage;
        this.image= image;
        this.rate= rate;
        this.checkbuy = checkbuy;
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
    public int getImage(){
        return image;
    }
    public double getRate(){
        return rate;
    }
    public int getAmount(){
        return amount;
    }
    public boolean getCheckbuy(){
        return checkbuy;
    }
}
