package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PackagesList extends AppCompatActivity {

    RecyclerView payment_recyc;
    private PackagesAdapter adapter;
    private List<PackagesItemList> dietLists;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages_list);

        payment_recyc = findViewById(R.id.payment_recyc);

        // Set click listener for adding payment


        payment_recyc.setHasFixedSize(true);
        payment_recyc.setLayoutManager(new LinearLayoutManager(this));

        dietLists = new ArrayList<>();
        adapter = new PackagesAdapter(this, dietLists);
        payment_recyc.setAdapter(adapter);

        // Show ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Load data from Firestore


        // Set back button click listener
        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data every time activity is resumed
        loadPackageData();
    }

    private void loadPackageData() {
        // Clear previous data
        dietLists.clear();

        // Fetch data from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("packages").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String price = documentSnapshot.getString("price");
                String duration = documentSnapshot.getString("duration");
                String description = documentSnapshot.getString("description");
                PackagesItemList diet = new PackagesItemList(name, price, duration, description);
                dietLists.add(diet);
            }

            // Notify adapter about data changes
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
}