package com.example.doandd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.doandd.adapter.BannerAdapter;
import com.example.doandd.adapter.FoodAdapter;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.model.FoodModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static boolean mShouldRecreateActivity;
    ProgressBar progressBar, progressBar1;
    Toolbar toolbar;
    TextView tvSearch;
    private ViewPager page;
    private TabLayout tabLayout;
    RecyclerView recyclerView_hotfoods, recyclerView_bestseller_foods;
    BottomNavigationView bottomNavigationView;
    Button btnCom, btnBunPho, btnDoUong, btnBanh;

    List<FoodModel> list_best_seller_food = new ArrayList<>();
    List<FoodModel> list_hot_food = new ArrayList<>();
    List<Integer> images = new ArrayList<>();
    FirestoreDatabase fb = new FirestoreDatabase();

    private void findViewByIds(){
        page = findViewById(R.id.my_pager) ;
        tabLayout = findViewById(R.id.my_tablayout);
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
        sliderImage();
        //
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
        //Navigation
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            switch(id){
                //check id
                case R.id.home: {

                }
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

    /*public void addFood(){
        FirestoreDatabase fb = new FirestoreDatabase();
        List<FoodModel> list = new ArrayList();
        list.add(new FoodModel("Chè mít đác","Đồ uống","","https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fche_mit_dac.jfif?alt=media&token=356002f9-911c-402e-86ce-ef6da6492371",15000,0,4.5));
        list.add(new FoodModel("Bánh hot dog","Bánh","","https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fhot_dog.jfif?alt=media&token=1a2b3f03-57c2-4293-9011-af5a15a66fb4",35000,5,4.8));
        list.add(new FoodModel("Bánh mì kebab","Bánh","","https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fkebab.jfif?alt=media&token=f65e7232-5743-460a-87bb-91a53d538b33",35000,0,4));
        list.add(new FoodModel("Bánh hamburger","Bánh","","https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fhamburger.jfif?alt=media&token=6070a327-a019-4251-af51-f1b4b582aa3b",35000,0,4.5));
        list.add(new FoodModel("Bánh xèo","Bánh","","https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fbanh_xeo.jfif?alt=media&token=bff95c25-bf3b-40a5-9d19-40f9623ef6f8",55000,5,4.1));
        list.add(new FoodModel("Trà bí đao","Đồ uống","","https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Ftra_bi_dao.jpg?alt=media&token=a12ef13a-a099-44df-9267-ce6bd5c50a81",20000,0,4));
        list.add(new FoodModel("Pepsi - Lon","Đồ uống","","https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fpepsi.jfif?alt=media&token=4ad1b227-bbd6-4d06-a216-486fa73f6b40",18000,0,4));
        list.add(new FoodModel("Coca Cola","Đồ uống","","https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fcoca_cola.jfif?alt=media&token=9839531e-2d2f-4a71-8612-8dd88ba6d7f3",18000,0,4));
        list.add(new FoodModel("Sữa đậu nành","Đồ uống","","https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fsua-dau-nanh.jpg?alt=media&token=42c415f7-5591-41cd-8548-55c75ad9f48f",15000,0,4));
        list.add(new FoodModel("Trà đào","Đồ uống","","https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Ftra_dao.png?alt=media&token=85394df8-26af-4ccf-8dbe-fc8d37c11d6d",28000,0,4.3));
        list.add(new FoodModel("Nước ép cam","Đồ uống","","https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fnuoc_ep_cam.jfif?alt=media&token=2a15881a-4bf4-4405-94fa-1d64aa0a7d96",30000,0,3));
        list.add(new FoodModel("Cơm chiên húng quế","Cơm","","https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fbasil-minced-pork-with-rice-fried-egg.jpg?alt=media&token=da4891dc-f51f-406c-b1dc-66e5aa83a5bb",55000,5,4));
        list.add(new FoodModel("Cá chua ngọt","Cơm","","https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fchinese-food-sour-sweet-fish.jpg?alt=media&token=86a64e5c-874d-4bcd-93c5-792049f7ce92",50000,0,4.2));
        list.add(new FoodModel("Bánh cuốn chả ram","Bánh","","https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fdelicious-vietnamese-food-including-pho-ga-noodles-spring-rolls-white-wall.jpg?alt=media&token=0b4ce79a-d0d6-46b7-ac9e-9eb676be6cfa",45000,2,4.4));
        list.add(new FoodModel("Cơm thịt lợn xào lá chanh","Cơm","","https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fthai-food-stir-fried-pork-with-kaffir-lime-leaves-serve-with-rice.jpg?alt=media&token=619b2e24-8338-4d06-af45-e816d09ded77",55000,5,4.6));
        for(int i=0; i<list.size(); i++){
            fb.addFoods(list.get(i));
        }
    }*/

    private void sliderImage(){
        images.add(R.drawable.banner_demo);
        images.add(R.drawable.banner_1);
        images.add(R.drawable.banner_2);
        images.add(R.drawable.banner_3);
        images.add(R.drawable.banner_4);
        images.add(R.drawable.banner_5);
        images.add(R.drawable.banner_6);
        images.add(R.drawable.banner_7);
        images.add(R.drawable.banner_8);

        BannerAdapter itemsPager_adapter = new BannerAdapter(this, images);
        page.setAdapter(itemsPager_adapter);

        // The_slide_timer
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new The_slide_timer(),2000,3000);
        tabLayout.setupWithViewPager(page,true);
    }

    public class The_slide_timer extends TimerTask {
        @Override
        public void run() {

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (page.getCurrentItem()< images.size()-1) {
                        page.setCurrentItem(page.getCurrentItem()+1);
                    }
                    else
                        page.setCurrentItem(0);
                }
            });
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