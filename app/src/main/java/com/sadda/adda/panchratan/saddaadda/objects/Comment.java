package com.sadda.adda.panchratan.saddaadda.objects;

import java.io.PrintWriter;
import java.lang.ref.SoftReference;
import java.security.PrivateKey;

/**
 * Created by user on 22-07-2017.
 */
public class Comment {

    private int commentId;
    private String userName;
    private String comment;
    private String timeAndDate;
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimeAndDate() {
        return timeAndDate;
    }

    public void setTimeAndDate(String timeAndDate) {
        this.timeAndDate = timeAndDate;
    }
}
