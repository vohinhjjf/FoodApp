package com.example.doandd.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.InfoFoodActivity;
import com.example.doandd.ListFoodActivity;
import com.example.doandd.MainActivity;
import com.example.doandd.model.FoodModel;
import com.example.doandd.R;
import com.example.doandd.utils.Format;
import com.example.doandd.utils.ImageLoadTask;

import java.util.List;
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{
    public FoodAdapter(List<FoodModel> list_food,Activity context, String id) {
        this.list_food = list_food;
        this.context = context;
        this.id = id;
    }
    private final List<FoodModel> list_food;
    private final Activity context;
    private final String id;
    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food,parent,false);
        return new FoodViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodModel foodModel= list_food.get(list_food.size()-position-1);
        if(foodModel==null){
            return;
        }
        new ImageLoadTask(foodModel.getImage(), holder.img).execute();
        double discountPrice = foodModel.getPrice()*(100 -foodModel.getDiscountPercentage())/100;
        holder.name.setText(foodModel.getName());
        holder.rate.setRating((float) foodModel.getRate());
        if(foodModel.getDiscountPercentage()!=0) {
            holder.price.setVisibility(View.VISIBLE);
            holder.price.setText(new Format().currency(foodModel.getPrice()) + "đ");
            holder.price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.percent.setText(foodModel.getDiscountPercentage()+"%");
        }else {
            holder.price.setVisibility(View.GONE);
            holder.cLDiscount.setVisibility(View.INVISIBLE);
        }
        holder.priceDiscount.setText(new Format().currency(discountPrice) +"đ");
        holder.llFood.setOnClickListener(view ->{
            Intent intent = new Intent(context, InfoFoodActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("name", foodModel.getName());
            intent.putExtra("price", foodModel.getPrice());
            intent.putExtra("discount percent", foodModel.getDiscountPercentage());
            intent.putExtra("discount", new Format().currency(discountPrice) +"đ");
            intent.putExtra("description", foodModel.getDescription());
            intent.putExtra("image", foodModel.getImage());
            intent.putExtra("rating", (float) foodModel.getRate());
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        if (list_food!= null){
            return list_food.size();}
        return 0;
    }
    static class FoodViewHolder extends RecyclerView.ViewHolder{
        private final TextView name,price,priceDiscount, percent;
        private final ImageView img;
        private final RatingBar rate;
        private final ConstraintLayout cLDiscount;
        private final LinearLayout llFood;
        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_best_seller_food);
            price = itemView.findViewById(R.id.price_best_seller_food);
            priceDiscount = itemView.findViewById(R.id.new_price_best_seller_food);
            img = itemView.findViewById(R.id.imgListFood);
            rate = itemView.findViewById(R.id.rate);
            cLDiscount = itemView.findViewById(R.id.discount);
            percent = itemView.findViewById(R.id.tvPercent);
            llFood = itemView.findViewById(R.id.llFood);
        }
    }
}
