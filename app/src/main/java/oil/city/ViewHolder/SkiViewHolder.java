package oil.city.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import oil.city.Interface.ItemClickListener;
import oil.city.R;

public class SkiViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView nameOfSki, textOfSki;
    public ImageView imageOfSki, imageShareRest;

    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public SkiViewHolder(@NonNull View itemView) {
        super(itemView);

        nameOfSki = itemView.findViewById(R.id.restoraunt_name);
        textOfSki = itemView.findViewById(R.id.restoraunt_text_bottom);
        imageOfSki = itemView.findViewById(R.id.restoraunt_image);
//        imageShareRest = itemView.findViewById(R.id.image_share_restoraunt);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        listener.onClick(v, getAdapterPosition(),false);
    }
}
