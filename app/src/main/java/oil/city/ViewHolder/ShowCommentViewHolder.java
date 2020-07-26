package oil.city.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import oil.city.Common.Common;
import oil.city.Interface.ItemClickListener;
import oil.city.R;

public class ShowCommentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener {

    public TextView txtUserName, txtComment;
    public RatingBar ratingBar;


    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public ShowCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        txtComment = itemView.findViewById(R.id.txtComment);
        txtUserName = itemView.findViewById(R.id.txtUserName);
        ratingBar = itemView.findViewById(R.id.ratingBarComment);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        listener.onClick(v, getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Оберіть дію");

        menu.add(0,0, getAdapterPosition(), Common.UPDATE);
        menu.add(0, 1, getAdapterPosition(), Common.DELETE);
    }
}

