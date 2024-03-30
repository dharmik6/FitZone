package com.example.fitzone.trainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.fitzone.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WriteReview extends AppCompatActivity {
    RatingBar xml_rating_bar;
    EditText review_text;
    Button review_button;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        // Initialize views
        xml_rating_bar = findViewById(R.id.xml_rating_bar);
        review_text = findViewById(R.id.review_text);
        review_button = findViewById(R.id.review_button);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding review...");
        progressDialog.setCancelable(false);

        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        review_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();
                    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                    db1.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String image = documentSnapshot.getString("image");

                            // Convert float rating to a string

                // Assuming xml_rating_bar.getRating() returns a float value
                float ratingString = xml_rating_bar.getRating();

                String rating = String.valueOf(ratingString);
                String review = review_text.getText().toString();
                progressDialog.show(); // Show progressDialog
                addCertificateToFirestore(review, rating,name,image);
                // Get the review details
                        }
                    });
                }
            }
        });
    }
    private void addCertificateToFirestore(String review, String rating,String name,String image) {

                    // Retrieve treid from intent
                    Intent intent = getIntent();
                    String treid = intent.getStringExtra("treid");

                    // Save the review to Firestore
                    Map<String, Object> reviewData = new HashMap<>();
                    reviewData.put("rating", rating);
                    reviewData.put("review", review);
                    reviewData.put("review_image", image);
                    reviewData.put("review_name", name);

                    db.collection("trainers")
                            .document(treid)
                            .collection("trainers_review")
                            .document()
                            .set(reviewData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss(); // Dismiss progressDialog
                                    Toast.makeText(WriteReview.this, "Review added successfully", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss(); // Dismiss progressDialog
                                    Toast.makeText(WriteReview.this, "Failed to add review: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

    }

}