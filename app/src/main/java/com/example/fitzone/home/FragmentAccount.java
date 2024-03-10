package com.example.fitzone.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.fitzone.Login;
import com.example.fitzone.Profile;
import com.example.fitzone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class FragmentAccount extends Fragment {
    RelativeLayout profile, logout;
    AppCompatTextView user_acc_name, user_acc_username;
    ImageView user_acc_image;
    String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Initialize views
        profile = view.findViewById(R.id.rl_profile);
        logout = view.findViewById(R.id.rl_logout);
        user_acc_name = view.findViewById(R.id.user_acc_name);
        user_acc_username = view.findViewById(R.id.user_acc_username);
        user_acc_image = view.findViewById(R.id.user_acc_image);

        // Retrieve the UID from arguments bundle
        if (getArguments() != null) {
            uid = getArguments().getString("uid");
        }
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Profile.class);
//                    intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("flag", false);
                editor.apply();

                // After logging out, navigate to the LoginActivity
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
                getActivity().finish(); // Close the current activity
            }
        });

        // Check if UID is not null before querying Firestore
        if (uid != null) {
            // Query Firestore for data
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String membername = documentSnapshot.getString("name");
                        String memberusername = documentSnapshot.getString("username");
                        String memberimage = documentSnapshot.getString("image");

                        user_acc_name.setText(membername != null ? membername : "No name");
                        user_acc_username.setText(memberusername != null ? memberusername : "No username");
                        if (memberimage != null) {
                            Glide.with(FragmentAccount.this)
                                    .load(memberimage)
                                    .into(user_acc_image);
                        }
                    }
                }).addOnFailureListener(e -> {
//                Toast.makeText(FragmentAccount.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
//            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            }
        }

        return view; // Move the return statement here
    }
}
