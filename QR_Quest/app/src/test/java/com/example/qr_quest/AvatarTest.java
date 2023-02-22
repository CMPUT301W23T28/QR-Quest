package com.example.qr_quest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AvatarTest {
    @Test
    public Avatar mockAvatar() {
        Avatar avatar = new Avatar("123456");
        return avatar;
    }

    @Test
    public void getAvatarName() {
        Avatar avatar = mockAvatar();
        assertEquals("FatOneHeadedTrickyLandKingKong", avatar.getAvatarName());
    }


}
