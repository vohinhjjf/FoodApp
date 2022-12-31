package com.example.doandd.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.PaymentActivity;
import com.example.doandd.R;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.SharedPreference;
import com.example.doandd.model.VoucherModel;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder>{
    public VoucherAdapter(List<VoucherModel> list_voucher, Boolean voucher_type, String voucher,Activity activity, double total) {
        this.list_coupon = list_voucher;
        this.voucher_type = voucher_type;
        this.voucher = voucher;
        this.activity = activity;
        this.total = total;
    }
    private final List<VoucherModel> list_coupon;
    private final Boolean voucher_type;
    private final String voucher;
    private final Activity activity;
    private final double total;
    @NonNull
    @Override
    public VoucherAdapter.VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher,parent,false);
        return new VoucherViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull VoucherAdapter.VoucherViewHolder holder, int position) {
        VoucherModel voucherModel = list_coupon.get(position);
        SharedPreference sharedpreference = new SharedPreference(activity);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Thất bại  !");
        builder.setMessage("Voucher không hợp lệ!");
        builder.setNegativeButton("Đóng", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        if(voucherModel ==null){
            return;
        }
        FirestoreDatabase fb = new FirestoreDatabase();
        holder.title.setText(voucherModel.getTitle());
        holder.date.setText(voucherModel.getDate());
        if(voucher_type) {
            holder.btnSelectVoucher.setOnClickListener(view -> {
                if(voucher.equals("my voucher")){
                    Intent get_intent = activity.getIntent();
                    Intent intent = new Intent(activity, PaymentActivity.class);
                    intent.putExtra("discount", voucherModel.getDiscount());
                    intent.putExtra("maxDiscount", voucherModel.getCondition());
                    intent.putExtra("total", get_intent.getStringExtra("total"));
                    intent.putStringArrayListExtra("listID", get_intent.getStringArrayListExtra("listID"));
                    if(voucherModel.getCondition()<=total) {
                        activity.startActivity(intent);
                    }else {
                        alertDialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                alertDialog.cancel();
                            }
                        }, 2000 );
                    }
                }
                else{
                    ProgressDialog mProgressDialog =new ProgressDialog(activity);
                    mProgressDialog.setMessage("Đang xử lý...!");
                    mProgressDialog.show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog.cancel();
                            fb.user.document(sharedpreference.getID()).collection("voucher").get().addOnCompleteListener(task ->{
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    if(document.getId().equals(voucherModel.getId())){
                                        System.out.println("Co");
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                                        builder1.setTitle("Lỗi !");
                                        builder1.setMessage("Khuyến mãi này đã có!");
                                        AlertDialog alertDialog1 = builder1.create();
                                        alertDialog1.show();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                alertDialog1.cancel();
                                            }
                                        },3000);
                                        return;
                                    }
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                builder.setTitle("Thông báo !");
                                builder.setMessage("Lưu khuyến mãi thành công!");
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                fb.selectVoucher(voucherModel, sharedpreference.getID());
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        alertDialog.cancel();
                                    }
                                },3000);
                            });

                        }
                    }, 2000 );
                }
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

    static class VoucherViewHolder extends RecyclerView.ViewHolder{
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
