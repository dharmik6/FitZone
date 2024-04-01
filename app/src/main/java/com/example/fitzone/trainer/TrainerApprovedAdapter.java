package com.example.fitzone.trainer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.fitzone.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TrainerApprovedAdapter extends RecyclerView.Adapter<TrainerApprovedAdapter.ViewHolder> {
    private List<TrainerApprovedItemList> trainersLists;
    Context context;

    public TrainerApprovedAdapter(Context context, List<TrainerApprovedItemList> trainersLists){
        this.trainersLists = trainersLists;
        this.context=context;

    }

    @NonNull
    @Override
    public TrainerApprovedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tra = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainer_approved_list_item, parent, false);
        return new TrainerApprovedAdapter.ViewHolder(tra);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerApprovedAdapter.ViewHolder holder, int position) {
        TrainerApprovedItemList member = trainersLists.get(position);
        holder.textTname.setText(member.getTname());
        holder.textexperience.setText(member.getExperience());
        holder.textspecialization.setText(member.getSpecialization());
//        holder.textreview.setText(member.getReview());

        // Check if the context is not null before loading the image
        if (context != null) {
            // Load image into CircleImageView using Glide library
            Glide.with(context)
                    .load(member.getTimage()) // Assuming getImage() returns the URL of the image
                    .apply(RequestOptions.circleCropTransform()) // Apply circle crop transformation for CircleImageView
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache image to disk
                    .into(holder.textTimage); // Load image into CircleImageView
        }

        // Get the context from the parent view
        final Context context = holder.itemView.getContext();
        // Set OnClickListener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    TrainerApprovedItemList item = trainersLists.get(position);

                    // Create an intent to start the MembersProfile activity
                    Intent intent = new Intent(context, ApprovedTrainerProfile.class);
                    // Pass data to the intent
                    intent.putExtra("image", item.getTimage());
                    intent.putExtra("name", item.getTname());
                    intent.putExtra("specialization", item.getSpecialization());
                    intent.putExtra("experience", item.getExperience());
                    intent.putExtra("id", item.getId());

                    // Start the activity
                    context.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return trainersLists.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textTname;
        public CircleImageView textTimage;
        public TextView textspecialization;
        public TextView textexperience;
        public TextView textreview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTname = itemView.findViewById(R.id.tra_name);
            textexperience = itemView.findViewById(R.id.tra_experience);
            textspecialization = itemView.findViewById(R.id.tra_specialization);
            textTimage = itemView.findViewById(R.id.tra_image);
//            textreview = itemView.findViewById(R.id.tra_review);
        }
    }
}
