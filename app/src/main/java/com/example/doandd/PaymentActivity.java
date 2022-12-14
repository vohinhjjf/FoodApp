package com.example.doandd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.adapter.CartAdapter;
import com.example.doandd.adapter.PaymentAdapter;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.model.AddressModel;
import com.example.doandd.model.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    TextView tvname, tvnameAddress, tvaddress, tvphone,tvtienhang, tvphivanchuyen, tvtong;
    Button btnUpdateAddress, btnAddress, btnBack;
    RadioButton rbGiaoHang;
    RecyclerView ryList;
    LinearLayout llAddress;
    FirestoreDatabase fb = new FirestoreDatabase();

    private void findViewsByIds() {
        tvname = findViewById(R.id.tvNameRecipient);
        tvnameAddress = findViewById(R.id.tvNameAddress);
        tvaddress = findViewById(R.id.tvAddress);
        tvphone = findViewById(R.id.tvPhone);
        tvtienhang = findViewById(R.id.tvTongTienHang);
        tvphivanchuyen = findViewById(R.id.tvPhiVanChuyen);
        tvtong = findViewById(R.id.tvTongTien);
        btnUpdateAddress = findViewById(R.id.btnUpdateAddress);
        btnAddress = findViewById(R.id.btnAddress);
        btnBack = findViewById(R.id.btnBackPayment);
        rbGiaoHang = findViewById(R.id.rbGiaoHang);
        ryList = findViewById(R.id.ryList);
        llAddress = findViewById(R.id.llAddress);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        findViewsByIds();
        getAddressDefault();

        List<CartModel> list_cart = new ArrayList<>();
        list_cart.add(new CartModel("Bún cá",35000,10,R.drawable.bun_ca,4,1,false));
        list_cart.add(new CartModel("Cơm sườn",30000,5,R.drawable.com_suon,4.5,1,false));
        list_cart.add(new CartModel("Hủ tiếu bò kho",40000,3,R.drawable.hu_tieu_bo_kho,4.6,1,false));
        list_cart.add(new CartModel("Cơm gà xôi mỡ",45000,5,R.drawable.com_ga_xoi_mo,4.6,1,false));
        list_cart.add(new CartModel("Bún bò huế",65000,10,R.drawable.bun_bo_hue,4.6,1,false));
        list_cart.add(new CartModel("Phở bò",60000,15,R.drawable.pho_bo,4.6,1,false));

        PaymentAdapter paymentAdapter = new PaymentAdapter(this, list_cart);
        ryList.setAdapter(paymentAdapter);
        ryList.setLayoutManager(
                new LinearLayoutManager(PaymentActivity.this));

        //Update address
        btnUpdateAddress.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddressActivity.class);
            startActivity(intent);
        });

        //Back
        btnBack.setOnClickListener(view ->{
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
    }

}
