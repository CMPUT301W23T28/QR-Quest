package com.example.qr_quest;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;

import static org.junit.Assert.*;

import com.example.qr_quest.ItemClickListener;
import com.example.qr_quest.QR;
import com.example.qr_quest.User;
import com.example.qr_quest.WalletAdapter;

import java.util.ArrayList;

public class WalletAdapterTest {

    private QR[] testWallets;
    private WalletAdapter testAdapter;

    @Mock
    private ItemClickListener mockClickListener;

    @Before
    public void setup() {
        ArrayList<String> qrCodes = new ArrayList<>();
        User user = new User("johnDoe", "johndoe@gmail.com", "John", "Doe", "123-456-7890", 100, qrCodes);
        testWallets = new QR[]{
                new QR("Wallet 1"),
                new QR("Wallet 2"),
                new QR("Wallet 3")
        };
        testAdapter = new WalletAdapter(testWallets, user, null);
        testAdapter.setClickListener(mockClickListener);
    }

    @Test
    public void testGetItemCount() {
        assertEquals(3, testAdapter.getItemCount());
    }
}
