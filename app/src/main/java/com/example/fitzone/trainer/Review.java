package com.example.fitzone.trainer;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.fitzone.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Review extends AppCompatActivity {
    Button Write_a_Review;
    TextView revireshoew, review_show_re, all_review; // Added all_review
    RecyclerView recyc_review;
    private TrainerReviewAdapter adapter;
    private List<TrainerReviewList> trainersLists;
    ProgressDialog progressDialog;
    RatingBar xml2_rating_bar;
    private static final int WRITE_REVIEW_REQUEST_CODE = 1001;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Write_a_Review = findViewById(R.id.Write_a_Review);
        recyc_review = findViewById(R.id.recyc_review);
        revireshoew = findViewById(R.id.revireshoew);
        xml2_rating_bar = findViewById(R.id.xml2_rating_bar);
        review_show_re = findViewById(R.id.review_show_re);

        Intent intent1 = getIntent();
        String treid = intent1.getStringExtra("treid");

        Log.d(TAG, "onCreate: trainer id "+treid);
        revireshoew.setText(treid);

        recyc_review.setHasFixedSize(true);
        recyc_review.setLayoutManager(new LinearLayoutManager(this));

        trainersLists = new ArrayList<>();
        adapter = new TrainerReviewAdapter(this, trainersLists);
        recyc_review.setAdapter(adapter);

        all_review = findViewById(R.id.all_rating);

        // Show ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").document(treid).collection("trainers_review").get().addOnSuccessListener(queryDocumentSnapshots -> {
            float totalRating = 0;
            int reviewCount = 0;

            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String rating = documentSnapshot.getString("rating");

                // Handle cases where rating is null
                float currentRating = 0;
                if (rating != null) {
                    currentRating = Float.parseFloat(rating);
                    totalRating += currentRating;
                    reviewCount++;
                }

                String review = documentSnapshot.getString("review");
                String name = documentSnapshot.getString("review_name");
                String image = documentSnapshot.getString("review_image");
                TrainerReviewList member = new TrainerReviewList(String.valueOf(currentRating), review, name, image); // Use currentRating here
                trainersLists.add(member);
            }

            // If there are reviews with ratings
            if (reviewCount > 0) {
                float averageRating = totalRating / reviewCount;
                xml2_rating_bar.setRating(averageRating);
                review_show_re.setText(String.valueOf(averageRating));
            } else {
                // If there are no reviews with ratings, set default rating to 0
                xml2_rating_bar.setRating(0);
                review_show_re.setText("0");
            }

            adapter.notifyDataSetChanged();
            // Dismiss ProgressDialog when data is loaded
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            // Set text for all_review TextView
            all_review.setText("All Reviews (" + reviewCount + ")");
        }).addOnFailureListener(e -> {
            // Handle failures
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        });


        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Write_a_Review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Review.this, WriteReview.class);
                intent1.putExtra("treid", treid);
                startActivityForResult(intent1, WRITE_REVIEW_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE_REVIEW_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Write review successful, reload data
                reloadData();
            }
        }
    }

    private void reloadData() {
        // Show ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Intent intent = getIntent();
        String treid = intent.getStringExtra("treid");

        trainersLists.clear();

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").document(treid).collection("trainers_review").get().addOnSuccessListener(queryDocumentSnapshots -> {
            float totalRating = 0;
            int reviewCount = 0;

            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String rating = documentSnapshot.getString("rating");

                if (rating != null) {
                    totalRating += Float.parseFloat(rating);
                    reviewCount++;
                }

                String review = documentSnapshot.getString("review");
                String name = documentSnapshot.getString("review_name");
                String image = documentSnapshot.getString("review_image");
                TrainerReviewList member = new TrainerReviewList(rating, review, name,image);
                trainersLists.add(member);
            }

            if (reviewCount > 0) {
                float averageRating = totalRating / reviewCount;
                xml2_rating_bar.setRating(averageRating);
                review_show_re.setText(String.valueOf(averageRating));

            }

            adapter.notifyDataSetChanged();
            // Dismiss ProgressDialog when data is loaded
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            // Handle failures
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        });
    }
}
