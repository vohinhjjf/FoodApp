package com.example.doandd.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import com.bumptech.glide.Glide;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.CartActivity;
import com.example.doandd.R;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.SharedPreference;
import com.example.doandd.model.CartModel;
import com.example.doandd.utils.Format;
import com.example.doandd.utils.ImageLoadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private final Activity context;
    TextView tvTotal;
    private double total = 0;
    public final List<CartModel> list_cart;
    List<String> listId;
    double price, amount1=0;
    public CartAdapter(Activity context,List<CartModel> list_cart,List<String> listId, TextView tvTotal) {
        this.list_cart = list_cart;
        this.listId = listId;
        this.context = context;
        this.tvTotal = tvTotal;
    }
    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
        return new CartViewHolder(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        CartModel cartModel= list_cart.get(position);
        FirestoreDatabase fb = new FirestoreDatabase();
        SharedPreference sharedpreference = new SharedPreference(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo !");
        builder.setMessage("Bạn có chắc muốn xóa món ăn này?");
        builder.setPositiveButton("Đồng ý", (DialogInterface.OnClickListener) (dialog, which) -> {
            fb.cart.document(sharedpreference.getID()).collection("products")
                    .document(cartModel.getId()).delete().addOnSuccessListener(value -> {
                        dialog.cancel();
                    });
            Intent i = new Intent(context, CartActivity.class);
            context.finish();
            context.overridePendingTransition(0,0);
            context.startActivity(i);
            context.overridePendingTransition(0,0);
        });
        builder.setNegativeButton("Hủy", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        if(cartModel==null){
            return;
        }
        AtomicInteger amount = new AtomicInteger((int) cartModel.getAmount());
        double discountPrice = cartModel.getPrice()*(100 -cartModel.getDiscountPercentage())/100;

        new ImageLoadTask(cartModel.getImage(), holder.imageView).execute();
        //Glide.with(context).load(R.drawable.loading1).into(holder.imageView);
        holder.name.setText(cartModel.getName());
        holder.price.setText(new Format().currency(cartModel.getPrice())+"đ");
        holder.price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.discount.setText(new Format().currency(discountPrice) +"đ");
        holder.discountPercent.setText(cartModel.getDiscountPercentage() +"%");
        holder.rate.setRating((float) cartModel.getRate());
        holder.checkbuy.setChecked(cartModel.getCheckbuy());
        holder.cartView.setOnClickListener(view -> {
            if(!holder.checkbuy.isChecked()){
                total = total + discountPrice*amount.get();
                listId.add(cartModel.getId());
            }else {
                listId.remove(cartModel.getId());
                total = total - discountPrice*amount.get();
            }
            System.out.println(amount.get());
            tvTotal.setText(new Format().currency(total) +"đ");
            holder.checkbuy.setChecked(!holder.checkbuy.isChecked());
            Map<String, Object> data = new HashMap<>();
            data.put("total", total);
        });
        holder.btnMinus.setOnClickListener(view -> {
            if (amount.get()!=1){
                amount.getAndDecrement();
                holder.amount.setText(String.valueOf((amount.get())));
                if(holder.checkbuy.isChecked()){
                    total = total - discountPrice;
                    tvTotal.setText(new Format().currency(total) +"đ");
                }
                fb.cart.document(sharedpreference.getID()).collection("products").document(cartModel.getId()).update("amount", amount.get());
            } else {
                alertDialog.show();
            }
            if (amount.get()==1){
                holder.btnMinus.setForeground(ContextCompat.getDrawable(context, R.drawable.ic_delete));
            }else {
                holder.btnMinus.setForeground(ContextCompat.getDrawable(context, R.drawable.ic_minus));
            }
        });
        holder.btnPlus.setOnClickListener(view -> {
            amount.getAndIncrement();
            holder.amount.setText(String.valueOf((amount.get())));
            holder.btnMinus.setForeground(ContextCompat.getDrawable(context, R.drawable.ic_minus));
            if(holder.checkbuy.isChecked()){
                total = total + discountPrice;
                tvTotal.setText(new Format().currency(total) +"đ");
            }
            fb.cart.document(sharedpreference.getID()).collection("products").document(cartModel.getId()).update("amount", amount.get());
        });
        holder.amount.setText(String.valueOf((amount.get())));
        //holder.name.set
    }
    @Override
    public int getItemCount() {
        return list_cart.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder{
        private final TextView name,price,discount, discountPercent, amount;
        private final RatingBar rate;
        private final CheckBox checkbuy;
        private final ImageView imageView;
        private final LinearLayout cartView;
        private final Button btnMinus, btnPlus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.tvName);
            price = itemView.findViewById(R.id.tvPrice);
            discount = itemView.findViewById(R.id.tvDiscount);
            discountPercent = itemView.findViewById(R.id.tvDiscountPercent);
            rate = itemView.findViewById(R.id.rating);
            checkbuy = itemView.findViewById(R.id.checkBuy);
            amount = itemView.findViewById(R.id.tvAmount);
            cartView = itemView.findViewById(R.id.cart_view);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
        }
    }
}
