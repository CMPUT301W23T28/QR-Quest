package com.example.qr_quest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

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