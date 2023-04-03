package com.example.qr_quest;

import android.content.Context;
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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is used to handle the Firebase Cloud Firestore database operations related to QR codes.
 * It allows to add and update QR codes, as well as to check if a specific QR code already exists
 * in the database and update the users that scanned it.
 */
public class QRDatabase {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference qrCodesRef = db.collection("QRs");

    private Context context;
    private QR qr;

    private QRDatabase.QRExistsCallback existsCallback;

    /**
     * Constructs a new instance of the QRDatabase class with the specified context and QR object.
     * @param context
     *      The context of the application
     * @param new_QR
     *      The QR object that is being added or updated in the database
     */
    public QRDatabase(Context context, QR new_QR) {
        this.context = context;
        this.qr = new_QR;
    }


    /**
     * The interface for the callback method to be called when checking if a QR exists in the database.
     */
    public interface QRExistsCallback {
        void onQRExists(boolean exists);
    }

    /**
     * Sets the callback object for QR existence check events.
     * @param existsCallback The object implementing the QRExistsCallback interface
     */
    public void setQRExistsCallback(QRDatabase.QRExistsCallback existsCallback) {
        this.existsCallback = existsCallback;
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
                        addUserToQrCode(context, username, success -> listener.onSuccess(true));
                    }).addOnFailureListener(e -> {
                        // If there was an error adding the document to the "QRs" collection, show an error message
                        Toast.makeText(context, "Failed to add QR code", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // If the QR code exists, add the user to the scanned_by list
                    addUserToQrCode(context, username, listener);
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
        qrCode.put("comments", new HashMap<String, List<String>>());
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

    /**
     * Retrieves all QR codes from all users in the Firestore "Users" collection and returns them as a list.
     * @param listener an OnSuccessListener that receives the list of QR codes
     */
    public static void getAllQRs(OnSuccessListener<List<QR>> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollectionRef = db.collection("Users");
        List<QR> qrList = new ArrayList<>();

        usersCollectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<DocumentSnapshot> userDocs = task.getResult().getDocuments();
                AtomicInteger completedTasks = new AtomicInteger(0);

                for (DocumentSnapshot userDoc : userDocs) {
                    userDoc.getReference().collection("qr_codes").get().addOnCompleteListener(qrTask -> {
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
                        } else {
                            listener.onSuccess(qrList);
                        }
                        if (completedTasks.incrementAndGet() == userDocs.size()) {
                            listener.onSuccess(qrList);
                        }
                    });
                }
                // If there are no users, call the listener with an empty list
                if (userDocs.isEmpty()) {
                    listener.onSuccess(qrList);
                }
            } else {
                listener.onSuccess(qrList);
            }
        });
    }

    /**
     * Retrieves all QR codes associated with a given user from Firestore and returns them as an array.
     * @param username the username of the user whose QR codes are to be retrieved
     * @param listener an OnSuccessListener that receives the array of QR codes
     */
    public static void getUserQRs(String username, OnSuccessListener<QR[]> listener) {
        List<QR> qrList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollectionRef = db.collection("Users");

        usersCollectionRef.whereEqualTo("user_name", username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
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
                        listener.onSuccess(new QR[0]);
                    }
                });
            } else {
                listener.onSuccess(new QR[0]);
            }
        }).addOnFailureListener(e -> listener.onSuccess(new QR[0]));
    }

    /**
     * Checks whether a given user has scanned a given QR code.
     * @param username the username to check
     * @param qrCode the QR code to check
     * @param listener a listener that will be called with the result of the check
     */
    public static void checkIfUserHasQR(String username, QR qrCode, OnSuccessListener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("QRs").document(qrCode.getQRName());

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                ArrayList<String> scanned_users = (ArrayList<String>) documentSnapshot.get("scanned_by");
                if(scanned_users.contains(username)) {
                    listener.onSuccess(true);
                } else {
                    listener.onSuccess(false);
                }
            } else {
                listener.onSuccess(false);
            }
        }).addOnFailureListener(e -> {
            listener.onSuccess(false);
        });
    }

    /**
     * Adds a comment to a QR code in the database, associated with a specific user.
     * @param comment the comment to add
     * @param username the username of the user adding the comment
     * @param qrCode the QR code to add the comment to
     * @param listener a listener that will be called with the result of the operation
     */
    public static void addComment(String comment, String username, QR qrCode, OnSuccessListener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("QRs").document(qrCode.getQRName());

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                HashMap<String, Object> comments = (HashMap<String, Object>) documentSnapshot.get("comments");
                if (comments == null) {
                    comments = new HashMap<>();
                }
                List<String> userComments;
                Object value = comments.get(username);
                if (value == null) {
                    userComments = new ArrayList<>();
                } else if (value instanceof List) {
                    userComments = (List<String>) value;
                } else {
                    // Migrate existing data structure (String) to the new data structure (List<String>)
                    userComments = new ArrayList<>();
                    userComments.add((String) value);
                }
                userComments.add(comment);
                comments.put(username, userComments);
                docRef.update("comments", comments)
                        .addOnSuccessListener(aVoid -> listener.onSuccess(true))
                        .addOnFailureListener(e -> listener.onSuccess(false));
            } else {
                listener.onSuccess(false);
            }
        }).addOnFailureListener(e -> listener.onSuccess(false));
    }

    /**
     * Gets all comments associated with a specific QR code.
     * @param qrCode the QR code to get comments for
     * @param listener a listener that will be called with the resulting map of comments
     */
    public static void getAllComments(QR qrCode, OnSuccessListener<HashMap> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("QRs")
                .document(qrCode.getQRName())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        HashMap<String, List<String>> commentsMap = (HashMap<String, List<String>>) documentSnapshot.get("comments");
                        listener.onSuccess(commentsMap);
                    } else {
                        listener.onSuccess(new HashMap<>());
                    }
                })
                .addOnFailureListener(e -> listener.onSuccess(new HashMap<>()));
    }

    /**
     * Retrieves the rank of a QR code based on its score compared to other QR codes in the database.
     * @param qrName The name of the QR code for which to retrieve the rank.
     * @param listener An OnSuccessListener that will be called with the rank of the QR code as an Integer.
     */

    public static void getQRRank(String qrName, OnSuccessListener<Integer> listener) {
        FirebaseFirestore.getInstance().collection("QRs")
                .orderBy("score", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int rank = 0;
                        long prevScore = -1; // initialize to a value lower than any possible score
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            long currentScore = document.getLong("score");
                            if (currentScore != prevScore) {
                                rank++;
                                prevScore = currentScore;
                            }
                            if ((document.getId()).equals(qrName)) {
                                listener.onSuccess(rank);
                                return;
                            }
                        }
                    } else {
                        listener.onSuccess(-999);
                    }
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
                                        listener.onSuccess(null);
                                    }
                                });
                    } else {
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
                                        listener.onSuccess(null);
                                    }
                                });
                    } else {
                        listener.onSuccess(null);
                    }
                });
    }

    /**
     * Deletes a QR code from the database and updates the user's document and QR code document accordingly.
     * @param context The context of the calling activity.
     * @param qrCode The QR code to be deleted.
     * @param listener An OnSuccessListener<Boolean> that listens for the completion of the deletion process and returns a boolean value indicating success or failure.
     */
    public static void deleteQR(Context context, QR qrCode, OnSuccessListener<Boolean> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("Users");
        CollectionReference qrCodesRef = db.collection("QRs");

        String deviceID = UserDatabase.getDevice(context);
        String name = qrCode.getQRName();
        long score = qrCode.getScore();

        usersRef.document(deviceID).get().addOnCompleteListener(userDocTask -> {
            if (userDocTask.isSuccessful()) {
                String username = userDocTask.getResult().getString("user_name");

                // Delete the QR code from the user's subcollection
                usersRef.document(deviceID).collection("qr_codes").document(name).delete()
                        .addOnCompleteListener(deleteUserSubcollectionTask -> {
                            if (deleteUserSubcollectionTask.isSuccessful()) {
                                // Update the user's document: remove the QR code from the user's qr_code_list and decrement the user's score
                                usersRef.document(deviceID).update(
                                        "qr_code_list", FieldValue.arrayRemove(name),
                                        "score", FieldValue.increment(-1 * score)
                                ).addOnCompleteListener(updateUserTask -> {
                                    if (updateUserTask.isSuccessful()) {
                                        // Update the QR code document: remove the user from the scanned_by list
                                        qrCodesRef.document(name).update("scanned_by", FieldValue.arrayRemove(username))
                                                .addOnCompleteListener(updateQRTask -> {
                                                    if (updateQRTask.isSuccessful()) {
                                                        listener.onSuccess(true);
                                                    } else {
                                                        listener.onSuccess(false);
                                                    }
                                                });
                                    } else {
                                        listener.onSuccess(false);
                                    }
                                });
                            } else {
                                listener.onSuccess(false);
                            }
                        });
            } else {
                listener.onSuccess(false);
            }
        });
    }

    /**
     * Gets the list of users who have scanned a particular QR code.
     * @param username The username of the current user.
     * @param qrCode The QR code for which the list of scanned users is requested.
     * @param listener An OnSuccessListener<List<String>> that listens for the completion of the retrieval process and returns a list of usernames that have scanned the QR code.
     */
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
        .addOnFailureListener(e -> listener.onSuccess(new ArrayList<>()));
    }
}
