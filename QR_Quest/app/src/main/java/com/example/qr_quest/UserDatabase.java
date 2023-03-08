package com.example.qr_quest;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
    private User player;

    private RegistrationCallback register_callback;
    private UserExistsCallback exists_callback;

    public UserDatabase() {}

    @SuppressLint("HardwareIds")
    public UserDatabase(Context context, User new_player) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("com.example.qr_quest",
                Context.MODE_PRIVATE);

        this.player = new_player;

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

    public void registerCheck() {
        // Check if the username is already taken by querying the "Users" collection
        usersRef.whereEqualTo("user_name", player.getUsername()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Check if any documents were returned by the query
                if (!task.getResult().isEmpty()) {
                    // If there are any documents with the same username, show an error message and return
                    Toast.makeText(context, "Username is already taken", Toast.LENGTH_SHORT).show();
                } else {
                    // If there are no documents with the same username, create a new document in the "Users" collection
                    usersRef.document(deviceId).set(addUser()).addOnSuccessListener(aVoid -> {
                        // If the document was successfully added to the "Users" collection, show a success message
                        Toast.makeText(context, "User registration successful", Toast.LENGTH_SHORT).show();

                        // Save the device ID and username to the device's shared preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("deviceId", deviceId);
                        editor.apply();

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

    public void checkIfUserExists(String documentId) {
        if (documentId.equals("")) {
            exists_callback.onUserExists(false);
            return;
        }

        usersRef.document(documentId).get().addOnCompleteListener(task ->
                exists_callback.onUserExists(task.isSuccessful() && task.getResult().exists()));
    }

    public Map<String, Object> addUser() {
        Map<String, Object> user = new HashMap<>();

        user.put("user_name", player.getUsername());
        user.put("email", player.getEmail());
        user.put("first_name", player.getFirstName());
        user.put("last_name", player.getLastName());
        user.put("phone", player.getPhoneNumber());

        user.put("qr_code_list", new ArrayList<>());
        user.put("score", 0);
        return user;
    }

    public static String getDevice(Context context) {
        // Get a reference to the SharedPreferences object for the device
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.qr_quest",
                Context.MODE_PRIVATE);

        return sharedPreferences.getString("deviceId", "");
    }

    public static void getCurrentUser(String deviceId, OnSuccessListener<DocumentSnapshot> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(deviceId)
                .get()
                .addOnSuccessListener(listener)
                .addOnFailureListener(e -> {
                    listener.onSuccess(null);
                    Log.e(TAG, "Error getting user document", e);
                });
    }

    public static void getRank(String deviceId, OnSuccessListener<Integer> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .orderBy("score", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int rank = 0;
                        int prevScore = -1; // initialize to a value lower than any possible score
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            int currentScore = document.getLong("score").intValue();
                            if (currentScore != prevScore) {
                                rank++;
                                prevScore = currentScore;
                            }
                            if (document.getId().equals(deviceId)) {
                                listener.onSuccess(rank);
                                return;
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                        listener.onSuccess(-999);
                    }
                });
    }
}
