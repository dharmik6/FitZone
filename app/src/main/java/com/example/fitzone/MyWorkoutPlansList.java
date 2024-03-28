package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyWorkoutPlansList extends AppCompatActivity {
    RecyclerView myworkout_recyc;
    private MyWorkoutPlansAdapter adapter;
    private List<MyWorkoutPlansItemList> exercisesItemLists;
    private ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_workout_plans_list);
        myworkout_recyc = findViewById(R.id.myworkout_recyc);

        myworkout_recyc.setHasFixedSize(true);
        myworkout_recyc.setLayoutManager(new LinearLayoutManager(this));

        exercisesItemLists = new ArrayList<>(); // Initialize exercisesItemLists
        adapter = new MyWorkoutPlansAdapter(this, exercisesItemLists); // Use correct adapter
        myworkout_recyc.setAdapter(adapter);

        // Show ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db2 = FirebaseFirestore.getInstance();
            db2.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot2 -> {
                if (documentSnapshot2.exists()) {
                    String membername = documentSnapshot2.getId();

                    // Query Firestore for data
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users").document(membername).collection("user_workout_plans").get().addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String name = documentSnapshot.getString("name");
                            String body = documentSnapshot.getString("goal");
                            String image = documentSnapshot.getString("image");
                            String id = documentSnapshot.getId();
                            MyWorkoutPlansItemList exe = new MyWorkoutPlansItemList(name, body, image, id);
                            exercisesItemLists.add(exe);
                        }

                        adapter.notifyDataSetChanged();
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

        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        CardView add_my_workout = findViewById(R.id.add_my_workout);
        add_my_workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyWorkoutPlansList.this,CreateWorkoutPlan.class));
            }
        });
    }
}