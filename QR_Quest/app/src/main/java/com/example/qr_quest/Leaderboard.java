package com.example.qr_quest;

import java.util.ArrayList;

public class Leaderboard {
    ArrayList<User> usersSortedByPoints, usersSortedByQRsCollected;
    ArrayList<QR> qrsSortedByPoints;


    // TEMPORARY
    ArrayList<User> userList;
    ArrayList<QR> qrList;
    private void mockDatabase() {
        ArrayList<String> qrs = new ArrayList<>();
        userList = new ArrayList<>();
        qrList = new ArrayList<>();

        // Add  User objects to the userList
        userList.add(new User("user1", "u@u.com", "us", "er", "1234", 100L, qrs));
        userList.add(new User("user2", "u@u.com", "us", "er", "1234", 300L, qrs));
        userList.add(new User("user3", "u@u.com", "us", "er", "1234", 300L, qrs));

        // add QRs objects to the qrList
        qrList.add(new QR("mmm"));
        qrList.add(new QR("ooo"));
        qrList.add(new QR("aaa"));
    }


    Leaderboard() {
        mockDatabase();
        // update lists with database
        usersSortedByPoints = new ArrayList<>(userList);
        usersSortedByQRsCollected = new ArrayList<>(userList);
        qrsSortedByPoints = new ArrayList<>(qrList);
    }

    public ArrayList<User> getUsersSortedByPoints() {
        return usersSortedByPoints;
    }

    public ArrayList<User> getUsersSortedByQRsCollected() {
        return usersSortedByQRsCollected;
    }

    public ArrayList<QR>  getQrsSortedByPoints() {
        return qrsSortedByPoints;
    }





}
