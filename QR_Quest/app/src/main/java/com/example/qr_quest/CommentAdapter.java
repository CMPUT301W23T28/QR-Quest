package com.example.qr_quest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
    This class is the adapter for the comment class and it extends from the RecyclerView class
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList;

    /**
     * Constructor for the class
     * @param commentList
     *       List of comments
     */
    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    /**
     * Called when RecyclerView needs a new {@link CommentViewHolder} of the given type to represent
     * a comment.
     * @param parent
     *      The ViewGroup into which the new View will be added after it is bound to
     *      an adapter position.
     * @param viewType
     *      The view type of the new View.
     * @return
     *      A new CommentViewHolder that holds a View of the given view type.
     *
     */
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout,parent,false);
        return new CommentViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method updates the contents of
     * the CommentViewHolder to reflect the item at the given position.
     *
     * @param holder   the CommentViewHolder which should be updated to represent the contents of the item at the given position in the data set
     * @param position the position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.nameTv.setText(commentList.get(position).getCommenter());
        holder.commentTv.setText(commentList.get(position).getComment());
    }

    /**
     * Returns the total number of comments in the data set held by the adapter.
     *
     * @return the total number of comments in the data set
     */
    @Override
    public int getItemCount() {
        return commentList.size();
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView commentTv;

        /**
         * Constructs a new CommentViewHolder object with the given View object.
         * This constructor sets the TextView fields of the CommentViewHolder object to the corresponding views in the given View object.
         *
         * @param itemView The View object representing the layout of a single comment item in the RecyclerView.
         */
        public CommentViewHolder(@NonNull View itemView)  {
            super(itemView);

            nameTv = itemView.findViewById(R.id.txtview_comment_name);
            commentTv = itemView.findViewById(R.id.txtview_comment_text);
        }
    }
}

