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
    public ImageView news_image, img, img1;

    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        news_name = itemView.findViewById(R.id.food_name);

        news_image = itemView.findViewById(R.id.food_image);
        img = itemView.findViewById(R.id.food_img1);
        img1 = itemView.findViewById(R.id.food_img1);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }
}


