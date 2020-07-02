package oil.city.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import oil.city.Interface.ItemClickListener;
import oil.city.R;

public class RestorauntViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView nameOfRest, textOfRest, phoneOfRest;
    public ImageView imageOfRest, imageShareRest, img, img1;
    public RatingBar ratingBarRest;

    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public RestorauntViewHolder(@NonNull View itemView) {
        super(itemView);

        nameOfRest = itemView.findViewById(R.id.restoraunt_name);
        textOfRest = itemView.findViewById(R.id.restoraunt_text_bottom);
        phoneOfRest = itemView.findViewById(R.id.restoraunt_phone);
        imageOfRest = itemView.findViewById(R.id.restoraunt_image);
//        imageShareRest = itemView.findViewById(R.id.image_share_restoraunt);
        img = itemView.findViewById(R.id.img);
        img1 = itemView.findViewById(R.id.img1);
//        ratingBarRest = itemView.findViewById(R.id.ratingBarRest);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }
}
