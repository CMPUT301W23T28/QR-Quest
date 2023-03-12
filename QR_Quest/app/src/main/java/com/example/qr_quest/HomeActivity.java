package com.example.qr_quest;

import androidx.appcompat.app.AppCompatActivity;
        import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
        import android.view.MenuItem;


public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottonnav);

        bottomNavigationView.setOnItemSelectedListener(this);
        Intent intent = getIntent();
        boolean comingFromQRActivity = intent.getBooleanExtra("comingFromQRActivity", false);

        if (comingFromQRActivity) {
            // navigate to the ProfileFragment if the user is coming from QRActivity
            loadFragment(new ProfileFragment());
            MenuItem menuItem = bottomNavigationView.getMenu().findItem(R.id.profile);
            menuItem.setChecked(true);
        } else {
            // otherwise, load the default HomeFragment
            loadFragment(new HomeFragment());
            MenuItem menuItem = bottomNavigationView.getMenu().findItem(R.id.home);
            menuItem.setChecked(true);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.home:
                fragment = new HomeFragment();
                break;

            case R.id.leaderboard:
                fragment = new LeaderboardFragment();
                break;

            case R.id.profile:
                fragment = new ProfileFragment();
                break;

            case R.id.camera:
                intent = new Intent(HomeActivity.this, CameraActivity.class);
                startActivity(intent);
                break;

            case R.id.search:
                fragment = new MapsFragment();
                break;
        }

        if (fragment != null) {
            loadFragment(fragment);
        }
        return true;
    }

    void loadFragment(Fragment fragment) {
        //to attach fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout, fragment).commit();
    }
}