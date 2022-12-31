package com.example.doandd;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doandd.database.SharedPreference;
import com.example.doandd.login.LoginActivity;
import com.example.doandd.login.SignUpActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends AppCompatActivity {
    Button btnLogout, btnMode, btnLocation, btnHistory, btnSupport, btnTaikhoan;
    ArrayList<String> listId = new ArrayList();

    private void findViewByIds(){
        btnTaikhoan = findViewById(R.id.btnTaikhoan);
        btnMode = findViewById(R.id.btnMode);
        btnLocation = findViewById(R.id.btnLocation);
        btnHistory = findViewById(R.id.btnHistory);
        btnSupport = findViewById(R.id.btnSupport);
        btnLogout = findViewById(R.id.btnLogout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        findViewByIds();
        //
        btnTaikhoan.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });
        //
        btnMode.setOnClickListener(view -> {
            Intent intent = new Intent(this, DayNightActivity.class);
            startActivity(intent);
        });
        //
        btnLocation.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddressActivity.class);
            intent.putExtra("total", "account");
            intent.putExtra("listID", listId);
            startActivity(intent);
        });
        //
        btnLogout.setOnClickListener(view ->
        {
            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
            SharedPreference sharedPreference = new SharedPreference(this);
            sharedPreference.deleteID();
            FirebaseAuth.getInstance().signOut();
            finish();
        });
        //
        btnHistory.setOnClickListener(view -> {
            Intent intent = new Intent(this, PurchaseOrderActivity.class);
            startActivity(intent);
        });
        //
        btnSupport.setOnClickListener(view -> {
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);
        });
        //
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.account);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            switch(id){
                //check id
                case R.id.home: {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.voucher:{
                    Intent intent = new Intent(this, VoucherActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.account: {}
            }
            return true;
        });

    }
}
