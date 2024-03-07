package com.example.fitzone.diet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fitzone.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Diet extends AppCompatActivity {
    TextView show_name,show_description;
    ImageView show_image;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        show_name=findViewById(R.id.show_name);
        show_description=findViewById(R.id.show_description);
        show_image=findViewById(R.id.show_image);

        Intent intent = getIntent();
        String dietid = intent.getStringExtra("name");

        progressDialog = new ProgressDialog(Diet.this);
        progressDialog.setMessage("Deleting...");
        progressDialog.setCancelable(false);

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("diets").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String description = documentSnapshot.getString("description");
                String image = documentSnapshot.getString("imageUrl");
//                 Check if the userNameFromIntent matches the user
                if (dietid.equals(name)) {
                    // Display the data only if they match
                    show_name.setText(name != null ? name : "No name");
                    show_description.setText(description != null ? description : "No username");
                    if (image != null) {
                        Glide.with(Diet.this)
                                .load(image)
                                .into(show_image);
                    }
                } else {
                    // userNameFromIntent and user don't match, handle accordingly
//                    showToast("User data does not match the intent.");
                }
            }
        });
    }
}