package com.example.doandd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.model.hot_food;
import com.example.doandd.R;

import java.util.List;
public class hot_food_adapter extends RecyclerView.Adapter<hot_food_adapter.HotFoodViewHolder> {
    private List<hot_food>hot_food_list;
    public hot_food_adapter(List<hot_food> hot_food_list) {
        this.hot_food_list = hot_food_list;
    }
    @NonNull
    @Override
    public HotFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_best_hot_food,parent,false);
        return new HotFoodViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HotFoodViewHolder holder, int position) {
        hot_food hotFood= hot_food_list.get(position);
        if(hotFood==null){
            return;
        }
        holder.name.setText(hotFood.getName());
        holder.description.setText(hotFood.getDescription());
        holder.img.setImageResource(hotFood.getImage());
    }
    @Override
    public int getItemCount() {
        if (hot_food_list!= null){
            return hot_food_list.size();}
        return 0;
    }
    class HotFoodViewHolder extends RecyclerView.ViewHolder{
        private final TextView name;
        private final TextView description;
        private ImageView img;
        public HotFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_hot_food);
            description = itemView.findViewById(R.id.description_hot_food);
            img = itemView.findViewById(R.id.img);
        }
    }
}
