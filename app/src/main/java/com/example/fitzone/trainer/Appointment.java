package com.example.fitzone.trainer;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fitzone.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Appointment extends AppCompatActivity {
    TextView selectedTimeTextView;
    CalendarView SelectedCalendarView ;
    CircleImageView app_tre_image;
    TextView app_tre_name,app_tre_specialization,app_tre_experience,app_tre_review;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        selectedTimeTextView = findViewById(R.id.selected_time);
        SelectedCalendarView = findViewById(R.id.calendarView);

        app_tre_review = findViewById(R.id.app_tre_review);
        app_tre_experience = findViewById(R.id.app_tre_experience);
        app_tre_specialization = findViewById(R.id.app_tre_specialization);
        app_tre_name = findViewById(R.id.app_tre_name);
        app_tre_image = findViewById(R.id.app_tre_image);
/////
        Intent intent = getIntent();
        String trainerName = intent.getStringExtra("trainer_name");
        String trainerImage = intent.getStringExtra("trainer_img");
        String trainerReview = intent.getStringExtra("trainer_review");
        String functionalStrength = intent.getStringExtra("Functional_Strength");
        String exeee = intent.getStringExtra("trainer_eee_txt");

        app_tre_review.setText(trainerReview);
        app_tre_experience.setText(functionalStrength);
// Assuming trainer specialization is stored in exeee
        app_tre_specialization.setText(exeee);
        app_tre_name.setText(trainerName);

// Assuming trainer image is stored as resource ID
//        if (trainerImage != null) {
//            int resourceId = Integer.parseInt(trainerImage); // Assuming trainerImage is a resource ID
//            app_tre_image.setImageResource(resourceId);
//        } else {
//            // If trainer image is stored as a URL or another format, handle it accordingly
//        }



        // Get tomorrow's date
       Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);

        // Set minimum date for CalendarView
        SelectedCalendarView.setMinDate(tomorrow.getTimeInMillis());
        // Set up listener for CalendarView
        SelectedCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Create a Calendar object for the selected date
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                if (selectedDate.before(tomorrow)) {
                    Toast.makeText(Appointment.this, "Please select a date starting from tomorrow.", Toast.LENGTH_SHORT).show();
                } else {
                    // Format the selected date
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String selectedDateString = sdf.format(selectedDate.getTime());
                    Toast.makeText(Appointment.this, selectedDateString, Toast.LENGTH_SHORT).show();
                }
            }
        });

/////
        selectedTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Appointment.this,
                R.style.MyTimePickerDialogStyle,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = hourOfDay + ":" + minute;
                        if (validateTime(selectedTime)) {
                            selectedTimeTextView.setText(selectedTime);
                        } else {
                            Toast.makeText(Appointment.this, "Please select time between 7:00 AM and 7:00 PM", Toast.LENGTH_SHORT).show();

                        }
                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }

    private boolean validateTime(String time) {
        String[] timeParts = time.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        if (hour >= 7 && hour < 19) {
            return true;
        } else {
            return false;
        }
    }
}