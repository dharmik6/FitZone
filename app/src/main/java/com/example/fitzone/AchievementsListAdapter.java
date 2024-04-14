package com.example.fitzone;

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

import java.util.List;

public class AchievementsListAdapter extends RecyclerView.Adapter<AchievementsListAdapter.ViewHolder> {
    private List<AchievementsListItem> certificatesList;
    private Context context;

    public AchievementsListAdapter(Context context, List<AchievementsListItem> certificatesList) {
        this.context = context;
        this.certificatesList = certificatesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievements_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AchievementsListItem item = certificatesList.get(position);
        holder.name_of_review.setText(item.getName());
        Glide.with(context)
                .load(item.getImageUrl())
                .into(holder.image_show);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    AchievementsListItem selectedItem = certificatesList.get(position);
                    Intent intent=new Intent(context, Achievements.class);
                    intent.putExtra("id",selectedItem.getCerid());
                    intent.putExtra("name",selectedItem.getName());
                    intent.putExtra("description",selectedItem.getDescription());
                    intent.putExtra("image",selectedItem.getImageUrl());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return certificatesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_show;
        public TextView name_of_review;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_show = itemView.findViewById(R.id.Achievements_image);
            name_of_review = itemView.findViewById(R.id.Achievements_name);
        }
    }
}
