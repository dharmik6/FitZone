package com.example.fitzone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.ViewHolder> {
    private List<PackagesItemList> dietLists;
    Context context;

    public PackagesAdapter(Context context, List<PackagesItemList> dietLists) {
        this.dietLists = dietLists;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View tra = LayoutInflater.from(parent.getContext()).inflate(R.layout.packages_list_item, parent, false);
        return new ViewHolder(tra);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PackagesItemList member = dietLists.get(position);
        holder.dietname.setText(member.getName());
        holder.price.setText(member.getPrice());
        holder.duration.setText(member.getDuration());
        holder.description.setText(member.getDescription());



        // Get the context from the parent view
        final Context context = holder.itemView.getContext();
        // Set OnClickListener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    PackagesItemList item = dietLists.get(position);

                    // Create an intent to start the MembersProfile activity
                    Intent intent = new Intent(context, Packages.class);
                    // Pass data to the intent
                    intent.putExtra("name", item.getName());
                    intent.putExtra("price", item.getPrice());
                    intent.putExtra("duration", item.getDuration());
                    intent.putExtra("description", item.getDescription());

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
        public TextView price;
        public TextView duration;
        public TextView description;

        @SuppressLint("WrongViewCast")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dietname = itemView.findViewById(R.id.pac_name);
            price = itemView.findViewById(R.id.pac_pri);
            duration = itemView.findViewById(R.id.pac_mon);
            description = itemView.findViewById(R.id.pac_des);
        }
    }
}
