package com.example.qr_quest.unitTests;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import com.example.qr_quest.User;

public class UserTest {

    @Test
    public void testConstructor() {
        ArrayList<String> qrCodes = new ArrayList<>();
        qrCodes.add("qr1");
        qrCodes.add("qr2");
        User user = new User("johnDoe", "johndoe@gmail.com", "John", "Doe", "123-456-7890", 100, qrCodes);

        assertEquals("johnDoe", user.getUsername());
        assertEquals("johndoe@gmail.com", user.getEmail());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("123-456-7890", user.getPhoneNumber());
        assertEquals(100, user.getScore());
        assertEquals(qrCodes, user.getQRCodes());
    }

    @Test
    public void testEmptyConstructor() {
        User user = new User();

        assertNull(user.getUsername());
        assertEquals(0, user.getScore());
        assertNull(user.getQRCodes());
    }

    @Test
    public void testGettersAndSetters() {
        User user = new User();
        user.setUsername("test_user");
        user.setScore(100);
        List<String> qrCodes = new ArrayList<>();
        qrCodes.add("qr1");
        qrCodes.add("qr2");
        user.setQRCodes(qrCodes);

        assertEquals("test_user", user.getUsername());
        assertEquals(100, user.getScore());
        assertEquals(qrCodes, user.getQRCodes());
        assertEquals(2, user.getQRCodes().size());
    }


}

