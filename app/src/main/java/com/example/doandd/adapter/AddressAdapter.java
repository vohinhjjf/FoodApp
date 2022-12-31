package com.example.doandd.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.AddAddressActivity;
import com.example.doandd.AddressActivity;
import com.example.doandd.CartActivity;
import com.example.doandd.MainActivity;
import com.example.doandd.PaymentActivity;
import com.example.doandd.R;
import com.example.doandd.SplashActivity;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.SharedPreference;
import com.example.doandd.login.LoginActivity;
import com.example.doandd.model.AddressModel;
import com.example.doandd.utils.Format;

import java.util.List;
import java.util.Objects;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder>{
    private Activity context;
    private double total = 0;
    public final List<AddressModel> list_Address;
    public AddressAdapter(Activity context,List<AddressModel> list_Address) {
        this.list_Address = list_Address;
        this.context = context;
    }
    @NonNull
    @Override
    public AddressAdapter.AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address,parent,false);
        return new AddressAdapter.AddressViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.AddressViewHolder holder, int position) {
        AddressModel addressModel= list_Address.get(position);
        SharedPreference sharedpreference = new SharedPreference(context);
        FirestoreDatabase fb = new FirestoreDatabase();
        Intent get_intent = context.getIntent();

        ProgressDialog mProgressDialog =new ProgressDialog(context);
        mProgressDialog.setMessage("Đang thiết lập địa chỉ mặc định!");

        if(addressModel==null){
            return;
        }

        if(Objects.equals(addressModel.getTypeAddress(), "Nhà riêng")){
            holder.imageView.setImageResource(R.drawable.home);
        } else{
            holder.imageView.setImageResource(R.drawable.office);
        }

        holder.name.setText(addressModel.getName());
        holder.phone.setText(addressModel.getPhone());
        holder.nameAddress.setText(addressModel.getAddress());
        holder.address.setText(addressModel.getXa()+", "+addressModel.getHuyen()+", "+addressModel.getTinh());
        holder.btnEdit.setOnClickListener(view -> {
            Intent intent = new Intent(context, AddAddressActivity.class);
            intent.putExtra("Id", addressModel.getId());
            intent.putExtra("TypeAddress", addressModel.getTypeAddress());
            intent.putExtra("Name", addressModel.getName());
            intent.putExtra("Phone", addressModel.getPhone());
            intent.putExtra("Address", addressModel.getAddress());
            intent.putExtra("Province", addressModel.getTinh());
            intent.putExtra("District", addressModel.getHuyen());
            intent.putExtra("Commune", addressModel.getXa());
            intent.putExtra("Check", addressModel.getMacDinh());
            intent.putExtra("total", get_intent.getStringExtra("total"));
            intent.putStringArrayListExtra("listID", get_intent.getStringArrayListExtra("listID"));
            context.startActivity(intent);
        });
        holder.btnDefault.setVisibility(addressModel.getMacDinh() ?View.INVISIBLE:View.VISIBLE);
        holder.cartView.setOnClickListener(view -> {
            fb.setDefaultAddress(addressModel.getId(), sharedpreference.getID());
            if(!get_intent.getStringExtra("total").equals("account")){
                mProgressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, PaymentActivity.class);
                        intent.putExtra("total", get_intent.getStringExtra("total"));
                        intent.putStringArrayListExtra("listID", get_intent.getStringArrayListExtra("listID"));
                        context.startActivity(intent);
                    }
                }, 2000 );
            }
            else {
                Intent i = new Intent(context, AddressActivity.class);
                i.putExtra("total", get_intent.getStringExtra("total"));
                i.putStringArrayListExtra("listID", get_intent.getStringArrayListExtra("listID"));
                context.finish();
                context.overridePendingTransition(0,0);
                context.startActivity(i);
                context.overridePendingTransition(0,0);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list_Address.size();
    }
    static class AddressViewHolder extends RecyclerView.ViewHolder{
        private final TextView name,phone,nameAddress, address;
        private final ImageView imageView;
        private final Button btnEdit, btnDefault;
        private final LinearLayout cartView;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgLocation);
            name = itemView.findViewById(R.id.tvNameRecipient1);
            phone = itemView.findViewById(R.id.tvPhoneRecipient);
            nameAddress = itemView.findViewById(R.id.tvNameAddress);
            address = itemView.findViewById(R.id.tvAddress);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDefault = itemView.findViewById(R.id.btnDefault);
            cartView = itemView.findViewById(R.id.cart_view);
        }
    }
}
