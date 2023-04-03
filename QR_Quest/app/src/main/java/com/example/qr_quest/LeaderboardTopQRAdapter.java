package com.example.qr_quest;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for the RecyclerView that displays the top QR codes on the leaderboard.
 */
public class LeaderboardTopQRAdapter extends RecyclerView.Adapter<LeaderboardTopQRAdapter.UserViewHolder> {
    private ArrayList<QR> qrs;
    private User user;

    /**
     * Constructor for the adapter.
     * @param qrs
     *      the list of QR codes to be displayed
     * @param user
     *      the User object of the current user on the app
     */
    public LeaderboardTopQRAdapter(User user, ArrayList<QR> qrs)
    {
        this.qrs = new ArrayList<>(qrs);
        this.user = user;
    }

    /**
     * Filters the data set of User objects to be displayed in the RecyclerView based on a filtered list.
     *
     * @param filteredList
     *         The filtered list of User objects.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<QR> filteredList) {
        qrs = filteredList;
        notifyDataSetChanged();
    }

    /**
     * This method inflates the layout for a ViewHolder.
     *
     * @param parent
     *      The ViewGroup into which the new View will be added.
     * @param viewType
     *      The type of the new View.
     * @return A new UserViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new UserViewHolder(view);
    }

    /**
     * This method binds data to a ViewHolder.
     *
     * @param holder
     *       The UserViewHolder to bind the data to.
     * @param position
     *       The position of the user in the leaderboard.
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.number.setText(Integer.toString(qrs.get(position).getRank()));
        holder.username.setText(qrs.get(position).getQRName());
        holder.info.setText(qrs.get(position).getScore() + " pts");

        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.leaderboard_default));
        if (chkQR(qrs.get(position).getQRName())) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.teal_200));
        }
    }

    /**
     * Checks if a given QR code name exists in the list of QR codes owned by the user.
     *
     * @param qrName
     *      The name of the QR code to check.
     * @return true if the QR code name exists in the list of user's QR codes, false otherwise.
     */
    public Boolean chkQR(String qrName) {
        List<String> qrCodes = user.getQRCodes();
        for(String qrNamerIterator : qrCodes) {
            if(qrNamerIterator.equals(qrName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method returns the number of users in the leaderboard.
     *
     * @return The number of users in the leaderboard.
     */
    @Override
    public int getItemCount() {
        return qrs.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView number;
        public TextView username;
        public TextView info;
        public CardView cardView;
        View mView;

        /**
         * Constructor for the UserViewHolder class.
         *
         * @param itemView
         *      View that represents the user item view
         */
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            number = mView.findViewById(R.id.number);
            username = mView.findViewById(R.id.txtview_listitem_name);
            info = mView.findViewById(R.id.txtview_listitem_info);
            cardView = mView.findViewById(R.id.leaderboard_card);
        }
    }
}
