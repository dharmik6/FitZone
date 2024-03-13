package com.example.fitzone.home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitzone.ExercisesAdapter;
import com.example.fitzone.ExercisesItemList;
import com.example.fitzone.R;
import com.example.fitzone.WorkoutItemList;
import com.example.fitzone.WorkoutPlan;
import com.example.fitzone.WorkoutPlanAdapter;
import com.example.fitzone.diet.DietAdapter;
import com.example.fitzone.diet.DietItemList;
import com.example.fitzone.diet.DietList;
import com.example.fitzone.trainer.ApprovedTrainerProfile;
import com.example.fitzone.trainer.TrainerApprovedList;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {
    TextView diet_more,view_exe,workout_mor;
    RecyclerView diet_data_recyc,wor_recyc,exercises_recyc;
    CardView trainers_all_list;
    private DietDataAdapter adapter;
    private ExercisesAdapter exeadapter;
    private List<ExercisesItemList> exeLists;
    private WorkoutPlanAdapter Woradapter;
    private List<DietItemList> dietLists;
    private List<WorkoutItemList> worLists;
    private ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        trainers_all_list=view.findViewById(R.id.trainers_all_list);
        trainers_all_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TrainerApprovedList.class));
            }
        });


        // Workout List
        wor_recyc = view.findViewById(R.id.wor_recyc);
        workout_mor = view.findViewById(R.id.workout_mor);
        workout_mor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WorkoutPlan.class);
                startActivity(intent);
            }
        });

        // Set horizontal layout manager
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        wor_recyc.setLayoutManager(layoutManager1);

        worLists = new ArrayList<>();
        Woradapter = new WorkoutPlanAdapter(getContext(), worLists);
        wor_recyc.setAdapter(Woradapter);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseFirestore dworb = FirebaseFirestore.getInstance();
        dworb.collection("exercises").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String body = documentSnapshot.getString("body");
                String image = documentSnapshot.getString("imageUrl");
                WorkoutItemList diet = new WorkoutItemList(name, body, image);
                worLists.add(diet);
            }

            Woradapter.notifyDataSetChanged();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        });

        // Diet List

        diet_data_recyc = view.findViewById(R.id.diet_data_recyc);
        diet_more = view.findViewById(R.id.diet_more);
        diet_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DietList.class);
                startActivity(intent);
            }
        });

        // Set horizontal layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        diet_data_recyc.setLayoutManager(layoutManager);

        dietLists = new ArrayList<>();
        adapter = new DietDataAdapter(getContext(), dietLists);
        diet_data_recyc.setAdapter(adapter);

//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("diets").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String description = documentSnapshot.getString("description");
                String image = documentSnapshot.getString("imageUrl");
                DietItemList diet = new DietItemList(name, description, image);
                dietLists.add(diet);
            }

            adapter.notifyDataSetChanged();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        });


        // Exercises List

        exercises_recyc = view.findViewById(R.id.exercises_recyc);
        view_exe = view.findViewById(R.id.view_exe);
        view_exe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WorkoutPlan.class);
                startActivity(intent);
            }
        });

        // Set horizontal layout manager
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        exercises_recyc.setLayoutManager(layoutManager2);

        exeLists = new ArrayList<>();
        exeadapter = new ExercisesAdapter(getContext(), exeLists);
        exercises_recyc.setAdapter(exeadapter);

//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        db2.collection("workout_plans").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String description = documentSnapshot.getString("goal");
                String image = documentSnapshot.getString("image");
                ExercisesItemList diet = new ExercisesItemList(name, description, image);
                exeLists.add(diet);
            }

            exeadapter.notifyDataSetChanged();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        });
        return view;
    }
}
