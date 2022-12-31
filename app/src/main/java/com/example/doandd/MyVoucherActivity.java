package com.example.doandd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.adapter.VoucherAdapter;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.SharedPreference;
import com.example.doandd.model.VoucherModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyVoucherActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    TextView tvEmpty;
    Button btnBack;
    RecyclerView rycvMyVoucher;
    List<VoucherModel> list_voucher = new ArrayList<>();
    FirestoreDatabase fb = new FirestoreDatabase();
    double total=0;

    private void findViewByIds(){
        tvEmpty = findViewById(R.id.tvEmpty);
        progressBar = findViewById(R.id.pgbMyVoucher);
        btnBack = findViewById(R.id.btnBackMyVoucher);
        rycvMyVoucher = findViewById(R.id.rcvMyVoucher);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_voucher);
        findViewByIds();
        progressBar.setVisibility(View.VISIBLE);
        SharedPreference sharedpreference = new SharedPreference(this);
        fb.user.document(sharedpreference.getID()).collection("voucher").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()){
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list_voucher.add(new VoucherModel(
                                document.getId(),
                                document.getBoolean("active"),
                                document.getString("title"),
                                document.getDouble("discount"),
                                document.getDouble("condition"),
                                document.getString("datetime")
                        ));
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    VoucherAdapter voucherAdapterKm = new VoucherAdapter(list_voucher, true, "my voucher", MyVoucherActivity.this, total);
                    rycvMyVoucher.setAdapter(voucherAdapterKm);
                }
                else {
                    Log.w( "Error", task.getException());
                }
            }
        });
        rycvMyVoucher.setLayoutManager(
                new LinearLayoutManager(this));

        btnBack.setOnClickListener(view -> {
            Intent get_intent = getIntent();
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("total", get_intent.getStringExtra("total"));
            intent.putStringArrayListExtra("listID", get_intent.getStringArrayListExtra("listID"));
            startActivity(intent);
        });
    }
}
