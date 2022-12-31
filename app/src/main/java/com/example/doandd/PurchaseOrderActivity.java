package com.example.doandd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.adapter.OrderAdapter;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.SharedPreference;
import com.example.doandd.model.CartModel;
import com.example.doandd.model.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderActivity extends AppCompatActivity {
    TextView tvEmpty;
    ProgressBar progressBar;
    RecyclerView rcv_processing, rcv_received, rcv_canceled;
    TabLayout tabLayout;
    Button btnBackPurchase;
    FirestoreDatabase fb = new FirestoreDatabase();
    List<OrderModel> list_processing = new ArrayList<>();
    List<CartModel> list_cart = new ArrayList<>();

    private void findViewByIds(){
        progressBar = findViewById(R.id.pbPurchase);
        rcv_processing = findViewById(R.id.rcv_processing);
        rcv_received = findViewById(R.id.rcv_received);
        rcv_canceled = findViewById(R.id.rcv_canceled);
        tvEmpty = findViewById(R.id.tvempty);
        tabLayout = findViewById(R.id.tabLayout);
        btnBackPurchase = findViewById(R.id.btnBackPurchase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order);
        findViewByIds();
        progressBar.setVisibility(View.VISIBLE);
        SharedPreference sharedpreference = new SharedPreference(this);
        //
        fb.user.document(sharedpreference.getID()).collection("purchase history").whereEqualTo("orderStatus", "Đang xử lý").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()){
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list_processing.add(new OrderModel(document.getId(),
                                document.getString("nameRecipient"),
                                document.getString("addressRecipient"),
                                document.getString("phoneRecipient"),
                                document.getDouble("discount"),
                                document.getDouble("total"),
                                document.getString("orderDate"),
                                document.getString("orderStatus"),
                                (List<CartModel>) document.get("foods")
                        ));
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    OrderAdapter orderAdapter = new OrderAdapter(list_processing, PurchaseOrderActivity.this, "Đang xử lý");
                    rcv_processing.setAdapter(orderAdapter);
                }
                else {
                    //Log.w( "Error getting documents.", task.getException());
                }
            }
        });
        rcv_processing.setLayoutManager(
                new LinearLayoutManager(this));
        rcv_received.setLayoutManager(
                new LinearLayoutManager(this));
        rcv_canceled.setLayoutManager(
                new LinearLayoutManager(this));
        //
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                List<OrderModel> list = new ArrayList<>();
                tvEmpty.setVisibility(View.INVISIBLE);
                switch (tab.getPosition()){
                    case 0: {
                        rcv_processing.setVisibility(View.VISIBLE);
                        rcv_received.setVisibility(View.INVISIBLE);
                        rcv_canceled.setVisibility(View.INVISIBLE);
                        fb.user.document(sharedpreference.getID()).collection("purchase history").whereEqualTo("orderStatus", "Đang xử lý").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()){

                                    }
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        list.add(new OrderModel(
                                                document.getId(),
                                                document.getString("nameRecipient"),
                                                document.getString("addressRecipient"),
                                                document.getString("phoneRecipient"),
                                                document.getDouble("discount"),
                                                document.getDouble("total"),
                                                document.getString("orderDate"),
                                                document.getString("orderStatus"),
                                                (List<CartModel>) document.get("foods")
                                        ));
                                    }
                                    progressBar.setVisibility(View.INVISIBLE);
                                    OrderAdapter orderAdapter = new OrderAdapter(list, PurchaseOrderActivity.this, "Đang xử lý");
                                    rcv_processing.setAdapter(orderAdapter);
                                }
                                else {
                                    //Log.w( "Error getting documents.", task.getException());
                                }
                            }
                        });
                    };break;
                    case 1: {
                        rcv_processing.setVisibility(View.INVISIBLE);
                        rcv_received.setVisibility(View.VISIBLE);
                        rcv_canceled.setVisibility(View.INVISIBLE);
                        fb.user.document(sharedpreference.getID()).collection("purchase history").whereEqualTo("orderStatus", "Đã giao hàng").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()){
                                        tvEmpty.setVisibility(View.VISIBLE);
                                    }
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        list.add(new OrderModel(document.getId(),
                                                document.getString("nameRecipient"),
                                                document.getString("addressRecipient"),
                                                document.getString("phoneRecipient"),
                                                document.getDouble("discount"),
                                                document.getDouble("total"),
                                                document.getString("orderDate"),
                                                document.getString("orderStatus"),
                                                (List<CartModel>) document.get("foods")
                                        ));
                                    }
                                    progressBar.setVisibility(View.INVISIBLE);
                                    OrderAdapter orderAdapter = new OrderAdapter(list, PurchaseOrderActivity.this, "Đã giao hàng");
                                    rcv_received.setAdapter(orderAdapter);
                                }
                                else {
                                    //Log.w( "Error getting documents.", task.getException());
                                }
                            }
                        });
                    };break;
                    case 2: {
                        rcv_processing.setVisibility(View.INVISIBLE);
                        rcv_received.setVisibility(View.INVISIBLE);
                        rcv_canceled.setVisibility(View.VISIBLE);
                        fb.user.document(sharedpreference.getID()).collection("purchase history").whereEqualTo("orderStatus", "Đơn đã hủy").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()){
                                        tvEmpty.setVisibility(View.VISIBLE);
                                    }
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        list.add(new OrderModel(document.getId(),
                                                document.getString("nameRecipient"),
                                                document.getString("addressRecipient"),
                                                document.getString("phoneRecipient"),
                                                document.getDouble("discount"),
                                                document.getDouble("total"),
                                                document.getString("orderDate"),
                                                document.getString("orderStatus"),
                                                (List<CartModel>) document.get("foods")
                                        ));
                                    }
                                    progressBar.setVisibility(View.INVISIBLE);
                                    OrderAdapter orderAdapter = new OrderAdapter(list, PurchaseOrderActivity.this, "Đơn đã hủy");
                                    rcv_canceled.setAdapter(orderAdapter);
                                }
                                else {
                                    //Log.w( "Error getting documents.", task.getException());
                                }
                            }
                        });
                    };break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //
        btnBackPurchase.setOnClickListener(view -> {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        });
    }
}
