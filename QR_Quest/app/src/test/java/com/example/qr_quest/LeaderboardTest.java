package com.example.qr_quest;

import static com.example.qr_quest.LeaderboardDatabase.getAllUsersByQRNums;

import com.example.qr_quest.Leaderboard;
import com.example.qr_quest.QR;
import com.example.qr_quest.User;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class LeaderboardTest {
    ArrayList<User> users;
    ArrayList<QR> qrs;

    Leaderboard leaderboard;


    @Before
    public void setUp() {
        leaderboard = new Leaderboard();
        users = new ArrayList<User>();
        users.add(new User("user1", "user1@example.com", "user1", "user1", "1234567890", 10L, new ArrayList<String>()));
        users.add(new User("user2", "user2@example.com", "user2", "user2", "1234567890", 20L, new ArrayList<String>()));
        users.add(new User("user3", "user3@example.com", "user3", "user3", "1234567890", 30L, new ArrayList<String>()));
        leaderboard.setUsersSortedByPoints(users);
        leaderboard.setUsersSortedByQRsCollected(users);

        qrs = new ArrayList<>();
        QR qr1 = new QR("QR1");
        qr1.setLocation(111.11, 111.11, "Edmonton");
        qrs.add(qr1);
        qr1.setName("BestQR");
        qrs.add(new QR("QR2"));
        qrs.add(new QR("QR3"));
        leaderboard.setQrsSortedByPoints(qrs);

    }

    @Test
    public void testFilterQRs(){
        assertEquals(leaderboard.getQrsSortedByPoints().size(), 3);
        leaderboard.filter("-","Edmonton" );
        assertEquals(leaderboard.getQrsSortedByPoints().size(), 1);

        leaderboard.filter("BestQR","-" );
        assertEquals(leaderboard.getQrsSortedByPoints().size(), 1);

        leaderboard.filter("BestQR", "Edmonton");
        assertEquals(leaderboard.getQrsSortedByPoints().size(), 1);

    }

    @Test
    public void testFilterUser() {
        assertEquals(leaderboard.getUsersSortedByQRsCollected().size(), 3);

        leaderboard.filter("user", "-");
        assertEquals(leaderboard.getUsersSortedByPoints().size(), 3);

        leaderboard.filter("user1", "-");
        assertEquals(leaderboard.getUsersSortedByPoints().size(), 1);

        leaderboard.filter("user1", "Edmonton");
        assertEquals(leaderboard.getUsersSortedByPoints().size(), 1);
    }

}
