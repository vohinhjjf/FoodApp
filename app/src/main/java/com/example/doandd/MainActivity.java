package com.example.doandd;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.doandd.adapter.FoodAdapter;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.model.FoodModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static boolean mShouldRecreateActivity;
    ProgressBar progressBar, progressBar1;
    Toolbar toolbar;
    TextView tvSearch;
    RecyclerView recyclerView_hotfoods, recyclerView_bestseller_foods;
    BottomNavigationView bottomNavigationView;
    Button btnCom, btnBunPho, btnDoUong, btnBanh;

    List<FoodModel> list_best_seller_food = new ArrayList<>();
    List<FoodModel> list_hot_food = new ArrayList<>();
    FirestoreDatabase fb = new FirestoreDatabase();

    private void findViewByIds(){
        toolbar = findViewById(R.id.toolbar);
        tvSearch = findViewById(R.id.tvSearch);
        btnCom = findViewById(R.id.btnCom);
        btnBunPho = findViewById(R.id.btnBunPho);
        btnDoUong = findViewById(R.id.btnDoUong);
        btnBanh = findViewById(R.id.btnBanh);
        progressBar = findViewById(R.id.progressBar);
        progressBar1 = findViewById(R.id.progressBar1);
        recyclerView_hotfoods = findViewById(R.id.rcv_hotfoods);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        recyclerView_bestseller_foods = findViewById(R.id.rcv_bestseller_foods);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewByIds();
        selectCategory();
        progressBar.setVisibility(View.VISIBLE);
        progressBar1.setVisibility(View.VISIBLE);
        //Best Seller
        fb.food.whereGreaterThan("discountPercentage", 0).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(int i = task.getResult().getDocuments().size()-1; i >= 0 ; i --)  {
                        list_best_seller_food.add(new FoodModel(
                                task.getResult().getDocuments().get(i).getString("name"),
                                task.getResult().getDocuments().get(i).getString("category"),
                                task.getResult().getDocuments().get(i).getString("description"),
                                task.getResult().getDocuments().get(i).getString("image"),
                                task.getResult().getDocuments().get(i).getDouble("price"),
                                Math.toIntExact(task.getResult().getDocuments().get(i).getLong("discountPercentage")),
                                task.getResult().getDocuments().get(i).getDouble("rate")
                        ));
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    recyclerView_bestseller_foods.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                    FoodAdapter food_adapter = new FoodAdapter(list_best_seller_food, MainActivity.this,"MainActivity");
                    recyclerView_bestseller_foods.setAdapter(food_adapter);
                }
                else {
                    //Log.w( "Error getting documents.", task.getException());
                }
            }
        });
        //Hot Food
        fb.food.whereGreaterThanOrEqualTo("rate", 4).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list_hot_food.add(new FoodModel(
                                        document.getString("name"),
                                        document.getString("category"),
                                        document.getString("description"),
                                        document.getString("image"),
                                        document.getDouble("price"),
                                        Math.toIntExact(document.getLong("discountPercentage")),
                                        document.getDouble("rate")
                                ));
                            }
                            progressBar1.setVisibility(View.INVISIBLE);
                            recyclerView_hotfoods.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                            FoodAdapter food_adapter = new FoodAdapter(list_hot_food, MainActivity.this,"MainActivity");
                            recyclerView_hotfoods.setAdapter(food_adapter);
                        }
                        else {
                            //Log.w( "Error getting documents.", task.getException());
                        }
                    }
                });

        //Toolbar
        setSupportActionBar(toolbar);
        //
        tvSearch.setOnClickListener(view -> {
            startActivity(new Intent(this, SearchActivity.class));
        });
        //
        SharedPreferences sharedPreferences = getSharedPreferences("night",0);
        boolean booleanValue = sharedPreferences.getBoolean("night_mode",false);
        if (booleanValue){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        //Navigation
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            switch(id){
                //check id
                case R.id.home: {}
                case R.id.voucher:{
                    Intent intent = new Intent(this, VoucherActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.account: {
                    Intent intent = new Intent(this, AccountActivity.class);
                    startActivity(intent);
                    break;
                }
            }
            return true;
        });
    }

    public void addFood(){
        FirestoreDatabase fb = new FirestoreDatabase();
        List<FoodModel> list = new ArrayList();
        list.add(new FoodModel("Phở bò","Bún/Phở","","",40000,0,4));
        list.add(new FoodModel("Bún bò Huế","Bún/Phở","","",60000,15,4.8));
        list.add(new FoodModel("Cơm gà xối mỡ","Cơm","","",35000,0,4));
        list.add(new FoodModel("Hủ tiếu nam vang","Bún/Phở","","",60000,10,4.5));
        list.add(new FoodModel("Bánh mì thập cẩm","Bánh","","",20000,10,4.1));
        list.add(new FoodModel("Phở gà","Bún/Phở","","",40000,0,4));
        list.add(new FoodModel("Cơm chiên hải sản","Cơm","","",40000,5,5));
        list.add(new FoodModel("Hủ tiếu bò kho","Bún/Phở","","",35000,0,4));
        list.add(new FoodModel("Cơm sườn","Cơm","","",30000,0,4));
        list.add(new FoodModel("Cơm sườn bì chả","Cơm","","",40000,0,4));
        list.add(new FoodModel("Bún thịt nướng","Bún/Phở","","",40000,0,3.5));
        list.add(new FoodModel("Cơm cá kho tộ","Cơm","","",40000,10,3));
        list.add(new FoodModel("Cơm chiên dương châu","Cơm","","",30000,0,4));
        list.add(new FoodModel("Bún cá","Bún/Phở","","",40000,0,4));
        for(int i=0; i<list.size(); i++){
            fb.addFoods(list.get(i));
        }
    }

    public void selectCategory(){
        Intent intent=new Intent(MainActivity.this, ListFoodActivity.class);
        btnCom.setOnClickListener(view -> {
            intent.putExtra("category", "Cơm");
            startActivity(intent);
        });
        btnBanh.setOnClickListener(view -> {
            intent.putExtra("category", "Bánh");
            startActivity(intent);
        });
        btnBunPho.setOnClickListener(view -> {
            intent.putExtra("category", "Bún/Phở");
            startActivity(intent);
        });
        btnDoUong.setOnClickListener(view -> {
            intent.putExtra("category", "Đồ uống");
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                startActivity(new Intent(this, CartActivity.class));
                break;
            case R.id.notification:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}