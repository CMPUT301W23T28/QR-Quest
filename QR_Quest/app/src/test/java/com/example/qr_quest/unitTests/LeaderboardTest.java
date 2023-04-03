package com.example.qr_quest.unitTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.example.qr_quest.Leaderboard;
import com.example.qr_quest.LeaderboardDatabase;
import com.example.qr_quest.QR;
import com.example.qr_quest.User;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LeaderboardTest {

    @Mock
    private LeaderboardDatabase leaderboardDatabaseMock;

    private Leaderboard leaderboard;

    @Before
    public void setUp() {
        leaderboard = new Leaderboard();
        leaderboard.callDatabase(OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                leaderboard.setLists(success -> {});
            }
        });
    }

    @Test
    public void testSetLists() {
        // Mock the return values of the LeaderboardDatabase methods
        ArrayList<User> userListByPoints = new ArrayList<>();
//        userListByPoints.add(new User("user1", 100));
//        userListByPoints.add(new User("user2", 200));
        when(leaderboardDatabaseMock.getAllUsersByPoints(any())).thenAnswer((Answer<Void>) invocation -> {
            OnSuccessListener<ArrayList<User>> listener = invocation.getArgument(0);
            listener.onSuccess(userListByPoints);
            return null;
        });

        ArrayList<User> userListByQRCollected = new ArrayList<>();
//        userListByQRCollected.add(new User("user1", 2));
//        userListByQRCollected.add(new User("user2", 1));
        when(leaderboardDatabaseMock.getAllUsersByQRNums(any(String.class))).thenAnswer((Answer<Void>) invocation -> {
            OnSuccessListener<ArrayList<User>> listener = invocation.getArgument(0);
            listener.onSuccess(userListByQRCollected);
            return null;
        });

        ArrayList<QR> qrList = new ArrayList<>();
        qrList.add(new QR("qr1"));
        qrList.add(new QR("qr2"));
        when(leaderboardDatabaseMock.getAllQRsByScore(any(String.class))).thenAnswer((Answer<Void>) invocation -> {
            OnSuccessListener<ArrayList<QR>> listener = invocation.getArgument(0);
            listener.onSuccess(qrList);
            return null;
        });

        // Call the setLists method and verify the results
        OnSuccessListener<Boolean> listenerMock = Mockito.mock(OnSuccessListener.class);
        leaderboard.setLists(listenerMock);

        verify(listenerMock).onSuccess(true);
        assertEquals(userListByPoints, leaderboard.getUsersSortedByPoints());
        assertEquals(userListByQRCollected, leaderboard.getUsersSortedByQRsCollected());
        assertEquals(qrList, leaderboard.getQrsSortedByPoints());
    }

}
