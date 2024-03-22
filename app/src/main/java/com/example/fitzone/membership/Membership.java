package com.example.fitzone.membership;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.fitzone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;

public class Membership extends AppCompatActivity {
    FirebaseFirestore db;

    ProgressDialog progressDialog;

    // Package details textviews
    AppCompatTextView pkgName,pkgDuration,pkgStatus,pkgPrice,pkgDate,pkgExpire;

    //  package purchase textviews
    AppCompatTextView payId , payDate,payAmount ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false); // Prevent dismissal by tapping outside


        pkgName = findViewById(R.id.pkg_name);
        pkgDuration = findViewById(R.id.pkg_duration);
        pkgStatus = findViewById(R.id.pkg_status);
        pkgPrice = findViewById(R.id.pkg_price);
        pkgDate = findViewById(R.id.pkg_date);
        pkgExpire = findViewById(R.id.pkg_expire);

        payId = findViewById(R.id.pay_id);
        payAmount = findViewById(R.id.pay_amount);
        payDate = findViewById(R.id.pay_date);

        progressDialog.show();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String time = intent.getStringExtra("time");
        String status = intent.getStringExtra("status");

        pkgName.setText(name);
        pkgDuration.setText(time);
        pkgStatus.setText(status);

        db = FirebaseFirestore.getInstance();

        db.collection("purchases").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        // Dismiss ProgressDialog when data fetching is complete
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve additional data and set them to respective TextViews
                                String price = document.getString("price");
                                String date = document.getString("purchasesDate");
//                                String expire = document.getString("purchasesDate");
                                String pay_id = document.getString("paymentId");

                                pkgPrice.setText(price != null ? price : "No price");
                                payAmount.setText(price != null ? price : "No payment amount");
                                pkgDate.setText(date != null ? date : "No purchases date");
                                payDate.setText(date != null ? date : "No payment date");
                                payId.setText(pay_id != null ? pay_id : "No payment id");
//                                pkgExpire.setText(bookingDate != null ? bookingDate : "No date");

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

        db.collection("packages").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        // Dismiss ProgressDialog when data fetching is complete


                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve additional data and set them to respective TextViews
                                String price = document.getString("price");
                                String date = document.getString("purchasesDate");
//                                String expire = document.getString("purchasesDate");
                                String pay_id = document.getString("paymentId");

                                pkgPrice.setText(price != null ? price : "No price");
                                payAmount.setText(price != null ? price : "No payment amount");
                                pkgDate.setText(date != null ? date : "No purchases date");
                                payDate.setText(date != null ? date : "No payment date");
                                payId.setText(pay_id != null ? pay_id : "No payment id");
//                                pkgExpire.setText(bookingDate != null ? bookingDate : "No date");

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