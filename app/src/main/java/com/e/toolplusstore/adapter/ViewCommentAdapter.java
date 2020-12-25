package com.e.toolplusstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.toolplusstore.R;
import com.e.toolplusstore.beans.Comment;
import com.e.toolplusstore.databinding.CommentChildLayoutBinding;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ViewCommentAdapter extends RecyclerView.Adapter<ViewCommentAdapter.ViewCommentViewHolder> {
    Context context;
    List<Comment> commentList;
    public ViewCommentAdapter(Context context, List<Comment> commentList){
        this.context = context;
        this.commentList= commentList;
    }
    @NonNull
    @Override
    public ViewCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentChildLayoutBinding binding = CommentChildLayoutBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewCommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCommentViewHolder holder, int position) {
        Comment c = commentList.get(position);
        holder.binding.tvComment.setText(c.getComment());
        holder.binding.userName.setText(c.getUserName());
        Timestamp timestamp = new Timestamp(c.getTimestamp());
        Date date = new Date(timestamp.getTime());
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        holder.binding.tvDate.setText(""+format.format(date));
        Picasso.get().load(c.getUserImageUrl()).placeholder(R.drawable.person).into(holder.binding.userImage);
        switch (c.getRating()){
            case  0: holder.binding.ratingBar.setVisibility(View.GONE);
            case  1: holder.binding.ratingBar.setRating(1);break;
            case  2: holder.binding.ratingBar.setRating(2);break;
            case  3: holder.binding.ratingBar.setRating(3);break;
            case  4: holder.binding.ratingBar.setRating(4);break;
            case  5: holder.binding.ratingBar.setRating(5);break;

        }
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewCommentViewHolder extends RecyclerView.ViewHolder{
        CommentChildLayoutBinding binding;
        public ViewCommentViewHolder(@NonNull CommentChildLayoutBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
