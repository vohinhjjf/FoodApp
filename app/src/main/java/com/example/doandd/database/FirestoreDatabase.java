package com.example.doandd.database;

import static com.google.android.gms.tasks.Tasks.await;
import static com.google.android.gms.tasks.Tasks.whenAllComplete;
import static com.google.android.gms.tasks.Tasks.whenAllSuccess;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.doandd.model.CouponModel;
import com.example.doandd.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirestoreDatabase {
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference user = db.collection("Users");
    public CollectionReference voucher = db.collection("Voucher");

    public void createUser(UserModel userModel){
        user.document(userModel.getId()).set(userModel.toMap());
    }

    public List<CouponModel> getListVoucher() {
        List<CouponModel> list = new ArrayList();
        System.out.println("Buoc 1");
        try {
            voucher.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            list.add(new CouponModel(
                                    document.getBoolean("active"),
                                    document.getString("name"),
                                    document.getDouble("discount"),
                                    document.getDouble("condition"),
                                    document.getString("datetime")
                            ));
                        }
                        //System.out.println(list.size());
                        System.out.println("Buoc 3");
                    } else {
                        //Log.w( "Error getting documents.", task.getException());
                    }
                }
            }).wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println("Buoc 4");
        //System.out.println(list.size());
        return list;
    }

}
