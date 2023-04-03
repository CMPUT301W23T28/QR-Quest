package com.example.qr_quest;

import android.view.View;
/**
 * Interface definition for a callback to be invoked when an item in a RecyclerView is clicked.
 */
public interface ItemClickListener {
    /**
     * Called when an item in a RecyclerView has been clicked.
     *
     * @param view The View that was clicked.
     * @param position The position of the clicked item in the RecyclerView.
     */
    void onClick(View view, int position);
}
