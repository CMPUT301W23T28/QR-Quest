package com.example.qr_quest;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comment {

    String commenter;
    String comment;

    public Comment(String commenter, String comment) {
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

    public static void fillComment(QR qrCode, OnSuccessListener<Comment[]> listener) {
        // Retrieve all QR comments from the database
        QRDatabase.getAllComments(qrCode, commentMap -> {
            List<Comment> commentList = new ArrayList<>();
            for (Object commenter : commentMap.keySet()) {
                String comment = (String) commentMap.get(commenter);
                String commenterString = (String) commenter;

                // Create a new Comment object for each comment and add it to the list
                Comment newComment = new Comment(commenterString, comment);
                commentList.add(newComment);
            }
            // Convert the list to an array and return it through the listener
            Comment[] comments = commentList.toArray(new Comment[0]);
            listener.onSuccess(comments);
        });
    }
}
