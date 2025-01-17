package com.example.fitzone;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
//import android.widget.NumberPicker;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.widget.Toast;


import com.example.fitzone.home.FragmentReports;
import com.shawnlin.numberpicker.NumberPicker;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FragmentWeightBottomSheet extends BottomSheetDialogFragment {
    NumberPicker weight_up_new;
    Button exe_update,exe_delete;
    ProgressDialog progressDialog; // Progress dialog for showing upload progress
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weight_bottom_sheet, container, false);
        // Initialize views and set up any necessary functionality
        exe_delete = view.findViewById(R.id.exe_delete);
        exe_update = view.findViewById(R.id.exe_update);
        weight_up_new = view.findViewById(R.id.weight_up_new);
        progressDialog = new ProgressDialog(getActivity()); // Initialize ProgressDialog
        progressDialog.setMessage("Updating weight..."); // Set message for ProgressDialog
        // Initialize FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        exe_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        exe_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected weight value
                int Weight = weight_up_new.getValue();
                String newWeight = String.valueOf(Weight);

                // Show progressDialog before starting weight update process
                progressDialog.show();

                // Upload weight to Firestore
                updateFirestoreDocument(newWeight);
            }
        });
        return view;
    }

    private void updateFirestoreDocument(String newWeight) {
        // Create a map with the updated fields
        Map<String, Object> weightData = new HashMap<>();

        // Get current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(new Date());

        weightData.put("weight", newWeight);
        weightData.put("date", currentDate);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Update the document in Firestore
            db.collection("users")
                    .document(userId)
                    .collection("weight")
                    .document(currentDate)
                    .set(weightData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss(); // Dismiss progressDialog after the update process completes
                            if (task.isSuccessful()) {
                                // Document updated successfully
                                Toast.makeText(getActivity(), "Weight date updated successfully", Toast.LENGTH_SHORT).show();
                                dismiss(); // Dismiss the bottom sheet after successful update
                            } else {
                                // Error updating document
                                Toast.makeText(getActivity(), "Failed to update weight date: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    // This method is called when the BottomSheet is dismissed
    // This method is called when the BottomSheet is dismissed
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        // Trigger refresh of the page in the parent fragment/activity if the parent is FragmentReports
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof FragmentReports) {
            ((FragmentReports) parentFragment).fetchDataAndUpdateUI();
        }
    }

}
