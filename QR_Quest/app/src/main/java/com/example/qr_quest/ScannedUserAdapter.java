package com.example.qr_quest;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

/**
 * Adapter for the RecyclerView that displays the list of scanned users.
 */
public class ScannedUserAdapter extends RecyclerView.Adapter<ScannedUserAdapter.ViewHolder> {
    private List<String> mScannedUserList;

    /**
     * ViewHolder class for the ScannedUserAdapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mUserName;

        /**
         * Constructs a new ViewHolder object.
         * @param cardView
         *      the CardView that contains the user information
         */
        public ViewHolder(CardView cardView) {
            super(cardView);
            mCardView = cardView;
            mUserName = cardView.findViewById(R.id.txtview_scanned_user_list_user_name);
        }
    }

    /**
     * Constructs a new ScannedUserAdapter object.
     * @param scannedUserList
     *      the list of scanned users to display
     */
    public ScannedUserAdapter(List<String> scannedUserList) {
        mScannedUserList = scannedUserList;
    }

    /**
     * Creates a new ViewHolder object
     * @param parent
     *          the ViewGroup into which the new View will be added after it is bound to an adapter position
     * @param viewType
     *      the type of the new View
     * @return a new ViewHolder object
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.scanned_users_list_item, parent, false);
        return new ViewHolder(cardView);
    }

    /**
     * Binds a ViewHolder object to the data at the specified position in the list.
     *
     * @param holder
     *      the ViewHolder object to bind
     * @param position
     *      the position of the data to bind
     */

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String scannedUser = mScannedUserList.get(position);
        holder.mUserName.setText(scannedUser);
    }

    /**
     * Returns the number of items in the list.
     *
     * @return the number of items in the list
     */
    @Override
    public int getItemCount() {
        return mScannedUserList.size();
    }
}