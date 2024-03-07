package com.example.fitzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileUserName extends AppCompatActivity {
    CircleImageView user_image;
    RelativeLayout user_camera;
    AppCompatEditText user_username, address, phone;
    AppCompatButton btn_next;
    private FirebaseFirestore db;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user_name);

        db = FirebaseFirestore.getInstance();

        user_camera = findViewById(R.id.user_camera);
        user_image = findViewById(R.id.user_image);
        user_username = findViewById(R.id.user_username);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);

        user_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iuser = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iuser, PICK_IMAGE_REQUEST);
            }
        });

        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataToFirestore();
                redirectActivity(ProfileUserName.this, Gender.class);
            }
        });
    }

    // Method to handle image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            user_image.setImageURI(selectedImageUri);
        }
    }

    private void saveDataToFirestore() {
        String username = user_username.getText().toString().trim();
        String userAddress = address.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();

        Intent intent = getIntent();
        String nameid = intent.getStringExtra("name");

        // Check if all fields are filled
        if (!username.isEmpty() && !userAddress.isEmpty() && !userPhone.isEmpty() && selectedImageUri != null) {
            // Assuming you have a Firestore collection named "users"
            // Replace 'users' with your desired collection name
            // Assuming 'email' is the unique identifier for each user
            // Replace 'email' with your desired unique identifier
            // Create a map to store user data
            Map<String, Object> userData = new HashMap<>();
            userData.put("username", username);
            userData.put("address", userAddress);
            userData.put("number", userPhone);
            userData.put("image", selectedImageUri.toString());

            // Add the user data to Firestore
            db.collection("users")
                    .document(nameid)
                    .set(userData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ProfileUserName.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            Intent intent1 =new Intent(ProfileUserName.this,Gender .class);
                            intent1.putExtra("name", nameid);
                            // Start the activity
                            startActivity(intent1);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to save data
                        }
                    });
        }
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}
