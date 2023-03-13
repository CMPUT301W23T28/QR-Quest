package com.example.qr_quest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * An adapter class for binding an array of wallet objects to a RecyclerView.
 */
public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder> {

  private Wallet[] qrList;
  private ItemClickListener clickListener;

    /**
     * Constructor for the WalletAdapter class that takes in an array of Wallet objects and sets the
     * qrList field to that array.
     *
     * @param qrList an array of Wallet objects to be displayed in the RecyclerView
     */
    public WalletAdapter(Wallet[] qrList) {
        this.qrList = qrList;
    }

    /**
     * Creates a new WalletViewHolder instance by inflating the layout for each item in the RecyclerView.
     *
     * @param parent the ViewGroup into which the new View will be added after it is bound to an adapter position
     * @param viewType the view type of the new View
     * @return a new WalletViewHolder instance
     */
    @NonNull
    @Override
    public WalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_item,parent,false);
        return new WalletViewHolder(view);
    }

    /**
     * Binds the Wallet data at the specified position to the ViewHolder's views.
     *
     * @param holder the ViewHolder instance to be updated
     * @param position the position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(@NonNull WalletViewHolder holder, int position) {
     holder.nameTv.setText(qrList[position].getName());
     holder.pointsTv.setText(qrList[position].getPoints());
     holder.img.setText(qrList[position].getD());
    }

    /**
     * Returns the number of items in the adapter's data set.
     *
     * @return the number of items in the adapter's data set
     */

    @Override
    public int getItemCount() {
        return qrList.length;
    }

    /**
     * Sets the click listener for the adapter.
     *
     * @param itemClickListener the ItemClickListener instance to set
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    /**
     * The WalletViewHolder class is responsible for managing the individual views for each Wallet item
     * in the RecyclerView. It implements View.OnClickListener and calls the clickListener's onClick
     * method when the view is clicked.
     */

    class WalletViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nameTv;
        TextView pointsTv;
        TextView img;

        /**
         * Constructor for the WalletViewHolder class that sets up the views and sets the click listener
         * for the item view.
         *
         * @param itemView the item view for the ViewHolder
         */

        public WalletViewHolder(@NonNull View itemView)  {
            super(itemView);

            nameTv = itemView.findViewById(R.id.img_name);
            pointsTv = itemView.findViewById(R.id.img_points);
            img = itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);
        }

        /**
         * Calls the clickListener's onClick method when the view is clicked.
         *
         * @param view the view that was clicked
         */

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getBindingAdapterPosition());
        }
    }
}
