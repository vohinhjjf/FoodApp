package com.example.doandd;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doandd.database.FirestoreDatabase;
import com.example.doandd.database.SharedPreference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {
    EditText edtInfo;
    Spinner spProblem;
    Button btnSend, btnCamera,btnBackFeedback;
    ImageView IVPreviewImage;
    FirestoreDatabase fb =new FirestoreDatabase();
    int SELECT_PICTURE = 200;
    Uri selectedImageUri;

    private void findViewByIds(){
        edtInfo = findViewById(R.id.edtInfo);
        spProblem = findViewById(R.id.spProblem);
        btnSend = findViewById(R.id.btnSend);
        btnCamera = findViewById(R.id.btnCamera);
        btnBackFeedback = findViewById(R.id.btnBackFeedback);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        findViewByIds();
        SharedPreference sharedpreference = new SharedPreference(this);
        //
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProblem.setAdapter(adapter);
        //
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        //
        btnBackFeedback.setOnClickListener(view -> {
            startActivity(new Intent(this, AccountActivity.class));
        });
        //
        btnSend.setOnClickListener(view -> {
            DateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
            String date = df.format(Calendar.getInstance().getTime());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thông báo");
            builder.setMessage("Gửi yêu cầu thành công!");
            builder.setNegativeButton("Đóng", (DialogInterface.OnClickListener) (dialog, which) -> {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            Map<String, Object> data = new HashMap<>();
            data.put("problemType", spProblem.getSelectedItem());
            data.put("description", edtInfo.getText().toString());
            data.put("timeStap", date);
            fb.uploadImage(data, selectedImageUri, sharedpreference.getID(),"feedback");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.cancel();
                    //startActivity(new Intent(FeedbackActivity.this, ProfileActivity.class));
                }
            }, 2000 );
        });
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
                    IVPreviewImage.setImageURI(selectedImageUri);
                }
            }
        }
    }
}
