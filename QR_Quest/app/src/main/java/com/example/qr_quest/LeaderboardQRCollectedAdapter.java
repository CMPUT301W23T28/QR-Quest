package com.example.qr_quest;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LeaderboardQRCollectedAdapter extends RecyclerView.Adapter<LeaderboardQRCollectedAdapter.UserViewHolder> {
    private ArrayList<User> users;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public LeaderboardQRCollectedAdapter(ArrayList<User> users,OnItemClickListener listener)
    {
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
        View mView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            try{
                number = (TextView) mView.findViewById(R.id.number);
                username = (TextView) mView.findViewById(R.id.name);
                info = (TextView) mView.findViewById(R.id.info);
            }catch (Exception e){
                Log.d("Error in LeaderboardQRCollectedAdapter", "UserViewHolder: ", e);
            }
        }
    }
}
