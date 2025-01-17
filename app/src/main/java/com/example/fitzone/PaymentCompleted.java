package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PaymentCompleted extends AppCompatActivity {

    AppCompatTextView tr_name, tr_review, date, start_time, end_time;
    ImageView back ;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_completed);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        tr_name = findViewById(R.id.tr_name1);
        tr_review = findViewById(R.id.tr_review);
        date = findViewById(R.id.add_date);
        start_time = findViewById(R.id.start_time1);
        end_time = findViewById(R.id.end_time1);

        Intent intent = getIntent();
        String trainerName = intent.getStringExtra("trainer_name");
        String trainerReview = intent.getStringExtra("trainer_review");
        String appDate = intent.getStringExtra("regi_date");
        String startTime = intent.getStringExtra("start_time");
        String endTime = intent.getStringExtra("end_time");

        tr_name.setText(trainerName);
        tr_review.setText(trainerReview);
        date.setText(appDate);
        start_time.setText(startTime);
        end_time.setText(endTime);


    }

    // Method to generate PDF



}
