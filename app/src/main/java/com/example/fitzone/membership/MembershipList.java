package com.example.fitzone.membership;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.fitzone.R;
import com.example.fitzone.booking.BookingItemList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MembershipList extends AppCompatActivity {
    RecyclerView recyc_membership ;
   private MembershipAdapter adapter ;
   private List<MembershipItemList> membershiplist ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_list);

        recyc_membership = findViewById(R.id.membership);
        recyc_membership.setHasFixedSize(true);
        recyc_membership.setLayoutManager(new LinearLayoutManager(this));

        membershiplist = new ArrayList<>();
        adapter = new MembershipAdapter(this,membershiplist);

        recyc_membership.setAdapter(adapter);
    }
    protected void onResume() {
        super.onResume();
        loadDietData();
    }

    private void loadDietData() {
        membershiplist.clear(); // Clear the previous list
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();

        db.collection("purchases")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String name = documentSnapshot.getString("packageName");
                        String status = documentSnapshot.getString("status");
                        String time = documentSnapshot.getString("duration");
                        String id = documentSnapshot.getId();

                        MembershipItemList bookingList = new MembershipItemList(name, time, status ,id);
                        membershiplist.add(bookingList);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }


}