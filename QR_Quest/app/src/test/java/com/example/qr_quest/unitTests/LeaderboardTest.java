package com.example.qr_quest.unitTests;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import com.example.qr_quest.Leaderboard;
import com.example.qr_quest.QR;
import com.example.qr_quest.User;

public class LeaderboardTest {
    private Leaderboard leaderboard;

    @Before
    public void setUp() {
//        leaderboard = new Leaderboard();
    }

    @Test
    public void testGetUsersSortedByPoints() {
        ArrayList<User> users = leaderboard.getUsersSortedByPoints();
        assertNotNull(users);
        assertEquals(3, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
        assertEquals("user3", users.get(2).getUsername());
    }

    @Test
    public void testGetUsersSortedByQRsCollected() {
        ArrayList<User> users = leaderboard.getUsersSortedByQRsCollected();
        assertNotNull(users);
        assertEquals(3, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
        assertEquals("user3", users.get(2).getUsername());
    }

    @Test
    public void testGetQrsSortedByPoints() {
        ArrayList<QR> qrs = leaderboard.getQrsSortedByPoints();
        assertNotNull(qrs);
        assertEquals(3, qrs.size());
        assertEquals("mmm", qrs.get(0).getContent());
        assertEquals("ooo", qrs.get(1).getContent());
        assertEquals("aaa", qrs.get(2).getContent());
    }
}
