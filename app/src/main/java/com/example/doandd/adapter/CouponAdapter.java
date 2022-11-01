package com.example.doandd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.R;
import com.example.doandd.model.CouponModel;

import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponViewHolder>{
    public CouponAdapter(List<CouponModel> list_coupon) {
        this.list_coupon = list_coupon;
    }
    private final List<CouponModel> list_coupon;
    @NonNull
    @Override
    public CouponAdapter.CouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon,parent,false);
        return new CouponAdapter.CouponViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CouponAdapter.CouponViewHolder holder, int position) {
        CouponModel couponModel= list_coupon.get(position);
        if(couponModel==null){
            return;
        }
        holder.title.setText(couponModel.getTitle());
        holder.date.setText(couponModel.getDate());
    }
    @Override
    public int getItemCount() {
        if (list_coupon!= null){
            return list_coupon.size();}
        return 0;
    }
    class CouponViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView date;
        public CouponViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            date = itemView.findViewById(R.id.item_hsd);
        }
    }
}
