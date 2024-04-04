package com.example.fitzone.booking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fitzone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookingList extends AppCompatActivity {

    ImageView back;
    RecyclerView RecycAcceptedList;
    private BookingAdapter adapter;
    private List<BookingItemList> acceptedLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        back = findViewById(R.id.back);
        RecycAcceptedList = findViewById(R.id.booking);
        RecycAcceptedList.setHasFixedSize(true);
        RecycAcceptedList.setLayoutManager(new LinearLayoutManager(this));

        acceptedLists = new ArrayList<>();
        adapter = new BookingAdapter(this, acceptedLists);
        RecycAcceptedList.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookingData();
    }

    private void loadBookingData() {
        acceptedLists.clear(); // Clear the previous list
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();

        db.collection("bookings")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String userId = documentSnapshot.getString("trainerId");
                        String status = documentSnapshot.getString("paymentStatus");
                        String id = documentSnapshot.getId();

                        // Fetch user details using userId
                        db.collection("trainers")
                                .document(userId)
                                .get()
                                .addOnSuccessListener(userDocumentSnapshot -> {
                                    // Retrieve user details
                                    String name = userDocumentSnapshot.getString("name");
                                    String specialization = userDocumentSnapshot.getString("specialization");
                                    // Create a BookingItemList object with user details
                                    BookingItemList bookingItem = new BookingItemList(name, specialization, status, id);
                                    acceptedLists.add(bookingItem);
                                    adapter.notifyDataSetChanged(); // Notify adapter about data changes
                                })
                                .addOnFailureListener(e -> {
                                    // Handle failure to fetch user details
                                    Toast.makeText(BookingList.this, "Failed to fetch trainer details", Toast.LENGTH_SHORT).show();
                                    // Log error message for debugging: Log.e("BookingList", "Failed to fetch trainer details", e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure to fetch bookings
                    Toast.makeText(BookingList.this, "Failed to fetch bookings", Toast.LENGTH_SHORT).show();
                    // Log error message for debugging: Log.e("BookingList", "Failed to fetch bookings", e);
                });
    }
}
