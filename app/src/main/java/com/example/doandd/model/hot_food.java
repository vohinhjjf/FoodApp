package com.example.doandd.model;

public class hot_food {
    private String hot_food_name;
    private String hot_food_description;
    private int hot_food_image ;

    public hot_food(String hot_food_name, String hot_food_description, int hot_food_image) {
        this.hot_food_name = hot_food_name;
        this.hot_food_description = hot_food_description;
        this.hot_food_image = hot_food_image;
    }

    public void setImage(int image) {
        this.hot_food_image = image;
    }
    public int getImage() {
        return hot_food_image;
    }

    public String getName() {
        return hot_food_name;
    }
    public void setName(String name) {
        this.hot_food_name = name;
    }

    public String getDescription() {
        return hot_food_description;
    }
    public void setDescription(String description) {
        this.hot_food_description = description;
    }


}