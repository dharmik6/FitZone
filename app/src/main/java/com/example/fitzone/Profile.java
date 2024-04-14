package com.example.fitzone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.data.Entry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    CircleImageView show_image;
    AppCompatTextView show_name, show_username, show_email, show_number, show_address, show_gender, show_age, show_height, show_weight, show_goal, show_level;
    ImageView back;
    private ProgressDialog progressDialog;
    String memberimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        show_image = findViewById(R.id.show_image);
        show_name = findViewById(R.id.show_name);
        show_username = findViewById(R.id.show_username);
        show_email = findViewById(R.id.show_email);
        show_number = findViewById(R.id.show_number);
        show_address = findViewById(R.id.show_address);
        show_gender = findViewById(R.id.show_gender);
        show_age = findViewById(R.id.show_age);
        show_height = findViewById(R.id.show_height);
        show_weight = findViewById(R.id.show_weight);
        show_goal = findViewById(R.id.show_goal);
        show_level = findViewById(R.id.show_level);
        back = findViewById(R.id.back);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            progressDialog.show(); // Show progress dialog before fetching user data
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String membername = documentSnapshot.getString("name");
                    String memberusername = documentSnapshot.getString("username");
                    String memberactivity = documentSnapshot.getString("activity");
                    String memberaddress = documentSnapshot.getString("address");
                    String memberage = documentSnapshot.getString("age");
                    String membergoal = documentSnapshot.getString("goal");
                    String memberheight = documentSnapshot.getString("height");
                    String membergender = documentSnapshot.getString("gender");
                    String memberemail = documentSnapshot.getString("email");
                    String membernumber = documentSnapshot.getString("number");
                    memberimage = documentSnapshot.getString("image");

                    show_name.setText(membername != null ? membername : "No name");
                    show_username.setText(memberusername != null ? memberusername : "No username");
                    show_level.setText(memberactivity != null ? memberactivity : "No activity");
                    show_address.setText(memberaddress != null ? memberaddress : "No address");
                    show_age.setText(memberage != null ? memberage : "No age");
                    show_gender.setText(membergender != null ? membergender : "No gender");
                    show_email.setText(memberemail != null ? memberemail : "No email");
                    show_number.setText(membernumber != null ? membernumber : "No number");
                    show_goal.setText(membergoal != null ? membergoal : "No goal");
                    show_height.setText(memberheight != null ? memberheight : "No height");
                    if (memberimage != null) {
                        Glide.with(Profile.this)
                                .load(memberimage)
                                .into(show_image);
                    }

                    // Fetch weight data
                    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                    db1.collection("users").document(userId).collection("weight").get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                        List<Entry> entries = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots1) {
                            String memberweight = documentSnapshot1.getString("weight");
                            show_weight.setText(memberweight != null ? memberweight : "No weight");
                        }

                        progressDialog.dismiss(); // Dismiss progress dialog after fetching all data
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss(); // Dismiss progress dialog if there is an error
                        Toast.makeText(Profile.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    progressDialog.dismiss(); // Dismiss progress dialog if user data does not exist
                    Toast.makeText(Profile.this, "User data does not exist", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss(); // Dismiss progress dialog if there is an error
                Toast.makeText(Profile.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            progressDialog.dismiss(); // Dismiss progress dialog if user is not logged in
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }

        CardView editProfile = findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, UpdateProfile.class);
                intent.putExtra("name", show_name.getText().toString());
                intent.putExtra("username", show_username.getText().toString());
                intent.putExtra("email", show_email.getText().toString());
                intent.putExtra("number", show_number.getText().toString());
                intent.putExtra("address", show_address.getText().toString());
                intent.putExtra("gender", show_gender.getText().toString());
                intent.putExtra("age", show_age.getText().toString());
                intent.putExtra("height", show_height.getText().toString());
                intent.putExtra("weight", show_weight.getText().toString());
                intent.putExtra("goal", show_goal.getText().toString());
                intent.putExtra("level", show_level.getText().toString());
                if (memberimage != null) {
                    intent.putExtra("imageUri", memberimage);
                }

                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        progressDialog.show(); // Show progress dialog before fetching user data
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String membername = documentSnapshot.getString("name");
                    String memberusername = documentSnapshot.getString("username");
                    String memberactivity = documentSnapshot.getString("activity");
                    String memberaddress = documentSnapshot.getString("address");
                    String memberage = documentSnapshot.getString("age");
                    String membergoal = documentSnapshot.getString("goal");
                    String memberheight = documentSnapshot.getString("height");
                    String membergender = documentSnapshot.getString("gender");
                    String memberemail = documentSnapshot.getString("email");
                    String membernumber = documentSnapshot.getString("number");
                    memberimage = documentSnapshot.getString("image");

                    show_name.setText(membername != null ? membername : "No name");
                    show_username.setText(memberusername != null ? memberusername : "No username");
                    show_level.setText(memberactivity != null ? memberactivity : "No activity");
                    show_address.setText(memberaddress != null ? memberaddress : "No address");
                    show_age.setText(memberage != null ? memberage : "No age");
                    show_gender.setText(membergender != null ? membergender : "No gender");
                    show_email.setText(memberemail != null ? memberemail : "No email");
                    show_number.setText(membernumber != null ? membernumber : "No number");
                    show_goal.setText(membergoal != null ? membergoal : "No goal");
                    show_height.setText(memberheight != null ? memberheight : "No height");
                    if (memberimage != null) {
                        Glide.with(Profile.this)
                                .load(memberimage)
                                .into(show_image);
                    }

                    // Fetch weight data
                    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                    db1.collection("users").document(userId).collection("weight").get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                        List<Entry> entries = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots1) {
                            String memberweight = documentSnapshot1.getString("weight");
                            show_weight.setText(memberweight != null ? memberweight : "No weight");
                        }

                        progressDialog.dismiss(); // Dismiss progress dialog after fetching all data
                    }).addOnFailureListener(e -> {
                        progressDialog.dismiss(); // Dismiss progress dialog if there is an error
                        Toast.makeText(Profile.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    progressDialog.dismiss(); // Dismiss progress dialog if user data does not exist
                    Toast.makeText(Profile.this, "User data does not exist", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss(); // Dismiss progress dialog if there is an error
                Toast.makeText(Profile.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            progressDialog.dismiss(); // Dismiss progress dialog if user is not logged in
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

}