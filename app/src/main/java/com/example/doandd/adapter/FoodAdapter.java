package com.example.doandd.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.model.FoodModel;
import com.example.doandd.R;
import com.example.doandd.utils.Format;
import com.example.doandd.utils.ImageLoadTask;

import java.util.List;
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.best_seller_food_View_Holder>{
    public FoodAdapter(List<FoodModel> best_seller_food_list) {
        this.best_seller_food_list = best_seller_food_list;
    }
    private final List<FoodModel> best_seller_food_list;
    @NonNull
    @Override
    public best_seller_food_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food,parent,false);
        return new best_seller_food_View_Holder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull best_seller_food_View_Holder holder, int position) {
        FoodModel bestSellerFood= best_seller_food_list.get(best_seller_food_list.size()-position-1);
        if(bestSellerFood==null){
            return;
        }
        double discountPrice = bestSellerFood.getPrice()*(100 -bestSellerFood.getDiscountPercentage())/100;
        holder.name.setText(bestSellerFood.getName());
        holder.rate.setRating((float) bestSellerFood.getRate());
        if(bestSellerFood.getDiscountPercentage()!=0) {
            holder.price.setVisibility(View.VISIBLE);
            holder.price.setText(new Format().currency(bestSellerFood.getPrice()) + "đ");
            holder.price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.percent.setText(bestSellerFood.getDiscountPercentage()+"%");
        }else {
            holder.price.setVisibility(View.GONE);
            holder.cLDiscount.setVisibility(View.INVISIBLE);
        }
        holder.priceDiscount.setText(new Format().currency(discountPrice) +"đ");
        new ImageLoadTask(bestSellerFood.getImage(), holder.img).execute();
    }
    @Override
    public int getItemCount() {
        if (best_seller_food_list!= null){
            return best_seller_food_list.size();}
        return 0;
    }
    static class best_seller_food_View_Holder extends RecyclerView.ViewHolder{
        private final TextView name,price,priceDiscount, percent;
        private final ImageView img;
        private final RatingBar rate;
        private final ConstraintLayout cLDiscount;
        public best_seller_food_View_Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_best_seller_food);
            price = itemView.findViewById(R.id.price_best_seller_food);
            priceDiscount = itemView.findViewById(R.id.new_price_best_seller_food);
            img = itemView.findViewById(R.id.img);
            rate = itemView.findViewById(R.id.rate);
            cLDiscount = itemView.findViewById(R.id.discount);
            percent = itemView.findViewById(R.id.tvPercent);
        }
    }
}
