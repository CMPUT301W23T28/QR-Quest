package com.example.qr_quest;

import android.view.View;
/**
 * Interface definition for a callback to be invoked when an item in a RecyclerView is clicked.
 */
public interface ItemClickListener {
    void onClick(View view, int position);
}
