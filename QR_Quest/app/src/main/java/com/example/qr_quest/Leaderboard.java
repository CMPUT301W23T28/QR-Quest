package com.example.qr_quest;

import java.util.ArrayList;

/**
 * The Leaderboard class manages the data of the leaderboard.
 */
public class Leaderboard {
    ArrayList<User> usersSortedByPoints, usersSortedByQRsCollected;
    ArrayList<QR> qrsSortedByPoints;
    String region;
    ArrayList<User> userListByPoints, userListByQRCollected;
    ArrayList<QR> qrList;


    /**
     * Constructs a new Leaderboard object and initializes the lists of users and QRs.
     */
    public Leaderboard() {
        callDatabase();
        // update lists with database
        usersSortedByPoints = new ArrayList<>(userListByPoints);
        usersSortedByQRsCollected = new ArrayList<>(userListByQRCollected);
        qrsSortedByPoints = new ArrayList<>(qrList);
    }


    private void callDatabase() {
        LeaderboardDatabase.getAllUsersByPoints(userListByPoints ->
                this.userListByPoints = (ArrayList<User>) userListByPoints);

        LeaderboardDatabase.getAllUsersByQRNums(userListByQRCollected ->
                this.userListByQRCollected = (ArrayList<User>) userListByQRCollected);

        LeaderboardDatabase.getAllQRsByScore(qrList ->
                this.qrList = (ArrayList<QR>) qrList);
    }




    /**
     * Filters the users and QRs by the specified query.
     * @param query the query to filter by
     */
    public void filter(String query) {
        usersSortedByPoints.clear();
        usersSortedByQRsCollected.clear();
        qrsSortedByPoints.clear();

        if (query.isEmpty()) {
            usersSortedByPoints.addAll(userListByPoints);
            usersSortedByQRsCollected.addAll(userListByQRCollected);
            qrsSortedByPoints.addAll(qrList);
        } else {
            for (User user : userListByPoints) {
                if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                    usersSortedByPoints.add(user);
                }
            }
            for (User user : userListByQRCollected) {
                if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                    usersSortedByQRsCollected.add(user);
                }
            }
            for (QR qr : qrList) {
                if (qr.getQRName().toLowerCase().contains(query.toLowerCase())) {
                    qrsSortedByPoints.add(qr);
                }
            }
        }
    }

    /**
     * Filters the QRs by the specified region.
     * @param region the region to filter by
     */
    public void filterByRegion(String region) {
        qrsSortedByPoints.clear();
        if (region.isEmpty()) {
            this.region = "All";
            qrsSortedByPoints.addAll(qrList);
        } else {
            this.region = region;
            for (QR qr : qrList) {
                if (qr.getCity().toLowerCase().contains(region.toLowerCase())) {
                    qrsSortedByPoints.add(qr);
                }
            }
        }

    }

    /**
     * Returns the region that the QRs are currently filtered by.
     * @return the region that the QRs are filtered by
     */
    public String getRegion() {
        return region;
    }

    /**
     * Returns a list of users sorted by points.
     * @return a list of users sorted by points
     */
    public ArrayList<User> getUsersSortedByPoints() {
        return usersSortedByPoints;
    }

    /**
     * Returns a list of users sorted by the number of Rs collected.
     * @return a list of users sorted by the number of Rs collected
     */
    public ArrayList<User> getUsersSortedByQRsCollected() {
        return usersSortedByQRsCollected;
    }

    /**
     * Returns a list of QRs sorted by points.
     * @return a list of QRs sorted by points
     */
    public ArrayList<QR>  getQrsSortedByPoints() {
        return qrsSortedByPoints;
    }

    private void updateUsersSortedByPoints(){

    }

}

