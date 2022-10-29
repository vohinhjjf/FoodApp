package com.example.doandd.model;

public class best_seller_food {
    private String best_seller_food_name;
    private String best_seller_food_description;
    private int best_seller_food_image ;
    public best_seller_food(String best_seller_food_name, String best_seller_food_description, int  best_seller_food_image) {
        this.best_seller_food_name = best_seller_food_name;
        this.best_seller_food_description = best_seller_food_description;
        this.best_seller_food_image=best_seller_food_image;

    }
    public  best_seller_food(){}

    public void setImage(int image) {
        this.best_seller_food_image = image;
    }
    public int getImage() {
        return best_seller_food_image;
    }

    public String getName() {
        return best_seller_food_name;
    }
    public void setName(String name) {
        this.best_seller_food_name = name;
    }

    public String getDescription() {
        return best_seller_food_description;
    }
    public void setDescription(String description) {this.best_seller_food_description = description;
    }


}
