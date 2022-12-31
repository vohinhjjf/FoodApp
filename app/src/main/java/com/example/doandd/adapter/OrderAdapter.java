package com.example.doandd.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.CartActivity;
import com.example.doandd.InfoOrderActivity;
import com.example.doandd.PurchaseOrderActivity;
import com.example.doandd.R;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.SharedPreference;
import com.example.doandd.model.OrderModel;
import com.example.doandd.utils.Format;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    public OrderAdapter(List<OrderModel> list_orders, Activity activity, String status) {
        this.list_orders = list_orders;
        this.activity = activity;
        this.status = status;
    }
    private final List<OrderModel> list_orders;
    private final Activity activity;
    private final String status;
    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_order,parent,false);
        return new OrderAdapter.OrderViewHolder(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        OrderModel orderModel = list_orders.get(position);
        SharedPreference sharedpreference = new SharedPreference(activity);
        FirestoreDatabase fb = new FirestoreDatabase();
        ProgressDialog mProgressDialog =new ProgressDialog(activity);
        mProgressDialog.setMessage("Đang xử lý...!");

        if(orderModel ==null){
            return;
        }

        holder.tvTime.setText(orderModel.getOrderDate());
        holder.tvSoLuong.setText(orderModel.getListFood().size()+"");
        holder.tvtongtien.setText(new Format().currency(orderModel.getTotal())+"đ");
        holder.cartPurchase.setOnClickListener(view -> {
            Intent intent = new Intent(activity, InfoOrderActivity.class);
            intent.putExtra("id", orderModel.getId());
            activity.startActivity(intent);
        });
        switch(status){
            case "Đang xử lý":{
                holder.tvStatus.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(activity, R.drawable.ic_process),null,null,null);
                holder.btn.setText("Hủy đơn");
                holder.btn.setTextColor(ContextCompat.getColor(activity, R.color.red));
                holder.btn.setOnClickListener(view -> {
                    fb.updateStatus(sharedpreference.getID(), orderModel.getId(), "Đơn đã hủy").addOnCompleteListener(value -> {
                        if(value.isSuccessful()){
                            mProgressDialog.cancel();
                            Intent i = new Intent(activity, PurchaseOrderActivity.class);
                            activity.finish();
                            activity.overridePendingTransition(0,0);
                            activity.startActivity(i);
                            activity.overridePendingTransition(0,0);
                        }
                    });
                });
            };break;
            case "Đã giao hàng":{
                holder.tvStatus.setText("Đã giao hàng");
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.blue));
                holder.tvStatus.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(activity, R.drawable.ic_received),null,null,null);
                holder.tvStatus.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.blue)));
                holder.btn.setText("Mua lại");
                holder.btn.setTextColor(ContextCompat.getColor(activity, R.color.blue));
                holder.btn.setOnClickListener(view -> {
                    mProgressDialog.show();
                    fb.updateStatus(sharedpreference.getID(), orderModel.getId(), "Đang xử lý").addOnCompleteListener(value -> {
                        if(value.isSuccessful()){
                            mProgressDialog.cancel();
                            Intent i = new Intent(activity, PurchaseOrderActivity.class);
                            activity.finish();
                            activity.overridePendingTransition(0,0);
                            activity.startActivity(i);
                            activity.overridePendingTransition(0,0);
                        }
                    });
                });
            };break;
            case "Đơn đã hủy":{
                holder.tvStatus.setText("Đã hủy");
                holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.red));
                holder.tvStatus.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(activity, R.drawable.ic_cancel),null,null,null);
                holder.tvStatus.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.red)));
                holder.btn.setText("Mua lại");
                holder.btn.setTextColor(ContextCompat.getColor(activity, R.color.blue));
                holder.btn.setOnClickListener(view -> {
                    mProgressDialog.show();
                    fb.updateStatus(sharedpreference.getID(), orderModel.getId(), "Đang xử lý").addOnCompleteListener(value -> {
                        if(value.isSuccessful()){
                            mProgressDialog.cancel();
                            Intent i = new Intent(activity, PurchaseOrderActivity.class);
                            activity.finish();
                            activity.overridePendingTransition(0,0);
                            activity.startActivity(i);
                            activity.overridePendingTransition(0,0);
                        }
                    });
                });
            };break;
        }
    }

    @Override
    public int getItemCount() {
        if (list_orders!= null){
            return list_orders.size();}
        return 0;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvTime,tvSoLuong,tvtongtien,tvStatus;
        private final Button btn;
        private final LinearLayout cartPurchase;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuongFood);
            tvtongtien = itemView.findViewById(R.id.tvtongtien);
            btn = itemView.findViewById(R.id.btn);
            cartPurchase = itemView.findViewById(R.id.cartPurchase);
        }
    }
}
