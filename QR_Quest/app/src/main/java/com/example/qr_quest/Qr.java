package com.example.qr_quest;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Qr{
    private String content, hashValue;
    private Avatar avatar;
    private Integer score;
    private ArrayList<User> owners;

    Qr(String content){
        this.content = content;
        this.score = 0;
        generateHashValue(content);
        scoreQr(hashValue);
        // createAvatar(hashValue);
    }

    private void generateHashValue(String string) {
        this.hashValue = Hashing.sha256()
                .hashString(string, StandardCharsets.UTF_8)
                .toString();
    }

    private void scoreQr(String qrHashValue) {
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

    private void createAvatar(String qrHashValue) {
        this.avatar = new Avatar(qrHashValue);
    }

    public Integer getScore() {
        return this.score;
    }

    public String getHashValue() {
        return this.hashValue;
    }

    public Avatar getAvatar() {
        return this.avatar;
    }
 }
