package com.example.qr_quest;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

/**
 * The Leaderboard class manages the data of the leaderboard.
 */
public class Leaderboard {
    ArrayList<User> usersSortedByPoints = new ArrayList<>(), usersSortedByQRsCollected = new ArrayList<>();
    ArrayList<QR> qrsSortedByPoints = new ArrayList<>();
    String region="All", query="";
    ArrayList<User> userListByPoints = new ArrayList<>(), userListByQRCollected = new ArrayList<>();
    ArrayList<QR> qrList = new ArrayList<>();

    /**
     * Empty Constructor for the class
     */
    public Leaderboard() {}

    /**
     * This creates all the required lists need to be displayed in the leaderboard
     * @param listener
     *      An OnSuccessListener<Boolean> to receive a boolean indicating whether the operation was successful.
     */
    public void setLists(OnSuccessListener<Boolean> listener) {
        callDatabase(success ->{
            if(success) {
                // update lists with database
                usersSortedByPoints = new ArrayList<>(userListByPoints);
                usersSortedByQRsCollected = new ArrayList<>(userListByQRCollected);
                qrsSortedByPoints = new ArrayList<>(qrList);
                listener.onSuccess(true);
            } else {
                listener.onSuccess(false);
            }
        });
    }

    /**
     * Calls the LeaderboardDatabase to retrieve and update the lists of users and QR codes sorted by points and
     * QR codes collected, respectively.
     * @param listener
     *      An OnSuccessListener<Boolean> to receive a boolean indicating whether the operation was successful.
     */
    private void callDatabase(OnSuccessListener<Boolean> listener) {
        LeaderboardDatabase.getAllUsersByPoints(userListByPoints -> {
            this.userListByPoints = new ArrayList<>(userListByPoints);
            LeaderboardDatabase.getAllUsersByQRNums(userListByQRCollected -> {
                this.userListByQRCollected = (ArrayList<User>) userListByQRCollected;
                LeaderboardDatabase.getAllQRsByScore(qrList -> {
                    this.qrList = (ArrayList<QR>) qrList;
                    listener.onSuccess(true);
                });
            });
        });
    }

    /**
     * Filters the users and QRs by the query and/or region.
     *
     */
    public void filter(String query, String region) {
        if (!query.equals("-")) {
            this.query = query;
        }
        if (!region.equals("-")) {
            this.region = region;
        }
        filterByQuery();
        filterByRegion();
    }

    /**
     * Filters the users and QRs by the query.
     */
    private void filterByQuery() {
        usersSortedByPoints.clear();
        usersSortedByQRsCollected.clear();
        qrsSortedByPoints.clear();

        if (query.isEmpty()) {
            usersSortedByPoints.addAll(userListByPoints);
            usersSortedByQRsCollected.addAll(userListByQRCollected);
            qrsSortedByPoints.addAll(qrList);
        } else {
            for (User user : userListByPoints) {
                if (user.getUsername().toLowerCase().startsWith(query.toLowerCase())) {
                    usersSortedByPoints.add(user);
                }
            }
            for (User user : userListByQRCollected) {
                if (user.getUsername().toLowerCase().startsWith(query.toLowerCase())) {
                    usersSortedByQRsCollected.add(user);
                }
            }
            for (QR qr : qrList) {
                if (qr.getQRName().toLowerCase().startsWith(query.toLowerCase())) {
                    qrsSortedByPoints.add(qr);
                }
            }
        }
    }

    /**
     * Filters the QRs by the region.
     */
    private void filterByRegion() {
        if (region.isEmpty() || region.equalsIgnoreCase("all")) {
            this.region = "All";
        } else {
            ArrayList<QR> temp = new ArrayList<>(qrsSortedByPoints);
            qrsSortedByPoints.clear();
            for (QR qr : temp) {
                if (qr.getCity().equalsIgnoreCase(region)) {
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
     * sets a list of users sorted by points to the list of users provided.
     */
    public void setUsersSortedByPoints(ArrayList<User> users) {
        userListByPoints.clear();
        userListByPoints.addAll(users);
        usersSortedByPoints.clear();
        usersSortedByPoints.addAll(users);
    }

    /**
     * Returns a list of users sorted by the number of Rs collected.
     * @return a list of users sorted by the number of Rs collected
     */
    public ArrayList<User> getUsersSortedByQRsCollected() {
        return usersSortedByQRsCollected;
    }

    /**
     * sets a list of users sorted by number of QRs collected to the list of users provided.
     */
    public void setUsersSortedByQRsCollected(ArrayList<User> users) {
        userListByQRCollected.clear();
        userListByQRCollected.addAll(users);
        usersSortedByQRsCollected.clear();
        usersSortedByQRsCollected.addAll(users);
    }

    /**
     * Returns a list of QRs sorted by points.
     * @return a list of QRs sorted by points
     */
    public ArrayList<QR>  getQrsSortedByPoints() {
        return qrsSortedByPoints;
    }

    public void setQrsSortedByPoints(ArrayList<QR> qrs) {
        qrList.clear();
        qrList.addAll(qrs);
        qrsSortedByPoints.clear();
        qrsSortedByPoints.addAll(qrs);
    }
}

