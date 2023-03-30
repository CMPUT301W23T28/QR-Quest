package com.example.qr_quest.unitTests;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qr_quest.ItemClickListener;
import com.example.qr_quest.LeaderboardPointsAdapter;
import com.example.qr_quest.Leaderboard;
import com.example.qr_quest.User;

import java.util.ArrayList;
import java.util.Collection;

public class LeaderboardPointsAdapterTest {

    private ArrayList<User> users;
    private LeaderboardPointsAdapter testAdapter;

    @Mock
    private ItemClickListener mockClickListener;

    @Before
    public void setup() {
        users = new ArrayList<User>();
        users.add(new User("user1", "u1@gmail.com", "test0", "test1", "1234", 100L, new ArrayList<String>()));
        users.add(new User("user2", "u2@gmail.com", "test2", "test3", "5678", 200L, new ArrayList<String>()));
//        testAdapter = new LeaderboardPointsAdapter(users);
    };

    @Test
    public void testGetItemCount() {
        assertEquals(2, testAdapter.getItemCount());
    }
}
