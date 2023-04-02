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
/**
 * Adapter class for the RecyclerView that displays the top QR codes on the leaderboard.
 */
public class LeaderboardTopQRAdapter extends RecyclerView.Adapter<LeaderboardTopQRAdapter.UserViewHolder> {
    private ArrayList<QR> qrs;

    /**
     * Constructor for the adapter.
     * @param qrs
     *      the list of QR codes to be displayed
     */
    public LeaderboardTopQRAdapter(ArrayList<QR> qrs)
    {
        this.qrs = qrs;
    }

    /**
     * Filters the data set of User objects to be displayed in the RecyclerView based on a filtered list.
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
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.number.setText(Integer.toString(qrs.get(position).getRank()));
        holder.username.setText(qrs.get(position).getQRName());
        holder.info.setText(Math.toIntExact(qrs.get(position).getScore()) + " pts");
    }

    /**
     * This method returns the number of users in the leaderboard.
     * @return
     *      The number of users in the leaderboard.
     */
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
                username = (TextView) mView.findViewById(R.id.txtview_listitem_name);
                info = (TextView) mView.findViewById(R.id.txtview_listitem_info);
            }catch (Exception e){
                Log.d("Error in LeaderboardTopQRAdapter", "UserViewHolder: ", e);
            }
        }
    }
}
