package com.example.fitzone;

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
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MyWorkoutPlansAdapter extends RecyclerView.Adapter<MyWorkoutPlansAdapter.ViewHolder> {
    private List<MyWorkoutPlansItemList> exercisesItemLists;
    Context context;

    public MyWorkoutPlansAdapter(Context context, List<MyWorkoutPlansItemList> exercisesItemLists) {
        this.exercisesItemLists = exercisesItemLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_workout_plans_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyWorkoutPlansItemList member = exercisesItemLists.get(position);
        holder.exename.setText(member.getName());
        holder.exebody.setText(member.getGoal());

        if (context != null) {
            Glide.with(context)
                    .load(member.getImage())
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.exeimage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MyWorkoutPlansItemList item = exercisesItemLists.get(position);
                    // Handle item click event
                    Intent intent = new Intent(context, WorkoutPlans.class);
                    // Pass data to the intent
                    intent.putExtra("image", item.getImage());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("body", item.getGoal());
                    intent.putExtra("id", item.getId());

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
            exename = itemView.findViewById(R.id.myworkout_name);
            exebody = itemView.findViewById(R.id.workout_exe);
            exeimage = itemView.findViewById(R.id.img_mywor);
        }
    }
}
