package com.example.qr_quest.unitTests;

import static org.junit.Assert.assertEquals;

import com.example.qr_quest.QR;

import org.junit.Test;

import java.util.Optional;

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
        assertEquals(new Long(273), qr.getScore());
    }

    @Test
    public void getAvatar() {
        QR qr = mockQr();
        assertEquals("()VVVV()\n" + "5() e e ()5\n" + "()  ?  ()\n" + "()  x  ()\n" + "()VVVV()", qr.getAvatar().getAvatarFigure());
        assertEquals("The14AntennaedStrongBaldDino", qr.getAvatar().getAvatarName());
    }

    @Test
    public void testQRLocation() {
        QR qr = mockQr();
        qr.setLocation(123.0, 555.2, "Edmonton");
        assertEquals(qr.getLatitude(), 123.0, 0.0001);
        assertEquals(qr.getLongitude(), 555.2, 0.0001);
    }
}
