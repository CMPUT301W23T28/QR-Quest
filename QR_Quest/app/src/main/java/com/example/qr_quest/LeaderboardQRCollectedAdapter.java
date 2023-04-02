package com.example.qr_quest;

import android.annotation.SuppressLint;
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

public class LeaderboardQRCollectedAdapter extends RecyclerView.Adapter<LeaderboardQRCollectedAdapter.UserViewHolder> {
    private ArrayList<User> users;
    private String username;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public LeaderboardQRCollectedAdapter(String username, ArrayList<User> users,OnItemClickListener listener)
    {
        this.username = username;
        this.users = users;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<User> filteredList) {
        users = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new UserViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.number.setText(Integer.toString(users.get(position).getQRNumRank()));
        holder.username.setText(users.get(position).getUsername());
        holder.info.setText(users.get(position).getQRCodes().size() + " QRs");

        if (users.get(position).getUsername().equals(username)) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.teal_200));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(users.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView number;
        public TextView username;
        public TextView info;
        public CardView cardView;
        View mView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;

            number = (TextView) mView.findViewById(R.id.number);
            username = (TextView) mView.findViewById(R.id.txtview_listitem_name);
            info = (TextView) mView.findViewById(R.id.txtview_listitem_info);
            cardView = mView.findViewById(R.id.leaderboard_card);
        }
    }
}
