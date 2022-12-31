package com.example.doandd.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.R;
import com.example.doandd.utils.Format;
import com.example.doandd.utils.ImageLoadTask;

import java.util.List;
import java.util.Map;

public class InfoOrderAdapter extends RecyclerView.Adapter<InfoOrderAdapter.InfoOrderViewHolder>{
    private Activity context;
    private double total = 0;
    public final List<Map<String, Object>> list_order;
    public InfoOrderAdapter(Activity context, List<Map<String, Object>> list_order) {
        this.list_order = list_order;
        this.context = context;
    }
    @NonNull
    @Override
    public InfoOrderAdapter.InfoOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment,parent,false);
        return new InfoOrderAdapter.InfoOrderViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull InfoOrderAdapter.InfoOrderViewHolder holder, int position) {
        Map<String, Object> map = list_order.get(position);
        if(map==null){
            return;
        }
        double price = (double) map.get("price");
        double discountPercent = (double) map.get("discountPercentage");
        double discountPrice = price * (100 - discountPercent)/100;
        holder.name.setText(map.get("name").toString());
        holder.price.setText(new Format().currency(price)+"đ");
        holder.price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.discount.setText(new Format().currency(discountPrice) +"đ");
        holder.discountPercent.setText(map.get("discountPercentage") +"%");
        holder.amount.setText(map.get("amount").toString());
        new ImageLoadTask(map.get("image").toString(), holder.imageView).execute();
    }
    @Override
    public int getItemCount() {
        return list_order.size();
    }
    static class InfoOrderViewHolder extends RecyclerView.ViewHolder{
        private final TextView name,price,discount, discountPercent, amount;
        private final ImageView imageView;

        public InfoOrderViewHolder(@NonNull View itemView) {
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
