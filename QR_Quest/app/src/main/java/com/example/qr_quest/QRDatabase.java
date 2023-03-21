package com.example.qr_quest;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to handle the Firebase Cloud Firestore database operations related to QR codes.
 * It allows to add and update QR codes, as well as to check if a specific QR code already exists
 * in the database and update the users that scanned it.
 */
public class QRDatabase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference qrCodesRef = db.collection("QRs");

    private SharedPreferences sharedPreferences;
    private Context context;
    private String deviceId;
    private QR qr;

    private AdditionCallback additionCallback;

    /**
     * Constructs a new instance of the QRDatabase class with no arguments.
     */
    public QRDatabase() {}

    /**
     * Constructs a new instance of the QRDatabase class with the specified context and QR object.
     * @param context
     *      The context of the application
     * @param new_QR
     *      The QR object that is being added or updated in the database
     */
    public QRDatabase(Context context, QR new_QR) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("com.example.qr_quest",
                Context.MODE_PRIVATE);

        this.qr = new_QR;

        this.deviceId = UserDatabase.getDevice(context);
    }

    /**
     * The interface for the callback that is triggered when a QR code is successfully added to the database.
     */
    public interface AdditionCallback {
        void onAdditionSuccess();
    }

    /**
     * Sets the callback that is triggered when a QR code is successfully added to the database.
     * @param additionCallback
     *      The callback to be set
     */
    public void setAdditionCallback(AdditionCallback additionCallback) {
        this.additionCallback = additionCallback;
    }

    /**
     * Checks if the specified QR code already exists in the database and adds it if it doesn't.
     * Also updates the users that scanned the QR code.
     * @param username
     *      The username of the user that scanned the QR code
     */
    public void addQRCodeCheck(String username) {
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
                addUserToQrCode(context, qr, username, success -> {
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

    /**
     * Creates a map of QR code details.
     * @return A map of QR code details.
     */
    public Map<String, Object> addQR() {
        Map<String, Object> qrCode = new HashMap<>();

        qrCode.put("hash_value", qr.getHashValue());
        qrCode.put("photo", qr.getImgString());
        qrCode.put("score", qr.getScore());
        qrCode.put("avatar", qr.getQRIcon());
        qrCode.put("latitude", qr.getLatitude());
        qrCode.put("longitude", qr.getLongitude());
        qrCode.put("city", qr.getCity());
        qrCode.put("caption", qr.getCaption());

        qrCode.put("scanned_by", new ArrayList<>());
        qrCode.put("comments", new ArrayList<>());
        return qrCode;
    }

    /**
     * Updates a QR code in the database.
     * @param listener
     *      The success listener for the update.
     */
    public void updateQRCode(OnSuccessListener<Boolean> listener) {
        DocumentReference qrRef = db.collection("QRs").document(qr.getQRName());
        Map<String, Object> updated_qr = new HashMap<>();

        updated_qr.put("photo", qr.getImgString());

        updated_qr.put("latitude", qr.getLatitude());
        updated_qr.put("longitude", qr.getLongitude());
        updated_qr.put("city", qr.getCity());

        updated_qr.put("caption", qr.getCaption());

        qrRef.update(updated_qr).addOnSuccessListener(aVoid -> {
            listener.onSuccess(true);
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Failed to update QR in DB", Toast.LENGTH_SHORT).show();
            listener.onSuccess(false);
        });
    }

    /**
     * Adds a user to a QR code's scanned by list in the database.
     * @param context
     *      The context of the application.
     * @param qrCode
     *      The QR code to which the user should be added.
     * @param username
     *      The username of the user to be added.
     * @param listener
     *      The success listener for the addition.
     */
    public void addUserToQrCode(Context context, QR qrCode, String username, OnSuccessListener<Boolean> listener) {
        // Retrieve the current list of users for the QR code
        qrCodesRef.document(qrCode.getQRName()).get().addOnSuccessListener(documentSnapshot -> {
            List<String> userList = (ArrayList<String>) documentSnapshot.get("scanned_by");

            // Update the user's QR code list with the new QR code name and score
            userList.add(username);

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

    public static void getAllQRs(OnSuccessListener<List<QR>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference qrCollectionRef = db.collection("QRs");

        qrCollectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<QR> qrList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    QR qr = new QR();
                    qr.setName(document.getId());
                    qr.setIcon(document.getString("avatar"));
                    qr.setCaption(document.getString("caption"));
                    qr.setLocation(document.getDouble("latitude"),
                            document.getDouble("longitude"), document.getString("city"));
                    qr.setImgString(document.getString("photo"));
                    qr.setScore(document.getLong("score"));
                    qrList.add(qr);
                }
                listener.onSuccess(qrList);
            } else {
                Log.d(TAG, "Error getting QR documents: ", task.getException());
            }
        });
    }

    public static void getCurrentQR(String qrName, OnSuccessListener<DocumentSnapshot> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("QRs")
                .document(qrName)
                .get()
                .addOnSuccessListener(listener)
                .addOnFailureListener(e -> {
                    listener.onSuccess(null);
                    Log.e(TAG, "Error getting qr document", e);
                });
    }

    /**
     * Retrieves the QR document with the highest score that the specified user has scanned after
     * querying through all QR codes in the collections.
     * @param username
     *      The username of the user to check
     * @param listener
     *      A listener to be called with the resulting document snapshot
     */
    public static void getHighestQR(String username, OnSuccessListener<DocumentSnapshot> listener) {
        // Query the QR codes collection to retrieve the documents sorted in descending order by score
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("QRs")
                .orderBy("score", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        List<String> scannedByList = (ArrayList<String>) document.get("scanned_by");
                        if (scannedByList.contains(username)) {
                            // If the user is in the scanned_by list of this QR code document, return it
                            listener.onSuccess(document);
                            return;
                        }
                    }
            } else {
                // If there was an error retrieving the QR codes collection, show an error message
                Log.d(TAG, "Error getting documents: ", task.getException());
                listener.onSuccess(null);
            }
        });
    }

    /**
     * Retrieves the QR document with the lowest score that the specified user has scanned after
     * querying through all QR codes in the collections.
     * @param username
     *      The username of the user to check
     * @param listener
     *      A listener to be called with the resulting document snapshot
     */
    public static void getLowestQR(String username, OnSuccessListener<DocumentSnapshot> listener) {
        // Query the QR codes collection to retrieve the documents sorted in descending order by score
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("QRs")
                .orderBy("score", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            List<String> scannedByList = (ArrayList<String>) document.get("scanned_by");
                            if (scannedByList.contains(username)) {
                                // If the user is in the scanned_by list of this QR code document, return it
                                listener.onSuccess(document);
                                return;
                            }
                        }
                    } else {
                        // If there was an error retrieving the QR codes collection, show an error message
                        Log.d(TAG, "Error getting documents: ", task.getException());
                        listener.onSuccess(null);
                    }
                });
    }

    public static void deleteQR(String deviceID, String name, OnSuccessListener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("Users");
        CollectionReference qrCodesRef = db.collection("QRs");

        qrCodesRef.document(name).get().addOnSuccessListener(documentSnapshot -> {
            List<String> userList = (ArrayList<String>) documentSnapshot.get("scanned_by");

            // Update the user's QR code list with the new QR code name and score
            UserDatabase.getCurrentUser(deviceID, userDoc -> {
                userList.remove(userDoc.getString("user_name"));

                usersRef.document(deviceID).get().addOnSuccessListener(deleteUserQR -> {
                    List<String> qrList = (ArrayList<String>) documentSnapshot.get("qr_code_list");
                    qrList.remove(name);

                    usersRef.document(deviceID).update("qr_code_list", qrList);
                });
            });

            if(userList.isEmpty()) {
                qrCodesRef.document(name).delete().addOnSuccessListener(aVoid -> {
                    listener.onSuccess(true);
                });
            } else {
                // Update the qr document in the "QRs" collection with the new users scanned by list
                qrCodesRef.document(name).update("scanned_by", userList).addOnSuccessListener(aVoid -> {
                    listener.onSuccess(true);
                });
            }
        });
    }
}
