package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class WorkoutExercisesList extends AppCompatActivity {
    RecyclerView edit_exe_tr;

    private WorkoutExercisesListAdapter adapter;
    private List<WorkoutExercisesListItem> exercisesItemLists;
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_exercises_list);
        edit_exe_tr = findViewById(R.id.edit_exe_tr);

        Intent intent = getIntent();
        String wid = intent.getStringExtra("wid");
        String w_image = intent.getStringExtra("w_image");
        String w_name = intent.getStringExtra("w_name");

        edit_exe_tr.setHasFixedSize(true);
        edit_exe_tr.setLayoutManager(new LinearLayoutManager(this));

        exercisesItemLists = new ArrayList<>(); // Initialize exercisesItemLists
        adapter = new WorkoutExercisesListAdapter(this, exercisesItemLists); // Use correct adapter
        edit_exe_tr.setAdapter(adapter);

        // Show ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("exercises").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String body = documentSnapshot.getString("body");
                String image = documentSnapshot.getString("imageUrl");
                String id = documentSnapshot.getId();
                WorkoutExercisesListItem exe = new WorkoutExercisesListItem(name, body, image, id,wid,w_name,w_image);
                exercisesItemLists.add(exe);
            }

            // Now that we have fetched all exercises, let's filter out the ones that are already in the workout plan
            List<WorkoutExercisesListItem> filteredExercises = new ArrayList<>();
            if (wid != null) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    progressDialog.show(); // Show progress dialog before fetching user data
                    String userId = currentUser.getUid();
                    FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                    db2.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String uid = documentSnapshot.getId();

                // Fetch the exename array of the workout plan
                db.collection("users").document(uid).collection("user_workout_plans").document(wid).get().addOnSuccessListener(workoutPlanDocument -> {
                    if (workoutPlanDocument.exists()) {
                        List<String> exename = (List<String>) workoutPlanDocument.get("exename");
                        if (exename != null) {
                            // Iterate through all exercises and exclude the ones that are present in the exename array
                            for (WorkoutExercisesListItem exercise : exercisesItemLists) {
                                if (!exename.contains(exercise.getId())) {
                                    filteredExercises.add(exercise);
                                }
                            }
                        } else {
                            filteredExercises.addAll(exercisesItemLists);
                        }
                    } else {
                        filteredExercises.addAll(exercisesItemLists);
                    }

                    // Update the adapter with the filtered exercises
                    adapter.filterList(filteredExercises);
                    // Dismiss ProgressDialog when data is loaded
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(e -> {
                    // Handle failures
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                });
                        }
                    });
                }
            } else {
                // If wid is null, add all exercises to the filtered list
                filteredExercises.addAll(exercisesItemLists);
                adapter.filterList(filteredExercises);
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(e -> {
            // Handle failures
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        });

        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}
