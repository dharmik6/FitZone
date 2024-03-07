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
        Intent intent = getIntent();
        String nameid = intent.getStringExtra("name");

        // Assuming you have a Firestore collection named "users"
        // Replace 'users' with your desired collection name
        Map<String, Object> data = new HashMap<>();
        data.put("gender", gender);

        // Save the gender to Firestore for the user with userId
        db.collection("users")
                .document(nameid)
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Gender.this, "Gender saved successfully", Toast.LENGTH_SHORT).show();
                    // Redirect to the next activity
                    Intent intent1 =new Intent(Gender.this,Age .class);
                    intent1.putExtra("name", nameid);
                    // Start the activity
                    startActivity(intent1);
                })
                .addOnFailureListener(e -> Toast.makeText(Gender.this, "Error saving gender", Toast.LENGTH_SHORT).show());
    }
    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}