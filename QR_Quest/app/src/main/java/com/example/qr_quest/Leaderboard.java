package com.example.qr_quest;

import java.util.ArrayList;

/**
 * The Leaderboard class manages the data of the leaderboard.
 */
public class Leaderboard {
    ArrayList<User> usersSortedByPoints, usersSortedByQRsCollected;
    ArrayList<QR> qrsSortedByPoints;
    String region;
    ArrayList<User> userList;
    ArrayList<QR> qrList;

    // TEMPORARY
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
        qrList.add(new QR("lll"));
        qrList.add(new QR("qjcwi o i ibfo"));
    }


    /**
     * Constructs a new Leaderboard object and initializes the lists of users and QRs.
     */
    public Leaderboard() {
        mockDatabase();
        // update lists with database
        usersSortedByPoints = new ArrayList<>(userList);
        usersSortedByQRsCollected = new ArrayList<>(userList);
        qrsSortedByPoints = new ArrayList<>(qrList);
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
            usersSortedByPoints.addAll(userList);
            usersSortedByQRsCollected.addAll(userList);
            qrsSortedByPoints.addAll(qrList);
        } else {
            for (User user : userList) {
                if (user.getUsername().toLowerCase().contains(query.toLowerCase())) {
                    usersSortedByPoints.add(user);
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

}

