package oil.city.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import oil.city.Interface.ItemClickListener;
import oil.city.R;

public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView name, date, time, adress, price, description;
    public ImageView event_image,image_share,image_fav;


    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.event_name);
        date = itemView.findViewById(R.id.event_date);
        time = itemView.findViewById(R.id.event_time);
        adress = itemView.findViewById(R.id.event_adress);
        price = itemView.findViewById(R.id.price);
        image_share = itemView.findViewById(R.id.image_share);

        event_image = itemView.findViewById(R.id.event_image);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }
}

