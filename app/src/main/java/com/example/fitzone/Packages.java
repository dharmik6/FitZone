package com.example.fitzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Packages extends AppCompatActivity {
    AppCompatTextView pac_show_descri,pac_show_duration,pac_show_price,pac_show_name;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packages);

        pac_show_name = findViewById(R.id.pac_show_name);
        pac_show_price = findViewById(R.id.pac_show_price);
        pac_show_duration = findViewById(R.id.pac_show_duration);
        pac_show_descri = findViewById(R.id.pac_show_descri);

        Intent intent = getIntent();
        String pid = intent.getStringExtra("name");


        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("packages").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String description = documentSnapshot.getString("description");
                String price = documentSnapshot.getString("price");
                String duration = documentSnapshot.getString("duration");
//                 Check if the userNameFromIntent matches the user
                if (pid.equals(name)) {
                    // Display the data only if they match
                    pac_show_name.setText(name != null ? name : "No name");
                    pac_show_price.setText(price != null ? price : "No name");
                    pac_show_duration.setText(duration != null ? duration : "No name");
                    pac_show_descri.setText(description != null ? description : "No username");

                } else {
                    // userNameFromIntent and user don't match, handle accordingly
//                    showToast("User data does not match the intent.");
                }
            }
        });

        Checkout.preload(getApplicationContext());
    }

        public void initiatePayment(View view) {
            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_kwOuQYURkLzLPc"); // Replace with your actual Razorpay key

            // Convert charge from rupees to paise
            double chargeInRupees = Double.parseDouble(pac_show_price.getText().toString());
            int amountInPaise = (int) (chargeInRupees * 100); // Convert rupees to paise

            try {
                JSONObject options = new JSONObject();
                options.put("name", "FitZone");
                options.put("description", "Purchase Description");
                options.put("currency", "INR");
                options.put("amount", amountInPaise); // Amount in paise (e.g., ₹500 = 50000)
                options.put("prefill", new JSONObject().put("email", "customer@example.com"));

                checkout.open(this, options);
            } catch (Exception e) {
                Log.e("RazorpayError", "Error in starting Razorpay Checkout", e);
            }
        }



    public void onPaymentSuccess(String paymentId) {
        // Payment successful, handle success logic here
        Toast.makeText(this, "Payment successful. Payment ID: " + paymentId, Toast.LENGTH_SHORT).show();

    }


    public void onPaymentError(int i, String s) {
        // Payment failed, handle error logic here
        Toast.makeText(this, "Payment failed: " + s, Toast.LENGTH_SHORT).show();
    }
    private void showToast(String message) {
        Toast.makeText(Packages.this, message, Toast.LENGTH_SHORT).show();
    }
}