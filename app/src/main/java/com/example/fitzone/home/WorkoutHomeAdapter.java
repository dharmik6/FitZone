package com.example.fitzone.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fitzone.EditWorkoutPlanList;
import com.example.fitzone.ExercisesAdapter;
import com.example.fitzone.ExercisesItemList;
import com.example.fitzone.R;
import com.example.fitzone.WorkoutPlan;

import java.util.List;

public class WorkoutHomeAdapter  extends RecyclerView.Adapter<WorkoutHomeAdapter.ViewHolder> {
    private List<ExercisesItemList> exercisesItemLists;
    Context context;

    public WorkoutHomeAdapter(Context context, List<ExercisesItemList> exercisesItemLists) {
        this.exercisesItemLists = exercisesItemLists;
        this.context = context;

    }

    @NonNull
    @Override
    public WorkoutHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tra = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_workout_plans_home_list_item, parent, false);
        return new WorkoutHomeAdapter.ViewHolder(tra);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutHomeAdapter.ViewHolder holder, int position) {
        ExercisesItemList member = exercisesItemLists.get(position);
        holder.exename.setText(member.getName());
        holder.exebody.setText(member.getGoal());

        // Check if the context is not null before loading the image
        if (context != null) {
            // Load image into CircleImageView using Glide library
            Glide.with(context)
                    .load(member.getImage()) // Assuming getImage() returns the URL of the image
//                    .apply(RequestOptions.circleCropTransform()) // Apply circle crop transformation for CircleImageView
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache image to disk
                    .into(holder.exeimage); // Load image into CircleImageView
        }

        // Get the context from the parent view
        final Context context = holder.itemView.getContext();
        // Set OnClickListener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ExercisesItemList item = exercisesItemLists.get(position);

//                     Create an intent to start the MembersProfile activity
                    Intent intent = new Intent(context, EditWorkoutPlanList.class);
                    // Pass data to the intent
                    intent.putExtra("image", item.getImage());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("goal", item.getGoal());
                    intent.putExtra("wid", item.getWid());

                    // Start the activity
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return exercisesItemLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView exename;
        public TextView exebody;
        public ImageView exeimage;

        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            exename = itemView.findViewById(R.id.exercises_name);
            exebody = itemView.findViewById(R.id.exercises_alllist);
            exeimage = itemView.findViewById(R.id.exercises_image);
        }
    }
}
