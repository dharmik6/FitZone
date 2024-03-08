package com.example.fitzone.home;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.fitzone.Profile;
import com.example.fitzone.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class FragmentAccount extends Fragment {
    RelativeLayout rl_profile;
    AppCompatTextView user_acc_name, user_acc_username;
    ImageView user_acc_image;
    String uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Initialize views
        rl_profile = view.findViewById(R.id.rl_profile);
        user_acc_name = view.findViewById(R.id.user_acc_name);
        user_acc_username = view.findViewById(R.id.user_acc_username);
        user_acc_image = view.findViewById(R.id.user_acc_image);

        // Retrieve the UID from arguments bundle
        if (getArguments() != null) {
            uid = getArguments().getString("uid");
        }
        rl_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open Profile activity with UID passed as extra
                Intent intent = new Intent(getActivity(), Profile.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        // Check if UID is not null before querying Firestore
        if (uid != null) {
            // Query Firestore for data
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String membername = documentSnapshot.getString("name");
                    String memberusername = documentSnapshot.getString("username");
                    String memberimage = documentSnapshot.getString("image");

                    // Check if the userNameFromIntent matches the user
                    if (uid.equals(membername)) {
                        // Display the data only if they match
                        user_acc_name.setText(membername != null ? membername : "No name");
                        user_acc_username.setText(memberusername != null ? memberusername : "No username");
                        if (memberimage != null) {
                            Glide.with(getActivity())
                                    .load(memberimage)
                                    .into(user_acc_image);
                        }
                        // Exit loop since the user is found
                        break;
                    }
                }
            }).addOnFailureListener(e -> {
                // Handle Firestore query failure
                e.printStackTrace();
            });
        } else {
            // Handle the case where UID is null
            Toast.makeText(getActivity(), "not", Toast.LENGTH_SHORT).show();
            // For example, show an error message or take appropriate action
        }

        return view;
    }
}
