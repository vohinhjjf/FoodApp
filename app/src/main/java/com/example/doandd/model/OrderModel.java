package com.example.doandd.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderModel {
    String id;
    String addressRecipient;
    String nameRecipient;
    String phoneRecipient;
    double discount;
    double total;
    String orderDate;
    String orderStatus;
    List<CartModel> foods;

    public OrderModel(String id, String nameRecipient, String addressRecipient,
                      String phoneRecipient, double discount, double total,
                      String orderDate, String orderStatus, List<CartModel> foods){
        this.id = id;
        this.nameRecipient = nameRecipient;
        this.addressRecipient = addressRecipient;
        this.phoneRecipient = phoneRecipient;
        this.discount = discount;
        this.total = total;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.foods = foods;
    }
    public String getId(){
        return id;
    }
    public String getNameRecipient(){
        return nameRecipient;
    }
    public String getAddressRecipient(){
        return addressRecipient;
    }
    public String getPhoneRecipient(){
        return phoneRecipient;
    }
    public double getDiscount(){
        return discount;
    }
    public double getTotal(){
        return total;
    }
    public String getOrderDate(){
        return orderDate;
    }
    public String getOrderStatus(){
        return orderStatus;
    }
    public List<CartModel> getListFood(){
        return foods;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("nameRecipient", nameRecipient);
        data.put("addressRecipient", addressRecipient);
        data.put("phoneRecipient", phoneRecipient);
        data.put("discount", discount);
        data.put("total", total);
        data.put("orderDate", orderDate);
        data.put("orderStatus", orderStatus);
        data.put("foods", foods.stream().map(CartModel::toMap).collect(Collectors.toList()));
        return data;
    }
}
