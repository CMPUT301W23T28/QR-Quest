package com.example.qr_quest;

import com.google.common.hash.Hashing;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 * QR class represents a QR code and stores its content, hash value, score, and avatar.
 * It also provides methods to generate hash value, score the QR code, and create an avatar from its hash value.
 * Additionally, it provides methods to set and get location, caption, and image string of the QR code.
 */
public class QR implements Serializable {
    private String content, hashValue, name, icon;
    private Avatar avatar;
    private Integer score;

    String city = "", imgString = "", caption = "";
    double latitude = -999.0, longitude = -999.0;


    QR () {}

    /**
     * Constructs a new QR object with the given content.
     * It generates the hash value, scores the QR code, and creates an avatar from the hash value.
     *
     * @param content the content of the QR code
     */
    public QR(String content){
        this.content = content;
        this.score = 0;
        generateHashValue(content);
        scoreQR(hashValue);
        createAvatar(hashValue);
    }

    /**
     * Generates the hash value of the given string using SHA-256 algorithm and UTF-8 encoding.
     *
     * @param string the string to be hashed
     */

    private void generateHashValue(String string) {
        this.hashValue = Hashing.sha256()
                .hashString(string, StandardCharsets.UTF_8)
                .toString();
    }

    /**
     * Scores the QR code based on its hash value.
     * The score is calculated by XOR-ing the decimal value of each character with the count of consecutive same characters,
     * raised to the power of the count minus one.
     *
     * @param qrHashValue the hash value of the QR code
     */
    private void scoreQR(String qrHashValue) {
        int count = 0;
        char previousChar = ' ';
        for (int i=0; i<qrHashValue.length(); i++) {
            char currentChar = qrHashValue.charAt(i);
            if(currentChar == previousChar) {
                count++;
            }
            else {
                int decimalValue = Integer.parseInt(String.valueOf(currentChar), 16);
                if (decimalValue == 0) {
                    this.score += 20 ^ (count -1);
                }
                else{
                    this.score += decimalValue ^ (count -1);
                }
            }
            previousChar = currentChar;
        }
    }

    /**
     * Creates an avatar from the hash value of the QR code.
     *
     * @param qrHashValue the hash value of the QR code
     */

    private void createAvatar(String qrHashValue) {
        this.avatar = new Avatar(qrHashValue);
        this.name = this.avatar.getAvatarName();
        this.icon = this.avatar.getAvatarFigure();
    }


    /**
     * Returns the score of the QR code.
     * The score is calculated by XOR-ing the decimal value of each character with the count of consecutive same characters,
     * raised to the power of the count minus one.
     * @return the score of the QR code
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Returns the hash value of the QR code.
     * @return the hash value of the QR code
     */

    public String getHashValue() {
        return this.hashValue;
    }

    /**
     * Returns the avatar object of the QR code.
     * @return the avatar object of the QR code
     */

    public Avatar getAvatar() {
        return this.avatar;
    }

    /**
     * Returns the name of the QR code's avatar.
     * @return the name of the QR code's avatar
     */

    public String getQRName(){
        return this.name;
    }

    /**
     * Returns the icon of the QR code's avatar.
     * @return the icon of the QR code's avatar
     */

    public String getQRIcon(){
        return this.icon;
    }
    /**
     * Returns the latitude of the QR code's location.
     * @return the latitude of the QR code's location
     */


    public double getLatitude() { return this.latitude;}

    /**
     * Returns the longitude of the QR code's location.
     * @return the longitude of the QR code's location
     */

    public double getLongitude() { return this.longitude;}

    /**
     * Returns the city of the QR code's location.
     * @return the city of the QR code's location
     */

    public String getCity() { return this.city;}

    /**
     * Returns the caption of the QR code's image.
     * @return the caption of the QR code's image
     */

    public String getCaption() { return this.caption;}

    /**
     * Returns the image string of the QR code's image.
     * @return the image string of the QR code's image
     */

    public String getImgString() { return this.imgString;}

    /**
     * Sets the location of the QR code.
     * @param latitude the latitude of the QR code's location
     * @param longitude the longitude of the QR code's location
     * @param city the city of the QR code's location
     */

    public void setLocation(double latitude, double longitude, String city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    /**
     * Sets the caption of the QR code's image.
     * @param caption the caption of the QR code's image
     */

    public void setCaption(String caption) { this.caption = caption;}

    /**
     * Sets the image string for the object.
     * @param imgString a string representation of the image to be set
     */
    public void setImgString(String imgString) { this.imgString = imgString;}
 }
