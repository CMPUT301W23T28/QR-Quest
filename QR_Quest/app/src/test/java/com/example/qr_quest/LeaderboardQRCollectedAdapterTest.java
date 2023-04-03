package com.example.qr_quest;

import static org.junit.Assert.assertEquals;

import com.example.qr_quest.LeaderboardQRCollectedAdapter;
import com.example.qr_quest.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

public class LeaderboardQRCollectedAdapterTest {
    private ArrayList<User> users;
    private LeaderboardQRCollectedAdapter testAdapter;

    @Mock
    private LeaderboardQRCollectedAdapter.OnItemClickListener mockClickListener;

    @Before
    public void setup() {
        users = new ArrayList<User>();
        users.add(new User("user1", "u1@gmail.com", "test0", "test1", "1234", 100L, new ArrayList<String>()));
        users.add(new User("user2", "u2@gmail.com", "test2", "test3", "5678", 200L, new ArrayList<String>()));
        testAdapter = new LeaderboardQRCollectedAdapter("user1",users,  mockClickListener);
    };

    @Test
    public void testGetItemCount() {
        assertEquals(2, testAdapter.getItemCount());
    }

}
