package com.example.doandd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.R;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.model.VoucherModel;

import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder>{
    public VoucherAdapter(List<VoucherModel> list_voucher, Boolean voucher_type) {
        this.list_coupon = list_voucher;
        this.voucher_type = voucher_type;
    }
    private final List<VoucherModel> list_coupon;
    private final Boolean voucher_type;
    @NonNull
    @Override
    public VoucherAdapter.VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher,parent,false);
        return new VoucherAdapter.VoucherViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull VoucherAdapter.VoucherViewHolder holder, int position) {
        VoucherModel voucherModel = list_coupon.get(position);
        if(voucherModel ==null){
            return;
        }
        FirestoreDatabase fb = new FirestoreDatabase();
        holder.title.setText(voucherModel.getTitle());
        holder.date.setText(voucherModel.getDate());
        if(voucher_type) {
            holder.btnSelectVoucher.setOnClickListener(view -> {
                //fb.selectVoucher(voucherModel.getId());
                System.out.println(voucherModel.getId());
            });
            holder.btnSelectVoucher.setText("Chọn");
        }
        else{
            holder.btnSelectVoucher.setText("");
        }
    }
    @Override
    public int getItemCount() {
        if (list_coupon!= null){
            return list_coupon.size();}
        return 0;
    }
    class VoucherViewHolder extends RecyclerView.ViewHolder{
        private final TextView title,date;
        private final Button btnSelectVoucher;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            date = itemView.findViewById(R.id.item_hsd);
            btnSelectVoucher = itemView.findViewById(R.id.btnSelectVoucher);
        }
    }
}
