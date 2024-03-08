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
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

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
                saveAgeToFirestore(String.valueOf(age));
            }
        });

    }
    private void saveAgeToFirestore(String age) {
        // Get the UID from the Intent extras
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");

        // Assuming you have a Firestore collection named "users"
        // Replace 'users' with your desired collection name
        Map<String, Object> data = new HashMap<>();
        data.put("age", age);

        // Add the age data to Firestore
        db.collection("users") // This line is causing the NullPointerException
                .document(uid)
                .update(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(Age.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    // Redirect to the next activity
                    redirectActivity(Age.this, Height.class,uid);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Age.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                    // Handle failure
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