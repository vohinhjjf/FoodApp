package com.example.doandd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.adapter.InfoOrderAdapter;
import com.example.doandd.adapter.PaymentAdapter;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.SharedPreference;
import com.example.doandd.model.CartModel;
import com.example.doandd.utils.Format;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InfoOrderActivity extends AppCompatActivity {
    TextView tvId, tvDateOrder, tvStatusOrder, tvNameReceipment, tvPhoneReceipment,
            tvAddressReceipment, tvTongTienHang, tvPhiVanChuyen, tvKhuyenMai, tvTongTien;
    Button btn, btnBackInfoOrder;
    RecyclerView ryListFood;
    FirestoreDatabase fb = new FirestoreDatabase();
    String id="";

    private void findViewsByIds() {
        tvId = findViewById(R.id.tvId);
        tvDateOrder = findViewById(R.id.tvDateOrder);
        tvStatusOrder = findViewById(R.id.tvStatusOrder);
        tvNameReceipment = findViewById(R.id.tvNameReceipment1);
        tvPhoneReceipment = findViewById(R.id.tvPhoneReceipment1);
        tvAddressReceipment = findViewById(R.id.tvAddressReceipment1);
        tvTongTienHang = findViewById(R.id.tvTongTienHang1);
        tvPhiVanChuyen = findViewById(R.id.tvPhiVanChuyen1);
        tvKhuyenMai = findViewById(R.id.tvKhuyenMai1);
        tvTongTien = findViewById(R.id.tvTongTien1);
        btnBackInfoOrder = findViewById(R.id.btnBackInfoOrder);
        btn = findViewById(R.id.btn);
        ryListFood = findViewById(R.id.ryListFood);
    }

    private void getData(){
        SharedPreference sharedpreference = new SharedPreference(this);
        ProgressDialog mProgressDialog =new ProgressDialog(this);
        mProgressDialog.setMessage("Đang xử lý...!");
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        tvId.setText(id);
        fb.user.document(sharedpreference.getID()).collection("purchase history")
                .document(id).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        tvDateOrder.setText(task.getResult().getString("orderDate"));
                        tvStatusOrder.setText(task.getResult().getString("orderStatus"));
                        tvNameReceipment.setText(task.getResult().getString("nameRecipient"));
                        tvPhoneReceipment.setText(task.getResult().getString("phoneRecipient"));
                        tvAddressReceipment.setText(task.getResult().getString("addressRecipient"));
                        tvTongTienHang.setText(new Format().currency(task.getResult().getDouble("total")+task.getResult().getDouble("discount"))+"đ");
                        tvKhuyenMai.setText(new Format().currency(task.getResult().getDouble("discount"))+"đ");
                        tvTongTien.setText(new Format().currency(task.getResult().getDouble("total"))+"đ");
                        if(!Objects.equals(task.getResult().getString("orderStatus"), "Đang xử lý")){
                            tvStatusOrder.setTextColor(ContextCompat.getColor(this,R.color.red));
                            btn.setText("MUA LẠI");
                            btn.setBackgroundColor(ContextCompat.getColor(this,R.color.blue));
                            btn.setOnClickListener(view -> {
                                mProgressDialog.show();
                                fb.updateStatus(sharedpreference.getID(), id, "Đang xử lý").addOnCompleteListener(value -> {
                                    if(value.isSuccessful()){
                                        mProgressDialog.cancel();
                                        startActivity(new Intent(this, PurchaseOrderActivity.class));
                                    }
                                });
                            });
                        }
                        else {
                            btn.setOnClickListener(view -> {
                                mProgressDialog.show();
                                fb.updateStatus(sharedpreference.getID(), id, "Đơn đã hủy").addOnCompleteListener(value -> {
                                    if(value.isSuccessful()){
                                        mProgressDialog.cancel();
                                        startActivity(new Intent(this, PurchaseOrderActivity.class));
                                    }
                                });;
                            });
                        }
                        List<Map<String, Object>> list = (List<Map<String, Object>>) task.getResult().get("foods");
                        InfoOrderAdapter infoOrderAdapter = new InfoOrderAdapter(this, list);
                        ryListFood.setAdapter(infoOrderAdapter);
                        ryListFood.setLayoutManager(
                                new LinearLayoutManager(this));
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_order);
        findViewsByIds();
        getData();
        //Back
        btnBackInfoOrder.setOnClickListener(view -> {
            startActivity(new Intent(this, PurchaseOrderActivity.class));
        });
    }
}
