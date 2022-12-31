package com.example.doandd.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doandd.CartActivity;
import com.example.doandd.R;
import com.example.doandd.SearchActivity;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.SharedPreference;
import com.example.doandd.model.FoodModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{
    public SearchAdapter(List<Map<String, Object>> list_recent, Activity activity, RecyclerView rvList,
                         TextView  tvNumber, LinearLayout llShowList, LinearLayout llTimKiem) {
        this.list_recent = list_recent;
        this.activity = activity;
        this.rvList = rvList;
        this.tvNumber = tvNumber;
        this.llShowList = llShowList;
        this.llTimKiem = llTimKiem;
    }
    private final List<Map<String, Object>> list_recent;
    private final Activity activity;
    private final LinearLayout llShowList,llTimKiem;
    private final TextView  tvNumber;
    private final RecyclerView rvList;
    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_recent,parent,false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        Map<String, Object> data = list_recent.get(position);
        FirestoreDatabase fb = new FirestoreDatabase();
        SharedPreference sharedPreference = new SharedPreference(activity);
        List<FoodModel> list_food = new ArrayList<>();
        List<FoodModel> list = new ArrayList<>();
        if(data ==null){
            return;
        }

        holder.tvMonAnDaTim.setText(data.get("value").toString());
        holder.btnDelete.setOnClickListener(view -> {
            fb.deleteSearchRecent(sharedPreference.getID(), data.get("id").toString());
            Intent i = new Intent(activity, SearchActivity.class);
            activity.finish();
            activity.overridePendingTransition(0,0);
            activity.startActivity(i);
            activity.overridePendingTransition(0,0);
        });
        holder.tvMonAnDaTim.setOnClickListener(view -> {
            fb.food.whereGreaterThanOrEqualTo("name", data.get("value").toString()).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    list_food.add(new FoodModel(
                                            document.getString("name"),
                                            document.getString("category"),
                                            document.getString("description"),
                                            document.getString("image"),
                                            document.getDouble("price"),
                                            Math.toIntExact(document.getLong("discountPercentage")),
                                            document.getDouble("rate")
                                    ));
                                }
                                for (int i =0; i<list_food.size(); i++){
                                    if(list_food.get(i).getName().toLowerCase().contains(data.get("value").toString().toLowerCase())){
                                        list.add(list_food.get(i));
                                    }
                                }
                                rvList.setLayoutManager(new GridLayoutManager(activity, 2));
                                FoodAdapter food_adapter = new FoodAdapter(list, activity, "MainActivity");
                                rvList.setAdapter(food_adapter);
                                tvNumber.setText(list.size()+" món ăn được tìm thấy");
                                llShowList.setVisibility(View.VISIBLE);
                                llTimKiem.setVisibility(View.INVISIBLE);
                            }
                            else {
                                Log.w( "Error", task.getException());
                            }
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        if (list_recent!= null){
            return list_recent.size();}
        return 0;
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvMonAnDaTim;
        private final Button btnDelete;
        private final RelativeLayout rlRecent;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonAnDaTim = itemView.findViewById(R.id.tvMonAnDaTim);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            rlRecent = itemView.findViewById(R.id.rlRecent);
        }
    }
}
