package com.example.doandd.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doandd.AccountActivity;
import com.example.doandd.MainActivity;
import com.example.doandd.R;
import com.example.doandd.database.SharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    TextView tvForgotPassword;
    EditText email, password;
    Button btnLogin, btnSignUp;
    CheckBox checkBox, cbRemember;
    FirebaseAuth mAuth;

    private void findViewByIds(){
        email = (EditText) findViewById(R.id.editEmail);
        password = (EditText) findViewById(R.id.editPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        checkBox = findViewById(R.id.checkBox);
        cbRemember = findViewById(R.id.cbRemember);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewByIds();
        HideShowPassword();
        getEmailPassword();
        mAuth = FirebaseAuth.getInstance();
        //
        btnLogin.setOnClickListener(view ->
        {
            System.out.println(email.getText());
            System.out.println(password.getText());
            loginUser();
        });
        //
        btnSignUp.setOnClickListener(view ->
        {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
        //
        tvForgotPassword.setOnClickListener(view ->{
            startActivity(new Intent(LoginActivity.this, RepassActivity.class));
        });
        //

    }
    public void getEmailPassword(){
        SharedPreference sharedPreference = new SharedPreference(this);
        email.setText(sharedPreference.getEmail());
        password.setText(sharedPreference.getPassword());
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sharedPreference.saveEmailPassword(email.getText().toString(), password.getText().toString());
                }
                else {
                    sharedPreference.saveEmailPassword("", "");
                }
            }
        });
    }

    private void HideShowPassword(){
        password.setTransformationMethod(new PasswordTransformationMethod());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    password.setTransformationMethod(null);
                }
                else {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
    }

    private void loginUser(){
        String _email = email.getText().toString();
        String _password = password.getText().toString();

        if (TextUtils.isEmpty(_email)){
            email.setError("Email can not empty");
            email.requestFocus();
        }else if (TextUtils.isEmpty(_password)){
            password.setError("Password can not empty");
            password.requestFocus();
        }else {
            mAuth.signInWithEmailAndPassword(_email,_password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("ID", mAuth.getUid());
                        editor.commit();
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(LoginActivity.this, "User login successfully",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Authentication failed."+ Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
