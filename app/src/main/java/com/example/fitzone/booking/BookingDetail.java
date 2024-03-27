package com.example.fitzone.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fitzone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;

public class BookingDetail extends AppCompatActivity {

    ProgressDialog progressDialog;
    FirebaseFirestore db;
    AppCompatTextView booking_id, trainer_name, booking_date, start_time, end_time, book_status, pay_id, amounT, date, member_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false); // Prevent dismissal by tapping outside

        // Find views
        booking_id = findViewById(R.id.booking_id);
        trainer_name = findViewById(R.id.trainer_id);
        booking_date = findViewById(R.id.booking_date);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        book_status = findViewById(R.id.book_status);
        pay_id = findViewById(R.id.pay_id);
        amounT = findViewById(R.id.amount);
        date = findViewById(R.id.date);
        member_name = findViewById(R.id.member_name);

        ImageView backPress = findViewById(R.id.back);
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String status = intent.getStringExtra("status");

        booking_id.setText(id);
        member_name.setText(name);
        book_status.setText(status);

        db = FirebaseFirestore.getInstance();

        // Show ProgressDialog
        progressDialog.show();

        db.collection("bookings").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        // Dismiss ProgressDialog when data fetching is complete
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve additional data and set them to respective TextViews
                                String bookingDate = document.getString("bookingDate");
                                String startTime = document.getString("startTime");
                                String endTime = document.getString("endTime");
                                String trainerId = document.getString("treinerId");
                                String payId = document.getString("paymentId");
                                String amount = document.getString("charge");

                                db.collection("trainers")
                                        .document(trainerId)
                                        .get()
                                        .addOnSuccessListener(userDocumentSnapshot -> {
                                            // Retrieve user details
                                            String name = userDocumentSnapshot.getString("name");

                                            trainer_name.setText(name != null ? name : "No name");

                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle failure to fetch user details
                                        });

                                Log.d("FirestoreData", "Booking Date: " + bookingDate);
                                Log.d("FirestoreData", "Start Time: " + startTime);
                                Log.d("FirestoreData", "End Time: " + endTime);

                                booking_date.setText(bookingDate != null ? bookingDate : "No date");
                                date.setText(bookingDate != null ? bookingDate : "No date");
                                start_time.setText(startTime != null ? startTime : "No date");
                                end_time.setText(endTime != null ? endTime : "No date");
                                end_time.setText(endTime != null ? endTime : "No date");
                                pay_id.setText(payId != null ? payId : "No payment id");
                                amounT.setText(amount != null ? amount : "No amount");

                            } else {
                                // Handle the case where the document does not exist
                            }

                        } else {
                            // Handle exceptions while fetching data from Firestore
                            FirebaseFirestoreException exception = (FirebaseFirestoreException) task.getException();
                            // Log the exception or handle it as needed
                        }
                    }
                });
    }
}