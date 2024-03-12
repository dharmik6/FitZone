package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.shawnlin.numberpicker.NumberPicker;

public class Height extends AppCompatActivity {
    ImageView back ;
    NumberPicker height_num;
    Integer height;
    Button next_page;
    // Firestore instance
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        back = findViewById(R.id.back);
        height_num = findViewById(R.id.height_num);
        next_page = findViewById((R.id.btn_height));
// Initialize Firestore
        db = FirebaseFirestore.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        height_num.setValue(150);
        height=90;
        height_num.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal < 90) {
                    height = 90;
                    picker.setValue(90); // Ensure the picker shows the minimum value
                } else if (newVal > 250) {
                    height = 250;
                    picker.setValue(250); // Ensure the picker shows the maximum value
                } else {
                    height = newVal;
                }
            }
        });
        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save selected height to Firestore
                saveHeightToFirestore(String.valueOf(height));
            }
        });
    }
    private void saveHeightToFirestore(String height) {
        // Get the UID from the Intent extras
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");

        // Assuming you have a Firestore collection named "users"
        // Replace 'users' with your desired collection name

        // Save the height to Firestore for the user with userId
        db.collection("users")
                .document(uid)
                .update("height", height)
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                    // Redirect to the next activity if needed
                    Toast.makeText(Height.this, "Age saved successfully", Toast.LENGTH_SHORT).show();
                    // Redirect to the next activity
                    redirectActivity(Height.this, Weight.class,uid);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Height.this, "Error saving Age", Toast.LENGTH_SHORT).show();
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