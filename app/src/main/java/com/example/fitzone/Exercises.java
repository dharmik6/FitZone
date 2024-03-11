package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Exercises extends AppCompatActivity {

    ImageView exe_image;
    TextView exercised_name;
    EditText exercised_body,exercised_equipment,exercised_description;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        exe_image = findViewById(R.id.exe_image);
        exercised_name = findViewById(R.id.exercised_name);
        exercised_body = findViewById(R.id.exercised_body);
        exercised_equipment = findViewById(R.id.exercised_equipment);
        exercised_description = findViewById(R.id.exercised_description);


        Intent intent = getIntent();
        String exeid = intent.getStringExtra("name");

        progressDialog = new ProgressDialog(Exercises.this);
        progressDialog.setMessage("Deleting...");
        progressDialog.setCancelable(false);

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("exercises").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String body = documentSnapshot.getString("body");
                String equipment = documentSnapshot.getString("equipment");
                String description = documentSnapshot.getString("description");
                String image = documentSnapshot.getString("imageUrl");
//                 Check if the userNameFromIntent matches the user
                if (exeid.equals(name)) {
                    // Display the data only if they match
                    exercised_name.setText(name != null ? name : "No name");
                    exercised_equipment.setText(equipment != null ? equipment : "No equipment");
                    exercised_description.setText(description != null ? description : "No description");
                    exercised_body.setText(body != null ? body : "No body");
                    if (image != null) {
                        Glide.with(Exercises.this)
                                .load(image)
                                .into(exe_image);
                    }
                } else {
                    // userNameFromIntent and user don't match, handle accordingly
//                    showToast("User data does not match the intent.");
                }
            }
        });
        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}