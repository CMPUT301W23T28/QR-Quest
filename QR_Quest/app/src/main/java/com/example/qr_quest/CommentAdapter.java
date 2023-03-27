package com.example.qr_quest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private comment[] commentList;

    public CommentAdapter(comment[] commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout,parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.nameTv.setText(commentList[position].getCommenter());
        holder.commentTv.setText(commentList[position].getComment());

    }

    @Override
    public int getItemCount() {
        return commentList.length;
    }



    public class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        TextView commentTv;


        public CommentViewHolder(@NonNull View itemView)  {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameComment);
            commentTv = itemView.findViewById(R.id.commentText);

        }



    }
}

