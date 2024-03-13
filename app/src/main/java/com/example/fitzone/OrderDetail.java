package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;

import org.json.JSONObject;

public class OrderDetail extends AppCompatActivity {
    private AppCompatTextView tr_name, tr_spec, tr_reting, or_date, or_start, or_end,or_charge;
    String charge ;
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
        checkout.setKeyID("rzp_test_vh8BpFvCnwiatc"); // Replace with your actual Razorpay key

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
    }


    public void onPaymentError(int i, String s) {
        // Payment failed, handle error logic here
        Toast.makeText(this, "Payment failed: " + s, Toast.LENGTH_SHORT).show();
    }
}
