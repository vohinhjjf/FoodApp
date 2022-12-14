package com.example.doandd;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.adapter.VoucherAdapter;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.model.VoucherModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class VoucherActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ProgressBar progressBar;
    List<VoucherModel> list_coupon_km = new ArrayList<>();
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
                        list_coupon_km.add(new VoucherModel(
                                document.getId(),
                                document.getBoolean("active"),
                                document.getString("title"),
                                document.getDouble("discount"),
                                document.getDouble("condition"),
                                document.getString("datetime")
                        ));
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    VoucherAdapter voucherAdapterKm = new VoucherAdapter(list_coupon_km, true);
                    recyclerView_coupon_km.setAdapter(voucherAdapterKm);
                }
                else {
                    //Log.w( "Error getting documents.", task.getException());
                }
            }
        });
        recyclerView_coupon_km.setLayoutManager(
                new LinearLayoutManager(this));
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
                List<VoucherModel> list = new ArrayList<>();
                btnCaNhan.setBackgroundResource(R.drawable.rounded_coupon);
                btnCaNhan.setTextAppearance(R.style.blueText);
                recyclerView_coupon_ca_nhan.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                btnKhuyenMai.setBackgroundResource(R.color.white);
                btnKhuyenMai.setTextAppearance(R.style.grayText);
                recyclerView_coupon_km.setVisibility(View.INVISIBLE);
                fb.user.document(fb.mAuth.getCurrentUser().getUid()).collection("voucher").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            fb.voucher.document(document.getId()).get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    list.add(new VoucherModel(
                                            document.getId(),
                                            task1.getResult().getBoolean("active"),
                                            task1.getResult().getString("title"),
                                            task1.getResult().getDouble("discount"),
                                            task1.getResult().getDouble("condition"),
                                            task1.getResult().getString("datetime")
                                    ));
                                    progressBar.setVisibility(View.INVISIBLE);
                                    VoucherAdapter voucherAdapterCn = new VoucherAdapter(list, false);
                                    recyclerView_coupon_ca_nhan.setAdapter(voucherAdapterCn);
                                }
                            });
                        }
                    }
                    else {
                        //Log.w( "Error getting documents.", task.getException());
                    }
                });
            }
        });

        btnKhuyenMai.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v)
            {
                List<VoucherModel> list = new ArrayList<>();
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
                                list.add(new VoucherModel(
                                        document.getId(),
                                        document.getBoolean("active"),
                                        document.getString("title"),
                                        document.getDouble("discount"),
                                        document.getDouble("condition"),
                                        document.getString("datetime")
                                ));
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                            VoucherAdapter voucherAdapterKm = new VoucherAdapter(list, true);
                            recyclerView_coupon_km.setAdapter(voucherAdapterKm);
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
