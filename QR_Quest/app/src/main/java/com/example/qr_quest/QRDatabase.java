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
import com.google.firebase.firestore.FieldValue;
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
    public void addQRCodeCheck(String username, OnSuccessListener<Boolean> listener) {
        // Check if the QR code already exists in the database
        qrCodesRef.document(qr.getQRName()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().exists()) {
                    // If the QR code does not exist, create a new document in the "QRs" collection
                    qrCodesRef.document(qr.getQRName()).set(addQR()).addOnSuccessListener(aVoid -> {
                        // update the user that scanned it if needed
                        addUserToQrCode(context, username, success -> {
                            listener.onSuccess(true);
                        });
                    }).addOnFailureListener(e -> {
                        // If there was an error adding the document to the "QRs" collection, show an error message
                        Toast.makeText(context, "Failed to add QR code", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // If the QR code exists, add the user to the scanned_by list
                    addUserToQrCode(context, username, success -> {
                        listener.onSuccess(success);
                    });
                }
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
        qrCode.put("score", qr.getScore());
        qrCode.put("avatar", qr.getQRIcon());

        qrCode.put("scanned_by", new ArrayList<>());
        qrCode.put("comments", new HashMap<>());
        return qrCode;
    }

    /**
     * Adds a user to a QR code's scanned by list in the database.
     * @param context
     *      The context of the application.
     * @param username
     *      The username of the user to be added.
     * @param listener
     *      The success listener for the addition.
     */
    public void addUserToQrCode(Context context, String username, OnSuccessListener<Boolean> listener) {
        // Retrieve the current list of users for the QR code
        qrCodesRef.document(qr.getQRName()).get().addOnSuccessListener(documentSnapshot -> {
            List<String> userList = (ArrayList<String>) documentSnapshot.get("scanned_by");

            // Update the user's QR code list with the new QR code name and score
            if(!userList.contains(username)) {
                userList.add(username);

                // Update the qr document in the "QRs" collection with the new users scanned by list
                qrCodesRef.document(qr.getQRName()).update("scanned_by", userList).addOnSuccessListener(aVoid -> {
                    listener.onSuccess(true);
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to add user to your list", Toast.LENGTH_SHORT).show();
                    listener.onSuccess(false);
                });
            } else {
                Toast.makeText(context, "You have already scanned this QR code!", Toast.LENGTH_SHORT).show();
                listener.onSuccess(false);
            }
        }).addOnFailureListener(e -> {
            // If there was an error retrieving the document, show an error message
            Toast.makeText(context, "Failed to get QR document", Toast.LENGTH_SHORT).show();
            listener.onSuccess(false);
        });
    }

    public static void getAllQRs(OnSuccessListener<List<QR>> listener) {
        List<QR> qrList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollectionRef = db.collection("Users");

        usersCollectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    document.getReference().collection("qr_codes").get().addOnCompleteListener(qrTask -> {
                        if (qrTask.isSuccessful()) {
                            for (QueryDocumentSnapshot qrDoc : qrTask.getResult()) {
                                QR qr = new QR();
                                qr.setName(qrDoc.getId());
                                qr.setIcon(qrDoc.getString("avatar"));
                                qr.setScore(qrDoc.getLong("score"));
                                qr.setCaption(qrDoc.getString("caption"));
                                qr.setLocation(qrDoc.getDouble("latitude"),
                                        qrDoc.getDouble("longitude"), qrDoc.getString("city"));
                                qr.setImgString(qrDoc.getString("photo"));
                                qrList.add(qr);
                            }
                            listener.onSuccess(qrList);
                        } else {
                            Log.d(TAG, "Error getting QR documents: ", qrTask.getException());
                            listener.onSuccess(qrList);
                        }
                    });
                }
            } else {
                Log.d(TAG, "Error getting user documents: ", task.getException());
                listener.onSuccess(qrList);
            }
        });
    }

    public static void getUserQRs(String DeviceID, OnSuccessListener<QR[]> listener) {
        List<QR> qrList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocumentRef = db.collection("Users").document(DeviceID);

        userDocumentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    document.getReference().collection("qr_codes").get().addOnCompleteListener(qrTask -> {
                        if (qrTask.isSuccessful()) {
                            for (QueryDocumentSnapshot qrDoc : qrTask.getResult()) {
                                QR qr = new QR();
                                qr.setName(qrDoc.getId());
                                qr.setIcon(qrDoc.getString("avatar"));
                                qr.setScore(qrDoc.getLong("score"));
                                qr.setCaption(qrDoc.getString("caption"));
                                qr.setLocation(qrDoc.getDouble("latitude"),
                                        qrDoc.getDouble("longitude"), qrDoc.getString("city"));
                                qr.setImgString(qrDoc.getString("photo"));
                                qrList.add(qr);
                            }
                            QR[] qrArray = qrList.toArray(new QR[0]);
                            listener.onSuccess(qrArray);
                        } else {
                            Log.d(TAG, "Error getting QR documents: ", qrTask.getException());
                            listener.onSuccess(new QR[0]);
                        }
                    });
                } else {
                    Log.d(TAG, "User document not found with DeviceID: " + DeviceID);
                    listener.onSuccess(new QR[0]);
                }
            } else {
                Log.d(TAG, "Error getting user document: ", task.getException());
                listener.onSuccess(new QR[0]);
            }
        });
    }

    public static void addComment(String comment, String username, QR qrCode, OnSuccessListener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("QRs").document(qrCode.getQRName());

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                HashMap<String, String> comments = (HashMap<String, String>) documentSnapshot.get("comments");
                if (comments == null) {
                    comments = new HashMap<>();
                }
                comments.put(username, comment);
                docRef.update("comments", comments)
                        .addOnSuccessListener(aVoid -> {
                            listener.onSuccess(true);
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Error updating comments field in QR document", e);
                            listener.onSuccess(false);
                        });
            } else {
                Log.d(TAG, "No such QR document");
                listener.onSuccess(false);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error getting QR document", e);
            listener.onSuccess(false);
        });
    }

    public static void getAllComments(QR qrCode, OnSuccessListener<HashMap> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("QRs")
                .document(qrCode.getQRName())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        HashMap<String, String> commentsMap = (HashMap<String, String>) documentSnapshot.get("comments");
                        listener.onSuccess(commentsMap);
                    } else {
                        listener.onSuccess(new HashMap<>());
                        Log.e(TAG, "QR document not found with QRName: " + qrCode.getQRName());
                    }
                })
                .addOnFailureListener(e -> {
                    listener.onSuccess(new HashMap<>());
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
    public static void getHighestQR(String username, OnSuccessListener<QR> listener) {
        // Query the Users collection to retrieve the document for the user with the given username
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollectionRef = db.collection("Users");

        usersCollectionRef.whereEqualTo("user_name", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot userDoc = task.getResult().getDocuments().get(0);

                        // Retrieve the QR documents for the user's scanned qr_codes
                        userDoc.getReference().collection("qr_codes")
                                .orderBy("score", Query.Direction.DESCENDING)
                                .get()
                                .addOnCompleteListener(qrTask -> {
                                    if (qrTask.isSuccessful() && !qrTask.getResult().isEmpty()) {
                                        // Return the QR object for the highest scoring QR code
                                        DocumentSnapshot qrDoc = qrTask.getResult().getDocuments().get(0);
                                        QR qr = new QR();
                                        qr.setName(qrDoc.getId());
                                        qr.setIcon(qrDoc.getString("avatar"));
                                        qr.setScore(qrDoc.getLong("score"));
                                        qr.setCaption(qrDoc.getString("caption"));
                                        qr.setLocation(qrDoc.getDouble("latitude"),
                                                qrDoc.getDouble("longitude"), qrDoc.getString("city"));
                                        qr.setImgString(qrDoc.getString("photo"));
                                        listener.onSuccess(qr);
                                    } else {
                                        // If there was an error retrieving the user's QR codes, or if the user has not scanned any QR codes, return null
                                        Log.d(TAG, "Error getting QR documents: ", qrTask.getException());
                                        listener.onSuccess(null);
                                    }
                                });
                    } else {
                        // If there was an error retrieving the user document, or if the user with the given username does not exist, return null
                        Log.d(TAG, "Error getting user document: ", task.getException());
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
    public static void getLowestQR(String username, OnSuccessListener<QR> listener) {
        // Query the Users collection to retrieve the document for the user with the given username
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollectionRef = db.collection("Users");

        usersCollectionRef.whereEqualTo("user_name", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot userDoc = task.getResult().getDocuments().get(0);

                        // Retrieve the QR documents for the user's scanned qr_codes
                        userDoc.getReference().collection("qr_codes")
                                .orderBy("score", Query.Direction.ASCENDING)
                                .get()
                                .addOnCompleteListener(qrTask -> {
                                    if (qrTask.isSuccessful() && !qrTask.getResult().isEmpty()) {
                                        // Return the QR object for the highest scoring QR code
                                        DocumentSnapshot qrDoc = qrTask.getResult().getDocuments().get(0);
                                        QR qr = new QR();
                                        qr.setName(qrDoc.getId());
                                        qr.setIcon(qrDoc.getString("avatar"));
                                        qr.setScore(qrDoc.getLong("score"));
                                        qr.setCaption(qrDoc.getString("caption"));
                                        qr.setLocation(qrDoc.getDouble("latitude"),
                                                qrDoc.getDouble("longitude"), qrDoc.getString("city"));
                                        qr.setImgString(qrDoc.getString("photo"));
                                        listener.onSuccess(qr);
                                    } else {
                                        // If there was an error retrieving the user's QR codes, or if the user has not scanned any QR codes, return null
                                        Log.d(TAG, "Error getting QR documents: ", qrTask.getException());
                                        listener.onSuccess(null);
                                    }
                                });
                    } else {
                        // If there was an error retrieving the user document, or if the user with the given username does not exist, return null
                        Log.d(TAG, "Error getting user document: ", task.getException());
                        listener.onSuccess(null);
                    }
                });
    }

    public static void deleteQR(Context context, QR qrCode, OnSuccessListener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("Users");
        CollectionReference qrCodesRef = db.collection("QRs");

        String deviceID = UserDatabase.getDevice(context);
        String name = qrCode.getQRName();
        long score = qrCode.getScore();

        // Delete the QR code from the user's subcollection
        usersRef.document(deviceID).collection("qr_codes").document(name).delete()
                .addOnSuccessListener(aVoid -> {
                    // Remove the QR code from the user's qr_code_list and decrement the user's score
                    usersRef.document(deviceID).update(
                            "qr_code_list", FieldValue.arrayRemove(name),
                            "score", FieldValue.increment(-1 * score)
                    ).addOnSuccessListener(aVoid1 -> {
                        // Retrieve the QR code document
                        qrCodesRef.document(name).get().addOnSuccessListener(documentSnapshot -> {
                            // Get the list of users that have scanned the QR code
                            List<String> userList = (ArrayList<String>) documentSnapshot.get("scanned_by");
                            // Remove the user from the list
                            userList.remove(deviceID);

                            if (userList.isEmpty()) {
                                // If there are no more users that have scanned the QR code, delete the QR code document
                                qrCodesRef.document(name).delete().addOnSuccessListener(aVoid2 -> {
                                    listener.onSuccess(true);
                                });
                            } else {
                                // Otherwise, update the list of users that have scanned the QR code
                                qrCodesRef.document(name).update("scanned_by", userList)
                                        .addOnSuccessListener(aVoid2 -> {
                                            listener.onSuccess(true);
                                        });
                            }
                        }).addOnFailureListener(e -> {
                            listener.onSuccess(false);
                        });
                    }).addOnFailureListener(e -> {
                        listener.onSuccess(false);
                    });
                }).addOnFailureListener(e -> {
                    listener.onSuccess(false);
                });
    }

    public static void getAllScannedUsers(String username, QR qrCode, OnSuccessListener<List<String>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("QRs").document(qrCode.getQRName());

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    List<String> scannedByList = (List<String>) document.get("scanned_by");
                    if (scannedByList != null && !scannedByList.isEmpty()) {
                        scannedByList.remove(username);
                        listener.onSuccess(scannedByList);
                        return;
                    }
                }
            }
            listener.onSuccess(new ArrayList<>());
        })
        .addOnFailureListener(e -> {
            Log.e("QRActivity", "Error getting scanned users: " + e.getMessage());
            listener.onSuccess(new ArrayList<>());
        });
    }
}
