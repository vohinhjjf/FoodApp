package com.example.doandd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    ImageButton ibBack;

    private void FindViewByIds() {
        ibBack =  (ImageButton) findViewById(R.id.ibBack);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FindViewByIds();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        try{
            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent iActivityMain = new Intent(SearchActivity.this, MainActivity.class);
                    startActivity(iActivityMain);
                    Toast.makeText(getApplicationContext(),"Bat duoc su kien",Toast.LENGTH_LONG).show();
                }
            });
        } catch(Exception e) {
            System.out.println("Error " + e.getMessage());
            return;
        }

//        ibBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent iActivityMain = new Intent(SearchActivity.this, MainActivity.class);
//                startActivity(iActivityMain);
//            }
//        });
    }
}