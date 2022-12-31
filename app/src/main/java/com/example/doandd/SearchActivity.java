package com.example.doandd;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.adapter.FoodAdapter;
import com.example.doandd.adapter.SearchAdapter;
import com.example.doandd.adapter.VoucherAdapter;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.SharedPreference;
import com.example.doandd.model.FoodModel;
import com.example.doandd.model.VoucherModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    Button btnBackSearch;
    TextView btnSearch, tvNumber;
    RecyclerView rvList, rvSearchRecent;
    FirestoreDatabase fb = new FirestoreDatabase();
    List<FoodModel> list_food = new ArrayList<>();
    List<Map<String, Object>> list_value = new ArrayList<>();
    EditText edtSearchFood;
    LinearLayout llShowList,llTimKiem;

    private void FindViewByIds() {
        btnBackSearch =  findViewById(R.id.btnBackSearch);
        edtSearchFood =  findViewById(R.id.edtSearchFood);
        btnSearch =  findViewById(R.id.btnSearch);
        tvNumber =  findViewById(R.id.tvNumber);
        rvList =  findViewById(R.id.rvList);
        rvSearchRecent =  findViewById(R.id.rvSearchRecent);
        llShowList =  findViewById(R.id.llShowList);
        llTimKiem =  findViewById(R.id.llTimKiem);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        FindViewByIds();
        SharedPreference sharedPreference = new SharedPreference(this);
        //
        btnBackSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iActivityMain = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(iActivityMain);
            }
        });
        //
        btnSearch.setOnClickListener(view -> {
            List<FoodModel> list = new ArrayList<>();
            String key = edtSearchFood.getText().toString();
            fb.user.document(sharedPreference.getID()).collection("search history")
                            .whereEqualTo("value", key).get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            if(task.getResult().isEmpty()){
                                fb.addSearchRecent(sharedPreference.getID(), key);
                            }
                        }
                    });
            fb.food.whereGreaterThanOrEqualTo("name", key).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list_food.add(new FoodModel(
                                        document.getString("name"),
                                        document.getString("category"),
                                        document.getString("description"),
                                        document.getString("image"),
                                        document.getDouble("price"),
                                        Math.toIntExact(document.getLong("discountPercentage")),
                                        document.getDouble("rate")
                                ));
                            }
                            for (int i =0; i<list_food.size(); i++){
                                if(list_food.get(i).getName().toLowerCase().contains(key.toLowerCase())){
                                    list.add(list_food.get(i));
                                }
                            }
                            rvList.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
                            FoodAdapter food_adapter = new FoodAdapter(list, SearchActivity.this, "ListFoodActivity");
                            rvList.setAdapter(food_adapter);
                            tvNumber.setText(list.size()+" món ăn được tìm thấy");
                            llShowList.setVisibility(View.VISIBLE);
                            llTimKiem.setVisibility(View.INVISIBLE);
                        }
                        else {
                            //Log.w( "Error getting documents.", task.getException());
                        }
                    }
                });
        });
        //
        edtSearchFood.setOnClickListener(view -> {
            llShowList.setVisibility(View.INVISIBLE);
            llTimKiem.setVisibility(View.VISIBLE);
        });
        //
        fb.user.document(sharedPreference.getID()).collection("search history").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("id", document.getId());
                                data.put("value", document.getString("value"));
                                list_value.add(data);
                            }
                            rvSearchRecent.setLayoutManager(
                                    new LinearLayoutManager(SearchActivity.this));;
                            SearchAdapter search_adapter = new SearchAdapter(list_value, SearchActivity.this,rvList,tvNumber, llShowList, llTimKiem);
                            //SearchAdapter search_adapter = new SearchAdapter(list_value, SearchActivity.this);
                            rvSearchRecent.setAdapter(search_adapter);
                        }
                        else {
                            Log.w( "Error", task.getException());
                        }
                    }
                });
    }
}