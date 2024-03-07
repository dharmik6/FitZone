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
import com.shawnlin.numberpicker.NumberPicker;

import java.util.HashMap;
import java.util.Map;

public class Age extends AppCompatActivity {

    ImageView back;
    NumberPicker age_num ;
    Button next_page ;
    int age ;
    // Firestore instance
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age);
        next_page = findViewById(R.id.btn_age);
        back = findViewById(R.id.back);
        age_num = findViewById(R.id.age_num);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        age_num.setValue(18);
        age= 18 ;
        age_num.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(age_num==null)
                {
                    age = 150 ;
                }
                else{
                    age = newVal;
                }
            }
        });

        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save selected age to Firestore
                saveAgeToFirestore(age);
            }
        });

    }
    private void saveAgeToFirestore(int age) {
        Intent intent = getIntent();
        String nameid = intent.getStringExtra("name");

        // Assuming you have a Firestore collection named "users"
        // Replace 'users' with your desired collection name
        Map<String, Object> data = new HashMap<>();
        data.put("age", age);

        // Save the age to Firestore for the user with userId
        db.collection("users")
                .document(nameid)
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    // Handle successful save
                    // Redirect to the next activity
                    //redirectActivity(Age.this, NextActivity.class);
                    Toast.makeText(Age.this, "Age saved successfully", Toast.LENGTH_SHORT).show();
                    // Redirect to the next activity
                    Intent intent1 =new Intent(Age.this,Height .class);
                    intent1.putExtra("name", nameid);
                    // Start the activity
                    startActivity(intent1);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Age.this, "Error saving Age", Toast.LENGTH_SHORT).show();
                });
    }
    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}