package com.example.fitzone.membership;

        import android.content.Context;
        import android.content.Intent;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.core.content.ContextCompat;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.fitzone.R;

        import java.util.List;

public class MembershipAdapter extends RecyclerView.Adapter<MembershipAdapter.MembershipViewHolder> {
    private List<MembershipItemList> membershipList;
    private Context context;

    // Constructor
    public MembershipAdapter(Context context, List<MembershipItemList> membershipList) {
        this.context = context;
        this.membershipList = membershipList;
    }

    @NonNull
    @Override
    public MembershipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.membership_list_item, parent, false);
        return new MembershipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MembershipViewHolder holder, int position) {
        MembershipItemList membership = membershipList.get(position);
        holder.nameTextView.setText(membership.getName());
        holder.typeTextView.setText(membership.getTime());
        holder.statusTextView.setText(membership.getStatus());

        // Set text color based on status
        if (membership.getStatus().equalsIgnoreCase("active")) {
            holder.statusTextView.setTextColor(ContextCompat.getColor(context,R.color.dark_cyan)); // Set your desired color for active status
        } else {
            holder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.notactive)); // Set your desired color for other statuses
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MembershipItemList item = membershipList.get(position);

                    // Create an intent to start the MembershipDetail activity
                    Intent intent = new Intent(context, Membership.class);
                    // Pass data to the intent
                    intent.putExtra("time", item.getTime());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("status", item.getStatus());
                    intent.putExtra("id", item.getId());
                    Log.d("id adapter", item.getId());
                    // Start the activity
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return membershipList.size();
    }

    // ViewHolder class
    public static class MembershipViewHolder extends RecyclerView.ViewHolder {
        // Declare views here
        TextView nameTextView;
        TextView typeTextView;
        TextView statusTextView;

        public MembershipViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views here
            nameTextView = itemView.findViewById(R.id.package_name);
            typeTextView = itemView.findViewById(R.id.package_time);
            statusTextView = itemView.findViewById(R.id.package_status);
        }
    }
}
