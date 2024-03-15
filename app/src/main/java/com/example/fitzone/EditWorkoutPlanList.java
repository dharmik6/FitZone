package com.example.fitzone;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EditWorkoutPlanList extends AppCompatActivity {
RecyclerView wor_plan_recyc;
    private EditWorkoutPlanListAdapter adapter;
    private List<EditWorkoutPlanListItem> exercisesItemLists;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_workout_plan_list);

        wor_plan_recyc = findViewById(R.id.wor_plan_recyc);


        Intent intent = getIntent();
        String wid = intent.getStringExtra("wid");


        wor_plan_recyc.setHasFixedSize(true);
        wor_plan_recyc.setLayoutManager(new LinearLayoutManager(this));

        exercisesItemLists = new ArrayList<>(); // Initialize exercisesItemLists
        adapter = new EditWorkoutPlanListAdapter(this, exercisesItemLists); // Use correct adapter
        wor_plan_recyc.setAdapter(adapter);

        // Show ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
//        progressDialog.show();

        fetchAndDisplayExerciseDetails(wid);

        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
    }
    private void fetchAndDisplayExerciseDetails(String wid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("workout_plans")
                .document(wid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<String> exerciseIds = (List<String>) documentSnapshot.get("exename");
                        if (exerciseIds != null) {
                            for (String exerciseId : exerciseIds) {
                                fetchExerciseDetails(exerciseId);
                            }

                        }
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error fetching workout document", e));
    }

    // Function to fetch exercise details
    private void fetchExerciseDetails(String exerciseId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("exercises")
                .document(exerciseId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        EditWorkoutPlanListItem item = documentSnapshot.toObject(EditWorkoutPlanListItem.class);
                        if (item != null) {
                            exercisesItemLists.add(item);
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error fetching exercise document", e));
    }
}