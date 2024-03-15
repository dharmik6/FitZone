package com.example.fitzone.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.example.fitzone.FragmentHeightBottomSheet;
import com.example.fitzone.FragmentWeightBottomSheet;
import com.example.fitzone.Profile;
import com.example.fitzone.R;
import com.example.fitzone.UpdateProfile;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentReports#} factory method to
 * create an instance of this fragment.
 */
public class FragmentReports extends Fragment {
    TextView show_Weight, show_Height;
    LineChart lineChart;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        show_Weight = view.findViewById(R.id.show_Weight);
        show_Height = view.findViewById(R.id.show_Height);
        lineChart = view.findViewById(R.id.lineChart);

        // Add click listener to edit_weight ImageView
        ImageView editWeightImageView = view.findViewById(R.id.edit_weight);
        editWeightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show FragmentWeightBottomSheet when edit_weight is clicked
                FragmentWeightBottomSheet bottomSheet = new FragmentWeightBottomSheet();
                bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
            }
        });

        // Add click listener to edit_weight ImageView
        ImageView editHeightImageView = view.findViewById(R.id.edit_height);
        editHeightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show FragmentWeightBottomSheet when edit_weight is clicked
                FragmentHeightBottomSheet bottomSheet = new FragmentHeightBottomSheet();
                bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
            }
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String memberweight = documentSnapshot.getString("weight");
                    String memberheight = documentSnapshot.getString("height");

                    show_Height.setText(memberheight != null ? memberheight : "No height");

                    // Get the weight and height as strings from TextViews
                    String weightStr = memberweight;
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

                    // chart code
                    // Create a list of entries representing the data points on the chart
                    List<Entry> entries = new ArrayList<>();
                    entries.add(new Entry(10, 88));
                    entries.add(new Entry(11, 85));
                    entries.add(new Entry(13, 78));
                    entries.add(new Entry(15, Float.parseFloat(memberweight)));

                    // Create a dataset from the entries
                    LineDataSet dataSet = new LineDataSet(entries, "Label for the dataset");
                    dataSet.setColor(Color.BLUE); // Set the color of the line
                    dataSet.setValueTextColor(Color.RED); // Set the color of the values
                    dataSet.setLineWidth(2f); // Set the width of the line

                    // Create a LineData object with the dataset
                    LineData lineData = new LineData(dataSet);

                    // Set the data to the chart
                    lineChart.setData(lineData);

                    // Customize the appearance of the chart
                    lineChart.getDescription().setEnabled(false); // Disable description
                    lineChart.setTouchEnabled(true); // Enable touch gestures
                    lineChart.setDragEnabled(true); // Enable drag and drop gestures
                    lineChart.setScaleEnabled(true); // Enable scaling gestures
                    lineChart.setPinchZoom(true); // Enable pinch zoom
                    lineChart.setDrawGridBackground(false); // Disable grid background

                    // Customize the X axis
                    XAxis xAxis = lineChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Position at the bottom
                    xAxis.setGranularity(1f); // Interval between each X axis value

                    // Customize the Y axis
                    YAxis yAxis = lineChart.getAxisLeft();
                    yAxis.setGranularity(5f); // Interval between each Y axis value

                    // Disable the right Y axis
                    lineChart.getAxisRight().setEnabled(false);

                    // Invalidate the chart to refresh
                    lineChart.invalidate();

                }
            });
        }

        return view;
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