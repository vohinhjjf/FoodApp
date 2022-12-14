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
import com.example.doandd.model.AddressModel;

import java.util.List;

public class AddAddressActivity extends AppCompatActivity {
    Spinner spProvince, spDistric, spCommune;
    Button btnHome, btnOffice, btnAdd, btnBack;
    EditText etName, etPhone, etAddress;
    CheckBox cbMacDinh;
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
        cbMacDinh = findViewById(R.id.cbMacdinh);
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

    public void addAddress(){
        if (etName.getText().toString().trim().equalsIgnoreCase("")) {
            etName.setError("This field can not be blank");
        } else if (etPhone.getText().toString().trim().equalsIgnoreCase("")) {
            etPhone.setError("This field can not be blank");
        } else if (etAddress.getText().toString().trim().equalsIgnoreCase("")) {
            etAddress.setError("This field can not be blank");
        }else{
            String typeAddress=home_office?"Nhà riêng":"Văn phòng";
            System.out.println("Home/Office: "+typeAddress);
            System.out.println("Name: "+etName.getText());
            System.out.println("Phone: "+etPhone.getText());
            System.out.println("Province: "+province);
            System.out.println("District: "+district);
            System.out.println("Commune: "+commune);
            System.out.println("Address: "+etAddress.getText());
            System.out.println("Default: "+checkMacDinh);
            AddressModel addressModel = new AddressModel(
                    "", etName.getText().toString(), etPhone.getText().toString(),
                    province, district, commune, etAddress.getText().toString(), typeAddress, checkMacDinh);
            fb.addAddress(addressModel);
            ProgressDialog mProgressDialog =new ProgressDialog(this);
            mProgressDialog.setMessage("Adding address!");
            mProgressDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(AddAddressActivity.this,AddressActivity.class);
                    startActivity(intent);
                }
            }, 2000 );
        }
    }

    public void getListProvince(){
        List<String> list_province = jsonRead.getListProvince();
        province = list_province.get(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list_province);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvince.setAdapter(adapter);
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
        district = items.get(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDistric.setAdapter(adapter);
        spDistric.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                district = items.get(position);
                System.out.println("Select: "+jsonRead.getListCommune(province,district).get(0));
                getListCommune(jsonRead.getListCommune(province,district));
            }

            public void onNothingSelected(AdapterView parent) {
            }
        });
    }

    public void getListCommune(List<String> items){
        commune = items.get(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCommune.setAdapter(adapter);
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

        findViewsByIds();
        setHomeOffice();
        //Them dia chi
        btnAdd.setOnClickListener(view -> addAddress());
        //Tinh
        getListProvince();
        //
        cbMacDinh.setOnClickListener(view -> {
            checkMacDinh = !checkMacDinh;
            cbMacDinh.setChecked(checkMacDinh);
        });
        //Back
        btnBack.setOnClickListener(view ->{
            Intent intent = new Intent(this, AddressActivity.class);
            startActivity(intent);
        });
    }
}
