package oil.city.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import oil.city.Interface.ItemClickListener;
import oil.city.R;

public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView news_name;
    public ImageView news_image;

    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        news_name = itemView.findViewById(R.id.news_name);

        news_image = itemView.findViewById(R.id.news_image);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }
}


