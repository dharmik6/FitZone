package com.example.fitzone.trainer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fitzone.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ApprovedTrainerProfile extends AppCompatActivity {

    ImageView trainer_img_txt;
    Button book_btn_registration;
    TextView Functional_Strength_txt,trainer_name_txt,trBio,chargeTx,trainer_review_txt,review_show_allkaku,trainer_eee_txt,kalu;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_trainer_profile);

        trainer_img_txt=findViewById(R.id.trainer_img_txt);
        book_btn_registration=findViewById(R.id.book_btn_registration);
        Functional_Strength_txt=findViewById(R.id.Functional_Strength_txt);
        trainer_name_txt=findViewById(R.id.trainer_name_txt);
        chargeTx=findViewById(R.id.charge);
        trBio=findViewById(R.id.tr_bio);
        trainer_review_txt=findViewById(R.id.trainer_review_txt);
        review_show_allkaku=findViewById(R.id.review_show_allkaku);
        trainer_eee_txt=findViewById(R.id.trainer_eee_txt);
        kalu=findViewById(R.id.kalu);

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
                String charge = documentSnapshot.getString("charge");
                String bio = documentSnapshot.getString("bio");
                String treid1 = documentSnapshot.getId();
//                 Check if the userNameFromIntent matches the user

                if (memberid.equals(namee)) {
                    // Display the data only if they match
                    trainer_name_txt.setText(namee != null ? namee : "No name");
                    trainer_eee_txt.setText(experience != null ? experience : "No experience");
                    Functional_Strength_txt.setText(name != null ? name : "Non specialist");
                    trBio.setText(bio != null ? bio : "No bio");
//                    trainer_review_txt.setText(specializatione != null ? specializatione : "No review");
                    chargeTx.setText(charge != null ? charge : "No price");
                    kalu.setText(treid1 != null ? treid1 : "00.00");

                    if (imagee != null) {
                        Glide.with(ApprovedTrainerProfile.this)
                                .load(imagee)
                                .into(trainer_img_txt);
                    }
                }
                String trid=kalu.getText().toString().trim();
                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                db2.collection("trainers").document(trid).collection("trainers_review").get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                    for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots1) {
                        String rating = documentSnapshot1.getString("rating");
                        String review = documentSnapshot1.getString("review");
                        trainer_review_txt.setText(rating != null ? rating : "No name");
                    }
                });

                review_show_allkaku.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(ApprovedTrainerProfile.this, Review.class);
                        intent1.putExtra("treid", trid);
                        startActivity(intent1);
                    }
                });
            }
        });
//
//        String treid=kalu.getText().toString().trim();

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
                intent1.putExtra("charge", chargeTx.getText().toString());
                startActivity(intent1);
            }
        });

    }
}