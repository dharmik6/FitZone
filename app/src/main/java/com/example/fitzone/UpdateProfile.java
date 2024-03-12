package com.example.fitzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
    EditText member_add_address,member_add_number,member_add_email,member_add_username,member_add_name;
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
        member_add_email = findViewById(R.id.member_add_email);
        member_add_username = findViewById(R.id.member_add_username);
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
                updateProfile();
            }
        });
    }

    private void updateProfile() {
        String name = member_add_name.getText().toString().trim();
        String username = member_add_username.getText().toString().trim();
        String email = member_add_email.getText().toString().trim();
        String phoneNumber = member_add_number.getText().toString().trim();
        String address = member_add_address.getText().toString().trim();

        // Validate name
        if (TextUtils.isEmpty(name)) {
            member_add_name.setError("Please enter your name");
            member_add_name.requestFocus();
            return;
        }

        // Validate username
        if (TextUtils.isEmpty(username)) {
            member_add_username.setError("Please enter your username");
            member_add_username.requestFocus();
            return;
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            member_add_email.setError("Please enter your email");
            member_add_email.requestFocus();
            return;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            member_add_email.setError("Please enter a valid email address");
            member_add_email.requestFocus();
            return;
        }

        // Validate phoneNumber
        if (TextUtils.isEmpty(phoneNumber)) {
            member_add_number.setError("Please enter your phone number");
            member_add_number.requestFocus();
            return;
        }

        // Validate address
        if (TextUtils.isEmpty(address)) {
            member_add_address.setError("Please enter your address");
            member_add_address.requestFocus();
            return;
        }

        // If all validations pass, proceed with updating the profile
        // You can place the existing code for updating the profile here
        // Example:
        // uploadProfileData(name, username, email, phoneNumber, address);
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
