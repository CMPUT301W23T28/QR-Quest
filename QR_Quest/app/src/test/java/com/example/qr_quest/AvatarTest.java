package com.example.qr_quest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AvatarTest {
    private Avatar mockAvatar() {
        return new Avatar("123456");
    }

    @Test
    public void getAvatarName() {
        Avatar avatar = mockAvatar();
        assertEquals("FatOneHeadedTrickyLandKingKong", avatar.getAvatarName());
    }


}