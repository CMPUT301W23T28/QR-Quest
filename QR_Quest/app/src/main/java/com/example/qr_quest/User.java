package com.example.qr_quest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The User class represents a user in the QR Quest game.
 * It contains information such as the user's username, email, first and last name, phone number, score and QR codes.
 */
public class User implements Serializable {

    private String username, email = "", firstName = "", lastName = "", phoneNumber = "";
    private long score = 0;
    private List<String> qrCodes = new ArrayList<>();    
    private int pointsRank = -999, QRNumRank = -999;

    public User() {}

    /**
     * Constructor for the User class.
     *
     * @param username
     *      The user's username.
     * @param email
     *      The user's email address.
     * @param firstName
     *      The user's first name.
     * @param lastName
     *      The user's last name.
     * @param phoneNumber
     *      The user's phone number.
     * @param score
     *      The user's score.
     * @param qrCodes
     *      A list of the user's QR codes.
     */
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

    /**
     * Returns the user's username.
     *
     * @return Returns the user's username.
     */
    public String getUsername() { return username; }

    /**
     * Sets the user's username.
     *
     * @param username
     *      The new username.
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Returns the user's score.
     *
     * @return Returns the user's score.
     */
    public long getScore() { return score; }

    /**
     * Sets the user's score.
     *
     * @param score
     *      The new score.
     */
    public void setScore(long score) { this.score = score; }

    /**
     * Returns a list of the user's QR codes.
     *
     * @return Returns a list of the user's QR codes.
     */
    public List<String> getQRCodes() { return qrCodes; }

    /**
     * Sets the user's QR codes.
     *
     * @param qrCodes
     *      An Array list of the user's QR codes.
     */
    public void setQRCodeList(ArrayList<String> qrCodes) {
        this.qrCodes = qrCodes;
    }

    /**
     * Returns the user's email address.
     *
     * @return Returns the user's email address.
     */
    public String getEmail() { return email; }

    /**
     * Sets the user's email address.
     *
     * @param email
     *      The new email address.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Returns the user's first name.
     *
     * @return Returns the user's first name.
     */
    public String getFirstName() { return firstName; }

    /**
     * Sets the user's first name.
     *
     * @param firstName
     *      The new first name.
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /**
     * Returns the user's last name.
     *
     * @return Returns the user's last name.
     */
    public String getLastName() { return lastName; }

    /**
     * Sets the user's last name.
     *
     * @param lastName
     *      The new last name.
     */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /**
     * Returns the user's phone number.
     *
     * @return Returns the user's phone
     */
    public String getPhoneNumber() { return phoneNumber; }

    /**
     * Sets the phone number of the user.
     *
     * @param phoneNumber
     *      the phone number to set
     */
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    /**
     * Sets the rank of the user based on their points.
     *
     * @param rank
     *      the rank to set
     */
    public void setPointsRank(int rank) { this.pointsRank = rank;}

    /**
     * Returns the rank of the user based on their points.
     *
     * @return the user's points rank
     */
    public int getPointsRank() { return this.pointsRank;}

    /**
     * Sets the rank of the user based on their number of scanned QR codes.
     *
     * @param rank
     *      the rank to set
     */
    public void setQRNumRank(int rank) { this.QRNumRank = rank;}

    /**
     * Returns the rank of the user based on their number of scanned QR codes.
     *
     * @return the user's QR code rank
     */
    public int getQRNumRank() {return this.QRNumRank;}
}
