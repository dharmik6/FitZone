package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Achievements extends AppCompatActivity {
    ImageView certificates_image;
    TextView certificates_name, certificates_dec;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        Intent intent1 = getIntent();
        String name1 = intent1.getStringExtra("name");
        String tid = intent1.getStringExtra("id");
        String image = intent1.getStringExtra("image");
        String description = intent1.getStringExtra("description");

        certificates_image = findViewById(R.id.certificates_image);
        certificates_dec = findViewById(R.id.certificates_dec);
        certificates_name = findViewById(R.id.certificates_name);

        certificates_name.setText(name1);
        certificates_dec.setText(description);
        // Load image using Glide
        Glide.with(this)
                .load(image)
                .into(certificates_image);

        progressDialog = new ProgressDialog(Achievements.this);
        progressDialog.setMessage("Deleting...");
        progressDialog.setCancelable(false);


        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}