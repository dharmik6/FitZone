package com.example.fitzone.trainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fitzone.AchievementsList;
import com.example.fitzone.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ApprovedTrainerProfile extends AppCompatActivity {

    ImageView trainer_img_txt;
    Button book_btn_registration;
    ProgressDialog progressDialog;
    String treid1;
    CardView achieve;
    RatingBar xml3_rating_bar;
    TextView trainer_review_txt3, Functional_Strength_txt, trainer_name_txt, trBio, chargeTx, review_show_allkaku, trainer_eee_txt, kalu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_trainer_profile);

        trainer_img_txt = findViewById(R.id.trainer_img_txt);
        book_btn_registration = findViewById(R.id.book_btn_registration);
        Functional_Strength_txt = findViewById(R.id.Functional_Strength_txt);
        trainer_name_txt = findViewById(R.id.trainer_name_txt);
        chargeTx = findViewById(R.id.charge);
        trBio = findViewById(R.id.tr_bio);
        review_show_allkaku = findViewById(R.id.review_show_allkaku);
        trainer_eee_txt = findViewById(R.id.trainer_eee_txt);
        kalu = findViewById(R.id.kalu);
        achieve = findViewById(R.id.achieve);
        trainer_review_txt3 = findViewById(R.id.trainer_review_txt3);
        xml3_rating_bar = findViewById(R.id.xml3_rating_bar);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show(); // Show ProgressDialog

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String specialization = documentSnapshot.getString("specialization");
                        String image = documentSnapshot.getString("image");
                        String experience = documentSnapshot.getString("experience");
                        String charge = documentSnapshot.getString("charge");
                        String bio = documentSnapshot.getString("bio");
                        treid1 = documentSnapshot.getId();

                        // Display the data
                        trainer_name_txt.setText(name != null ? name : "No name");
                        trainer_eee_txt.setText(experience != null ? experience : "No experience");
                        Functional_Strength_txt.setText(specialization != null ? specialization : "Non specialist");
                        trBio.setText(bio != null ? bio : "No bio");
                        chargeTx.setText(charge != null ? charge : "No price");

                        if (image != null) {
                            Glide.with(ApprovedTrainerProfile.this)
                                    .load(image)
                                    .into(trainer_img_txt);
                        }

                        // Retrieve and display reviews
                        retrieveAndDisplayReviews(treid1);
                    } else {
                        // Handle document not found
                        progressDialog.dismiss();
                        Toast.makeText(ApprovedTrainerProfile.this, "Trainer not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    progressDialog.dismiss();
                    Toast.makeText(ApprovedTrainerProfile.this, "Error loading trainer profile", Toast.LENGTH_SHORT).show();
                });

        review_show_allkaku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ApprovedTrainerProfile.this, Review.class);
                intent1.putExtra("treid", treid1);
                startActivity(intent1);
            }
        });

        achieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ApprovedTrainerProfile.this, AchievementsList.class);
                intent2.putExtra("treid", treid1);
                startActivity(intent2);
            }
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        book_btn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ApprovedTrainerProfile.this, Appointment.class);
                // Use different keys for each putExtra statement
                intent1.putExtra("trainer_name", trainer_name_txt.getText().toString());
                intent1.putExtra("trainer_review", trainer_review_txt3.getText().toString());
                intent1.putExtra("Functional_Strength", Functional_Strength_txt.getText().toString());
                intent1.putExtra("trainer_eee_txt", trainer_eee_txt.getText().toString());
                intent1.putExtra("charge", chargeTx.getText().toString());
                intent1.putExtra("trid", treid1);
                startActivity(intent1);
            }
        });
    }

    private void retrieveAndDisplayReviews(String trainerId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").document(trainerId).collection("trainers_review").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    float totalRating = 0;
                    int reviewCount = 0;

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String rating = documentSnapshot.getString("rating");

                        if (rating != null) {
                            totalRating += Float.parseFloat(rating);
                            reviewCount++;
                        } else {
                            // Handle null ratings by incrementing the review count
                            reviewCount++;
                        }
                    }

                    if (reviewCount > 0) {
                        float averageRating = totalRating / reviewCount;
                        xml3_rating_bar.setRating(averageRating);
                        trainer_review_txt3.setText(String.valueOf(averageRating));
                    } else {
                        // If there are no reviews, set default rating to 0
                        xml3_rating_bar.setRating(0);
                        trainer_review_txt3.setText("0");
                    }

                    // Dismiss ProgressDialog when data is loaded
                    progressDialog.dismiss();
                })
                .addOnFailureListener(e -> {
                    // Handle failures
                    progressDialog.dismiss();
                    Toast.makeText(ApprovedTrainerProfile.this, "Error loading reviews", Toast.LENGTH_SHORT).show();
                });
    }
}
