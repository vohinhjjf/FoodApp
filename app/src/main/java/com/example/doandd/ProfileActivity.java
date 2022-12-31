package com.example.doandd;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.SharedPreference;
import com.example.doandd.utils.ImageLoadTask;

import java.util.Objects;


public class ProfileActivity extends AppCompatActivity {
    private TextView addressTxtView, nameTxtView, birthdayTxtView;
    private TextView emailTxtView, phoneTxtView, genderTxtView;
    private ImageView imgUser;
    Button btnBack, btnEditProfile;
    private final String TAG = this.getClass().getName().toUpperCase();
    FirestoreDatabase fb =new FirestoreDatabase();
    
    private void findViewByIds(){
        nameTxtView = findViewById(R.id.tvNameUser);
        addressTxtView = findViewById(R.id.address_textview);
        birthdayTxtView = findViewById(R.id.birthday_textview);
        genderTxtView = findViewById(R.id.gender_textview);
        emailTxtView = findViewById(R.id.email_textview);
        phoneTxtView = findViewById(R.id.phone_textview);
        imgUser = findViewById(R.id.imgUser);
        btnBack = findViewById(R.id.btnBackProfile);
        btnEditProfile = findViewById(R.id.btnProfile);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findViewByIds();
        SharedPreference sharedpreference = new SharedPreference(this);

        // Read from the database
        fb.user.document(sharedpreference.getID()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                nameTxtView.setText(task.getResult().getString("name"));
                emailTxtView.setText(task.getResult().getString("email"));
                birthdayTxtView.setText(task.getResult().getString("birthday"));
                genderTxtView.setText(task.getResult().getString("gender"));
                addressTxtView.setText(task.getResult().getString("address"));
                phoneTxtView.setText(task.getResult().getString("phone"));
                if(!Objects.requireNonNull(task.getResult().getString("image")).equals("")){
                    new ImageLoadTask(task.getResult().getString("image"), imgUser).execute();
                }
            }
            else {
                Log.w( "Error", task.getException());
            }
        });
        //
        btnEditProfile.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });
        //
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        });
    }
}
