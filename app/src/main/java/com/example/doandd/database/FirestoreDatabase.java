package com.example.doandd.database;

import static com.google.android.gms.tasks.Tasks.await;
import static com.google.android.gms.tasks.Tasks.whenAllComplete;
import static com.google.android.gms.tasks.Tasks.whenAllSuccess;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.doandd.model.AddressModel;
import com.example.doandd.model.CartModel;
import com.example.doandd.model.OrderModel;
import com.example.doandd.model.VoucherModel;
import com.example.doandd.model.FoodModel;
import com.example.doandd.model.UserModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FirestoreDatabase {
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage rootRef = FirebaseStorage.getInstance();
    StorageReference storageRef = rootRef.getReferenceFromUrl("gs://foodapp-dc6b4.appspot.com/");
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference user = db.collection("Users");
    public CollectionReference voucher = db.collection("Voucher");
    public CollectionReference food = db.collection("Foods");
    public CollectionReference cart = db.collection("Cart");

    public void createUser(UserModel userModel){
        user.document(userModel.getId()).set(userModel.toMap());
    }

    public void addFoods(FoodModel foodModel){
        food.add(foodModel.toMap());
    }

    public void selectVoucher(VoucherModel voucher, String uid){
        user.document(Objects.requireNonNull(uid)).collection("voucher").document(voucher.getId()).set(voucher.toMap());
    }

    public void addAddress(AddressModel addressModel, String uid){
        user.document(uid).collection("delivery address").add(addressModel.toMap());
    }

    public void updateAddress(AddressModel addressModel, String uid){
        user.document(uid).collection("delivery address").document(addressModel.getId()).update(addressModel.toMap());
    }

    public void setDefaultAddress(String id, String uid){
        user.document(uid).collection("delivery address").whereEqualTo("addressDefault", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           if(task.getResult().getDocuments().size()>0) {
                               user.document(uid).collection("delivery address").document(task.getResult().getDocuments().get(0).getId()).update("addressDefault", false);
                           }
                           user.document(uid).collection("delivery address").document(id).update("addressDefault", true);
                       }
                   }
               });
    }

    public Task addCart(CartModel cartModel, String uid){
        return cart.document(uid).collection("products").add(cartModel.toMap());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void orderFood(OrderModel orderModel, String uid){
        user.document(uid).collection("purchase history").add(orderModel.toMap()).addOnCompleteListener(task -> {
            for(int i=0; i< orderModel.getListFood().size(); i++) {
                cart.document(uid).collection("products").document(orderModel.getListFood().get(i).getId()).delete();
            }
        });
    }

    public Task updateStatus(String uid, String id, String status){
        DateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        Map<String, Object> data = new HashMap<>();
        data.put("orderStatus", status);
        if(Objects.equals(status, "Đang xử lý")) {
            data.put("orderDate", date);
        }
       return user.document(uid).collection("purchase history").document(id).update(data);
    }

    public void addSearchRecent(String id, String value){
        Map<String, Object> data = new HashMap<>();
        data.put("value",value);
        user.document(id).collection("search history").add(data);
    }

    public void deleteSearchRecent(String id, String value){
        user.document(id).collection("search history").document(value).delete();
    }

    public void uploadImage(Map<String, Object> data, Uri file, String uid, String type){
        StorageReference riversRef = storageRef.child("user/"+uid+"/"+type+"/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return riversRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            data.put("image", downloadUri);
                            if(Objects.equals(type, "avatar")) {
                                user.document(uid).update(data);
                            }
                            else {
                                user.document(uid).collection("feecback").add(data);
                            }
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });

            }
        });
    }
}
