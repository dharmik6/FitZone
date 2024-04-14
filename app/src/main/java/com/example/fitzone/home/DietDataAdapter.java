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
import com.bumptech.glide.request.RequestOptions;
import com.example.fitzone.R;
import com.example.fitzone.diet.Diet;
import com.example.fitzone.diet.DietItemList;

import java.util.List;

public class DietDataAdapter extends RecyclerView.Adapter<DietDataAdapter.ViewHolder> {
    private List<DietItemList> dietLists;
    Context context;

    public DietDataAdapter(Context context, List<DietItemList> dietLists) {
        this.dietLists = dietLists;
        this.context = context;

    }

    @NonNull
    @Override
    public DietDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tra = LayoutInflater.from(parent.getContext()).inflate(R.layout.diet_list_item, parent, false);
        return new DietDataAdapter.ViewHolder(tra);
    }

    @Override
    public void onBindViewHolder(@NonNull DietDataAdapter.ViewHolder holder, int position) {
        DietItemList member = dietLists.get(position);
        holder.dietname.setText(member.getName());
//        holder.textexperience.setText(member.getDescription());

        // Check if the context is not null before loading the image
        if (context != null) {
            // Load image into CircleImageView using Glide library
            Glide.with(context)
                    .load(member.getImageUrl()) // Assuming getImage() returns the URL of the image
//                    .apply(RequestOptions.circleCropTransform()) // Apply circle crop transformation for CircleImageView
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache image to disk
                    .into(holder.dietimage); // Load image into CircleImageView
        }

        // Get the context from the parent view
        final Context context = holder.itemView.getContext();
        // Set OnClickListener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    DietItemList item = dietLists.get(position);

                    // Create an intent to start the MembersProfile activity
                    Intent intent = new Intent(context, Diet.class);
                    // Pass data to the intent
                    intent.putExtra("imageUrl", item.getImageUrl());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("Description", item.getDescription());

                    // Start the activity
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dietLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dietname;
        public ImageView dietimage;

        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dietname = itemView.findViewById(R.id.diet_name);
            dietimage = itemView.findViewById(R.id.diet_image);
        }
    }
}
