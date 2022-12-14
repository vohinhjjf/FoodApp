package com.example.doandd.database;

import android.app.Activity;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JSONRead {
    private Activity context;
    public JSONRead(Activity context) {
        this.context = context;
    }
    public List<String> getListProvince() {
        List<String> provinceArray= new ArrayList<>();
        try {
            // get JSONObject from JSON file
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            // fetch JSONArray named users
            JSONArray dataArray = obj.getJSONArray("data");
            // implement for loop for getting users list data
            for (int i = 0; i < dataArray.length(); i++) {
                // create a JSONObject for fetching single user data
                JSONObject dataDetail = dataArray.getJSONObject(i);
                provinceArray.add(dataDetail.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return provinceArray;
    }
    public List<String> getListDistrict(String province) {
        List<String> districtArray= new ArrayList<>();
        try {
            // get JSONObject from JSON file
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            // fetch JSONArray named users
            JSONArray dataArray = obj.getJSONArray("data");
            // implement for loop for getting users list data
            for (int i = 0; i < dataArray.length(); i++) {
                // create a JSONObject for fetching single user data
                JSONObject dataDetail = dataArray.getJSONObject(i);
                if(dataDetail.getString("name").equals(province)){
                    JSONArray level2sArray = dataDetail.getJSONArray("level2s");
                    for (int j = 0; j < level2sArray.length(); j++) {
                        districtArray.add(level2sArray.getJSONObject(j).getString("name"));
                    }
                };
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return districtArray;
    }

    public List<String> getListCommune(String province, String district) {
        List<String> communeArray= new ArrayList<>();
        try {
            // get JSONObject from JSON file
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            // fetch JSONArray named users
            JSONArray dataArray = obj.getJSONArray("data");
            // implement for loop for getting users list data
            for (int i = 0; i < dataArray.length(); i++) {
                // create a JSONObject for fetching single user data
                JSONObject dataDetail = dataArray.getJSONObject(i);
                if(dataDetail.getString("name").equals(province)){
                    JSONArray level2sArray = dataDetail.getJSONArray("level2s");
                    for (int j = 0; j < level2sArray.length(); j++) {
                        JSONObject level3sDetail = level2sArray.getJSONObject(j);
                        if(level3sDetail.getString("name").equals(district)){
                            JSONArray level3sArray = level3sDetail.getJSONArray("level3s");
                            for (int g = 0; g < level3sArray.length(); g++) {
                                communeArray.add(level3sArray.getJSONObject(g).getString("name"));
                            }
                        };
                    }
                };
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return communeArray;
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("dvhcvn.json");
            //InputStream is = new FileInputStream("dvhcvn.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
