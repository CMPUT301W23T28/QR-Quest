package com.example.qr_quest;

import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a comment made by a user on a QR code. It includes the user's name who made the
 * comment and the comment's text. This class also provides a method to fill a list of comments for a
 * given QR code, retrieved from a database.
 */
public class Comment {

    String commenter;
    String comment;

    /**
     * Creates a new Comment object with the specified commenter and comment.
     *
     * @param commenter
     *      The name of the user who made the comment.
     * @param comment
     *      The comment made by the user.
     */

    public Comment(String commenter, String comment) {
        this.commenter = commenter;
        this.comment = comment;
    }

    /**
     * Gets the name of the commenter who made the comment.
     *
     * @return The name of the commenter who made the comment.
     */
    public String getCommenter() {
        return commenter;
    }

    /**
     * Sets the name of the commenter who made the comment.
     *
     * @param commenter
     *      The name of the commenter who made the comment.
     */
    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    /**
     * Gets the comment made by the user.
     *
     * @return The comment made by the user.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment made by the user.
     *
     * @param comment
     *      The comment made by the user.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     Fills a list of comments for a QR code, retrieved from a database, and returns it via a listener.

     @param qrCode
            A QR object representing the QR code to retrieve comments for.
     @param listener
            An OnSuccessListener object that will receive the list of comments
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
