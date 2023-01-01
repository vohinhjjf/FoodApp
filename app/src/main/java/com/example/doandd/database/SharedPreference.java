package com.example.doandd.database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class SharedPreference {
    private Activity activity;
    public SharedPreference(Activity activity){
        this.activity = activity;
    }
    public String getID(){
        SharedPreferences sharedpreferences = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedpreferences.getString("ID","");
    }

    public void deleteID(){
        SharedPreferences sharedpreferences = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("ID", "");
        editor.commit();
    }
    public void saveEmailPassword(String email, String password){
        SharedPreferences sharedpreferences = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Email",email);
        editor.putString("Password",password);
        editor.commit();
    }
    public String getEmail(){
        SharedPreferences sharedpreferences = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedpreferences.getString("Email","");
    }
    public String getPassword(){
        SharedPreferences sharedpreferences = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedpreferences.getString("Password","");
    }
    public void getMode(){
        SharedPreferences sharedPreferences = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean booleanValue = sharedPreferences.getBoolean("night_mode",false);
        if (booleanValue){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
