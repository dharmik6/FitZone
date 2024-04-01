package com.example.fitzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {

    EditText feedback_edit;
    Button feedback_send;
    ProgressDialog progressDialog;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedback_send = findViewById(R.id.feedback_send);
        feedback_edit = findViewById(R.id.feedback_edit);

        feedback_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve current user information from Firebase Authentication
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                // Check if currentUser is null
                if (currentUser != null) {
                    progressDialog = new ProgressDialog(Feedback.this); // Move progressDialog instantiation here
                    // Showing progress dialog while uploading
                    progressDialog.setTitle("Uploading");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userId = currentUser.getUid();
                    db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String membername = documentSnapshot.getString("name");
                            // Prepare data to update
                            String feed_edit = feedback_edit.getText().toString();

                            Map<String, Object> userData = new HashMap<>();
                            userData.put("feedback", feed_edit);
                            userData.put("feedback_name", membername);

                            // Update Firestore document with the new data
                            DocumentReference userRef = db.collection("feedback").document();
                            userRef.set(userData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Document successfully updated
                                            progressDialog.dismiss();
                                            Toast.makeText(Feedback.this, "Thank You for feedback", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle errors
                                            progressDialog.dismiss();
                                            Toast.makeText(Feedback.this, "Failed to send feedback: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                } else {
                    Toast.makeText(Feedback.this, "Current user is null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}