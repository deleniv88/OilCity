package oil.city.ViewHolder;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import oil.city.R;

public class ShowCommentViewHolder extends RecyclerView.ViewHolder {

    public TextView txtUserName, txtComment;
    public RatingBar ratingBar;

    public ShowCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        txtComment = itemView.findViewById(R.id.txtComment);
        txtUserName = itemView.findViewById(R.id.txtUserName);
        ratingBar = itemView.findViewById(R.id.ratingBarComment);
    }
}

