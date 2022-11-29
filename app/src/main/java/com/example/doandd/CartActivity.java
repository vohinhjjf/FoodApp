package com.example.doandd;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.adapter.CartAdapter;
import com.example.doandd.model.CartModel;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    TextView tvTotal;
    Button btnBack, btnThanhToan;
    RecyclerView recycList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        tvTotal = findViewById(R.id.tvTotal);
        btnBack = findViewById(R.id.btnBack);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        recycList = findViewById(R.id.recycListCart);
        List<CartModel> list_cart = new ArrayList<>();
        list_cart.add(new CartModel("Bún cá",35000,10,R.drawable.bun_ca,4,1,false));
        list_cart.add(new CartModel("Cơm sườn",30000,5,R.drawable.com_suon,4.5,1,false));
        list_cart.add(new CartModel("Hủ tiếu bò kho",40000,3,R.drawable.hu_tieu_bo_kho,4.6,1,false));
        list_cart.add(new CartModel("Cơm gà xôi mỡ",45000,5,R.drawable.com_ga_xoi_mo,4.6,1,false));
        list_cart.add(new CartModel("Bún bò huế",65000,10,R.drawable.bun_bo_hue,4.6,1,false));
        list_cart.add(new CartModel("Phở bò",60000,15,R.drawable.pho_bo,4.6,1,false));
        CartAdapter cartAdapter = new CartAdapter(this,list_cart);
        recycList.setAdapter(cartAdapter);
        recycList.setLayoutManager(
                new LinearLayoutManager(CartActivity.this));
        //Back
        btnBack.setOnClickListener(view ->{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}
