package com.example.qr_quest;

import android.annotation.SuppressLint;
import android.content.Intent;
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

    private QR[] qrList;
    User user;
    private ItemClickListener clickListener;


    /**
     * Adapter for displaying the user's wallet of QR codes in a RecyclerView
     * @param qrList array of QR codes to display in the RecyclerView
     * @param user current user
     */
    public WalletAdapter(QR[] qrList, User user) {
        this.qrList = qrList;
        this.user = user;
    }

    /**
    * Creates a new WalletViewHolder instance by inflating the layout for each item in the RecyclerView.
    * @param parent
    *      The ViewGroup into which the new View will be added after it is bound to an adapter position
    * @param viewType
    *      The view type of the new View
    * @return
    *      Returns a new WalletViewHolder instance
    */
    @NonNull
    @Override
    public WalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_item,parent,false);
        return new WalletViewHolder(view);
    }

    /**
    * Binds the QR data at the specified position to the ViewHolder's views.
    * @param holder
    *      The ViewHolder instance to be updated
    * @param position
    *      The position of the item within the adapter's data set
    */
    @Override
    public void onBindViewHolder(@NonNull WalletViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.nameTv.setText(qrList[position].getQRName());

        if(user != null) {
            QRDatabase.checkIfUserHasQR(user.getUsername(), qrList[position], check ->{
                if(check) {
                    holder.pointsTv.setText(qrList[position].getScore() + " pts");
                }
            });
        } else {
            holder.pointsTv.setText(qrList[position].getScore() + " pts");
        }

        holder.img.setText(qrList[position].getQRIcon());
        holder.itemView.setOnClickListener(view -> {
            if (clickListener != null) {
                QR qr = qrList[position];
                Intent intent = new Intent(view.getContext(), QRActivity.class);
                intent.putExtra("scannedQR", qr);
                intent.putExtra("user", user);
                view.getContext().startActivity(intent); // start new activity
            }
        });
    }

    /**
    * Sets the click listener for the adapter.
    * @param itemClickListener
    *       The ItemClickListener instance to set
    */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    /**
     * Returns the number of items in the adapter's data set.
     * @return Returns the number of items in the adapter's data set
     */
    @Override
    public int getItemCount() {
        return qrList.length;
    }

    /**
    * The WalletViewHolder class is responsible for managing the individual views for each QR item
    * in the RecyclerView. It implements View.OnClickListener and calls the clickListener's onClick
    * method when the view is clicked.
    */
    public class WalletViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nameTv;
        TextView pointsTv;
        TextView img;

        /**
        * Constructor for the WalletViewHolder class that sets up the views and sets the click listener
        * for the item view.
        * @param itemView
        *       The item view for the ViewHolder
        */
        public WalletViewHolder(@NonNull View itemView)  {
            super(itemView);

            nameTv = itemView.findViewById(R.id.txtview_walletitem_img_name);
            pointsTv = itemView.findViewById(R.id.txtview_walletitem_img_pts);
            img = itemView.findViewById(R.id.txtview_walletitem_img);
            itemView.setOnClickListener(this);
        }

        /**
        * Calls the clickListener's onClick method when the view is clicked.
        * @param view
        *       The view that was clicked
        */
        @Override
        public void onClick(View view) {
        }
    }
}