package com.example.qr_quest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Get a reference to the EditText view where the user will enter their username
        EditText usernameEditText = findViewById(R.id.username_edit_text);

        submitBtn = findViewById(R.id.submit);
        submitBtn.setOnClickListener(view -> {

            String username = usernameEditText.getText().toString().trim();
            if (username.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Username is required", Toast.LENGTH_SHORT).show();
                return;
            }

            EditText emailEditText = findViewById(R.id.email_edit_text);
            EditText f_nameEditText = findViewById(R.id.f_name_edit_text);
            EditText l_nameEditText = findViewById(R.id.l_name_edit_text);
            EditText phoneEditText = findViewById(R.id.phones_edit_text);

            String email = emailEditText.getText().toString().trim();
            String f_name = f_nameEditText.getText().toString().trim();
            String l_name = l_nameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            User new_player = new User(username, email, f_name, l_name, phone, 0, new ArrayList<>());

            // Get a reference to the SharedPreferences object for the device
            SharedPreferences sharedPreferences = getSharedPreferences("com.example.qr_quest",
                    Context.MODE_PRIVATE);

            // Check if the username is already taken by querying the "Users" collection
            UserDatabase userDatabaseRegister = new UserDatabase(getApplicationContext(), new_player);
            userDatabaseRegister.setRegistrationCallback(() -> {
                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(intent);
            });

            userDatabaseRegister.registerCheck();
        });
    }
}