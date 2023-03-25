package com.example.qr_quest.unitTests;


import static org.junit.Assert.assertEquals;

import com.example.qr_quest.comment;

import org.junit.Test;

public class CommentTest {

    @Test
    public void testConstructor(){
        comment testComment = new comment("John", "test comment");
        assertEquals(testComment.getCommenter() , "John");
        assertEquals(testComment.getComment(), "test comment");
    }

    @Test
    public void testGetCommenter() {
        comment testComment = new comment("John", "This is a test comment");
        assertEquals("John", testComment.getCommenter());
    }

    @Test
    public void testSetCommenter() {
        comment testComment = new comment("John", "This is a test comment");
        testComment.setCommenter("Jane");
        assertEquals("Jane", testComment.getCommenter());
    }

    @Test
    public void testGetComment() {
        comment testComment = new comment("John", "This is a test comment");
        assertEquals("This is a test comment", testComment.getComment());
    }

    @Test
    public void testSetComment() {
        comment testComment = new comment("John", "This is a test comment");
        testComment.setComment("This is a new comment");
        assertEquals("This is a new comment", testComment.getComment());
    }

}
