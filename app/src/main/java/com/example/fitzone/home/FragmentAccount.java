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
import com.example.fitzone.Feedback;
import com.example.fitzone.Login;
import com.example.fitzone.PrivacyPolicy;
import com.example.fitzone.Profile;
import com.example.fitzone.R;
import com.example.fitzone.booking.BookingList;
import com.example.fitzone.membership.Membership;
import com.example.fitzone.membership.MembershipList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class FragmentAccount extends Fragment {
    RelativeLayout profile, logout, bookings, membership, feedBack, privacyPolicy;
    AppCompatTextView user_acc_name, user_acc_username;
    String uid;
    ImageView show_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Initialize views
        profile = view.findViewById(R.id.rl_profile);
        logout = view.findViewById(R.id.rl_logout);
        bookings = view.findViewById(R.id.rl_bookings);
        membership = view.findViewById(R.id.rl_membership);
        privacyPolicy = view.findViewById(R.id.rl_privacy_policy);
        feedBack = view.findViewById(R.id.rl_feedback);

        user_acc_name = view.findViewById(R.id.user_acc_name);
        user_acc_username = view.findViewById(R.id.user_acc_username);
        show_image = view.findViewById(R.id.show_image); // Initialize user_acc_image

        String uid = getArguments().getString("uid");

        bookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), BookingList.class);
                startActivity(intent);
            }
        });
        membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MembershipList.class);
                startActivity(intent);
            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PrivacyPolicy.class);
                startActivity(intent);
            }
        });

        feedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Feedback.class);
                startActivity(intent);
            }
        });

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
                                    .into(show_image);
                        }
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            }


        return view; // Move the return statement here
    }
}