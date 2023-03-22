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
    //   private user[] users;
    private ArrayList<User> users;

//    public LeaderBoardAdapter(user[] users) {
//        this.users = users;
//    }

    public LeaderboardQRCollectedAdapter(ArrayList<User> users)
    {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new UserViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.number.setText(Integer.toString(holder.getBindingAdapterPosition()+1));
        holder.username.setText(users.get(position).getUsername());
        holder.info.setText(users.get(position).getQRCodes().size() + " QRs");
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