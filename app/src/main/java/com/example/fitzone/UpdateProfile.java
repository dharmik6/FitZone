package com.example.fitzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends AppCompatActivity {
    Button member_add_button;
    Spinner member_add_weight,member_add_height,member_add_goal,member_add_level,member_add_age,member_add_gender;
    EditText member_add_address,member_add_number,member_add_name;
    LinearLayout member_add_camera;
    CircleImageView member_add_image;
    ProgressDialog progressDialog; // Progress dialog for showing upload progress
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private Uri selectedImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        member_add_button = findViewById(R.id.member_add_button);

        member_add_weight = findViewById(R.id.member_add_weight);
        member_add_height = findViewById(R.id.member_add_height);
        member_add_goal = findViewById(R.id.member_add_goal);
        member_add_level = findViewById(R.id.member_add_level);
        member_add_age = findViewById(R.id.member_add_age);
        member_add_gender = findViewById(R.id.member_add_gender);

        member_add_address = findViewById(R.id.member_add_address);
        member_add_number = findViewById(R.id.member_add_number);
        member_add_name = findViewById(R.id.member_add_name);

        member_add_camera = findViewById(R.id.member_add_camera);
        member_add_image = findViewById(R.id.member_add_image);

        // Initialize FirebaseFirestore and StorageReference
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        // gender spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        member_add_gender.setAdapter(genderAdapter);

        // goal spinner
        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(this,
                R.array.goal_array, android.R.layout.simple_spinner_item);
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        member_add_goal.setAdapter(goalAdapter);

        // activity Level spinner
        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(this,
                R.array.activity_array, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        member_add_level.setAdapter(levelAdapter);

        // age spinner
        ArrayAdapter<Integer> ageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Populate the age spinner with ages from 18 to 100
        for (int i = 10; i <= 100; i++) {
            ageAdapter.add(i);
        }
        member_add_age.setAdapter(ageAdapter);

        // height spinner
        ArrayAdapter<Integer> heightAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        heightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Populate the age spinner with ages from 18 to 100
        for (int i = 90; i <= 250; i++) {
            heightAdapter.add(i);
        }
        member_add_height.setAdapter(heightAdapter);

        // weight spinner
        ArrayAdapter<Integer> weightAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        weightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Populate the age spinner with ages from 18 to 100
        for (int i = 30; i <= 250; i++) {
            weightAdapter.add(i);
        }
        member_add_weight.setAdapter(weightAdapter);

        member_add_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iuser = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iuser, PICK_IMAGE_REQUEST);

            }
        });
        member_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve current user information from Firebase Authentication
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    // Prepare data to update
                    String userId = currentUser.getUid();
                    String name = member_add_name.getText().toString();
                    String phoneNumber = member_add_number.getText().toString();
                    String address = member_add_address.getText().toString();
                    String gender = member_add_gender.getSelectedItem().toString();
                    String age = member_add_age.getSelectedItem().toString();
                    String height = member_add_height.getSelectedItem().toString();
                    String weight = member_add_weight.getSelectedItem().toString();
                    String goal = member_add_goal.getSelectedItem().toString();
                    String level = member_add_level.getSelectedItem().toString();

                    // Showing progress dialog while uploading
                    progressDialog = new ProgressDialog(UpdateProfile.this);
                    progressDialog.setTitle("Uploading");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();

                    // Upload image to Firebase Storage
                    final StorageReference imageRef = storageRef.child("profile_images/" + userId + ".jpg");
                    UploadTask uploadTask = imageRef.putFile(selectedImageUri);

                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return imageRef.getDownloadUrl();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            // Image uploaded successfully, now update Firestore document with image URL
                            String imageUrl = downloadUri.toString();

                            // Create a map to store user data
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", name);
                            userData.put("number", phoneNumber);
                            userData.put("address", address);
                            userData.put("gender", gender);
                            userData.put("age", age);
                            userData.put("height", height);
                            userData.put("weight", weight);
                            userData.put("goal", goal);
                            userData.put("activity", level);
                            userData.put("image", imageUrl);

                            // Update Firestore document with the new data
                            DocumentReference userRef = db.collection("users").document(userId);
                            userRef.update(userData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Document successfully updated
                                            progressDialog.dismiss();
                                            Toast.makeText(UpdateProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle errors
                                            progressDialog.dismiss();
                                            Toast.makeText(UpdateProfile.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle image upload failure
                            progressDialog.dismiss();
                            Toast.makeText(UpdateProfile.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    // User not logged in
                    Toast.makeText(UpdateProfile.this, "User not logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Retrieve intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String username = intent.getStringExtra("username");
            String email = intent.getStringExtra("email");
            String number = intent.getStringExtra("number");
            String address = intent.getStringExtra("address");
            String gender = intent.getStringExtra("gender");
            String age = intent.getStringExtra("age");
            String height = intent.getStringExtra("height");
            String weight = intent.getStringExtra("weight");
            String goal = intent.getStringExtra("goal");
            String level = intent.getStringExtra("level");

            String imageUriString = intent.getStringExtra("imageUri");
            if (imageUriString != null) {
                // Convert string URI to URI object
                selectedImageUri = Uri.parse(imageUriString);

                // Display the selected image
                Glide.with(this)
                        .load(selectedImageUri)
                        .into(member_add_image);
            }
            // Set retrieved data to corresponding views
            member_add_name.setText(name);
            member_add_number.setText(number);
            member_add_address.setText(address);
            // Set other fields similarly...
        }
    }

    // Method to handle image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            member_add_image.setImageURI(selectedImageUri);
        }
    }
}
