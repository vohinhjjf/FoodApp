package com.example.doandd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.adapter.CartAdapter;
import com.example.doandd.adapter.FoodAdapter;
import com.example.doandd.adapter.PaymentAdapter;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.SharedPreference;
import com.example.doandd.model.AddressModel;
import com.example.doandd.model.CartModel;
import com.example.doandd.model.FoodModel;
import com.example.doandd.model.OrderModel;
import com.example.doandd.utils.Format;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    TextView tvname, tvnameAddress, tvaddress, tvphone,tvtienhang, tvphivanchuyen, tvkhuyenmai, tvtong;
    Button btnUpdateAddress, btnAddress, btnBack, btnVoucher, btnThanhToan;
    RadioButton rbGiaoHang;
    RecyclerView ryList;
    LinearLayout llAddress;
    ConstraintLayout ctKhuyenmai;
    FirestoreDatabase fb = new FirestoreDatabase();
    double discount=0, maxDiscount=0, total=0;
    ArrayList<String> listId = new ArrayList();

    private void findViewsByIds() {
        tvname = findViewById(R.id.tvNameRecipient);
        tvnameAddress = findViewById(R.id.tvNameAddressPayment);
        tvaddress = findViewById(R.id.tvAddressPayment);
        tvphone = findViewById(R.id.tvPhone);
        tvtienhang = findViewById(R.id.tvTongTienHang);
        tvphivanchuyen = findViewById(R.id.tvPhiVanChuyen);
        tvkhuyenmai = findViewById(R.id.tvKhuyenMai);
        tvtong = findViewById(R.id.tvTongTien);
        btnUpdateAddress = findViewById(R.id.btnUpdateAddress);
        btnAddress = findViewById(R.id.btnAddress);
        btnBack = findViewById(R.id.btnBackPayment);
        btnThanhToan = findViewById(R.id.btnPayment);
        btnVoucher = findViewById(R.id.btnVoucher);
        rbGiaoHang = findViewById(R.id.rbGiaoHang);
        ryList = findViewById(R.id.ryList);
        llAddress = findViewById(R.id.llAddress);
        ctKhuyenmai = findViewById(R.id.ctKhuyenMai);
    }
    private void getAddressDefault(){
        fb.user.document(fb.mAuth.getCurrentUser().getUid()).collection("delivery address").whereEqualTo("addressDefault", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()&& task.getResult().getDocuments().size()>0) {
                            QueryDocumentSnapshot document = task.getResult().iterator().next();
                            llAddress.setVisibility(View.VISIBLE);
                            tvname.setText(document.getString("recipientName"));
                            tvnameAddress.setText(document.getString("address"));
                            tvaddress.setText(document.getString("commune")+", "+document.getString("province")+
                                    ", "+document.getString("district"));
                            tvphone.setText(document.getString("phoneNumber"));
                            btnAddress.setText(document.getString("typeAddress"));
                            btnUpdateAddress.setText("THAY ĐỔI");
                        }else {
                            llAddress.setVisibility(View.GONE);
                            btnUpdateAddress.setText("THÊM ĐỊA CHỈ GIAO HÀNG");
                        }
                    }
                });
    }

    private void getDiscount(){
        Intent intent = getIntent();
        discount = intent.getDoubleExtra("discount",0.0)*1000;
        maxDiscount = intent.getDoubleExtra("maxDiscount",0.0);
        total = Double.parseDouble(intent.getStringExtra("total").replaceAll("đ",""))*1000;
        listId = intent.getStringArrayListExtra("listID");
        tvtienhang.setText(new Format().currency(total)+"đ");
        if(discount==0){
            ctKhuyenmai.setVisibility(View.GONE);
        }else{
            ctKhuyenmai.setVisibility(View.VISIBLE);
            if(total>maxDiscount){
                tvkhuyenmai.setText("-"+new Format().currency(discount)+"đ");
            }
        }
        if(total > discount){
            tvtong.setText(new Format().currency(total-discount)+"đ");
        }else {
            tvtong.setText("0đ");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        SharedPreference sharedpreference = new SharedPreference(this);
        findViewsByIds();
        getAddressDefault();
        getDiscount();

        List<CartModel> list_cart = new ArrayList<>();
        for (int i =0; i<listId.size();i++) {
            fb.cart.document(fb.mAuth.getUid()).collection("products").document(listId.get(i)).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            list_cart.add(new CartModel(
                                    task.getResult().getId(),
                                    task.getResult().getString("name"),
                                    task.getResult().getDouble("price"),
                                    task.getResult().getDouble("discountPercentage"),
                                    task.getResult().getString("image"),
                                    task.getResult().getDouble("rate"),
                                    task.getResult().getDouble("amount"),
                                    task.getResult().getBoolean("checkbuy")
                            ));
                            //progressBar1.setVisibility(View.INVISIBLE);
                            PaymentAdapter paymentAdapter = new PaymentAdapter(PaymentActivity.this, list_cart);
                            ryList.setAdapter(paymentAdapter);
                            ryList.setLayoutManager(
                                    new LinearLayoutManager(PaymentActivity.this));
                        } else {
                            //Log.w( "Error getting documents.", task.getException());
                        }
                    });
        }

        //Select voucher
        btnVoucher.setOnClickListener(view -> {
            Intent intent = new Intent(this, MyVoucherActivity.class);
            intent.putExtra("total", tvtienhang.getText());
            intent.putExtra("listID", listId);
            startActivity(intent);
        });

        //Update address
        btnUpdateAddress.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddressActivity.class);
            intent.putExtra("total", tvtienhang.getText());
            intent.putExtra("listID", listId);
            startActivity(intent);
        });

        //Thanh toan
        btnThanhToan.setOnClickListener(view -> {
            ProgressDialog mProgressDialog =new ProgressDialog(this);
            mProgressDialog.setMessage("Đang xử lý...!");
            mProgressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.cancel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                    builder.setTitle("Thông báo !");
                    builder.setMessage("Đặt hàng thành công!");
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    DateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                    String date = df.format(Calendar.getInstance().getTime());
                    OrderModel orderModel = new OrderModel("",
                            tvname.getText().toString(),
                            tvnameAddress.getText()+", "+tvaddress.getText(),
                            tvphone.getText().toString(),
                            discount, total, date,
                            "Đang xử lý", list_cart
                    );
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        fb.orderFood(orderModel, sharedpreference.getID());
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.cancel();
                            startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                        }
                    },3000);

                }
            }, 2000 );
        });
        //Back
        btnBack.setOnClickListener(view ->{
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
    }

}
