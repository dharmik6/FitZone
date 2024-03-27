package com.example.fitzone.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.example.fitzone.FragmentHeightBottomSheet;
import com.example.fitzone.FragmentWeightBottomSheet;
import com.example.fitzone.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentReports extends Fragment {
    TextView show_Weight, show_Height,tvlightest,tvHeaviest;
    LineChart lineChart;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        tvHeaviest = view.findViewById(R.id.tvHeaviest);
        tvlightest = view.findViewById(R.id.tvlightest);

        show_Weight = view.findViewById(R.id.show_Weight);
        show_Height = view.findViewById(R.id.show_Height);
        lineChart = view.findViewById(R.id.lineChart);

        ImageView editWeightImageView = view.findViewById(R.id.edit_weight);
        editWeightImageView.setOnClickListener(v -> {
            FragmentWeightBottomSheet bottomSheet = new FragmentWeightBottomSheet();
            bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
        });

        ImageView editHeightImageView = view.findViewById(R.id.edit_height);
        editHeightImageView.setOnClickListener(v -> {
            FragmentHeightBottomSheet bottomSheet = new FragmentHeightBottomSheet();
            bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String memberheight = documentSnapshot.getString("height");
                    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                    db1.collection("users").document(userId).collection("weight").get().addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Entry> entries = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots) {
                            String memberweight = documentSnapshot1.getString("weight");
                            String dateStr = documentSnapshot1.getString("date");

                            try {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM");
                                Date date = dateFormat.parse(dateStr); // Parse the date string

                                // Subtract one day's worth of milliseconds (24 hours * 60 minutes * 60 seconds * 1000 milliseconds)
                                long milliseconds = date.getTime() + (1 * 24 * 60 * 60 * 1000);

                                long days = milliseconds / (1000 * 60 * 60 * 24); // Convert milliseconds to days
                                float yValue = Float.parseFloat(memberweight); // Parse the weight value

                                // Create an Entry with xValue as days and yValue as weight
                                entries.add(new Entry(days, yValue));
                            } catch (ParseException e) {
                                e.printStackTrace(); // Handle the parse exception
                            }

                            show_Height.setText(memberheight != null ? memberheight : "No height");
                            show_Weight.setText(memberweight != null ? memberweight : "No weight");

                            // Get the weight and height as strings from TextViews
                            String weightStr = memberweight != null ? memberweight :  "No weight";
                            String heightStr = memberheight;

                            // Parse the strings to floats, if they are numeric
                            float weight = TextUtils.isDigitsOnly(weightStr) ? Float.parseFloat(weightStr) : 0;
                            float height = TextUtils.isDigitsOnly(heightStr) ? Float.parseFloat(heightStr) / 100 : 0; // Convert height to meters

                            // Calculate BMI
                            float bmi = calculateBMIValue(weight, height);

                            // Display the BMI
                            TextView tvResult = view.findViewById(R.id.totle_bmi_rep);
                            tvResult.setText("Your BMI : " + bmi);

                            // Setting up HalfGauge and CardView for BMI chart
                            HalfGauge halfGauge = view.findViewById(R.id.halfGauge);
                            CardView cardView = view.findViewById(R.id.tv_result);

                            double value = bmi; // Set your value here

                            // Setting up color ranges for HalfGauge
                            Range range1 = new Range();
                            range1.setColor(Color.parseColor("#2739C9"));
                            range1.setFrom(15);
                            range1.setTo(16);
                            halfGauge.addRange(range1);

                            Range range2 = new Range();
                            range2.setColor(Color.parseColor("#3977F0"));
                            range2.setFrom(16.0);
                            range2.setTo(18.5);
                            halfGauge.addRange(range2);

                            Range range3 = new Range();
                            range3.setColor(Color.parseColor("#5CC2D8"));
                            range3.setFrom(18.5);
                            range3.setTo(25);
                            halfGauge.addRange(range3);

                            Range range4 = new Range();
                            range4.setColor(Color.parseColor("#F7CC4A"));
                            range4.setFrom(25);
                            range4.setTo(30.0);
                            halfGauge.addRange(range4);

                            Range range5 = new Range();
                            range5.setColor(Color.parseColor("#F29837"));
                            range5.setFrom(30.0);
                            range5.setTo(35.0);
                            halfGauge.addRange(range5);

                            Range range6 = new Range();
                            range6.setColor(Color.parseColor("#D8313B"));
                            range6.setFrom(35.0);
                            range6.setTo(40.0);
                            halfGauge.addRange(range6);

                            // Setting up color ranges for CardView based on BMI value
                            int color;
                            String bmiCategory;
                            if (bmi < 16) {
                                color = Color.parseColor("#2739C9"); // Severely underweight
                                bmiCategory = "Severely underweight";
                            } else if (bmi < 18.5) {
                                color = Color.parseColor("#3977F0"); // Underweight
                                bmiCategory = "Underweight";
                            } else if (bmi < 25) {
                                color = Color.parseColor("#5CC2D8"); // Normal weight
                                bmiCategory = "Healthy weight";
                            } else if (bmi < 30) {
                                color = Color.parseColor("#F7CC4A"); // Overweight
                                bmiCategory = " Overweight";
                            } else if (bmi < 35) {
                                color = Color.parseColor("#F29837"); // Obese Class I
                                bmiCategory = "Moderately obese";
                            } else {
                                color = Color.parseColor("#D8313B"); // Obese Class II
                                bmiCategory = "Severely obese";
                            }

                            cardView.setCardBackgroundColor(color);
                            TextView valueText = view.findViewById(R.id.value_text);
                            valueText.setText(bmiCategory);

                            halfGauge.setMinValue(15);
                            halfGauge.setMaxValue(40.0);
                            halfGauge.setValue(value);
                        }
                        setChart(entries);
                    });
                    show_Height.setText(memberheight != null ? memberheight : "No height");
                }
            });
        }

        return view;
    }

    private void setChart(List<Entry> entries) {
        LineDataSet dataSet = new LineDataSet(entries, "Weight Entries");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.RED);
        dataSet.setLineWidth(2f);
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setDrawGridBackground(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Convert days to milliseconds
                long millis = (long) value * 24 * 60 * 60 * 1000;
                // Format date to exclude time
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM");
                return dateFormat.format(new Date(millis));
            }
        });
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setGranularity(5f);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.invalidate();
    }

    public static void redirectActivity(Activity activity, Class secondActivity) {
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    private float calculateBMIValue(float weight, float height) {
        return weight / (height * height);
    }
}
