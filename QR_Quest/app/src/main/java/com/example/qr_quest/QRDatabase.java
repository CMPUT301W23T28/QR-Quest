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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class QRDatabase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference qrCodesRef = db.collection("QRs");

    private SharedPreferences sharedPreferences;
    private Context context;
    private String deviceId;
    private QR qr;

    private AdditionCallback additionCallback;
    private QRExistsCallback existsCallback;

    public QRDatabase() {}

    public QRDatabase(Context context, QR new_QR) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("com.example.qr_quest",
                Context.MODE_PRIVATE);

        this.qr = new_QR;

        this.deviceId = UserDatabase.getDevice(context);
    }

    public interface AdditionCallback {
        void onAdditionSuccess();
    }

    public void setAdditionCallback(AdditionCallback register_callback) {
        this.additionCallback = additionCallback;
    }

    public interface QRExistsCallback {
        void onQRExists(boolean exists);
    }

    public void setQRExistsCallback(QRExistsCallback existsCallback) {
        this.existsCallback = existsCallback;
    }

    // have to check if current user has this qr code
    public void addQRCode(User user) {
        // Check if the QR code already exists in the database
        qrCodesRef.document(qr.getQRName()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().exists()) {
                    // If the QR code does not exist, create a new document in the "QRs" collection
                    qrCodesRef.document(qr.getQRName()).set(addQR()).addOnSuccessListener(aVoid -> {
                        // If the document was successfully added to the "QRs" collection, show a success message
                        Toast.makeText(context, "QR code added successfully", Toast.LENGTH_SHORT).show();

                        if (additionCallback != null) {
                            additionCallback.onAdditionSuccess();
                        }
                    }).addOnFailureListener(e -> {
                        // If there was an error adding the document to the "QRs" collection, show an error message
                        Toast.makeText(context, "Failed to add QR code", Toast.LENGTH_SHORT).show();
                    });
                }
                // update the user that scanned it
                addUsertoQrCode(context, qr, user, success -> {
                    if (success) {
                        if (additionCallback != null) {
                            additionCallback.onAdditionSuccess();
                        }
                    } else {
                        // If there was an error adding the user to the QR code document, show an error message
                        Toast.makeText(context, "Failed to add user to QR code", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // If there was an error querying the "QRs" collection, show an error message
                Toast.makeText(context, "Failed to check for existing QR codes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Map<String, Object> addQR() {
        Map<String, Object> qrCode = new HashMap<>();

        qrCode.put("hash_value", qr.getHashValue());
        qrCode.put("photo", qr.getImgString());
        qrCode.put("score", qr.getScore());
        qrCode.put("avatar", qr.getQRIcon());
        qrCode.put("latitude", qr.getLatitude());
        qrCode.put("longitude", qr.getLongitude());
        qrCode.put("caption", qr.getCaption());

        qrCode.put("scanned_by", new ArrayList<>());
        qrCode.put("comments", new ArrayList<>());
        return qrCode;
    }

    public void addUsertoQrCode(Context context, QR qrCode, User user, OnSuccessListener<Boolean> listener) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.qr_quest",
                Context.MODE_PRIVATE);
        String deviceId = sharedPreferences.getString("deviceId", "");

        // Retrieve the current list of users for the QR code
        qrCodesRef.document(deviceId).get().addOnSuccessListener(documentSnapshot -> {
            List<String> userList = (ArrayList<String>) documentSnapshot.get("scanned_by");

            // Update the user's QR code list with the new QR code name and score
            userList.add(user.getUsername());

            // Update the qr document in the "QRs" collection with the new users scanned by list
            qrCodesRef.document(qrCode.getQRName()).update("scanned_by", userList).addOnSuccessListener(aVoid -> {
                listener.onSuccess(true);
            }).addOnFailureListener(e -> {
                Toast.makeText(context, "Failed to add user to your list", Toast.LENGTH_SHORT).show();
                listener.onSuccess(false);
            });
        }).addOnFailureListener(e -> {
            // If there was an error retrieving the document, show an error message
            Toast.makeText(context, "Failed to get QR document", Toast.LENGTH_SHORT).show();
            listener.onSuccess(false);
        });
    }

}
