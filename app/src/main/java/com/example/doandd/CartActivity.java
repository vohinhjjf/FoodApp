package com.example.doandd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.adapter.CartAdapter;
import com.example.doandd.adapter.PaymentAdapter;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.SharedPreference;
import com.example.doandd.model.CartModel;
import com.example.doandd.utils.Format;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    TextView tvTotal;
    Button btnBack, btnThanhToan;
    RecyclerView recycList;
    ProgressBar pbCart;
    FirestoreDatabase fb =new FirestoreDatabase();
    double total =0;
    ArrayList<String> listId = new ArrayList();

    private void addCart(SharedPreference sharedpreference){
        CartModel cartModel1=new CartModel("","Bún cá",35000,10,"https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fbun_ca.jpg?alt=media&token=c008645c-ffba-4a1c-87fa-f1fe1ef62b89",4,1,false);
        CartModel cartModel2=new CartModel("","Cơm sườn",30000,5,"https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fcom_suon.jpg?alt=media&token=3cb85ba9-dc8d-47b2-baa6-f3bab7112db8",4.5,1,false);
        CartModel cartModel3=new CartModel("","Hủ tiếu bò kho",40000,3,"https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fhu_tieu_bo_kho.jpg?alt=media&token=4b1418b3-72ef-4d80-8acd-cbf7e80101f3",4.6,1,false);
        CartModel cartModel4=new CartModel("","Cơm gà xôi mỡ",45000,5,"https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fcom_ga_xoi_mo.jpg?alt=media&token=3f7e445e-cd28-43ec-a127-aa029b1f3a6c",4.6,1,false);
        CartModel cartModel5=new CartModel("","Bún bò huế",65000,10,"https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fbun_bo_hue.jpeg?alt=media&token=5b769ce9-d234-4f1f-9283-607cd0256311",4.6,1,false);
        CartModel cartModel6=new CartModel("","Phở bò",60000,15,"https://firebasestorage.googleapis.com/v0/b/foodapp-dc6b4.appspot.com/o/Foods%2Fpho_bo.png?alt=media&token=07f3429c-f84c-4ea6-8907-7c4d7eff1794",4.6,1,false);
        fb.addCart(cartModel1,sharedpreference.getID());
        fb.addCart(cartModel2,sharedpreference.getID());
        fb.addCart(cartModel3,sharedpreference.getID());
        fb.addCart(cartModel4,sharedpreference.getID());
        fb.addCart(cartModel5,sharedpreference.getID());
        fb.addCart(cartModel6,sharedpreference.getID());
    }

    private void findViewByIds(){
        tvTotal = findViewById(R.id.tvTotal);
        btnBack = findViewById(R.id.btnBack);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        recycList = findViewById(R.id.recycListCart);
        pbCart = findViewById(R.id.pbCart);
        pbCart.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        SharedPreference sharedpreference = new SharedPreference(this);
        findViewByIds();
        //addCart(sharedpreference);
        List<CartModel> list_cart = new ArrayList<>();
        fb.cart.document(sharedpreference.getID()).collection("products").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list_cart.add(new CartModel(
                                        document.getId(),
                                        document.getString("name"),
                                        document.getDouble("price"),
                                        document.getDouble("discountPercentage"),
                                        document.getString("image"),
                                        document.getDouble("rate"),
                                        document.getDouble("amount"),
                                        document.getBoolean("checkbuy")
                                ));
                            }
                            CartAdapter cartAdapter = new CartAdapter(CartActivity.this,list_cart, listId, tvTotal);
                            recycList.setAdapter(cartAdapter);
                            recycList.setLayoutManager(
                                    new LinearLayoutManager(CartActivity.this));
                            pbCart.setVisibility(View.INVISIBLE);
                        }
                        else {
                            //Log.w( "Error getting documents.", task.getException());
                        }
                    }
                });
        //Back
        btnBack.setOnClickListener(view ->{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        //Thanh toan
        btnThanhToan.setOnClickListener(view ->{
            if(tvTotal.getText().equals("0đ")){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Thông báo !");
                builder.setMessage("Vui lòng chọn món ăn cần thanh toán!");
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
            else {
                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra("ID", "cart");
                intent.putExtra("total", tvTotal.getText());
                intent.putStringArrayListExtra("listID", listId);
                startActivity(intent);
            }
        });
    }
}
