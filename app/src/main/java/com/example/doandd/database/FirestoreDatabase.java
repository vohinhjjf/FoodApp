package com.example.doandd.database;

import static com.google.android.gms.tasks.Tasks.await;
import static com.google.android.gms.tasks.Tasks.whenAllComplete;
import static com.google.android.gms.tasks.Tasks.whenAllSuccess;

import androidx.annotation.NonNull;

import com.example.doandd.model.AddressModel;
import com.example.doandd.model.VoucherModel;
import com.example.doandd.model.FoodModel;
import com.example.doandd.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FirestoreDatabase {
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference user = db.collection("Users");
    public CollectionReference voucher = db.collection("Voucher");
    public CollectionReference food = db.collection("Foods");

    public void createUser(UserModel userModel){
        user.document(userModel.getId()).set(userModel.toMap());
    }

    public void addFoods(FoodModel foodModel){
        food.add(foodModel.toMap());
    }

    public void selectVoucher(String voucherId){
        Map<String, Object> data = new HashMap<>();
        data.put("active", false);
        data.put("voucherId", voucherId);
        user.document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection("voucher").document(voucherId).set(data);
    }

    public void addAddress(AddressModel addressModel){
        user.document(mAuth.getCurrentUser().getUid()).collection("delivery address").add(addressModel.toMap());
    }

    public void setDefaultAddress(String id){
        user.document(mAuth.getCurrentUser().getUid()).collection("delivery address").whereEqualTo("addressDefault", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           if(task.getResult().getDocuments().size()>0) {
                               user.document(mAuth.getCurrentUser().getUid()).collection("delivery address").document(task.getResult().getDocuments().get(0).getId()).update("addressDefault", false);
                           }
                           user.document(mAuth.getCurrentUser().getUid()).collection("delivery address").document(id).update("addressDefault", true);
                       }
                   }
               });
    }
}
