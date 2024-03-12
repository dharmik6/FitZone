package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.fitzone.ProfileUserName;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    TextInputEditText member_name, member_email, member_pass;
    CheckBox member_check;
    AppCompatButton btn_registration;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);

        // Find views
        member_name = findViewById(R.id.member_name);
        member_email = findViewById(R.id.member_email);
        member_pass = findViewById(R.id.member_pass);
        member_check = findViewById(R.id.member_check);
        btn_registration = findViewById(R.id.btn_registration);

        btn_registration.setOnClickListener(view -> {
            progressDialog.show();
            registerUser();
        });
    }

    private void registerUser() {
        String name = member_name.getText().toString().trim();
        String email = member_email.getText().toString().trim();
        String password = member_pass.getText().toString().trim();
        boolean isChecked = member_check.isChecked();

        if (!isChecked) {
            progressDialog.dismiss();
            Toast.makeText(this, "Please agree to the terms to register.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            progressDialog.dismiss();
            member_name.setError("Please enter your name");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            progressDialog.dismiss();
            member_email.setError("Please enter your email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            progressDialog.dismiss();
            member_pass.setError("Please enter your password");
            return;
        }

        // Register the user with email and password using Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String userId = user.getUid();

                        // Create a user object
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("name", name);
                        userData.put("email", email);
                        userData.put("password", password);

                        // Add the user to Firestore with the generated UID
                        db.collection("users")
                                .document(userId)
                                .set(userData)
                                .addOnSuccessListener(aVoid -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(Registration.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    // Redirect to the profile activity
                                    redirectActivity(Registration.this, ProfileUserName.class, userId);
                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(Registration.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                    // Handle failure
                                });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(Registration.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static void redirectActivity(Activity activity, Class destination, String name) {
        Intent intent = new Intent(activity, destination);
        intent.putExtra("uid", name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}