package com.example.qr_quest;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class UserDBTest {
    private Solo solo;
    private UserDatabase mockUserDatabase;
    private UserDatabase.UserExistsCallback mockExistsCallback;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);


    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        mockUserDatabase = Mockito.mock(UserDatabase.class);
        mockExistsCallback = Mockito.mock(UserDatabase.UserExistsCallback.class);
    }

    @After
    public void tearDown() {
        mockUserDatabase = null;
    }

    @Test
    public void testUserExists() {
        Mockito.doAnswer(invocation -> {
            UserDatabase.UserExistsCallback callback = invocation.getArgument(0);
            callback.onUserExists(true);
            return null;
        }).when(mockUserDatabase).setUserExistsCallback(mockExistsCallback);

        mockUserDatabase.setUserExistsCallback(mockExistsCallback);
        Mockito.verify(mockExistsCallback).onUserExists(true);
    }

    @Test
    public void testUserDoesNotExist() {
        Mockito.doAnswer(invocation -> {
            UserDatabase.UserExistsCallback callback = invocation.getArgument(0);
            callback.onUserExists(false);
            return null;
        }).when(mockUserDatabase).setUserExistsCallback(mockExistsCallback);

        mockUserDatabase.setUserExistsCallback(mockExistsCallback);
        Mockito.verify(mockExistsCallback).onUserExists(false);

    }
}
