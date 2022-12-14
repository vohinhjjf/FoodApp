package com.example.doandd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.adapter.AddressAdapter;
import com.example.doandd.adapter.VoucherAdapter;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.model.AddressModel;
import com.example.doandd.model.VoucherModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity {
    Button btnAdd, btnBack;
    RecyclerView rycListAddress;
    private ProgressBar progressBar;
    FirestoreDatabase fb = new FirestoreDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBackAddress);
        rycListAddress = findViewById(R.id.ryListAddress);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        //
        List<AddressModel> list_address = new ArrayList<>();
        fb.user.document(fb.mAuth.getCurrentUser().getUid()).collection("delivery address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list_address.add(new AddressModel(
                                document.getId(),
                                document.getString("recipientName"),
                                document.getString("phoneNumber"),
                                document.getString("province"),
                                document.getString("district"),
                                document.getString("commune"),
                                document.getString("address"),
                                document.getString("typeAddress"),
                                Boolean.FALSE.equals(document.getBoolean("addressDefault"))
                        ));
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    AddressAdapter cartAdapter = new AddressAdapter(AddressActivity.this,list_address);
                    rycListAddress.setAdapter(cartAdapter);
                }
                else {
                    //Log.w( "Error getting documents.", task.getException());
                }
            }
        });
        rycListAddress.setLayoutManager(
                new LinearLayoutManager(AddressActivity.this));

        //Back
        btnBack.setOnClickListener(view ->{
            Intent intent = new Intent(this, PaymentActivity.class);
            startActivity(intent);
        });

        //Add1
        btnAdd.setOnClickListener(view ->{
            Intent intent = new Intent(this, AddAddressActivity.class);
            startActivity(intent);
        });
    }
}
