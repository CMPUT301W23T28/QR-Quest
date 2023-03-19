package com.example.qr_quest.unitTests;

import org.junit.Before;
import org.junit.Test;

import com.example.qr_quest.Wallet;

import static org.junit.Assert.*;

public class WalletTest {

    private String d = "testD";
    private String name = "testName";
    private String points = "10";

    @Test
    public void TestConstructor(){
        Wallet wallet = new Wallet(d, name, points);
        assertEquals(wallet.getD() , "testD");
        assertEquals(wallet.getName(), "testName");
        assertEquals(wallet.getPoints(), "10");
    }

    @Test
    public void testGetD() {
        Wallet wallet = new Wallet(d, name, points);
        assertEquals(d, wallet.getD());
    }

    @Test
    public void testSetD() {
        Wallet wallet = new Wallet(d, name, points);
        wallet.setD("456");
        assertEquals("456", wallet.getD());
    }

    @Test
    public void testGetName() {
        Wallet wallet = new Wallet(d, name, points);
        assertEquals(name, wallet.getName());
    }

    @Test
    public void testSetName() {
        Wallet wallet = new Wallet(d, name, points);
        wallet.setName("New Wallet");
        assertEquals("New Wallet", wallet.getName());
    }

    @Test
    public void testGetPoints() {
        Wallet wallet = new Wallet(d, name, points);
        assertEquals(points, wallet.getPoints());
    }

    @Test
    public void testSetPoints() {
        Wallet wallet = new Wallet(d, name, points);
        wallet.setPoints("100");
        assertEquals("100", wallet.getPoints());
    }

}
