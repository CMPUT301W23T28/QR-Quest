package com.example.qr_quest;

import static androidx.test.InstrumentationRegistry.getContext;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
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
 * This class is an adapter for displaying the leaderboard sorted by points.
 */
public class LeaderboardPointsAdapter extends RecyclerView.Adapter<LeaderboardPointsAdapter.UserViewHolder> {
    private ArrayList<User> users;
    private String username;
    private OnItemClickListener listener;

    /**
     * Constructor for the LeaderboardPointsAdapter class.
     * @param username
     *      The username of the current user.
     * @param users
     *       An ArrayList of users to display in the leaderboard.
     * @param listener
     *       An OnItemClickListener to handle item clicks in the RecyclerView.
     */
    public LeaderboardPointsAdapter(String username, ArrayList<User> users, OnItemClickListener listener)
    {
        this.users = users;
        this.username = username;
        this.listener = listener;
    }

    /**
     * Interface for handling item clicks in the RecyclerView.
     */
    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    /**
     * This method inflates the layout for a ViewHolder.
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
     * @param holder
     *       The UserViewHolder to bind the data to.
     * @param position
     *       The position of the user in the leaderboard.
     */
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.number.setText(Integer.toString(users.get(position).getPointsRank()));
        holder.username.setText(users.get(position).getUsername());
        holder.info.setText((int) users.get(position).getScore() + " pts");
        holder.mView.setTag(users.get(position).getUsername());

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
     * @return
     *      The number of users in the leaderboard.
     */
    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     *
     * This method filters the list of users in the leaderboard.
     * @param filteredList
     *          An ArrayList of users to filter the leaderboard by.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<User> filteredList) {
        users = filteredList;
        notifyDataSetChanged();
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
         * Returns the username of the user associated with this ViewHolder.
         * @return String containing the username of the user
         */
        public String  getUsername() {
            return  username.getText().toString();
        }

        /**
         * Constructor for the UserViewHolder class.
         * @param itemView View that represents the user item view
         */
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
          
            number = mView.findViewById(R.id.number);
            username = mView.findViewById(R.id.txtview_listitem_name);
            info = mView.findViewById(R.id.txtview_listitem_info);
            cardView = mView.findViewById(R.id.leaderboard_card);

        }
    }
}
