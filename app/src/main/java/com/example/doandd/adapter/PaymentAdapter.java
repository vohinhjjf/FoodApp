package com.example.doandd.adapter;

import android.app.Activity;
import android.graphics.Paint;
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

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.CartViewHolder>{
    private Activity context;
    private double total = 0;
    public final List<CartModel> list_cart;
    public PaymentAdapter(Activity context,List<CartModel> list_cart) {
        this.list_cart = list_cart;
        this.context = context;
    }
    @NonNull
    @Override
    public PaymentAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment,parent,false);
        return new PaymentAdapter.CartViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.CartViewHolder holder, int position) {
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
        holder.amount.setText(String.valueOf((cartModel.getAmount())));
        holder.imageView.setImageResource(cartModel.getImage());
    }
    @Override
    public int getItemCount() {
        return list_cart.size();
    }
    static class CartViewHolder extends RecyclerView.ViewHolder{
        private final TextView name,price,discount, discountPercent, amount;
        private final ImageView imageView;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgFood);
            name = itemView.findViewById(R.id.tvNameFood);
            price = itemView.findViewById(R.id.tvPriceFood);
            discount = itemView.findViewById(R.id.tvDiscountFood);
            discountPercent = itemView.findViewById(R.id.tvDiscountPercentFood);
            amount = itemView.findViewById(R.id.tvSoLuong);
        }
    }
}
