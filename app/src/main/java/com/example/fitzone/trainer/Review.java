package com.example.fitzone.trainer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fitzone.R;

public class Review extends AppCompatActivity {
Button Write_a_Review;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Write_a_Review=findViewById(R.id.Write_a_Review);
        Write_a_Review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Review.this, WriteReview.class));
            }
        });
    }
}