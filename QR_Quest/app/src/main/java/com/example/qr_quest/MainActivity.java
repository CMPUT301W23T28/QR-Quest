package com.example.qr_quest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

/**
 * MainActivity class. Initializes UserDatabase and confirms user.
 */
public class MainActivity extends AppCompatActivity {
    
    /**
     * Initializes MainActivity and a UserDatabase. The UserDatabase is used to check if the
     * user of the current devide already exists. If the user already exists, then MainActivity
     * navigates to HomeActivity. Otherwise, it navigates to RegisterActivity to register a new
     * user.
     *
     * @param savedInstanceState
     *     If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most 
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserDatabase userDatabase = new UserDatabase();
        userDatabase.setUserExistsCallback(userExists -> {
            Intent intent;
            // Check if the user exists in the database
            if (userExists) {
                // User exists, proceed to home activity
                intent = new Intent(MainActivity.this, HomeActivity.class);
            } else {
                // User does not exist, proceed to register activity
                intent = new Intent(MainActivity.this, RegisterActivity.class);
            }
            startActivity(intent);
        });

        userDatabase.checkIfUserExists(UserDatabase.getDevice(getApplicationContext()));
    }
}
