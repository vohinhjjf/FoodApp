package com.example.doandd.model;

import java.util.HashMap;
import java.util.Map;

public class UserModel {
    String id;
    String name;
    String birthday;
    String gender;
    String email;
    String phone;
    String address;
    String image;

    public UserModel(String id, String name,String birthday,String gender, String email,String phone,String address,String image){
        this.id =id;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.image = image;
    }

    public String getId(){
        return id;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("name", name);
        data.put("birthday", birthday);
        data.put("gender", gender);
        data.put("email", email);
        data.put("phone", phone);
        data.put("address", address);
        data.put("image", image);
        return data;
    }
}
