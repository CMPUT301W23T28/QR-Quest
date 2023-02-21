package com.example.qr_quest;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LeaderBoardAdapter1 extends RecyclerView.Adapter<LeaderBoardAdapter1.UserViewHolder> {
    //   private user[] users;
    private ArrayList<User> users;

//    public LeaderBoardAdapter(user[] users) {
//        this.users = users;
//    }

    public LeaderBoardAdapter1(ArrayList<User> users)
    {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.number.setText(Integer.toString(holder.getBindingAdapterPosition()+1));
        // holder.username.setText(users[position].getName());
        //holder.info.setText(Integer.toString(users[position].getRegionQr()));
        holder.username.setText(users.get(position).getUsername());
//        holder.info.setText(Integer.toString(users.get(position).getNumQRCodes()));
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
                Log.d("nomrmal ekta string", "UserViewHolder: ", e);
            }
        }
    }
}