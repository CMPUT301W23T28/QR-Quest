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
        super();
        this.content = content;
        this.score = 0;
    }

    private String generateHashValue() {
        this.hashValue = Hashing.sha256()
                .hashString(this.content, StandardCharsets.UTF_8)
                .toString();
        return this.hashValue;
    }

    private void scoreQr(String string) {
        int count = 0;
        char previousChar = ' ';
        for (int i=0; i<string.length(); i++) {
            char currentChar = string.charAt(i);
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

    private Avatar createAvatar() {
        this.avatar = new Avatar(this.hashValue);
        return this.avatar;
    }


 }
