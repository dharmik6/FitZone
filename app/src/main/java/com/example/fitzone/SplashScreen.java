package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.shimmer.ShimmerFrameLayout;



@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    ShimmerFrameLayout shimmerFrameLayout;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer(); // Start shimmer animation

        new Handler().postDelayed(() -> {
            // on below line we are
            // creating a new intent
            Intent i = new Intent(SplashScreen.this, Login.class);

            // on below line we are
            // starting a new activity.
            startActivity(i);

            // on the below line we are finishing
            // our current activity.
            finish();
        }, 4000);
    }
}