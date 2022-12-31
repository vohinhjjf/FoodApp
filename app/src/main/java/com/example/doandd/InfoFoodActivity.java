package com.example.doandd;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.SharedPreference;
import com.example.doandd.model.CartModel;
import com.example.doandd.utils.Format;
import com.example.doandd.utils.ImageLoadTask;

public class InfoFoodActivity extends AppCompatActivity {
    TextView tvName, tvPrice, tvDiscountPercent, tvDiscount, tvDescription;
    RatingBar rbFood;
    Button btnAdd, btnBack;
    ImageView imgFood;
    LinearLayout llPrice;
    FirestoreDatabase fb =new FirestoreDatabase();

    private void findViewByIds() {
        tvName =  findViewById(R.id.NameFood);
        tvPrice =  findViewById(R.id.tvPriceInfoFood);
        tvDiscountPercent =  findViewById(R.id.tvDiscountPercentInfoFood);
        tvDiscount =  findViewById(R.id.tvDiscountInfoFood);
        tvDescription =  findViewById(R.id.tvDescription);
        rbFood =  findViewById(R.id.rateInfoFood);
        btnAdd =  findViewById(R.id.btnAddCart);
        btnBack =  findViewById(R.id.btnBackInfoOrder);
        imgFood =  findViewById(R.id.imgInfoFood);
        llPrice = findViewById(R.id.llPrice);
    }

    private void getData(){
        SharedPreference sharedpreference = new SharedPreference(this);
        Intent get_intent = getIntent();
        String id = get_intent.getStringExtra("id");
        double price = get_intent.getDoubleExtra("price",0);
        double discount_price = get_intent.getDoubleExtra("discount percent",0);
        tvName.setText(get_intent.getStringExtra("name"));
        tvPrice.setText(new Format().currency(price) + "đ");
        tvDiscountPercent.setText(discount_price+"%");
        tvDiscount.setText(get_intent.getStringExtra("discount"));
        tvDescription.setText(get_intent.getStringExtra("description"));
        rbFood.setRating(get_intent.getFloatExtra("rating",0));
        new ImageLoadTask(get_intent.getStringExtra("image"), imgFood).execute();
        tvPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if(discount_price == 0){
            llPrice.setVisibility(View.GONE);
        }
        btnAdd.setOnClickListener(view -> {
            ProgressDialog mProgressDialog =new ProgressDialog(InfoFoodActivity.this);
            mProgressDialog.setMessage("Đang xử lý...!");
            mProgressDialog.show();
            CartModel cartModel = new CartModel("",
                    get_intent.getStringExtra("name"),
                    price, discount_price,
                    get_intent.getStringExtra("image"),
                    get_intent.getDoubleExtra("rating",0),1, false
                    );
            fb.addCart(cartModel, sharedpreference.getID()).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog.cancel();
                            AlertDialog.Builder builder = new AlertDialog.Builder(InfoFoodActivity.this);
                            builder.setTitle("Hoàn tất  !");
                            builder.setMessage("Đã thêm món ăn vào giỏ hàng!");
                            builder.setNegativeButton("Đóng", (DialogInterface.OnClickListener) (dialog, which) -> {
                                // If user click no then dialog box is canceled.
                                dialog.cancel();
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertDialog.cancel();
                                }
                            }, 3000 );
                        }
                    }, 2000 );
                }
            });
        });
        btnBack.setOnClickListener(view -> {
            switch (id){
                case"MainActivity":{
                    startActivity(new Intent(InfoFoodActivity.this, MainActivity.class));
                };break;
                case"ListFoodActivity":{
                    Intent intent = new Intent(InfoFoodActivity.this, ListFoodActivity.class);
                    intent.putExtra("category", get_intent.getStringExtra("category"));
                    startActivity(intent);
                };break;
                default:
                    startActivity(new Intent(InfoFoodActivity.this, MainActivity.class));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_food);
        findViewByIds();
        getData();
    }
}
