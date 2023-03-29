package com.example.qr_quest.unitTests;


import static org.junit.Assert.assertEquals;

import com.example.qr_quest.Comment;

import org.junit.Test;

public class CommentTest {

    @Test
    public void testConstructor(){
        Comment testComment = new Comment("John", "test comment");
        assertEquals(testComment.getCommenter() , "John");
        assertEquals(testComment.getComment(), "test comment");
    }

    @Test
    public void testGetCommenter() {
        Comment testComment = new Comment("John", "This is a test comment");
        assertEquals("John", testComment.getCommenter());
    }

    @Test
    public void testSetCommenter() {
        Comment testComment = new Comment("John", "This is a test comment");
        testComment.setCommenter("Jane");
        assertEquals("Jane", testComment.getCommenter());
    }

    @Test
    public void testGetComment() {
        Comment testComment = new Comment("John", "This is a test comment");
        assertEquals("This is a test comment", testComment.getComment());
    }

    @Test
    public void testSetComment() {
        Comment testComment = new Comment("John", "This is a test comment");
        testComment.setComment("This is a new comment");
        assertEquals("This is a new comment", testComment.getComment());
    }

}
