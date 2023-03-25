package com.example.qr_quest.unitTests;
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
import com.example.qr_quest.comment;
import com.example.qr_quest.CommentAdapter;

public class CommentAdapterTest {

    private comment[] testCommentList;
    private CommentAdapter testAdapter;

    @Mock
    private ItemClickListener mockClickListener;

    @Before
    public void setup() {
        testCommentList = new comment[]{
                new comment("John", "100"),
                new comment("Jane", "200")
        };
        testAdapter = new CommentAdapter(testCommentList);
    }

    @Test
    public void testGetItemCount() {
        assertEquals(2, testAdapter.getItemCount());
    }
}
