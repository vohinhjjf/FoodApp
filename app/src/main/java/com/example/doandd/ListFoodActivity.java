package com.example.doandd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.adapter.FoodAdapter;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.model.FoodModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListFoodActivity extends AppCompatActivity {
    Button btnBackListFood, btnCart;
    TextView tvSearch,tvTitle, tvEmptyList;
    ProgressBar pbListFood;
    RecyclerView rcvListFood;
    FirestoreDatabase fb = new FirestoreDatabase();
    List<FoodModel> list_food = new ArrayList<>();

    private void findViewByIds(){
        tvSearch = findViewById(R.id.tvSearch1);
        tvTitle = findViewById(R.id.tvTitle);
        tvEmptyList = findViewById(R.id.tvEmptyList);
        btnBackListFood = findViewById(R.id.btnBackListFood);
        btnCart = findViewById(R.id.btnCart);
        pbListFood = findViewById(R.id.pbListFood);
        rcvListFood = findViewById(R.id.rcv_listFood);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_food);
        findViewByIds();
        //
        pbListFood.setVisibility(View.VISIBLE);
        //
        Intent intent = getIntent();
        tvTitle.setText(intent.getStringExtra("category"));
        fb.food.whereEqualTo("category", intent.getStringExtra("category")).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()){
                                tvEmptyList.setVisibility(View.VISIBLE);
                            }
                            for(int i = task.getResult().getDocuments().size()-1; i >= 0 ; i --)  {
                                list_food.add(new FoodModel(
                                        task.getResult().getDocuments().get(i).getString("name"),
                                        task.getResult().getDocuments().get(i).getString("category"),
                                        task.getResult().getDocuments().get(i).getString("description"),
                                        task.getResult().getDocuments().get(i).getString("image"),
                                        task.getResult().getDocuments().get(i).getDouble("price"),
                                        Math.toIntExact(task.getResult().getDocuments().get(i).getLong("discountPercentage")),
                                        task.getResult().getDocuments().get(i).getDouble("rate")
                                ));
                            }
                            pbListFood.setVisibility(View.INVISIBLE);
                            rcvListFood.setLayoutManager(new GridLayoutManager(ListFoodActivity.this, 2));
                            FoodAdapter food_adapter = new FoodAdapter(list_food, ListFoodActivity.this,"ListFoodActivity");
                            rcvListFood.setAdapter(food_adapter);
                        }
                        else {
                            //Log.w( "Error getting documents.", task.getException());
                        }
                    }
                });
        //
        btnBackListFood.setOnClickListener(view ->{
            startActivity(new Intent(this, MainActivity.class));
        });
    }
}
