package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitzone.home.MainActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ActivityLevel extends AppCompatActivity {

    NumberPicker textPicker;
    Button btn_activitylevel;
    ImageView back;
    // Firestore instance
    private FirebaseFirestore db;
    @SuppressLint({"NewApi", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        textPicker = findViewById(R.id.np_level);
// Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get the UID from the Intent extras
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");


        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_activitylevel = findViewById(R.id.btn_activitylevel);


        final String[] items = {"Beginner", "Intermediate", "Advance","Beginner", "Intermediate", "Advance"};

        // Set the values for the text picker
        textPicker.setMinValue(0);
        textPicker.setMaxValue(items.length - 1);
        textPicker.setDisplayedValues(items);
        textPicker.setTextSize(60);

        btn_activitylevel.setOnClickListener(view -> {
            int selectedIndex = textPicker.getValue();
            String selectedText = items[selectedIndex];
            // Get the UID from the Intent extras
//            Intent intent = getIntent();
//            String uid = intent.getStringExtra("uid");

            // Initialize Firestore instance
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Create a Map to store the selected activity level
            Map<String, Object> data = new HashMap<>();
            data.put("activity", selectedText);

            // Update the document in Firestore
            db.collection("users")
                    .document(uid)
                    .update(data)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(ActivityLevel.this, "Activity level saved to Firestore!", Toast.LENGTH_SHORT).show();
                        // Redirect to the next activity
                        redirectActivity(ActivityLevel.this, PackagesList.class);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ActivityLevel.this, "Error saving activity level to Firestore!", Toast.LENGTH_SHORT).show();
                        Log.e("Firestore", "Error saving activity level", e);
                    });
        });
    }
    // In the Registration activity
    // Change the redirectActivity method to pass the UID instead of the name
    public static void redirectActivity(Activity activity, Class destination) {
        Intent intent = new Intent(activity, destination);
        activity.startActivity(intent);
    }
}