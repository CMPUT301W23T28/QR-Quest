package com.example.qr_quest;

import java.util.List;

public class User {

    private String username;
    private int score;
    private int numQRCodes;
    private List<String> qrCodes;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    // Getters and setters for all fields
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getNumQRCodes() { return numQRCodes; }
    public void setNumQRCodes(int numQRCodes) { this.numQRCodes = numQRCodes; }
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

    public User(String username, int score, int numQRCodes, List<String> qrCodes,
                String email, String firstName, String lastName, String phoneNumber) {
        this.username = username;
        this.score = score;
        this.numQRCodes = numQRCodes;
        this.qrCodes = qrCodes;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

}
