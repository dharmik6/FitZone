package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.fitzone.home.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    AppCompatTextView read_more;
    TextInputEditText member_name,member_email,member_pass;
    CheckBox member_check;
    AppCompatButton btn_registration;
    // Firestore instance
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        db = FirebaseFirestore.getInstance();

        member_name = findViewById(R.id.member_name);
        member_email = findViewById(R.id.member_email);
        member_pass = findViewById(R.id.member_pass);
        member_check = findViewById(R.id.member_check);
        btn_registration = findViewById(R.id.btn_registration);

        read_more = findViewById(R.id.read_more);
        read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, PrivacyPolicy.class));
            }
        });
        btn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String name = member_name.getText().toString().trim();
        String email = member_email.getText().toString().trim();
        String password = member_pass.getText().toString().trim();
        boolean isChecked = member_check.isChecked();

        if (!isChecked) {
            Toast.makeText(this, "Please agree to the terms to register.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            member_name.setError("Please enter your name");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            member_email.setError("Please enter your email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            member_pass.setError("Please enter your password");
            return;
        }

        // Assuming you have a Firestore collection named "users"
        // Replace 'users' with your desired collection name
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("password", password);

        db.collection("users")
                .document(name) // Assuming email is unique and used as document id
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(Registration.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(Registration.this, ProfileUserName.class);
                    intent.putExtra("name", name);
                    // Start the activity
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(Registration.this, "Registration failed", Toast.LENGTH_SHORT).show());
    }


    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}