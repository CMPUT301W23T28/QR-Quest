package com.example.qr_quest.unitTests;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import androidx.test.core.app.ApplicationProvider;

import com.example.qr_quest.LeaderboardDatabase;
import com.example.qr_quest.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.powermock.api.mockito.PowerMockito;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@RunWith(MockitoJUnitRunner.class)
@RunWith(JUnit4.class)
@Config(sdk = 33)
public class LeaderboardDatabaseTest {
    FirebaseFirestore mockFirebaseFirestore;
    CollectionReference mockCollectionRef;
    QuerySnapshot mockQuerySnapshot;
    DocumentSnapshot mockDocSnapshot;
    Query mockQuery;

    private List<User> testUserList;

    private static <T> Task<T> getTask(T result) {
        TaskCompletionSource<T> taskCompletionSource = new TaskCompletionSource<>();
        taskCompletionSource.setResult(result);
        return taskCompletionSource.getTask();
    }

    @Before
    public void setUp(){
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());
        mockFirebaseFirestore = PowerMockito.mock(FirebaseFirestore.class);
        mockCollectionRef = PowerMockito.mock(CollectionReference.class);
        mockQuery = PowerMockito.mock(Query.class);
        mockQuerySnapshot = PowerMockito.mock(QuerySnapshot.class);
        mockDocSnapshot = PowerMockito.mock(DocumentSnapshot.class);
        PowerMockito.when(mockFirebaseFirestore.collection("users")).thenReturn(mockCollectionRef);
        PowerMockito.when(mockCollectionRef.orderBy(any(String.class), any(Query.Direction.class))).thenReturn(mockQuery);
    }

    @Test
    public void testGetAllUsersByPoints() {
        // Create some mock data
        List<DocumentSnapshot> docList = new ArrayList<>();
        docList.add(mockDocSnapshot);
        docList.add(mockDocSnapshot);

        PowerMockito.when(mockQuery.get()).thenReturn(getTask(mockQuerySnapshot));
        PowerMockito.when(mockQuerySnapshot.getDocuments()).thenReturn(docList);
        PowerMockito.when(mockDocSnapshot.getLong("score")).thenReturn(100L);
        PowerMockito.when(mockDocSnapshot.getString("user_name")).thenReturn("Alice");
        PowerMockito.when(mockDocSnapshot.getString("email")).thenReturn("alice@example.com");
        PowerMockito.when(mockDocSnapshot.getString("first_name")).thenReturn("Alice");
        PowerMockito.when(mockDocSnapshot.getString("last_name")).thenReturn("Smith");
        PowerMockito.when(mockDocSnapshot.getString("phone")).thenReturn("555-1234");
        PowerMockito.when(mockDocSnapshot.get("qr_code_list")).thenReturn(Arrays.asList("abc123", "def456"));

        // Call the method under test
        TaskCompletionSource<List<User>> taskCompletionSource = new TaskCompletionSource<>();
        LeaderboardDatabase.getAllUsersByPoints(taskCompletionSource::setResult);

        // Wait for the task to complete
        while (!taskCompletionSource.getTask().isComplete()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        // Verify the results
        List<User> result = taskCompletionSource.getTask().getResult();
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getUsername());
        assertEquals(100L, result.get(0).getScore());
        assertEquals(1, result.get(0).getPointsRank());
        assertEquals("Alice", result.get(0).getFirstName());
        assertEquals("Smith", result.get(0).getLastName());
        assertEquals("alice@example.com", result.get(0).getEmail());
        assertEquals("555-1234", result.get(0).getPhoneNumber());
        // assertEquals(Arrays.asList("abc123", "def456"), result.get(0).getQRCodeList());
    }
    }

    @Test
    public void testGetAllUsersByQRNums() {
        // Mock the success listener
        OnSuccessListener<List<User>> listener = userList -> {
            // Verify that the list is sorted by QR code numbers
            assertEquals(testUserList.get(1).getUsername(), userList.get(0).getUsername());
            assertEquals(testUserList.get(0).getUsername(), userList.get(1).getUsername());

            // Verify that the user ranks are set correctly
            assertEquals(1, userList.get(0).getQRNumRank());
            assertEquals(2, userList.get(1).getQRNumRank());
        };

        // Call the method and pass in the mocked listener
        LeaderboardDatabase.getAllUsersByQRNums(listener);
    }
}