package com.example.doandd;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.SharedPreference;
import com.example.doandd.utils.ImageLoadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {
    TextView birthdayEdtView;
    private EditText addressEdtView, nameEdtView, emailEdtView, phoneEdtView;
    private ImageView imgUser;
    Spinner spGender;
    Button btnBack, btnEditProfile,btnEditImage;
    FirestoreDatabase fb =new FirestoreDatabase();
    int SELECT_PICTURE = 200;
    Uri selectedImageUri;

    private void findViewByIds(){
        nameEdtView = findViewById(R.id.name_editText);
        addressEdtView = findViewById(R.id.address_editText);
        birthdayEdtView = findViewById(R.id.birthday_editText);
        spGender = findViewById(R.id.spGender);
        emailEdtView = findViewById(R.id.email_editText);
        phoneEdtView = findViewById(R.id.phone_editText);
        imgUser = findViewById(R.id.imgEditProfile);
        btnBack = findViewById(R.id.btnBackEditProfile);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnEditImage = findViewById(R.id.btnEditImage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        findViewByIds();
        SharedPreference sharedpreference = new SharedPreference(this);

        // Read from the database
        fb.user.document(sharedpreference.getID()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                nameEdtView.setText(task.getResult().getString("name"));
                emailEdtView.setText(task.getResult().getString("email"));
                birthdayEdtView.setText(task.getResult().getString("birthday"));
                addressEdtView.setText(task.getResult().getString("address"));
                phoneEdtView.setText(task.getResult().getString("phone"));
                if(!Objects.requireNonNull(task.getResult().getString("image")).equals("")){
                    new ImageLoadTask(task.getResult().getString("image"), imgUser).execute();
                }
            }
            else {
                Log.w( "Error", task.getException());
            }
        });
        //
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(adapter);
        //
        btnEditImage.setOnClickListener(view -> {
            imageChooser();
        });
        //
        btnEditProfile.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thông báo");
            builder.setMessage("Cập nhật thông tin thành công!");
            builder.setNegativeButton("Đóng", (DialogInterface.OnClickListener) (dialog, which) -> {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Map<String, Object> data = new HashMap<>();
            data.put("name", nameEdtView.getText().toString());
            data.put("address", addressEdtView.getText().toString());
            data.put("gender", spGender.getSelectedItem().toString());
            data.put("phone", phoneEdtView.getText().toString());
            data.put("birthday", birthdayEdtView.getText().toString());
            fb.uploadImage(data, selectedImageUri, sharedpreference.getID(), "avatar");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.cancel();
                    startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                }
            }, 2000 );

        });
        //
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        });
        //
        birthdayEdtView.setOnClickListener(this::openDatePickerDialog);
        //
    }

    public void openDatePickerDialog(final View v) {

        Calendar cal = Calendar.getInstance();
        // Get Current Date
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    if (v.getId() == R.id.birthday_editText) {
                        ((TextView) v).setText(selectedDate);
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
        datePickerDialog.show();
    }

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    imgUser.setImageURI(selectedImageUri);
                }
            }
        }
    }
}
