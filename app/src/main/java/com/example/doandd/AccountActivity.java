package com.example.doandd;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    Button btnLogout, btnMode, btnLocation, btnHistory, btnSupport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        btnLogout = findViewById(R.id.btnLogout);
        btnMode = findViewById(R.id.btnMode);
        btnLocation = findViewById(R.id.btnLocation);
        btnHistory = findViewById(R.id.btnHistory);
        btnSupport = findViewById(R.id.btnSupport);

        btnLogout.setOnClickListener(view ->
        {
            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
            FirebaseAuth.getInstance().signOut();;
            finish();
        });
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
