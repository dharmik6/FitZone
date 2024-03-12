package com.example.fitzone.trainer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fitzone.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ApprovedTrainerProfile extends AppCompatActivity {

    ImageView trainer_img_txt;
    Button book_btn_registration;
    TextView Functional_Strength_txt,trainer_name_txt,trainer_pay_txt,trainer_review_txt,review_show_all,trainer_eee_txt;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_trainer_profile);
        trainer_img_txt=findViewById(R.id.trainer_img_txt);
        book_btn_registration=findViewById(R.id.book_btn_registration);
        Functional_Strength_txt=findViewById(R.id.Functional_Strength_txt);
        trainer_name_txt=findViewById(R.id.trainer_name_txt);
        trainer_pay_txt=findViewById(R.id.trainer_pay_txt);
        trainer_review_txt=findViewById(R.id.trainer_review_txt);
        review_show_all=findViewById(R.id.review_show_all);
        trainer_eee_txt=findViewById(R.id.trainer_eee_txt);

        Intent intent = getIntent();
        String memberid = intent.getStringExtra("name");

        // Query Firestore for data
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("trainers").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String namee = documentSnapshot.getString("name");
                String name = documentSnapshot.getString("specialization");
                String specializatione = documentSnapshot.getString("review");
                String emaile = documentSnapshot.getString("pay");
                String imagee = documentSnapshot.getString("image");
                String experience = documentSnapshot.getString("experience");

//                 Check if the userNameFromIntent matches the user
                if (memberid.equals(namee)) {
                    // Display the data only if they match
                    trainer_name_txt.setText(namee != null ? namee : "No name");
                    trainer_eee_txt.setText(experience != null ? experience : "No name");
                    Functional_Strength_txt.setText(name != null ? name : "No name");
                    trainer_review_txt.setText(specializatione != null ? specializatione : "No specialization");
                    trainer_pay_txt.setText(emaile != null ? emaile : "No email");
                    if (imagee != null) {
                        Glide.with(ApprovedTrainerProfile.this)
                                .load(imagee)
                                .into(trainer_img_txt);
                    }
                } else {
                    // userNameFromIntent and user don't match, handle accordingly
//                    showToast("User data does not match the intent.");
                }
            }
        });

        book_btn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ApprovedTrainerProfile.this, Appointment.class);
                // Use different keys for each putExtra statement
                intent1.putExtra("trainer_name", trainer_name_txt.getText().toString());
//                intent1.putExtra("trainer_img", trainer_img_txt.getText().toString());
                intent1.putExtra("trainer_review", trainer_review_txt.getText().toString());
                intent1.putExtra("Functional_Strength", Functional_Strength_txt.getText().toString());
                intent1.putExtra("trainer_eee_txt", trainer_eee_txt.getText().toString());
                startActivity(intent1);
            }
        });

        review_show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApprovedTrainerProfile.this,Review.class));
            }
        });
    }
}