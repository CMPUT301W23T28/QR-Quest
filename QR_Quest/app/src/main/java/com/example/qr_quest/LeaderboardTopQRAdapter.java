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

public class LeaderboardTopQRAdapter extends RecyclerView.Adapter<LeaderboardTopQRAdapter.UserViewHolder> {
    private ArrayList<QR> qrs;

    public LeaderboardTopQRAdapter(ArrayList<QR> qrs)
    {
        this.qrs = qrs;
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
        holder.username.setText(qrs.get(position).getQRName());
        holder.info.setText(Math.toIntExact(qrs.get(position).getScore()) + " pts");
    }

    @Override
    public int getItemCount() {
        return qrs.size();
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
                Log.d("Error in LeaderboardTopQRAdapter", "UserViewHolder: ", e);
            }
        }
    }
}