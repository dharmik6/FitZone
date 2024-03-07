package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shawnlin.numberpicker.NumberPicker;

public class Weight extends AppCompatActivity {
    ImageView backButton;
    NumberPicker weight_num ;
    Button next_page ;
    Integer weight ;
    // Firestore instance
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        weight_num = findViewById(R.id.weight);
        next_page = findViewById(R.id.btn_weight);
        backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });
        weight=60 ;

        weight_num.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(weight_num==null)
                {
                    weight = 150 ;
                }
                else{
                    weight = newVal;
                }
            }
        });
        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save selected weight to Firestore
                saveWeightToFirestore(weight);
            }
        });
    }
    private void saveWeightToFirestore(int weight) {
        Intent intent = getIntent();
        String nameid = intent.getStringExtra("name");

        // Assuming you have a Firestore collection named "users"
        // Replace 'users' with your desired collection name
        // Save the weight to Firestore for the user with userId
        db.collection("users")
                .document(nameid)
                .update("weight", weight)
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                    // Redirect to the next activity if needed
                    // Handle success
                    // Redirect to the next activity if needed
                    Toast.makeText(Weight.this, "Age saved successfully", Toast.LENGTH_SHORT).show();
                    // Redirect to the next activity
                    Intent intent1 =new Intent(Weight.this,Goal .class);
                    intent1.putExtra("name", nameid);
                    // Start the activity
                    startActivity(intent1);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Weight.this, "Error saving Age", Toast.LENGTH_SHORT).show();
                });
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}