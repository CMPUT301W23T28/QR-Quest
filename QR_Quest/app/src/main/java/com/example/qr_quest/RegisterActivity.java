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

/**
 * Class responsible for registering a new user
 */
public class RegisterActivity extends AppCompatActivity {
    Button submitBtn;

    /**
     * This method initializes the RegisterActivity and sets up the UI elements and event listeners
     * for the registration process. It gets a reference to the EditText view where the user will
     * enter their username, email, first name, last name, and phone number. It creates a new User
     * object with the entered information and registers it to the UserDatabase. If the registration is
     * successful, the user is redirected to the HomeActivity. If the username is already taken, a
     * Toast message is displayed to inform the user.
     *
     * @param savedInstanceState
     *      a Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Get a reference to the EditText view where the user will enter their username
        EditText usernameEditText = findViewById(R.id.edittext_reg_username);

        submitBtn = findViewById(R.id.btn_reg_submit);
        submitBtn.setOnClickListener(view -> {

            String username = usernameEditText.getText().toString().trim();
            if (username.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Username is required", Toast.LENGTH_SHORT).show();
                return;
            }

            EditText emailEditText = findViewById(R.id.edittext_reg_email);
            EditText f_nameEditText = findViewById(R.id.edittext_reg_fname);
            EditText l_nameEditText = findViewById(R.id.edittext_reg_lname);
            EditText phoneEditText = findViewById(R.id.edittext_reg_phone);

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
