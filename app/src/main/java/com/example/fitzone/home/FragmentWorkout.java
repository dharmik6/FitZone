package com.example.fitzone.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fitzone.R;


public class FragmentWorkout extends Fragment {

    private RecyclerView workoutList;
    private TextView emptyView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_workout, container, false);

        workoutList = view.findViewById(R.id.workout_list);
        emptyView = view.findViewById(R.id.empty_text_view);

// ...

        if (workoutList.getAdapter() != null && workoutList.getAdapter().getItemCount() == 0) {
            workoutList.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            workoutList.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        return view;
    }
}