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

/**
 * This class is responsible for adapting the data set of User objects to a RecyclerView
 * that displays a leaderboard of users based on the number of QR codes they have collected.
 */
public class LeaderboardQRCollectedAdapter extends RecyclerView.Adapter<LeaderboardQRCollectedAdapter.UserViewHolder> {
    private ArrayList<User> users;
    private String username;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    /**
     * Constructor for the LeaderboardQRCollectedAdapter.
     *
     * @param username
     *      The username
     * @param users
     *      The list of User objects
     * @param listener
     *      The listener for item clicks on the RecyclerView.
     */
    public LeaderboardQRCollectedAdapter(String username, ArrayList<User> users,OnItemClickListener listener)
    {
        this.username = username;
        this.users = users;
        this.listener = listener;
    }

    /**
     * Filters the data set of User objects to be displayed in the RecyclerView based on a filtered list.
     *
     * @param filteredList
     *         The filtered list of User objects.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<User> filteredList) {
        users = filteredList;
        notifyDataSetChanged();
    }

    /**
     * This method inflates the layout for a ViewHolder.
     *
     * @param parent
     *          The ViewGroup into which the new View will be added.
     * @param viewType
     *          The type of the new View.
     * @return
     *          A new UserViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
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
    public void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.number.setText(Integer.toString(users.get(position).getQRNumRank()));
        holder.username.setText(users.get(position).getUsername());
        holder.info.setText(users.get(position).getQRCodes().size() + " QRs");

        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.leaderboard_default));
        if (users.get(position).getUsername().equals(username)) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.teal_200));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(users.get(position));
            }
        });
    }

    /**
     * This method returns the number of users in the leaderboard.
     *
     * @return The number of users in the leaderboard.
     */
    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * Custom ViewHolder for the RecyclerView.
     */
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
