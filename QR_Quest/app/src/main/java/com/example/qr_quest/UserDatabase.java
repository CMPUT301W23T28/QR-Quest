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
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class is used to handle the Firebase Cloud Firestore database operations related to Users
 * It allows to add and query Users, as well as to check if a specific User already exists in the
 * database and QR codes scanned by them.
 */
public class UserDatabase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("Users");

    private SharedPreferences sharedPreferences;
    private Context context;
    private String deviceId;
    private User player;

    private RegistrationCallback registerCallback;
    private UserExistsCallback existsCallback;

    /**
     * Constructs a new UserDatabase instance with no arguments.
     */
    public UserDatabase() {}

    /**
     * Constructs a new UserDatabase instance with the specified context and user object.
     * @param
     *      context the Android application context
     * @param
     *      new_player the User object representing the current user
     */
    @SuppressLint("HardwareIds")
    public UserDatabase(Context context, User new_player) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("com.example.qr_quest",
                Context.MODE_PRIVATE);

        this.player = new_player;

        this.deviceId = Build.SERIAL + UUID.randomUUID().toString();
    }

    /**
     * The interface for the callback method to be called when user registration is successful.
     */
    public interface RegistrationCallback {
        void onRegistrationSuccess();
    }

    /**
     * Sets the callback object for user registration success events.
     * @param registerCallback the object implementing the RegistrationCallback interface
     */
    public void setRegistrationCallback(RegistrationCallback registerCallback) {
        this.registerCallback = registerCallback;
    }

    /**
     * The interface for the callback method to be called when checking if a user exists in the database.
     */
    public interface UserExistsCallback {
        void onUserExists(boolean exists);
    }

    /**
     * Sets the callback object for user existence check events.
     * @param existsCallback the object implementing the UserExistsCallback interface
     */
    public void setUserExistsCallback(UserExistsCallback existsCallback) {
        this.existsCallback = existsCallback;
    }

    /**
     * Checks if the current user's username is already taken, and registers the user in the database if not.
     * Also add the important deviceID to the SharedPreferences of the device.
     */
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

                        if (registerCallback != null) {
                            registerCallback.onRegistrationSuccess();
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

    /**
     * Checks whether a user with the specified document ID exists in the "Users" collection.
     * @param documentId The ID of the document to check.
     */
    public void checkIfUserExists(String documentId) {
        if (documentId.equals("")) {
            existsCallback.onUserExists(false);
            return;
        }

        usersRef.document(documentId).get().addOnCompleteListener(task ->
                existsCallback.onUserExists(task.isSuccessful() && task.getResult().exists()));
    }

    /**
     * Adds the user to the "Users" collection.
     * @return A map representing the user to be added to the "Users" collection.
     */
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

    /**
     * Gets the unique ID of the device.
     * @param
     *      context The context of the application.
     * @return
     *      The unique ID of the device.
     */
    public static String getDevice(Context context) {
        // Get a reference to the SharedPreferences object for the device
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.qr_quest",
                Context.MODE_PRIVATE);

        return sharedPreferences.getString("deviceId", "");
    }

    /**
     * Gets the user with the specified device ID from the "Users" collection.
     * @param
     *      deviceId The ID of the device to get the user for.
     * @param
     *      listener The listener to be executed when the user is successfully retrieved.
     */
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

    /**
     * Adds a QR code to the list of QR codes for the current user and updates it in the
     * QR collection, adding the QR to it if needed
     * @param
     *      context The context of the activity calling this method
     * @param
     *      qrCode The QR code to add to the user's list
     * @param
     *      username The username of the current user
     * @param
     *      listener A listener to be called when the QR code has been added. The listener should take a single Boolean parameter
     */
    public void addQRCodeToUser(Context context, QR qrCode, String username, OnSuccessListener<Boolean> listener) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.qr_quest",
                Context.MODE_PRIVATE);
        String deviceId = sharedPreferences.getString("deviceId", "");

        // Retrieve the current list of QR codes for the user
        usersRef.document(deviceId).get().addOnSuccessListener(documentSnapshot -> {
            List<String> qrCodeList = (ArrayList<String>) documentSnapshot.get("qr_code_list");
            Long score = documentSnapshot.getLong("score");

            // Check if the user has already scanned the QR code
            if (qrCodeList.contains(qrCode.getQRName())) {
                Toast.makeText(context, "You have already scanned this QR code", Toast.LENGTH_SHORT).show();
                listener.onSuccess(false);
                return;
            }

            // Update the user's QR code list with the new QR code name and score
            qrCodeList.add(qrCode.getQRName());
            score += qrCode.getScore();

            // Update the user document in the "Users" collection with the new QR code list
            usersRef.document(deviceId).update("qr_code_list", qrCodeList, "score", score).addOnSuccessListener(aVoid -> {
                Toast.makeText(context, "QR code added to your wallet", Toast.LENGTH_SHORT).show();
                listener.onSuccess(true);
            }).addOnFailureListener(e -> {
                Toast.makeText(context, "Failed to add QR code to your list", Toast.LENGTH_SHORT).show();
                listener.onSuccess(false);
            });

            // Update the QR collection with this new addition
            QRDatabase qrDatabase = new QRDatabase(context, qrCode);
            qrDatabase.setAdditionCallback(() -> {
                listener.onSuccess(true);
            });
            qrDatabase.addQRCodeCheck(username);
        }).addOnFailureListener(e -> {
            // If there was an error retrieving the document, show an error message
            Toast.makeText(context, "Failed to get user document", Toast.LENGTH_SHORT).show();
            listener.onSuccess(false);
        });
    }

    /**
     * Returns the rank of the user based on their score after comparing with all users in the
     * collection
     * @param
     *      deviceId the current device's deviceID
     * @param
     *      listener A listener to be called when the rank is determined. The listener should
     *      take a single Integer parameter
     */
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