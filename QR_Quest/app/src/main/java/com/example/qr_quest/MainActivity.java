package com.example.qr_quest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    Button submitBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a reference to the SharedPreferences object for the device
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.qr_quest", Context.MODE_PRIVATE);

        // Check if the device has a saved ID and username
        String chk_deviceId = sharedPreferences.getString("deviceId", null);
        String chk_username = sharedPreferences.getString("username", null);
        if (chk_deviceId != null && chk_username != null) {
            // If the device has a saved ID and username, log the user in and proceed to the next activity
            // You can use the deviceId and username to query the database and retrieve the user's scanned QR codes, etc.
            Intent intent = new Intent(MainActivity.this,home.class);
            startActivity(intent);
        } else {
            // If the device does not have a saved ID and username, proceed to the registration activity
            setContentView(R.layout.activity_main_shadab);

            // Get a reference to the "Users" collection in Firebase
            CollectionReference usersRef = db.collection("Users");

            // Get a reference to the EditText view where the user will enter their username
            EditText usernameEditText = findViewById(R.id.username_edit_text);

            // Get the user-input username from the EditText view
            String username = usernameEditText.getText().toString();

            submitBtn = findViewById(R.id.submit);

            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Check if the username is already taken by querying the "Users" collection
                    usersRef.whereEqualTo("name", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // Check if any documents were returned by the query
                                if (!task.getResult().isEmpty()) {
                                    // If there are any documents with the same username, show an error message and return
                                    Toast.makeText(getApplicationContext(), "Username is already taken", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                // If there are no documents with the same username, create a new document in the "Users" collection
                                Map<String, Object> user = new HashMap<>();
                                user.put("name", username);
                                user.put("scanned_qr_codes", new ArrayList<>());
                                usersRef.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        // If the document was successfully added to the "Users" collection, show a success message
                                        Toast.makeText(getApplicationContext(), "User registration successful", Toast.LENGTH_SHORT).show();

                                        // Generate a unique ID for the device using a combination of the device's serial number and a random UUID
                                        String deviceId = Build.SERIAL + UUID.randomUUID().toString();

                                        // Save the device ID to the device's shared preferences
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("deviceId", deviceId);
                                        editor.apply();

                                        // Save the user's username to the device's shared preferences
                                        editor.putString("username", username);
                                        editor.apply();

                                        /*
                                        *  //Get a reference to the "users" node in the Firebase database
                                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

                                            // Create a new user object with the desired fields
                                            User newUser = new User(username, score, numQRCodes, qrCodes, email, firstName, lastName, phoneNumber);

                                            // Get a new unique ID for the user using the push() method
                                            String userId = usersRef.push().getKey();

                                            // Upload the new user data to the database under the unique user ID
                                            usersRef.child(userId).setValue(newUser);
                                            This code gets a reference to the "users" node in your Firebase database, creates a new User object with the desired fields, generates a unique ID for the new user using the push() method, and then uploads the user data to the database using the setValue() method. The user data is stored under the unique ID generated by push(), which allows you to easily retrieve the user data later using the ID.
                                            * */

                                        Intent intent = new Intent(MainActivity.this,home.class);
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // If there was an error adding the document to the "Users" collection, show an error message
                                        Toast.makeText(getApplicationContext(), "User registration failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // If there was an error querying the "Users" collection, show an error message
                                Toast.makeText(getApplicationContext(), "User validation failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
    }
}