package com.example.doandd;

import static com.google.common.collect.ComparisonChain.start;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.adapter.CouponAdapter;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.login.LoginActivity;
import com.example.doandd.model.CouponModel;
import com.example.doandd.model.best_seller_food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class VoucherActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ProgressBar progressBar;
    List<CouponModel> list_coupon_km = new ArrayList<>();
    List<CouponModel> list_coupon_cn = new ArrayList<>();
    FirestoreDatabase fb = new FirestoreDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        progressBar = findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView recyclerView_coupon_km = findViewById(R.id.rcv_coupon_km);
        RecyclerView recyclerView_coupon_ca_nhan = findViewById(R.id.rcv_coupon_ca_nhan);
        fb.voucher.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list_coupon_km.add(new CouponModel(
                                document.getBoolean("active"),
                                document.getString("title"),
                                document.getDouble("discount"),
                                document.getDouble("condition"),
                                document.getString("datetime")
                        ));
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    CouponAdapter couponAdapterKm = new CouponAdapter(list_coupon_km);
                    recyclerView_coupon_km.setAdapter(couponAdapterKm);
                }
                else {
                    //Log.w( "Error getting documents.", task.getException());
                }
            }
        });
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
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                btnCaNhan.setBackgroundResource(R.drawable.rounded_coupon);
                btnCaNhan.setTextAppearance(R.style.blueText);
                recyclerView_coupon_ca_nhan.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                btnKhuyenMai.setBackgroundResource(R.color.white);
                btnKhuyenMai.setTextAppearance(R.style.grayText);
                recyclerView_coupon_km.setVisibility(View.INVISIBLE);
            }
        });

        btnKhuyenMai.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                List<CouponModel> list = new ArrayList<>();
                btnKhuyenMai.setBackgroundResource(R.drawable.rounded_coupon);
                btnKhuyenMai.setTextAppearance(R.style.blueText);
                recyclerView_coupon_km.setVisibility(View.VISIBLE);
                recyclerView_coupon_km.removeAllViewsInLayout();
                progressBar.setVisibility(View.VISIBLE);

                btnCaNhan.setBackgroundResource(R.color.white);
                btnCaNhan.setTextAppearance(R.style.grayText);
                recyclerView_coupon_ca_nhan.setVisibility(View.INVISIBLE);
                fb.voucher.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(new CouponModel(
                                        document.getBoolean("active"),
                                        document.getString("title"),
                                        document.getDouble("discount"),
                                        document.getDouble("condition"),
                                        document.getString("datetime")
                                ));
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                            CouponAdapter couponAdapterKm = new CouponAdapter(list);
                            recyclerView_coupon_km.setAdapter(couponAdapterKm);
                        }
                        else {
                            //Log.w( "Error getting documents.", task.getException());
                        }
                    }
                });
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
