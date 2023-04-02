package com.example.qr_quest;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represent the comment that the user makes on the QR code
 */
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

    /**
     Fills a list of comments for a QR code, retrieved from a database, and returns it via a listener.
     @param qrCode
            A QR object representing the QR code to retrieve comments for.
     @param listener
            An OnSuccessListener<List<Comment>> object that will receive the list of comments
     */
    public static void fillComment(QR qrCode, OnSuccessListener<List<Comment>> listener) {
        // Retrieve all QR comments from the database
        QRDatabase.getAllComments(qrCode, commentMap -> {
            if (commentMap == null || commentMap.isEmpty()) {
                listener.onSuccess(new ArrayList<>()); // return empty array
                return;
            }
            List<Comment> commentList = new ArrayList<>();
            for (Object commenter : commentMap.keySet()) {
                List<String> userComments = (List<String>) commentMap.get(commenter);
                String commenterString = (String) commenter;

                // Create a new Comment object for each comment and add it to the list
                for (String comment : userComments) {
                    Comment newComment = new Comment(commenterString, comment);
                    commentList.add(newComment);
                }
            }
            listener.onSuccess(commentList);
        });
    }
}
