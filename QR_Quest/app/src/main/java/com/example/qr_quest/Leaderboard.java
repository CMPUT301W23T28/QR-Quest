package com.example.qr_quest;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

/**
 * The Leaderboard class manages the data of the leaderboard.
 */
public class Leaderboard {
    ArrayList<User> usersSortedByPoints = new ArrayList<>(), usersSortedByQRsCollected = new ArrayList<>();
    ArrayList<QR> qrsSortedByPoints = new ArrayList<>();
    String region;
    ArrayList<User> userListByPoints = new ArrayList<>(), userListByQRCollected = new ArrayList<>();
    ArrayList<QR> qrList = new ArrayList<>();

    Leaderboard() {}

    /**
     * Constructs a new Leaderboard object and initializes the lists of users and QRs.
     */
    public void setLists(OnSuccessListener<Boolean> listener) {
        callDatabase(success ->{
            if(success) {
            // update lists with database
            usersSortedByPoints = new ArrayList<>(userListByPoints);
            usersSortedByQRsCollected = new ArrayList<>(userListByQRCollected);
            qrsSortedByPoints = new ArrayList<>(qrList);
            listener.onSuccess(true);} else {listener.onSuccess(false);}
        });
    }

    private void callDatabase(OnSuccessListener<Boolean> listener) {
        LeaderboardDatabase.getAllUsersByPoints(userListByPoints ->
                this.userListByPoints = new ArrayList<>(userListByPoints));

        LeaderboardDatabase.getAllUsersByQRNums(userListByQRCollected ->
                this.userListByQRCollected = (ArrayList<User>) userListByQRCollected);

        LeaderboardDatabase.getAllQRsByScore(qrList ->
                this.qrList = (ArrayList<QR>) qrList);
        listener.onSuccess(true);
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

