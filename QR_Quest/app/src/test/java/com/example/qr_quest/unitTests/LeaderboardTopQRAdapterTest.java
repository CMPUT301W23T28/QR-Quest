package com.example.qr_quest.unitTests;
import static org.junit.Assert.assertEquals;

import com.example.qr_quest.LeaderboardQRCollectedAdapter;
import com.example.qr_quest.LeaderboardTopQRAdapter;
import com.example.qr_quest.QR;
import com.example.qr_quest.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
public class LeaderboardTopQRAdapterTest {
    private ArrayList<QR> qrs;
    private LeaderboardTopQRAdapter testAdapter;

    @Mock
    private LeaderboardQRCollectedAdapter.OnItemClickListener mockClickListener;

    @Before
    public void setup() {
        qrs = new ArrayList<QR>();
        User testUser = new User("user1", "u1@gmail.com", "test0", "test1", "1234", 100L, new ArrayList<String>());
        qrs.add(new QR("testQR2"));
        qrs.add(new QR("testQR3"));
        testAdapter = new LeaderboardTopQRAdapter(testUser, qrs);
    }

    @Test
    public void testGetItemCount() {
        assertEquals(2, testAdapter.getItemCount());
    }


}
