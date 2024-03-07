package com.example.fitzone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Goal extends AppCompatActivity {

    NumberPicker textPicker;


    @SuppressLint({"NewApi", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        textPicker = findViewById(R.id.np_goal);

        Button goal = findViewById(R.id.btn_goal);

        final String[] items = {"Lose weight", "Get fitter", "Gain more flexible","Lose weight", "Get fitter", "Gain more flexible"};

        // Set the values for the text picker
        textPicker.setMinValue(0);
        textPicker.setMaxValue(items.length - 1);
        textPicker.setDisplayedValues(items);
        textPicker.setTextSize(60);

        goal.setOnClickListener(view -> {
            int selectedIndex = textPicker.getValue();
            String selectedText = items[selectedIndex];
            Toast.makeText(Goal.this, "Selected Goal: " + selectedText, Toast.LENGTH_SHORT).show();
            redirectActivity(Goal.this , ActivityLevel.class);
        });
    }
    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}