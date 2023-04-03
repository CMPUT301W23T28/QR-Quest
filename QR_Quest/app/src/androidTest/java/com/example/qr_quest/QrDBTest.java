package com.example.qr_quest;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

public class QrDBTest {
    private Solo solo;
    private QRDatabase mockQRDatabase;
    private QRDatabase.QRExistsCallback mockExistsCallback;

    @Before
    public void setUp() throws Exception{
        mockQRDatabase = Mockito.mock(QRDatabase.class);
        mockExistsCallback = Mockito.mock(QRDatabase.QRExistsCallback.class);
    }

    @After
    public void tearDown() {
        mockQRDatabase = null;
    }

    @Test
    public void testQRExists() {
        Mockito.doAnswer(invocation -> {
            QRDatabase.QRExistsCallback callback = invocation.getArgument(0);
            callback.onQRExists(true);
            return null;
        }).when(mockQRDatabase).setQRExistsCallback(mockExistsCallback);

        mockQRDatabase.setQRExistsCallback(mockExistsCallback);
        Mockito.verify(mockExistsCallback).onQRExists(true);
    }

    @Test
    public void testQRDoesNotExist() {
        Mockito.doAnswer(invocation -> {
            QRDatabase.QRExistsCallback callback = invocation.getArgument(0);
            callback.onQRExists(false);
            return null;
        }).when(mockQRDatabase).setQRExistsCallback(mockExistsCallback);

        mockQRDatabase.setQRExistsCallback(mockExistsCallback);
        Mockito.verify(mockExistsCallback).onQRExists(false);

    }
}
