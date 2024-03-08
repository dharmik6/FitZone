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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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


    // In the ProfileUserName activity
    private void saveDataToFirestore() {
        String username = user_username.getText().toString().trim();
        String userAddress = address.getText().toString().trim();
        String userPhone = phone.getText().toString().trim();

        // Get the UID from the Intent extras
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");

        // Check if all fields are filled
        if (!username.isEmpty() && !userAddress.isEmpty() && !userPhone.isEmpty() && selectedImageUri != null) {
            // Create a reference to the Firebase Storage location where you want to store the image
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("user_images").child(uid);

            // Upload the image to Firebase Storage
            storageRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL of the uploaded image
                        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();

                            // Create a map to store user data
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("username", username);
                            userData.put("address", userAddress);
                            userData.put("number", userPhone);
                            userData.put("image", imageUrl); // Save the image URL instead of URI

                            // Add the user data to Firestore using the same UID
                            db.collection("users")
                                    .document(uid)
                                    .update(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(ProfileUserName.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        // Redirect to the next activity
                                        redirectActivity(ProfileUserName.this, Gender.class, uid);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Failed to save data
                                        Toast.makeText(ProfileUserName.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                    });
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle failures
                        Toast.makeText(ProfileUserName.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        }
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
