package com.example.doandd.database;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

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
}
