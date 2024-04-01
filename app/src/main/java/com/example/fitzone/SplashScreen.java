package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.fitzone.home.MainActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private static final String PREF_NAME = "MyPrefs";
    private static final String FIRST_TIME_KEY = "firstTime";

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean isFirstTime = pref.getBoolean(FIRST_TIME_KEY, true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (isFirstTime) {
                    // First time opening the app, navigate to OnboardingScreen
                    intent = new Intent(SplashScreen.this, OnboardingScreen.class);
                } else {
                    // Check login status and navigate accordingly
                    SharedPreferences loginPref = getSharedPreferences("login", MODE_PRIVATE);
                    boolean isLoggedIn = loginPref.getBoolean("flag", false);
                    if (isLoggedIn) {
                        // User is logged in, navigate to MainActivity
                        intent = new Intent(SplashScreen.this, MainActivity.class);
                    } else {
                        // User is not logged in, navigate to LoginActivity
                        intent = new Intent(SplashScreen.this, Login.class);
                    }
                }

                startActivity(intent);
                finish(); // Close the splash screen activity after navigating
            }
        }, 2000);

        // Reset the flag indicating first-time launch
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FIRST_TIME_KEY, true);
        editor.apply();
    }
}
