package com.example.qr_quest;

public class comment {

    String commenter;
    String comment;

    public comment(String commenter, String comment) {
        this.commenter = commenter;
        this.comment = comment;
    }

    public String getCommenter() {
        return commenter;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
