package com.example.doandd.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.R;
import com.example.doandd.model.CartModel;
import com.example.doandd.utils.Format;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private Activity context;
    private double total = 0;
    public final List<CartModel> list_cart;
    public CartAdapter(Activity context,List<CartModel> list_cart) {
        this.list_cart = list_cart;
        this.context = context;
    }
    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
        return new CartViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        CartModel cartModel= list_cart.get(position);
        if(cartModel==null){
            return;
        }
        double discountPrice = cartModel.getPrice()*(100 -cartModel.getDiscountPercentage())/100;
        holder.name.setText(cartModel.getName());
        holder.price.setText(new Format().currency(cartModel.getPrice())+"đ");
        holder.price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.discount.setText(new Format().currency(discountPrice) +"đ");
        holder.discountPercent.setText(cartModel.getDiscountPercentage() +"%");
        holder.rate.setRating(Float.parseFloat(String.valueOf(cartModel.getRate())));
        holder.checkbuy.setChecked(cartModel.getCheckbuy());
        holder.amount.setText(String.valueOf((cartModel.getAmount())));
        holder.imageView.setImageResource(cartModel.getImage());
        holder.cartView.setOnClickListener(view -> {
            if(!holder.checkbuy.isChecked()){
                total = total + discountPrice;
            }else {
                total = total - discountPrice;
            }

            holder.checkbuy.setChecked(!holder.checkbuy.isChecked());
            System.out.println("Total: "+ total);
        });
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
        }
    }
}
