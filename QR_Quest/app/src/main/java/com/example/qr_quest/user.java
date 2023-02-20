package com.example.qr_quest;

public class user {

        String name;
        int topQr;
    int collectedQr;
    int regionQr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTopQr() {
        return topQr;
    }

    public void setTopQr(int topQr) {
        this.topQr = topQr;
    }

    public int getCollectedQr() {
        return collectedQr;
    }

    public void setCollectedQr(int collectedQr) {
        this.collectedQr = collectedQr;
    }

    public int getRegionQr() {
        return regionQr;
    }

    public void setRegionQr(int regionQr) {
        this.regionQr = regionQr;
    }



    public user(String name, int topQr, int collectedQr, int regionQr) {
        this.name = name;
        this.topQr = topQr;
        this.collectedQr = collectedQr;
        this.regionQr = regionQr;
    }
}
