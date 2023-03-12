package com.example.qr_quest;

public class wallet {
    String d;
    String name;
    String points;

    public wallet(String d, String name, String points) {
        this.d = d;
        this.name = name;
        this.points = points;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}

