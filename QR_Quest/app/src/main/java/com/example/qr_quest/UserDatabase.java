package com.example.qr_quest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserDatabase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("Users");

    private SharedPreferences sharedPreferences;
    private Context context;
    private String deviceId;
    private RegistrationCallback register_callback;
    private UserExistsCallback exists_callback;

    private String username;
    private String email;
    private String f_name;
    private String l_name;
    private String phone;

    public UserDatabase() {}

    @SuppressLint("HardwareIds")
    public UserDatabase(SharedPreferences sharedPreferences, Context context, User new_player) {
        this.sharedPreferences = sharedPreferences;
        this.context = context;

        this.username = new_player.getUsername();
        this.email = new_player.getEmail();
        this.f_name = new_player.getFirstName();
        this.l_name = new_player.getLastName();
        this.phone = new_player.getPhoneNumber();

        this.deviceId = Build.SERIAL + UUID.randomUUID().toString();
    }

    public interface RegistrationCallback {
        void onRegistrationSuccess();
    }

    public void setRegistrationCallback(RegistrationCallback register_callback) {
        this.register_callback = register_callback;
    }

    public interface UserExistsCallback {
        void onUserExists(boolean exists);
    }

    public void setUserExistsCallback(UserExistsCallback exists_callback) {
        this.exists_callback = exists_callback;
    }

    void register_check() {
        // Check if the username is already taken by querying the "Users" collection
        usersRef.whereEqualTo("user_name", username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Check if any documents were returned by the query
                if (!task.getResult().isEmpty()) {
                    // If there are any documents with the same username, show an error message and return
                    Toast.makeText(context, "Username is already taken", Toast.LENGTH_SHORT).show();
                } else {
                    // If there are no documents with the same username, create a new document in the "Users" collection
                    Map<String, Object> user = new HashMap<>();
                    user.put("user_name", username);
                    user.put("email", email);
                    user.put("f_name", f_name);
                    user.put("l_name", l_name);
                    user.put("phone", phone);

                    user.put("scanned_qr_codes", new ArrayList<>());
                    user.put("score", 0);
                    user.put("deviceId", deviceId);

                    usersRef.add(user).addOnSuccessListener(documentReference -> {
                        add_user();
                        if (register_callback != null) {
                            register_callback.onRegistrationSuccess();
                        }
                    }).addOnFailureListener(e -> {
                        // If there was an error adding the document to the "Users" collection, show an error message
                        Toast.makeText(context, "User registration failed", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                // If there was an error querying the "Users" collection, show an error message
                Toast.makeText(context, "User validation failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void add_user() {
        // If the document was successfully added to the "Users" collection, show a success message
        Toast.makeText(context, "User registration successful", Toast.LENGTH_SHORT).show();

        // Save the device ID and username to the device's shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("deviceId", deviceId);
        editor.apply();
    }

    public void checkIfUserExists(String deviceId) {
        usersRef.whereEqualTo("deviceId", deviceId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean exists = !task.getResult().isEmpty();
                exists_callback.onUserExists(exists);
            } else {
                exists_callback.onUserExists(false);
            }
        });
    }
}

