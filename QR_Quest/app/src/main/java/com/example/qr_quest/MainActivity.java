package com.example.qr_quest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a reference to the SharedPreferences object for the device
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.qr_quest",
                Context.MODE_PRIVATE);

        // Check if the device has a saved ID and username
        String chk_deviceId = sharedPreferences.getString("deviceId", null);
        String chk_username = sharedPreferences.getString("username", null);

        if (chk_deviceId != null && chk_username != null) {
            // If the device has a saved ID and username, log the user in and proceed to the next activity
            Intent intent = new Intent(MainActivity.this,home.class);
            startActivity(intent);
        } else {
            // If the device does not have a saved ID and username, proceed to the registration activity
            setContentView(R.layout.activity_main_shadab);

            // Get a reference to the EditText view where the user will enter their username
            EditText usernameEditText = findViewById(R.id.username_edit_text);
            String username = usernameEditText.getText().toString();

            UserDatabaseRegister user_chk = new UserDatabaseRegister(MainActivity.this,
                    sharedPreferences, getApplicationContext(), username);

            submitBtn = findViewById(R.id.submit);
            submitBtn.setOnClickListener(view -> {
                // Check if the username is already taken by querying the "Users" collection
                user_chk.register_check();
            });
        }
    }
}