package com.example.fitzone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Goal extends AppCompatActivity {

    NumberPicker textPicker;
    ImageView back;
    Button btn_goal;
    // Firestore instance
    private FirebaseFirestore db;

    @SuppressLint({"NewApi", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        btn_goal = findViewById(R.id.btn_goal);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        textPicker = findViewById(R.id.np_goal);

        Button goal = findViewById(R.id.btn_goal);

        final String[] items = {"Lose weight", "Get fitter", "Gain more flexible","Lose weight", "Get fitter", "Gain more flexible"};

        // Set the values for the text picker
        textPicker.setMinValue(0);
        textPicker.setMaxValue(items.length - 1);
        textPicker.setDisplayedValues(items);
        textPicker.setTextSize(60);

        goal.setOnClickListener(view -> {
            int selectedIndex = textPicker.getValue();
            String selectedText = items[selectedIndex];
            Toast.makeText(Goal.this, "Selected Goal: " + selectedText, Toast.LENGTH_SHORT).show();
        });

        btn_goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the UID from the Intent extras
                Intent intent = getIntent();
                String uid = intent.getStringExtra("uid");

                // Get the selected goal from the NumberPicker
                int selectedIndex = textPicker.getValue();
                String selectedText = items[selectedIndex];

                // Initialize Firestore instance
                db = FirebaseFirestore.getInstance();

                // Create a Map to update the document
                Map<String, Object> goalData = new HashMap<>();
                goalData.put("goal", selectedText);

                // Update the document in Firestore
                db.collection("users")
                        .document(uid)
                        .update(goalData)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(Goal.this, "Goal saved to Firestore!", Toast.LENGTH_SHORT).show();
                            // Redirect to the next activity
                            redirectActivity(Goal.this, ActivityLevel.class,uid);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(Goal.this, "Error saving goal to Firestore!", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore", "Error saving goal", e);
                        });

            }
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