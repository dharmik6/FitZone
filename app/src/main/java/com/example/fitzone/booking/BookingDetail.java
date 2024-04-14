package com.example.fitzone.booking;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.fitzone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BookingDetail extends AppCompatActivity {
    // Define permissions request code
    private static final int PERMISSION_REQUEST_CODE = 101;

    // Other variables and views
    private boolean isPdfCreated = false;
    private ProgressDialog progressDialog;
    private FirebaseFirestore db;

    private AppCompatTextView booking_id, trainer_name, booking_date, start_time, end_time, book_status, pay_id, amounT, date, member_name;
    private TextView invoice;

    ImageView back ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false); // Prevent dismissal by tapping outside

        // Find views
        booking_id = findViewById(R.id.booking_id);
        trainer_name = findViewById(R.id.trainer_id);
        booking_date = findViewById(R.id.booking_date);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        book_status = findViewById(R.id.book_status);
        pay_id = findViewById(R.id.pay_id);
        amounT = findViewById(R.id.amount);
        date = findViewById(R.id.date);
        member_name = findViewById(R.id.member_name);
        invoice = findViewById(R.id.invoice);

        // Check for permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }

        // Set click listener for the invoice TextView
        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the booking status is confirmed
                if (book_status.getText().toString().equals("confirmed")) {
                    // If confirmed, proceed to generate or open PDF
                    if (isPdfCreated) {
                        openPdf();
                    } else {
                        generatePDF();
                    }
                } else {

                    // If not confirmed, show a message indicating that the invoice cannot be downloaded
                    Toast.makeText(BookingDetail.this, "Invoice can only be downloaded for confirmed bookings.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    // Your initialization code
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String status = intent.getStringExtra("status");

        booking_id.setText(id);
        member_name.setText(name);
        book_status.setText(status);

        db = FirebaseFirestore.getInstance();

        // Show ProgressDialog
        progressDialog.show();

        db.collection("bookings").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        // Dismiss ProgressDialog when data fetching is complete
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve additional data and set them to respective TextViews
                                String bookingDate = document.getString("bookingDate");
                                String startTime = document.getString("startTime");
                                String endTime = document.getString("endTime");
                                String trainerId = document.getString("treinerId");
                                String payId = document.getString("paymentId");
                                String amount = document.getString("charge");

                                db.collection("trainers")
                                        .document(trainerId)
                                        .get()
                                        .addOnSuccessListener(userDocumentSnapshot -> {
                                            // Retrieve user details
                                            String name = userDocumentSnapshot.getString("name");

                                            trainer_name.setText(name != null ? name : "No name");

                                        })
                                        .addOnFailureListener(e -> {
                                            // Handle failure to fetch user details
                                        });

                                booking_date.setText(bookingDate != null ? bookingDate : "No date");
                                date.setText(bookingDate != null ? bookingDate : "No date");
                                start_time.setText(startTime != null ? startTime : "No date");
                                end_time.setText(endTime != null ? endTime : "No date");
                                end_time.setText(endTime != null ? endTime : "No date");
                                pay_id.setText(payId != null ? payId : "No payment id");
                                amounT.setText(amount != null ? amount : "No amount");

                            } else {
                                // Handle the case where the document does not exist
                            }

                        } else {
                            // Handle exceptions while fetching data from Firestore
                        }
                    }
                });
    }

    // Method to create PDF
    private void generatePDF() {
        // Retrieve booking ID
        String bookingId = booking_id.getText().toString();

        // Define PDF file name based on booking ID
        String pdfFileName = bookingId + ".pdf";

        // Create a new PdfDocument
        PdfDocument document = new PdfDocument();

        // Create a page info with the page attributes such as size and orientation
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size page in portrait mode

        // Start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // Get the canvas for drawing
        Canvas canvas = page.getCanvas();

        // Create a paint object for drawing
        Paint paint = new Paint();

        // Draw background color
        paint.setColor(ContextCompat.getColor(this, R.color.dark_cyan));
        canvas.drawRect(0, 0, canvas.getWidth(), 120, paint); // Assuming the company name area height is 120, adjust as needed

        // Set text color and size for company name
        paint.setColor(Color.WHITE);
        paint.setTextSize(30); // Increased font size to 30

        // Define company name
        String companyName = "FitZone";

        // Calculate text position for company name
        float textWidth = paint.measureText(companyName);
        float x = (canvas.getWidth() - textWidth) / 2; // Center the text horizontally
        float y = 80; // Adjust vertically as needed

        // Draw company name
        canvas.drawText(companyName, x, y, paint);

        // Set text color and size for booking details heading
        paint.setTextSize(24); // Set font size for booking details heading
        float headingX = (canvas.getWidth() - paint.measureText("Booking Details")) / 2; // Center the heading horizontally
        float headingY = 160; // Adjust vertically as needed

        // Draw booking details heading
        canvas.drawText("Booking Details", headingX, headingY, paint);

        // Draw horizontal separator line below the company name
        paint.setColor(Color.WHITE);
        canvas.drawLine(50, 120, canvas.getWidth() - 50, 120, paint); // Assuming separator line starts at x = 50 and ends at width - 50

        // Draw horizontal separator line below the booking details heading
        paint.setColor(Color.BLACK); // Set color back to black for drawing other content
        canvas.drawLine(50, 200, canvas.getWidth() - 50, 200, paint); // Assuming separator line starts at x = 50 and ends at width - 50

        // Create a paint object for drawing other content
        paint.setTextSize(12);

        // Define text content (booking details)
        String memberName = "Member Name: " + member_name.getText().toString();
        String bookingDate = "Booking Date: " + booking_date.getText().toString();
        String startDate = "Start Time: " + start_time.getText().toString();
        String endDate = "End Time: " + end_time.getText().toString();
        String bookStatus = "Booking Status: " + book_status.getText().toString();
        String paymentId = "Payment ID: " + pay_id.getText().toString();
        String amount = "Amount: " + amounT.getText().toString();
        String dateText = "Date: " + date.getText().toString();

        // Set up the layout
        int startX = 50;
        int startY = 220; // Start drawing content below the booking details heading
        int lineSpacing = 30;

        // Draw content
        canvas.drawText(memberName, startX, startY, paint);
        startY += lineSpacing;
        canvas.drawText(bookingDate, startX, startY, paint);
        startY += lineSpacing;
        canvas.drawText(startDate, startX, startY, paint);
        startY += lineSpacing;
        canvas.drawText(endDate, startX, startY, paint);
        startY += lineSpacing;
        canvas.drawText(bookStatus, startX, startY, paint);
        startY += lineSpacing;
        canvas.drawText(paymentId, startX, startY, paint);
        startY += lineSpacing;
        canvas.drawText(amount, startX, startY, paint);
        startY += lineSpacing;
        canvas.drawText(dateText, startX, startY, paint);

        // Finish the page
        document.finishPage(page);

        // Define the file where the PDF will be saved
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pdfFileName);

        try {
            // Write the document content to the file
            document.writeTo(new FileOutputStream(file));

            // Close the document
            document.close();

            // Set isPdfCreated to true
            isPdfCreated = true;
            Toast.makeText(this, "PDF saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

            // Open the PDF
            openPdf();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error creating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Method to open PDF
    private void openPdf() {
        // Retrieve booking ID
        String bookingId = booking_id.getText().toString();

        // Define PDF file name based on booking ID
        String pdfFileName = bookingId + ".pdf";

        // Get the file path
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pdfFileName);

        // Check if the file exists
        if (file.exists()) {
            // Toast the path of the PDF file
            Toast.makeText(this, "PDF downloaded at: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

            // Open the PDF using a FileProvider
            Uri pdfUri = FileProvider.getUriForFile(this, "com.example.fitzone.provider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant permission to read the file
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } else {
            // Show a message if the file does not exist
            Toast.makeText(this, "PDF not found", Toast.LENGTH_SHORT).show();
        }
    }

    // Override onRequestPermissionsResult to handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with functionality that requires it
            } else {
                // Permission denied, show message or handle it gracefully
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
