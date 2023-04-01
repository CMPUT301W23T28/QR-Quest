package com.example.qr_quest.unitTests;
import org.checkerframework.checker.units.qual.C;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qr_quest.ItemClickListener;
import com.example.qr_quest.Comment;
import com.example.qr_quest.CommentAdapter;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapterTest {

    private List<Comment> testCommentList;
    private CommentAdapter testAdapter;

    @Mock
    private ItemClickListener mockClickListener;

    @Before
    public void setup() {
        testCommentList = new ArrayList<Comment>();
        testCommentList.add(new Comment("John", "100"));
        testCommentList.add(new Comment("Jane", "200"));

        testAdapter = new CommentAdapter(testCommentList);
    }

    @Test
    public void testGetItemCount() {
        assertEquals(2, testAdapter.getItemCount());
    }
}
