package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Gender extends AppCompatActivity {
    ImageView male,female;
    boolean isSelected,isready;
    ImageView backButton;
    Button next_page ;
    String gender;
    // Firestore instance
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        male = findViewById(R.id.btn_male);
        female = findViewById(R.id.btn_female);
        backButton = findViewById(R.id.back);
        next_page = findViewById(R.id.btn_gender);



        isready = false ;

        //*******************************************************
        //back page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelected) {
                    male.setImageResource(R.drawable.ic_male_checked);
                    female.setImageResource(R.drawable.ic_female);
                    isSelected = true;
                    isready = true ;
                    gender = "male" ;
                } else {
                    male.setImageResource(R.drawable.ic_male);
                    isSelected = false;
                    isready = false ;
                    gender = null ;
                }
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected) {
                    male.setImageResource(R.drawable.ic_male);
                    female.setImageResource(R.drawable.ic_female_checkd);
                    isSelected = false;
                    isready = true ;
                    gender = "female" ;
                } else {
                    female.setImageResource(R.drawable.ic_female);
                    isSelected = true;
                    isready = false ;
                    gender = null ;
                }
            }
        });
        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gender == null) {
                    Toast.makeText(Gender.this, "plese selece gender", Toast.LENGTH_SHORT).show();
                } else {
                    // Save selected gender to Firestore
                    saveGenderToFirestore(gender);
                }
            }
        });

    }
    private void saveGenderToFirestore(String gender) {
        // Get the UID from the Intent extras
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");

        // Initialize Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a Map to store the selected gender
        Map<String, Object> data = new HashMap<>();
        data.put("gender", gender);

        // Update the document in Firestore
        db.collection("users")
                .document(uid)
                .update(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(Gender.this, "Gender saved !", Toast.LENGTH_SHORT).show();
                    // Redirect to the next activity
                    redirectActivity(Gender.this, Age.class,uid);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Gender.this, "Error saving gender to Firestore!", Toast.LENGTH_SHORT).show();
//                    Log.e("Firestore", "Error saving gender", e);
                });
    }
    // In the Registration activity
    // Change the redirectActivity method to pass the UID instead of the name
    public static void redirectActivity(Activity activity, Class destination, String uid) {
        Intent intent = new Intent(activity, destination);
        intent.putExtra("uid", uid);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}