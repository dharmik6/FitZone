package com.example.fitzone.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.fitzone.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNav ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the UID from the Intent extras
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");

        // hooks
        bottomNav = findViewById(R.id.bottom_nav_bar);
        bottomNav.setSelectedItemId(R.id.home);
        bottomNav.setItemIconTintList(null);
        loadFragment(new FragmentHome(),uid, true);
        bottomNav.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    loadFragment(new FragmentHome(),uid, false);
                } else if (itemId == R.id.repot) {
                    loadFragment(new FragmentReports(),uid, false);
                } else if (itemId == R.id.workout) {
                    loadFragment(new FragmentWorkout(),uid, false);
                } else if (itemId == R.id.account) {
                    loadFragment(new FragmentAccount(),uid, false);
                }
                return true;
            }
        });

        // greeting to user //


    }
    public void loadFragment(Fragment fragment, String uid, boolean flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag)
            ft.add(R.id.frag_container, fragment);
        else
            ft.replace(R.id.frag_container, fragment);

        // Pass the UID to the fragment using arguments
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        fragment.setArguments(bundle);

        ft.commit();
    }
}