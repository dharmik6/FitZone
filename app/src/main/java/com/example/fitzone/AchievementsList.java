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
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AchievementsList extends AppCompatActivity {
    RecyclerView achieve_list;
    ImageView back;
    private AchievementsListAdapter adapter;
    private List<AchievementsListItem> trainersLists;
    ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements_list);

        achieve_list=findViewById(R.id.achieve_list);
//        trainer_idc=findViewById(R.id.trainer_idc);
        Intent intent1 = getIntent();
        String treid = intent1.getStringExtra("treid");
//        trainer_idc.setText(treid);
        achieve_list.setHasFixedSize(true);
        achieve_list.setLayoutManager(new LinearLayoutManager(this));

        trainersLists = new ArrayList<>();
        adapter = new AchievementsListAdapter(this,trainersLists);
        achieve_list.setAdapter(adapter);

        // Show ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").document(treid).collection("certificates").get().addOnSuccessListener(queryDocumentSnapshots -> {
            // Check if the list is empty or null
            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String name = documentSnapshot.getString("name");
                    String imageUrl = documentSnapshot.getString("imageUrl");
                    String description = documentSnapshot.getString("description");
                    String cerid = documentSnapshot.getId();
                    AchievementsListItem member = new AchievementsListItem(name, imageUrl, description, cerid);
                    trainersLists.add(member);
                }
                adapter.notifyDataSetChanged();
            } else {
                // Show toast message indicating no certificates available
                Toast.makeText(AchievementsList.this, "No certificates available", Toast.LENGTH_SHORT).show();
            }

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

        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}