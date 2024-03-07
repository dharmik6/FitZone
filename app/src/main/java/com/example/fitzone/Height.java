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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        height_num.setValue(150);
        height=150;
        height_num.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(height_num==null)
                {
                    height = 150 ;
                }
                else{
                    height = newVal;
                }
            }
        });
        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save selected height to Firestore
                saveHeightToFirestore(height);
            }
        });
    }
    private void saveHeightToFirestore(int height) {
        Intent intent = getIntent();
        String nameid = intent.getStringExtra("name");

        // Assuming you have a Firestore collection named "users"
        // Replace 'users' with your desired collection name

        // Save the height to Firestore for the user with userId
        db.collection("users")
                .document(nameid)
                .update("height", height)
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                    // Redirect to the next activity if needed
                    Toast.makeText(Height.this, "Age saved successfully", Toast.LENGTH_SHORT).show();
                    // Redirect to the next activity
                    Intent intent1 =new Intent(Height.this,Weight .class);
                    intent1.putExtra("name", nameid);
                    // Start the activity
                    startActivity(intent1);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Height.this, "Error saving Age", Toast.LENGTH_SHORT).show();
                });
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}