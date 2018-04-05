package com.sadda.adda.panchratan.saddaadda.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sadda.adda.panchratan.saddaadda.R;
import com.sadda.adda.panchratan.saddaadda.objects.Comment;
import com.sadda.adda.panchratan.saddaadda.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 22-07-2017.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private static final String TAG = "CommentAdapter";
    private List<Comment> commentsArrayList;

    public CommentAdapter(List<Comment> commentsArrayList) {
        this.commentsArrayList = commentsArrayList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_userName, txt_comment, txt_dateAndTime;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_userName = (TextView) itemView.findViewById(R.id.txt_comment_user_name);
            txt_comment = (TextView) itemView.findViewById(R.id.txt_comment_user_comment);
            txt_dateAndTime = (TextView) itemView.findViewById(R.id.txt_comment_date_time);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = commentsArrayList.get(position);
        Log.i(TAG, "onBindViewHolder: Data Received: userName: " + comment.getUserName() + "\n comment: " + comment.getComment() + " \n dateAndTime: " + comment.getTimeAndDate());
        holder.txt_userName.setText(comment.getUserName());
        holder.txt_comment.setText(comment.getComment());
        String formatedTime = Utils.convertGMTToIST(comment.getTimeAndDate());
        holder.txt_dateAndTime.setText(formatedTime);
    }


    @Override
    public int getItemCount() {
        return commentsArrayList.size();
    }
}
