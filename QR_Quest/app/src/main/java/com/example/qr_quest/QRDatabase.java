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
    CollectionReference usersRef = db.collection("QRs");

    private SharedPreferences sharedPreferences;
    private Context context;
    private String deviceId;
    private QR qr;
//    private UserDatabase.RegistrationCallback register_callback;
//    private UserDatabase.UserExistsCallback exists_callback;

    private String hashValue, name, icon;
    private int score;
    private double latitude, longitude;

    public QRDatabase() {}

    public QRDatabase(Context context, QR new_QR) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("com.example.qr_quest",
                Context.MODE_PRIVATE);

        this.qr = new_QR;

        this.deviceId = Build.SERIAL + UUID.randomUUID().toString();
    }
}
