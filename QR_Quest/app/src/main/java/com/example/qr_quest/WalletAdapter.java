package com.example.qr_quest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder> {

  private Wallet[] qrList;
  private ItemClickListener clickListener;

    public WalletAdapter(Wallet[] qrList) {
        this.qrList = qrList;
    }

    @NonNull
    @Override
    public WalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_item,parent,false);
        return new WalletViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletViewHolder holder, int position) {
     holder.nameTv.setText(qrList[position].getName());
     holder.pointsTv.setText(qrList[position].getPoints());
     holder.img.setText(qrList[position].getD());
    }

    @Override
    public int getItemCount() {
        return qrList.length;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    class WalletViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nameTv;
        TextView pointsTv;
        TextView img;

        public WalletViewHolder(@NonNull View itemView)  {
            super(itemView);

            nameTv = itemView.findViewById(R.id.img_name);
            pointsTv = itemView.findViewById(R.id.img_points);
            img = itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getBindingAdapterPosition());
        }
    }
}
