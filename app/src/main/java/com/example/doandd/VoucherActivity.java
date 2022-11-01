package com.example.doandd;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.adapter.CouponAdapter;
import com.example.doandd.login.LoginActivity;
import com.example.doandd.model.CouponModel;
import com.example.doandd.model.best_seller_food;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class VoucherActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    List<CouponModel> list_coupon_km = new ArrayList<>();
    List<CouponModel> list_coupon_cn = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        RecyclerView recyclerView_coupon_km = findViewById(R.id.rcv_coupon_km);
        RecyclerView recyclerView_coupon_ca_nhan = findViewById(R.id.rcv_coupon_ca_nhan);
        list_coupon_km.add(new CouponModel("Khuyến mãi 15k","31/10/2022"));
        list_coupon_km.add(new CouponModel("Giảm 5k đơn từ 30k","31/10/2022"));
        list_coupon_km.add(new CouponModel("Khuyến mãi 30k","31/10/2022"));
        list_coupon_km.add(new CouponModel("Giảm 15k đơn từ 50k","31/10/2022"));
        list_coupon_km.add(new CouponModel("Giảm 25k đơn từ 100k","31/10/2022"));
        list_coupon_km.add(new CouponModel("Giảm 35k đơn từ 150k","31/10/2022"));
        list_coupon_km.add(new CouponModel("Giảm 45k đơn từ 200k","31/10/2022"));
        list_coupon_km.add(new CouponModel("Giảm 55k đơn từ 250k","31/10/2022"));
        CouponAdapter couponAdapterKm = new CouponAdapter(list_coupon_km);
        recyclerView_coupon_km.setAdapter(couponAdapterKm);
        recyclerView_coupon_km.setLayoutManager(
                new LinearLayoutManager(this));
        CouponAdapter couponAdapterCn = new CouponAdapter(list_coupon_cn);
        recyclerView_coupon_ca_nhan.setAdapter(couponAdapterCn);
        recyclerView_coupon_ca_nhan.setLayoutManager(
                new LinearLayoutManager(this));
        //
        Button btnCaNhan = (Button) findViewById(R.id.btnCaNhan);
        Button btnKhuyenMai = (Button) findViewById(R.id.btnKhuyenMai);
        btnCaNhan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                btnCaNhan.setBackgroundResource(R.drawable.rounded_coupon);
                btnCaNhan.setTextAppearance(R.style.blueText);
                recyclerView_coupon_ca_nhan.setVisibility(View.VISIBLE);
                btnKhuyenMai.setBackgroundResource(R.color.white);
                btnKhuyenMai.setTextAppearance(R.style.grayText);
                recyclerView_coupon_km.setVisibility(View.INVISIBLE);
            }
        });

        btnKhuyenMai.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                btnKhuyenMai.setBackgroundResource(R.drawable.rounded_coupon);
                btnKhuyenMai.setTextAppearance(R.style.blueText);
                recyclerView_coupon_km.setVisibility(View.VISIBLE);
                btnCaNhan.setBackgroundResource(R.color.white);
                btnCaNhan.setTextAppearance(R.style.grayText);
                recyclerView_coupon_ca_nhan.setVisibility(View.INVISIBLE);
            }
        });
        //
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.voucher);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            switch(id){
                //check id
                case R.id.home: {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.voucher:{}
                case R.id.account: {
                    Intent intent = new Intent(this, AccountActivity.class);
                    startActivity(intent);
                    break;
                }
            }
            return true;
        });
    }
}
