package com.example.fitzone;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitzone.home.WorkoutHomeAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class WorkoutPlanVatical extends AppCompatActivity {
    RecyclerView workoutplanvatical_list;
    private WorkoutPlanVaticalAdapter vaticalAdapter;
    private List<ExercisesItemList> exeLists;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout_plan_vatical);
        workoutplanvatical_list=findViewById(R.id.workoutplanvatical_list);
        // Set horizontal layout manager
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        workoutplanvatical_list.setLayoutManager(layoutManager2);

        exeLists = new ArrayList<>();
        vaticalAdapter = new WorkoutPlanVaticalAdapter(this, exeLists);
        workoutplanvatical_list.setAdapter(vaticalAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        db2.collection("workout_plans").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String description = documentSnapshot.getString("goal");
                String image = documentSnapshot.getString("image");
                ExercisesItemList diet = new ExercisesItemList(name, description, image);
                exeLists.add(diet);
            }

            vaticalAdapter.notifyDataSetChanged();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        });


    }
}