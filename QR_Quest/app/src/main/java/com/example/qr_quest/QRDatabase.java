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

public class QRDatabase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference qrCodesRef = db.collection("QRs");

    private SharedPreferences sharedPreferences;
    private Context context;
    private String deviceId;
    private QR qr;

//    private UserDatabase.RegistrationCallback register_callback;
//    private UserDatabase.UserExistsCallback exists_callback;

    public QRDatabase() {}

    public QRDatabase(Context context, QR new_QR) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("com.example.qr_quest",
                Context.MODE_PRIVATE);

        this.qr = new_QR;
    }

    public void checkQR() {}

    public void addQRCode() {
        // Check if the QR code already exists in the database
        qrCodesRef.document(qr.getQRName()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    // If the QR code already exists, show an error message and return
                    Toast.makeText(context, "QR code already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // If the QR code does not exist, create a new document in the "QRs" collection
                    qrCodesRef.document(qr.getQRName()).set(addQR()).addOnSuccessListener(aVoid -> {
                        // If the document was successfully added to the "QRs" collection, show a success message
                        Toast.makeText(context, "QR code added successfully", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        // If there was an error adding the document to the "QRs" collection, show an error message
                        Toast.makeText(context, "Failed to add QR code", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                // If there was an error querying the "QRs" collection, show an error message
                Toast.makeText(context, "Failed to check for existing QR codes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Map<String, Object> addQR() {
        Map<String, Object> qrCode = new HashMap<>();

        qrCode.put("hash_value", qr.getHashValue());
        qrCode.put("score", qr.getScore());
        qrCode.put("avatar", qr.getAvatar());
        qrCode.put("latitude", qr.getLatitude());
        qrCode.put("longitude", qr.getLongitude());

        qrCode.put("scanned_by", new ArrayList<>());
        return qrCode;
    }
}
