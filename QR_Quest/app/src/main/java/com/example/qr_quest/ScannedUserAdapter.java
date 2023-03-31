package com.example.qr_quest;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class ScannedUserAdapter extends RecyclerView.Adapter<ScannedUserAdapter.ViewHolder> {
    private List<String> mScannedUserList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mUserName;

        public ViewHolder(CardView cardView) {
            super(cardView);
            mCardView = cardView;
            mUserName = cardView.findViewById(R.id.Scanned_user_list_user_name);
        }
    }

    public ScannedUserAdapter(List<String> scannedUserList) {
        mScannedUserList = scannedUserList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scanned_users_list_item, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String scannedUser = mScannedUserList.get(position);
        holder.mUserName.setText(scannedUser);
        // Set user avatar image here if you have it
    }

    @Override
    public int getItemCount() {
        return mScannedUserList.size();
    }
}