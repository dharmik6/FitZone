package com.example.fitzone.booking;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitzone.R;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<BookingItemList>bookingList;
    private Context context;

    // Constructor
    public BookingAdapter(Context context, List<BookingItemList> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_list_item, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingItemList booking = bookingList.get(position);
        holder.nameTextView.setText(booking.getName());
        holder.splecializTextView.setText(booking.getEmail());
        holder.statusTextView.setText(booking.getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    BookingItemList item = bookingList.get(position);

                    // Create an intent to start the AcceptedBookingDetail activity
                    Intent intent = new Intent(context, BookingDetail.class);
                    // Pass data to the intent
                    intent.putExtra("email", item.getEmail());
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
        return bookingList.size();
    }

    // ViewHolder class
    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        // Declare views here
        TextView nameTextView;
        TextView splecializTextView;
        TextView statusTextView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views here
            nameTextView = itemView.findViewById(R.id.tr_name);
            splecializTextView = itemView.findViewById(R.id.sp_name);
            statusTextView = itemView.findViewById(R.id.book_status);
        }
    }
}