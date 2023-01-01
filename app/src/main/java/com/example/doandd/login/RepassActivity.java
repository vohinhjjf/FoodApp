package com.example.doandd.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doandd.R;
import com.example.doandd.database.FirestoreDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class RepassActivity extends AppCompatActivity {
    EditText editChangeEmail;
    Button btnBackLogin, btnReset;
    FirestoreDatabase fb =new FirestoreDatabase();

    private void findViewByIds(){
        editChangeEmail = findViewById(R.id.editChangeEmail);
        btnReset = findViewById(R.id.btnReset);
        btnBackLogin = findViewById(R.id.btnBackLogin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repass);
        findViewByIds();
        //
        btnBackLogin.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
        //
        btnReset.setOnClickListener(view -> {
            resetPassword();
        });
    }

    protected final void resetPassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("Đóng", (DialogInterface.OnClickListener) (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        fb.mAuth.sendPasswordResetEmail(editChangeEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            builder.setTitle("Gửi yêu cầu thành công!");
                            builder.setMessage("Vui lòng kiểm tra thư đã gửi đến địa chỉ email đã đăng ký.");
                            alertDialog.show();
                            Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_LONG).show();

                        } else {
                            builder.setTitle("Thất bại!");
                            builder.setMessage("Email không hợp lệ.");
                            Toast.makeText(getApplicationContext(),"Error: "+task.getException(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
