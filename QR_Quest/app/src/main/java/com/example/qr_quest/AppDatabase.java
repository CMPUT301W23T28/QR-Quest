package com.example.qr_quest;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppDatabase {

    final String TAG = "Sample";
    FirebaseFirestore db = FirebaseFirestore.getInstance();



    CollectionReference usersCollection = db.collection("users");
    CollectionReference qrCodesCollection = db.collection("qr_codes");

//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    db.collection("qr_codes").whereEqualTo("user", "john_doe")
//    .get()
//    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//        @Override
//        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//            if (task.isSuccessful()) {
//                for (QueryDocumentSnapshot document : task.getResult()) {
//                    Log.d(TAG, document.getId() + " => " + document.getData());
//                }
//            } else {
//                Log.d(TAG, "Error getting documents: ", task.getException());
//            }
//        }
//    });

    public void addUser(String username, String token, int score, int numQR, ArrayList<String> qrCodes) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("token", token);
        user.put("score", score);
        user.put("num_qr_codes", numQR);
        user.put("qr_codes", qrCodes);

        db.collection("users").document(username).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding user", e);
                    }
                });
    }

    // Add a new QR code
    public void addQRCode(String user, String photo, String geolocation, String uniqueName, String uniqueAvatar, int points, String comments) {
        Map<String, Object> qrCode = new HashMap<>();
        qrCode.put("user", user);
        qrCode.put("photo", photo);
        qrCode.put("geolocation", geolocation);
        qrCode.put("unique_name", uniqueName);
        qrCode.put("unique_avatar", uniqueAvatar);
        qrCode.put("points", points);
        qrCode.put("comments", comments);

        db.collection("qr_codes").add(qrCode)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "QR code added successfully! ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding QR code", e);
                    }
                });
    }

    public void updateUser(String username, String token, int score, int numQR, ArrayList<String> qrCodes) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("token", token);
        updates.put("score", score);
        updates.put("num_qr_codes", numQR);
        updates.put("qr_codes", qrCodes);

        db.collection("users").document(username).update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User updated successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating user", e);
                    }
                });
    }

    public void updateQRCode(String qrCodeId, String user, String photo, String geolocation, String uniqueName, String uniqueAvatar, int points, String comments) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("user", user);
        updates.put("photo", photo);
        updates.put("geolocation", geolocation);
        updates.put("unique_name", uniqueName);
        updates.put("unique_avatar", uniqueAvatar);
        updates.put("points", points);
        updates.put("comments", comments);

        db.collection("qr_codes").document(qrCodeId).update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "QR code updated successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating QR code", e);
                    }
                });
    }

    // Delete a user
    public void deleteUser(String username) {
        db.collection("users").document(username)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User deleted successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting user", e);
                    }
                });
    }

    // Delete a QR code
    public void deleteQRCode(String qrCodeId) {
        db.collection("qr_codes").document(qrCodeId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "QR code deleted successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting QR code", e);
                    }
                });
    }

}
