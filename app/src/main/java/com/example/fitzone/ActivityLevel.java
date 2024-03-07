package com.example.fitzone;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.fitzone.home.MainActivity;

public class ActivityLevel extends AppCompatActivity {

    NumberPicker textPicker;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        textPicker = findViewById(R.id.np_level);

        Button activitylevel = findViewById(R.id.btn_activitylevel);

        final String[] items = {"Beginner", "Intermediate", "Advance","Beginner", "Intermediate", "Advance"};

        // Set the values for the text picker
        textPicker.setMinValue(0);
        textPicker.setMaxValue(items.length - 1);
        textPicker.setDisplayedValues(items);
        textPicker.setTextSize(60);

        activitylevel.setOnClickListener(view -> {
            int selectedIndex = textPicker.getValue();
            String selectedText = items[selectedIndex];
            Toast.makeText(ActivityLevel.this, "Selected Activity Level: " + selectedText, Toast.LENGTH_SHORT).show();

            redirectActivity(ActivityLevel.this , MainActivity.class);
        });
    }
    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
}