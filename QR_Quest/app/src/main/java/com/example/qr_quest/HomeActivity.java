package com.example.qr_quest;

import androidx.appcompat.app.AppCompatActivity;
        import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Class responsible for navigating to and between home, leaderboard, profile, camera, and maps fragments.
 */
public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    /**
     * Initializes HomeActivity and creates bottomNavigationView. Sets the home item as the current
     * selected item and sets a new HomeFragment as the default fragment.
     *
     * @param savedInstanceState
     *     If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in the last instance.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottomnav);
        MenuItem menuItem = bottomNavigationView.getMenu().findItem(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(this);

        // check incase the intent is coming from QR page
        Intent intent = getIntent();
        boolean goingToMaps = intent.getBooleanExtra("goingToMapsFragment",false);
        QR searchedQR = (QR) getIntent().getSerializableExtra("searchedQR");

        boolean goingToProfile = intent.getBooleanExtra("goingToProfile",false);
        if (goingToProfile) {
            // navigate to the ProfileFragment if the user is coming from QRActivity
            loadFragment(new ProfileFragment());
            menuItem = bottomNavigationView.getMenu().findItem(R.id.profile);
        } else if (goingToMaps) {
            loadFragment(new MapsFragment(searchedQR));
            menuItem = bottomNavigationView.getMenu().findItem(R.id.edittext_leaderboard_search);
        } else {
            // otherwise, load the default HomeFragment
            loadFragment(new HomeFragment());
        }

        menuItem.setChecked(true);
    }

    /**
     * Handles the event when a navigation item has been selected. Depending on which item is selected,
     * HomeActivity navigates to HomeFragment, LeaderboardFragment, ProfileFragment, MapsFragment, or
     * starts a camera activity. Returns true to visibly show that the item has been selected.
     *
     * @param item
     *      The selected item
     * @return
     *      true which shows that the item as selected
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        Intent intent;

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

            case R.id.edittext_leaderboard_search:
                fragment = new MapsFragment();
                break;
        }

        if (fragment != null) {
            loadFragment(fragment);
        }
        return true;
    }
   
    /**
     * Replaces the current fragment and attaches fragment to the activity.
     *
     * @param fragment
     *      signifying the fragment needed to navigate to
     */
    void loadFragment(Fragment fragment) {
        //to attach fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout, fragment).commit();
    }
}
