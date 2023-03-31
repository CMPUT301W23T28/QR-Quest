package com.example.qr_quest.unitTests;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;

import static org.junit.Assert.*;

import com.example.qr_quest.ItemClickListener;
import com.example.qr_quest.QR;
import com.example.qr_quest.WalletAdapter;

public class WalletAdapterTest {

    private QR[] testWallets;
    private WalletAdapter testAdapter;

    @Mock
    private ItemClickListener mockClickListener;

    @Before
    public void setup() {
        testWallets = new QR[]{
                new QR("Wallet 1"),
                new QR("Wallet 2"),
                new QR("Wallet 3")
        };
        testAdapter = new WalletAdapter(testWallets);
        testAdapter.setClickListener(mockClickListener);
    }

    @Test
    public void testGetItemCount() {
        assertEquals(3, testAdapter.getItemCount());
    }
}
