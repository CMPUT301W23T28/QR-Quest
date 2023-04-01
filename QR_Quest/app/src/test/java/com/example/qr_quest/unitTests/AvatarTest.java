package com.example.qr_quest.unitTests;

import static org.junit.Assert.assertEquals;

import com.example.qr_quest.Avatar;

import org.junit.Test;

public class AvatarTest {
    private Avatar mockAvatar() {
        return new Avatar("123456");
    }

    @Test
    public void getAvatarName() {
        Avatar avatar = mockAvatar();
        assertEquals("Der3HeadedTrickyLandKong", avatar.getAvatarName());
    }

    @Test
    public void getAvatarFigure() {
        Avatar avatar = mockAvatar();
        assertEquals(":####:\n" + "e: - - :e\n" + ":  V  :\n" + ":  n  :\n" + ":####:", avatar.getAvatarFigure());
    }
}