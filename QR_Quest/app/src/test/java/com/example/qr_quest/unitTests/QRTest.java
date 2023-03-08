package com.example.qr_quest.unitTests;

import static org.junit.Assert.assertEquals;

import com.example.qr_quest.QR;

import org.junit.Test;

public class QRTest {
    private QR mockQr() {
        return new QR("123456");
    }

    @Test
    public void getHashScore() {
        QR qr = mockQr();
        assertEquals("8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", qr.getHashValue());
    }

    @Test
    public void getQrScore() {
        QR qr = mockQr();
        assertEquals(Integer.valueOf(451), qr.getScore());
    }

    @Test
    public void getAvatar() {
        QR qr = mockQr();
        assertEquals("()VVVV()\n" + "5() e e ()5\n" + "()  ?  ()\n" + "()  x  ()\n" + "()VVVV()", qr.getAvatar().getAvatarFigure());
        assertEquals("SmallFiveAntennaedStrongBaldDino", qr.getAvatar().getAvatarName());
    }

}
