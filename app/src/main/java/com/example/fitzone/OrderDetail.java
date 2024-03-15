package com.example.fitzone;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class OrderDetail extends AppCompatActivity {
    private static final String TAG = "OrderDetail";
    private AppCompatTextView tr_reting, or_date, or_start, or_end,or_charge;
    TextView tr_name, tr_spec;
    String charge ,treid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();
        String trainerName = intent.getStringExtra("trainer_name");
        String functionalStrength = intent.getStringExtra("Functional_Strength");
        String trainerReview = intent.getStringExtra("trainer_review");
        String appDate = intent.getStringExtra("regi_date");
        String startTime = intent.getStringExtra("start_time");
        String endTime = intent.getStringExtra("end_time");
        charge = intent.getStringExtra("charge");
        treid = intent.getStringExtra("treid");

        tr_name = findViewById(R.id.or_tre_name);
        tr_spec = findViewById(R.id.or_tre_experience);
        tr_reting = findViewById(R.id.or_tre_review);
        or_date = findViewById(R.id.or_date);
        or_start = findViewById(R.id.start_time);
        or_end = findViewById(R.id.end_time);
        or_charge = findViewById(R.id.or_charg);

        tr_name.setText(trainerName);
        tr_spec.setText(functionalStrength);
        tr_reting.setText(trainerReview);
        or_date.setText(appDate);
        or_start.setText(startTime);
        or_end.setText(endTime);
        or_charge.setText(charge);




        Checkout.preload(getApplicationContext());
    }

    public void initiatePayment(View view) {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_kwOuQYURkLzLPc"); // Replace with your actual Razorpay key

        // Convert charge from rupees to paise
        double chargeInRupees = Double.parseDouble(charge);
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        // Get current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        // Create a Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new booking document with a unique ID
        DocumentReference bookingRef = db.collection("bookings").document();

        // Create a booking object with required fields
        Map<String, Object> booking = new HashMap<>();
        booking.put("startTime", or_start.getText().toString());
        booking.put("endTime", or_end.getText().toString());
        booking.put("userId", userId);
        booking.put("treinerId", treid);
        booking.put("paymentId", paymentId);
        booking.put("charge", charge); // Add the charge value here
        booking.put("bookingDate", currentDate); // Add current date
        booking.put("paymentStatus", "pending"); // Add payment status

        // Add the booking object to Firestore with the generated ID
        bookingRef.set(booking)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Booking added with ID: " + bookingRef.getId());
                        // Now you can navigate to the payment completed activity
                        Intent intsuc = new Intent(OrderDetail.this, PaymentCompleted.class);
                        intsuc.putExtra("trainer_name", tr_name.getText());
                        intsuc.putExtra("trainer_review", tr_reting.getText());
                        intsuc.putExtra("regi_date", or_date.getText());
                        intsuc.putExtra("start_time", or_start.getText());
                        intsuc.putExtra("end_time", or_end.getText());
                        intsuc.putExtra("paymentId", paymentId);
                        intsuc.putExtra("treid", treid);
                        intsuc.putExtra("charge", charge); // Pass charge to PaymentCompleted activity
                        startActivity(intsuc);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding booking", e);
                        // Handle the error
                        Toast.makeText(OrderDetail.this, "Error adding booking", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void onPaymentError(int i, String s) {
        // Payment failed, handle error logic here
        Toast.makeText(this, "Payment failed: " + s, Toast.LENGTH_SHORT).show();
    }
}