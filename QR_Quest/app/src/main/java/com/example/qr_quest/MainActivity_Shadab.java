package com.example.qr_quest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class MainActivity_Shadab extends AppCompatActivity {
    Button submitBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_shadab);

        // Get a reference to the "Users" collection in Firebase
        CollectionReference usersRef = db.collection("Users");

        // Get a reference to the EditText view where the user will enter their username
        EditText usernameEditText = findViewById(R.id.username_edit_text);

        // Get the user-input username from the EditText view
        String username = usernameEditText.getText().toString();

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

        submitBtn = findViewById(R.id.submit);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Shadab.this,home.class);
                startActivity(intent);
            }
        });
    }
}