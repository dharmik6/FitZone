package com.example.fitzone.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fitzone.CreateWorkoutPlan;
import com.example.fitzone.ExercisesAdapter;
import com.example.fitzone.ExercisesItemList;
import com.example.fitzone.MyWorkoutPlansList;
import com.example.fitzone.R;
import com.example.fitzone.WorkoutPlan;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class FragmentWorkout extends Fragment {

    private RecyclerView workoutList;
    private TextView emptyView;
CardView active_card;
    private WorkoutHomeAdapter exeadapter;
    private List<ExercisesItemList> exeLists;

    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_workout, container, false);

        workoutList = view.findViewById(R.id.workout_list);
        active_card = view.findViewById(R.id.active_card);

//        emptyView = view.findViewById(R.id.empty_text_view);

//        emptyView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), WorkoutPlan.class);
//                startActivity(intent);
//            }
//        });

        // Set horizontal layout manager
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        workoutList.setLayoutManager(layoutManager2);

        exeLists = new ArrayList<>();
        exeadapter = new WorkoutHomeAdapter(getContext(), exeLists);
        workoutList.setAdapter(exeadapter);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseFirestore db2 = FirebaseFirestore.getInstance();
        db2.collection("workout_plans").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String description = documentSnapshot.getString("goal");
                String image = documentSnapshot.getString("image");
                String wid = documentSnapshot.getId();
                ExercisesItemList diet = new ExercisesItemList(name, description, image,wid);
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
        active_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getContext(), MyWorkoutPlansList.class);
                startActivity(intent1);
            }
        });
        return view;
    }
}