package com.example.qr_quest;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    private long score;
    private List<String> qrCodes;

    // Getters and setters for all fields
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public long getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public List<String> getQRCodes() { return qrCodes; }
    public void setQRCodes(List<String> qrCodes) { this.qrCodes = qrCodes; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public User() {}

    public User(String username, String email, String firstName, String lastName, String phoneNumber,
                long score, ArrayList<String> qrCodes) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;

        this.score = score;
        this.qrCodes = qrCodes;
    }

}
