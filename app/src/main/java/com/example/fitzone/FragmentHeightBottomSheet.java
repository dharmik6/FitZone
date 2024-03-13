package com.example.fitzone;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
//import android.widget.NumberPicker;
import android.widget.Toast;

import com.shawnlin.numberpicker.NumberPicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHeightBottomSheet#} factory method to
 * create an instance of this fragment.
 */
public class FragmentHeightBottomSheet extends BottomSheetDialogFragment {
    NumberPicker height_up_new;
    Button exe_het_update,exe_het_delete;
    ProgressDialog progressDialog; // Progress dialog for showing upload progress
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_height_bottom_sheet, container, false);
        // Initialize views and set up any necessary functionality
        exe_het_delete=view.findViewById(R.id.exe_het_delete);
        exe_het_update=view.findViewById(R.id.exe_het_update);
        height_up_new=view.findViewById(R.id.height_up_new);
        // Initialize FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        exe_het_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        exe_het_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show ProgressDialog
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Updating...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // Get the selected height value
                int height = height_up_new.getValue();
                String newHeight = String.valueOf(height);

                // Upload height to Firestore
                updateFirestoreDocument(newHeight);
            }
        });
        return view;
    }
    private void updateFirestoreDocument(String newHeight) {
        // Create a map with the updated fields
        Map<String, Object> dietData = new HashMap<>();
        dietData.put("height", newHeight);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Update the document in Firestore
            db.collection("users")
                    .document(userId)
                    .update(dietData)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Document updated successfully
                                Toast.makeText(getActivity(), "Height updated successfully", Toast.LENGTH_SHORT).show();
                                dismiss(); // Dismiss the bottom sheet after successful update

                                // Dismiss ProgressDialog
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } else {
                                // Error updating document
                                Toast.makeText(getActivity(), "Failed to update height: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                // Dismiss ProgressDialog
                                if (progressDialog != null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }
                        }
                    });
        }
    }
}
