package com.example.doandd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.example.doandd.adapter.best_seller_food_adapter;
import com.example.doandd.adapter.hot_food_adapter;
import com.example.doandd.model.best_seller_food;
import com.example.doandd.model.hot_food;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        RecyclerView recyclerView_bestseller_foods = findViewById(R.id.rcv_bestseller_foods);
        recyclerView_bestseller_foods.setLayoutManager(new GridLayoutManager(this, 2));
        List<best_seller_food> list_best_seller_food = new ArrayList<>();

        best_seller_food monan1 = new best_seller_food("Phở bò",           "35.000 VNĐ",R.drawable.pho_bo);
        best_seller_food monan2 = new best_seller_food("Bún bò Huế",       "35.000 VNĐ",R.drawable.bun_bo_hue);
        best_seller_food monan3 = new best_seller_food("Cơm gà xối mỡ",    "35.000 VNĐ",R.drawable.com_ga_xoi_mo);
        best_seller_food monan4 = new best_seller_food("Hủ tiếu nam vang", "35.000 VNĐ",R.drawable.hu_tieu_nam_vang);
        best_seller_food monan5 = new best_seller_food("Bánh mì thập cẩm ","30.000 VNĐ",R.drawable.banh_mi_thap_cam);
        best_seller_food monan6 = new best_seller_food("Phở gà ",          "35.000 VNĐ",R.drawable.pho_ga);
        best_seller_food monan7 = new best_seller_food("Cơm chiên hải sản","30.000 VNĐ",R.drawable.com_chien_hai_san);
        best_seller_food monan8 = new best_seller_food("Hủ tiếu bò kho ", "35.000 VNĐ",R.drawable.hu_tieu_bo_kho);
        list_best_seller_food.add(monan1);
        list_best_seller_food.add(monan2);
        list_best_seller_food.add(monan3);
        list_best_seller_food.add(monan4);
        list_best_seller_food.add(monan5);
        list_best_seller_food.add(monan6);
        list_best_seller_food.add(monan7);
        list_best_seller_food.add(monan8);

        best_seller_food_adapter bestseller_food_adapter = new best_seller_food_adapter(list_best_seller_food);

        RecyclerView recyclerView_hotfoods = findViewById(R.id.rcv_hotfoods);
        recyclerView_hotfoods.setLayoutManager(new GridLayoutManager(this, 2));
        List<hot_food> list_hot_food = new ArrayList<>();

        hot_food monan9  = new hot_food("Cơm sườn ",            "30.000 VNĐ",R.drawable.com_suon);
        hot_food monan10 = new hot_food("Cơm sườn bì chả ",     "35.000 VNĐ",R.drawable.com_suon_bi_cha);
        hot_food monan11 = new hot_food("Bún thịt nướng ",      "30.000 VNĐ",R.drawable.bun_thit_nuong);
        hot_food monan12 = new hot_food("Cơm cá kho tộ",        "35.000 VNĐ",R.drawable.com_ca_kho_to);
        hot_food monan13 = new hot_food("Cơm chiên dương châu", "30.000.000 VNĐ",R.drawable.com_chien_duong_chau);
        hot_food monan14 = new hot_food("Bún cá",               "35.000.000 VNĐ",R.drawable.bun_ca);


        list_hot_food.add(monan9);
        list_hot_food.add(monan10);
        list_hot_food.add(monan11);
        list_hot_food.add(monan12);
        list_hot_food.add(monan13);
        list_hot_food.add(monan14);


        hot_food_adapter hot_food_adapter = new hot_food_adapter(list_hot_food);



        recyclerView_bestseller_foods.setAdapter(bestseller_food_adapter);
        recyclerView_hotfoods.setAdapter(hot_food_adapter);

        setSupportActionBar(toolbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}