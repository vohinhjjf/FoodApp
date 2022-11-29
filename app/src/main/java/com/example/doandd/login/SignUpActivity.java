package com.example.doandd.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doandd.MainActivity;
import com.example.doandd.R;
import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    EditText name, email, password;
    TextView btnHaveAccount;
    Button btnSignUp;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = (EditText) findViewById(R.id.editName);
        email = (EditText) findViewById(R.id.editEmail2);
        password = (EditText) findViewById(R.id.editPassword2);
        btnSignUp = (Button) findViewById(R.id.btnSignUp2);
        btnHaveAccount = (TextView) findViewById(R.id.alreadyHaveAccount);

        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(view -> {
            createUser();
        });

        btnHaveAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }
    private void createUser(){
        FirestoreDatabase fb = new FirestoreDatabase();
        String _name = name.getText().toString();
        String _email = email.getText().toString();
        String _password = password.getText().toString();

        if(TextUtils.isEmpty(_name)){
            name.setError("Name can not empty");
            name.requestFocus();
        }else if (TextUtils.isEmpty(_email)){
            email.setError("Email can not empty");
            email.requestFocus();
        }else if (TextUtils.isEmpty(_password)){
            password.setError("Password can not empty");
            password.requestFocus();
        }else {
            mAuth.createUserWithEmailAndPassword(_email,_password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        fb.createUser(new UserModel(
                                task.getResult().getUser().getUid(),_name,"",_email, "","",""));
                        Toast.makeText(SignUpActivity.this, "User registered successfully",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(SignUpActivity.this, "Authentication failed."+ Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
