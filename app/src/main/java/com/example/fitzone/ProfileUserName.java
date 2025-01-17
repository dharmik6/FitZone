package com.example.fitzone;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ProfileUserName extends AppCompatActivity {
    CircleImageView user_image;
    AppCompatEditText user_username, address, phone;
    AppCompatButton btn_next;
    private FirebaseFirestore db;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user_name);
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading image...");
        progressDialog.setCancelable(false);
        user_image = findViewById(R.id.user_image);
        user_username = findViewById(R.id.user_username);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        findViewById(R.id.user_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use ImagePicker to select an image from camera or gallery
                ImagePicker.Companion.with(ProfileUserName.this)
                        .crop()         // Enable cropping
                        .cropOval()     // Crop shape to oval
                        .provider(ImageProvider.BOTH) // Or bothCameraGallery()
                        .createIntentFromDialog(new Function1<Intent, Unit>() {
                            @Override
                            public Unit invoke(Intent it) {
                                startActivityForResult(it, PICK_IMAGE_REQUEST);
                                return null;
                            }
                        });
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
        if (!validateFields()) {
            return;
        }
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        progressDialog.show();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("user_images").child(uid);
        storageRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("username", username);
                        userData.put("address", userAddress);
                        userData.put("number", userPhone);
                        userData.put("image", imageUrl);
                        db.collection("users")
                                .document(uid)
                                .update(userData)
                                .addOnSuccessListener(aVoid -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(ProfileUserName.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    redirectActivity(ProfileUserName.this, Gender.class, uid);
                                })
                                .addOnFailureListener(e -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(ProfileUserName.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                });
                    });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileUserName.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }

    private boolean validateFields() {
        String userName = user_username.getText().toString().trim();
        String addressText = address.getText().toString().trim();
        String phoneText = phone.getText().toString().trim();
        String usernamePattern = "^[a-z0-9_]+$"; // Pattern to allow lowercase letters, numbers, and underscores, but no spaces

        if (userName.isEmpty()) {
            user_username.setError("Username is required");
            user_username.requestFocus();
            return false;
        }
        if (!userName.matches(usernamePattern)) {
            user_username.setError("Username must contain only lowercase letters (a-z), numbers, and underscores (_), and no spaces");
            user_username.requestFocus();
            return false;
        }
        if (addressText.isEmpty()) {
            address.setError("Address is required");
            address.requestFocus();
            return false;
        }
        if (phoneText.isEmpty()) {
            phone.setError("Phone number is required");
            phone.requestFocus();
            return false;
        }
        if (phoneText.length() != 10) {
            phone.setError("Enter a valid 10-digit phone number");
            phone.requestFocus();
            return false;
        }
        return true;
    }


    public static void redirectActivity(Activity activity, Class destination, String uid) {
        Intent intent = new Intent(activity, destination);
        intent.putExtra("uid", uid);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}
