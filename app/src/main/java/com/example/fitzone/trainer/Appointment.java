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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fitzone.OrderDetail;
import com.example.fitzone.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Appointment extends AppCompatActivity {
    TextView startTime ,endTime,regiDate;
    CalendarView SelectedCalendarView ;
    CircleImageView app_tre_image;
    Button btnNext ;
    String selectedDateString;
    TextView app_tre_name,app_tre_specialization,app_tre_experience,app_tre_review;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        startTime = findViewById(R.id.strat_time);
        endTime = findViewById(R.id.end_time);
        SelectedCalendarView = findViewById(R.id.calendarView);

        app_tre_review = findViewById(R.id.app_tre_review);
        app_tre_experience = findViewById(R.id.app_tre_experience);
        app_tre_specialization = findViewById(R.id.app_tre_specialization);
        app_tre_name = findViewById(R.id.app_tre_name);
        app_tre_image = findViewById(R.id.app_tre_image);
        btnNext = findViewById(R.id.btn_next);
        regiDate = findViewById(R.id.regiDate);
/////



        Intent intent = getIntent();
        String trainerName = intent.getStringExtra("trainer_name");
        String trainerImage = intent.getStringExtra("trainer_img");
        String trainerReview = intent.getStringExtra("trainer_review");
        String functionalStrength = intent.getStringExtra("Functional_Strength");
        String experience = intent.getStringExtra("trainer_eee_txt");
        String charge = intent.getStringExtra("charge");
        String treid = intent.getStringExtra("trid");

        app_tre_review.setText(trainerReview);
        app_tre_experience.setText(functionalStrength);
// Assuming trainer specialization is stored in exeee
        app_tre_specialization.setText(experience);
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
        SelectedCalendarView.setMinDate(tomorrow.getTimeInMillis());
        // Set minimum date for CalendarView
        // Set up listener for CalendarView
        SelectedCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            // Inside the OnDateChangeListener
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
                    selectedDateString = sdf.format(selectedDate.getTime());
                    regiDate.setText(selectedDateString); // Set the text here
                    Toast.makeText(Appointment.this, selectedDateString, Toast.LENGTH_SHORT).show();
                }
            }

        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStrartTimePickerDialog();
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showEndTimePickerDialog();

            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if a date is selected, if not, select tomorrow's date
                String selectedDate = regiDate.getText().toString();
                if (selectedDate.isEmpty()) {
                    // Handle the case where no date is selected
                    Toast.makeText(Appointment.this, "Please select a date", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if start time is selected
                String startTimeText = startTime.getText().toString();
                if (startTimeText.isEmpty()) {
                    Toast.makeText(Appointment.this, "Please select start time", Toast.LENGTH_SHORT).show();
                    return; // Return from the method to prevent further execution
                }

                // Check if end time is selected
                String endTimeText = endTime.getText().toString();
                if (endTimeText.isEmpty()) {
                    Toast.makeText(Appointment.this, "Please select end time", Toast.LENGTH_SHORT).show();
                    return; // Return from the method to prevent further execution
                }

                // Check if end time is greater than start time
                if (!isEndTimeAfterStartTime(startTimeText, endTimeText)) {
                    Toast.makeText(Appointment.this, "End time must be after start time", Toast.LENGTH_SHORT).show();
                    return; // Return from the method to prevent further execution
                }

                // Validate and parse charge
                double totalCharge;
                try {
                    totalCharge = Double.parseDouble(charge);
                } catch (NumberFormatException e) {
                    // Handle the case where charge is not a valid numerical value
                    Toast.makeText(Appointment.this, "Invalid charge value", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Calculate the duration in hours
                double durationHours = calculateDurationHours(startTimeText, endTimeText);

                // Calculate the total charge
                totalCharge *= durationHours;

                // Proceed to OrderDetail activity
                Intent intent1 = new Intent(Appointment.this, OrderDetail.class);
                intent1.putExtra("trainer_name", trainerName);
                intent1.putExtra("trainer_review", trainerReview);
                intent1.putExtra("Functional_Strength", functionalStrength);
                intent1.putExtra("trainer_eee_txt", experience);
                intent1.putExtra("regi_date", selectedDate);
                intent1.putExtra("charge", String.valueOf(totalCharge)); // Pass the total charge
                intent1.putExtra("start_time", startTimeText);
                intent1.putExtra("end_time", endTimeText);
                intent1.putExtra("treid", treid);
                startActivity(intent1);
            }
        });


    }
    private boolean isEndTimeAfterStartTime(String startTime, String endTime) {
        String[] startTimeParts = startTime.split(":");
        String[] endTimeParts = endTime.split(":");

        int startHour = Integer.parseInt(startTimeParts[0]);
        int startMinute = Integer.parseInt(startTimeParts[1]);

        int endHour = Integer.parseInt(endTimeParts[0]);
        int endMinute = Integer.parseInt(endTimeParts[1]);

        // Check if end hour is greater than start hour
        if (endHour > startHour) {
            return true;
        } else if (endHour == startHour) {
            // If end hour is equal to start hour, check minutes
            return endMinute > startMinute;
        } else {
            return false;
        }
    }
        private void showStrartTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                Appointment.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = hourOfDay + ":00"; // Set minutes to 00
                        if (validateTime(selectedTime)) {
                            startTime.setText(selectedTime);
                        } else {
                            Toast.makeText(Appointment.this, "Please select time between 7:00 AM and 7:00 PM", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                hour,
                0, // Set minutes to 0
                false
        );

        timePickerDialog.show();
    }
    private void showEndTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                Appointment.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = hourOfDay + ":00"; // Set minutes to 00
                        if (validateTime(selectedTime)) {
                            endTime.setText(selectedTime);
                        } else {
                            Toast.makeText(Appointment.this, "Please select time between 7:00 AM and 7:00 PM", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                hour,
                0, // Set minutes to 0
                false
        );

        timePickerDialog.show();
    }
    private double calculateDurationHours(String startTime, String endTime) {
        String[] startTimeParts = startTime.split(":");
        String[] endTimeParts = endTime.split(":");

        int startHour = Integer.parseInt(startTimeParts[0]);
        int startMinute = Integer.parseInt(startTimeParts[1]);

        int endHour = Integer.parseInt(endTimeParts[0]);
        int endMinute = Integer.parseInt(endTimeParts[1]);

        int totalStartMinutes = startHour * 60 + startMinute;
        int totalEndMinutes = endHour * 60 + endMinute;

        return (totalEndMinutes - totalStartMinutes) / 60.0;
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