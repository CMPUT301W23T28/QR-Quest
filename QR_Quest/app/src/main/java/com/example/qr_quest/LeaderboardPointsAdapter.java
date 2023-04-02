package com.example.qr_quest;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LeaderboardPointsAdapter extends RecyclerView.Adapter<LeaderboardPointsAdapter.UserViewHolder> {
    private ArrayList<User> users;
    private  RecyclerView recyclerView;

    public LeaderboardPointsAdapter(RecyclerView recyclerView, ArrayList<User> users, OnItemClickListener listener)
    {
        this.recyclerView = recyclerView;
        this.users = users;
        this.listener = listener;
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(User user);
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
        holder.number.setText(Integer.toString(users.get(position).getPointsRank()));
        holder.username.setText(users.get(position).getUsername());
        holder.info.setText((int) users.get(position).getScore() + " pts");
        holder.mView.setTag(users.get(position).getUsername());


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


    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<User> filteredList) {
        users = filteredList;
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView number;
        public TextView username;
        public TextView info;
        public View mView;

        public String  getUsername() {
            return  username.getText().toString();
        }

        public int isViewVisible() {
            return mView.getVisibility();
        }



        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;

            number = (TextView) mView.findViewById(R.id.number);
            username = (TextView) mView.findViewById(R.id.name);
            info = (TextView) mView.findViewById(R.id.info);


        }
    }
}
