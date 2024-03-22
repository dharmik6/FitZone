package com.example.fitzone;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

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

    private RecyclerView workout_plan_list;

    private WorkoutPlanVaticalAdapter exeadapter;
    private List<ExercisesItemList> exeLists;

    private ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout_plan_vatical);
        workout_plan_list=findViewById(R.id.workout_plan_list);
        // Set horizontal layout manager
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        workout_plan_list.setLayoutManager(layoutManager2);

        exeLists = new ArrayList<>();
        exeadapter = new WorkoutPlanVaticalAdapter(this, exeLists);
        workout_plan_list.setAdapter(exeadapter);

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
                String wid = documentSnapshot.getId();
                ExercisesItemList diet = new ExercisesItemList(name, description, image,wid);
                exeLists.add(diet);
            }

            exeadapter.notifyDataSetChanged();
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