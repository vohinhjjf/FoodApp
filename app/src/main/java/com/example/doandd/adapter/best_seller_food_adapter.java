package com.example.doandd.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.model.best_seller_food;
import com.example.doandd.R;

import java.util.List;
public class best_seller_food_adapter extends RecyclerView.Adapter<best_seller_food_adapter.best_seller_food_View_Holder>{
    public best_seller_food_adapter(List<best_seller_food> best_seller_food_list) {
        this.best_seller_food_list = best_seller_food_list;
    }
    private List<best_seller_food> best_seller_food_list;
    @NonNull
    @Override
    public best_seller_food_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_best_seller_food,parent,false);
        return new best_seller_food_View_Holder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull best_seller_food_View_Holder holder, int position) {
        best_seller_food bestSellerFood= best_seller_food_list.get(position);
        if(bestSellerFood==null){
            return;
        }
        holder.name.setText(bestSellerFood.getName());
        holder.description.setText(bestSellerFood.getDescription());
        holder.description.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.img.setImageResource(bestSellerFood.getImage());
    }
    @Override
    public int getItemCount() {
        if (best_seller_food_list!= null){
            return best_seller_food_list.size();}
        return 0;
    }
    class best_seller_food_View_Holder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView description;
        private ImageView img;
        public best_seller_food_View_Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_best_seller_food);
            description = itemView.findViewById(R.id.price_best_seller_food);
            img = itemView.findViewById(R.id.img);
        }
    }
}
