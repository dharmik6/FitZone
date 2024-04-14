package com.example.fitzone.trainer;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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

public class TrainerReviewAdapter extends RecyclerView.Adapter<TrainerReviewAdapter.ViewHolder> {
    private List<TrainerReviewList> trainersLists;
    Context context;

    public TrainerReviewAdapter(Context context, List<TrainerReviewList> trainersLists){
        this.trainersLists = trainersLists;
        this.context=context;

    }

    @NonNull
    @Override
    public TrainerReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tra = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_review_list_item, parent, false);
        return new TrainerReviewAdapter.ViewHolder(tra);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainerReviewAdapter.ViewHolder holder, int position) {
        TrainerReviewList member = trainersLists.get(position);
        holder.review_show.setText(member.getReview());
        holder.rating_show.setText(member.getRating());
        holder.name_of_review.setText(member.getName());
        if (context != null) {
            // Load image into CircleImageView using Glide library
            Glide.with(context)
                    .load(member.getImage()) // Assuming getImage() returns the URL of the image
                    .apply(RequestOptions.circleCropTransform()) // Apply circle crop transformation for CircleImageView
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache image to disk
                    .into(holder.review_image); // Load image into CircleImageView
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    TrainerReviewList item = trainersLists.get(position);

                    // Create an intent to start the MembersProfile activity
//                    Intent intent = new Intent(context, ApprovedTrainerProfile.class);
                    // Pass data to the intent
//                    intent.putExtra("image", item.getTimage());
//                    intent.putExtra("name", item.getTname());
//                    intent.putExtra("specialization", item.getSpecialization());
//                    intent.putExtra("experience", item.getExperience());

                    // Start the activity
//                    context.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return trainersLists.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView review_show;
        public TextView rating_show;
        public TextView name_of_review;
        public CircleImageView review_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            review_show = itemView.findViewById(R.id.review_show);
            rating_show = itemView.findViewById(R.id.rating_show);
            name_of_review = itemView.findViewById(R.id.name_of_review);
            review_image = itemView.findViewById(R.id.review_image);
        }
    }
}

