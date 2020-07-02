package oil.city.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import oil.city.Interface.ItemClickListener;
import oil.city.R;

public class SportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView nameOfSport, textOfSport;
    public ImageView imageOfSport, imageShareSport;

    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public SportViewHolder(@NonNull View itemView) {
        super(itemView);

        nameOfSport = itemView.findViewById(R.id.sport_name);
        textOfSport = itemView.findViewById(R.id.sport_text_bottom);
        imageOfSport = itemView.findViewById(R.id.sport_image);
//        imageShareSport = itemView.findViewById(R.id.image_share_sport);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
//        listener.onClick(v, getAdapterPosition(),false);
    }
}
