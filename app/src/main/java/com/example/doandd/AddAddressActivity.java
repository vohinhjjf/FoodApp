package com.example.doandd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.JSONRead;
import com.example.doandd.database.SharedPreference;
import com.example.doandd.model.AddressModel;

import java.util.List;
import java.util.Objects;

public class AddAddressActivity extends AppCompatActivity {
    Spinner spProvince, spDistric, spCommune;
    Button btnHome, btnOffice, btnAdd, btnBack;
    EditText etName, etPhone, etAddress;
    String province, district, commune;
    JSONRead jsonRead = new JSONRead(this);
    FirestoreDatabase fb = new FirestoreDatabase();
    boolean home_office = true;
    boolean checkMacDinh = false;

    private void findViewsByIds() {
        btnHome = findViewById(R.id.btnHome);
        btnOffice = findViewById(R.id.btnOffice);
        btnAdd = findViewById(R.id.btnThemDiaChi);
        btnBack = findViewById(R.id.btnBackAddress);
        spProvince = findViewById(R.id.spinnerProvince);
        spDistric = findViewById(R.id.spinnerDistric);
        spCommune = findViewById(R.id.spinnerCommune);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
    }

    private void getData(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("Id");
        String typeAddress = intent.getStringExtra("TypeAddress");
        String name = intent.getStringExtra("Name");
        String phone = intent.getStringExtra("Phone");
        String address = intent.getStringExtra("Address");
        if(!Objects.equals(id, "id")){
            etName.setText(name);
            etPhone.setText(phone);
            etAddress.setText(address);
            home_office = Objects.equals(typeAddress, "Nhà riêng");
            btnAdd.setText("Cập nhật");
        }
    }

    public void setHomeOffice(){
        btnHome.setOnClickListener(view -> {
            home_office = true;
            btnHome.setBackground(ContextCompat.getDrawable(this,R.drawable.bg_shadow_button_click));
            btnOffice.setBackground(ContextCompat.getDrawable(this,R.drawable.bg_shadow_button));
        });
        btnOffice.setOnClickListener(view -> {
            home_office = false;
            btnOffice.setBackground(ContextCompat.getDrawable(this,R.drawable.bg_shadow_button_click));
            btnHome.setBackground(ContextCompat.getDrawable(this,R.drawable.bg_shadow_button));
        });
    }

    public void addAddress(SharedPreference sharedpreference){
        if (etName.getText().toString().trim().equalsIgnoreCase("")) {
            etName.setError("Trường này không được để trống");
        } else if (etPhone.getText().toString().trim().equalsIgnoreCase("")) {
            etPhone.setError("Trường này không được để trống");
        } else if (etAddress.getText().toString().trim().equalsIgnoreCase("")) {
            etAddress.setError("Trường này không được để trống");
        }else{
            String typeAddress=home_office?"Nhà riêng":"Văn phòng";
            System.out.println("Home/Office: "+typeAddress);
            System.out.println("Name: "+etName.getText());
            System.out.println("Phone: "+etPhone.getText());
            System.out.println("Province: "+province);
            System.out.println("District: "+district);
            System.out.println("Commune: "+commune);
            System.out.println("Address: "+etAddress.getText());
            AddressModel addressModel = new AddressModel(
                    "", etName.getText().toString(), etPhone.getText().toString(),
                    province, district, commune, etAddress.getText().toString(), typeAddress, checkMacDinh);
            fb.addAddress(addressModel, sharedpreference.getID());
            ProgressDialog mProgressDialog =new ProgressDialog(this);
            mProgressDialog.setMessage("Đang thêm địa chỉ!");
            mProgressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent get_intent = getIntent();
                    Intent intent = new Intent(AddAddressActivity.this,AddressActivity.class);
                    intent.putExtra("total", get_intent.getStringExtra("total"));
                    intent.putStringArrayListExtra("listID", get_intent.getStringArrayListExtra("listID"));
                    startActivity(intent);
                }
            }, 2000 );
        }
    }

    public void getListProvince(){
        Intent intent = getIntent();
        String st_province = intent.getStringExtra("Province");
        List<String> list_province = jsonRead.getListProvince();
        province = list_province.get(list_province.indexOf(st_province));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list_province);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvince.setAdapter(adapter);
        spProvince.setSelection(list_province.indexOf(st_province));
        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                province = list_province.get(position);
                getListDistrict(jsonRead.getListDistrict(province));
            }

            public void onNothingSelected(AdapterView parent) {
            }
        });
    }

    public void getListDistrict(List<String> items){
        Intent intent = getIntent();
        String st_district = intent.getStringExtra("District");
        district = st_district;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDistric.setAdapter(adapter);
        spDistric.setSelection(items.indexOf(st_district));
        spDistric.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                district = items.get(position);
                getListCommune(jsonRead.getListCommune(province,district));
            }

            public void onNothingSelected(AdapterView parent) {
            }
        });
    }

    public void getListCommune(List<String> items){
        Intent intent = getIntent();
        String st_commune = intent.getStringExtra("Commune");
        commune = st_commune;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCommune.setAdapter(adapter);
        spCommune.setSelection(items.indexOf(st_commune));
        spCommune.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                commune = items.get(position);
            }

            public void onNothingSelected(AdapterView parent) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        SharedPreference sharedpreference = new SharedPreference(this);
        findViewsByIds();
        getData();
        setHomeOffice();
        //
        Intent get_intent = getIntent();
        //Them dia chi
        btnAdd.setOnClickListener(view -> {
            if(get_intent.getStringExtra("Id").equals("id")){
                addAddress(sharedpreference);
            } else{
                updateAddress(get_intent.getStringExtra("Id"),sharedpreference);
            }
        });
        //Tinh
        getListProvince();
        //Back
        btnBack.setOnClickListener(view ->{
            Intent intent = new Intent(this, AddressActivity.class);
            intent.putExtra("total", get_intent.getStringExtra("total"));
            intent.putStringArrayListExtra("listID", get_intent.getStringArrayListExtra("listID"));
            startActivity(intent);
        });
    }

    private void updateAddress(String id,SharedPreference sharedpreference) {
        if (etName.getText().toString().trim().equalsIgnoreCase("")) {
            etName.setError("Trường này không được để trống");
        } else if (etPhone.getText().toString().trim().equalsIgnoreCase("")) {
            etPhone.setError("Trường này không được để trống");
        } else if (etAddress.getText().toString().trim().equalsIgnoreCase("")) {
            etAddress.setError("Trường này không được để trống");
        }else{
            String typeAddress=home_office?"Nhà riêng":"Văn phòng";
            AddressModel addressModel = new AddressModel(
                    id, etName.getText().toString(), etPhone.getText().toString(),
                    province, district, commune, etAddress.getText().toString(), typeAddress, checkMacDinh);
            fb.updateAddress(addressModel, sharedpreference.getID());
            ProgressDialog mProgressDialog =new ProgressDialog(this);
            mProgressDialog.setMessage("Đang cập nhật thông tin!");
            mProgressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent get_intent = getIntent();
                    Intent intent = new Intent(AddAddressActivity.this,AddressActivity.class);
                    intent.putExtra("total", get_intent.getStringExtra("total"));
                    intent.putStringArrayListExtra("listID", get_intent.getStringArrayListExtra("listID"));
                    startActivity(intent);
                }
            }, 2000 );
        }
    }
}
