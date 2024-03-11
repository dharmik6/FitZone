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


    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
                Boolean check = pref.getBoolean("flag", false);
                Intent inext;
                if (check) {
                    // User is logged in, navigate to home_page
                    inext = new Intent(SplashScreen.this, MainActivity.class);
                } else {
                    // User is not logged in, navigate to login_page
                    inext = new Intent(SplashScreen.this, Login.class);
                }

                startActivity(inext);
                finish(); // Close the splash screen activity after navigating
            }
        }, 2000);




    }
}