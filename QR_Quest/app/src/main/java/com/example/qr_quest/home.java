package com.example.qr_quest;

import androidx.appcompat.app.AppCompatActivity;
        import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;


import android.content.Intent;
import android.os.Bundle;
        import android.view.MenuItem;


public class home extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottonnav);
        MenuItem menuItem = bottomNavigationView.getMenu().findItem(R.id.home);
        menuItem.setChecked(true);
        bottomNavigationView.setOnItemSelectedListener(this);
        loadFragment(new HomeFragment());
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
                intent = new Intent(home.this, CameraFragment.class);
                startActivity(intent);
                break;

//            case R.id.search:
//                fragment = new MapsFragment();
//                break;
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