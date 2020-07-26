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
    public ImageView imageOfSport, imageShareSport, img, img1,img2,img3,img4,img5;

    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public SportViewHolder(@NonNull View itemView) {
        super(itemView);

        nameOfSport = itemView.findViewById(R.id.sport_name);
        textOfSport = itemView.findViewById(R.id.sport_text_bottom);
        imageOfSport = itemView.findViewById(R.id.sport_image);
        img = itemView.findViewById(R.id.img);
        img1 = itemView.findViewById(R.id.img1);
        img2 = itemView.findViewById(R.id.img2);
        img3 = itemView.findViewById(R.id.img3);
        img4 = itemView.findViewById(R.id.img4);
        img5 = itemView.findViewById(R.id.img5);
//        imageShareSport = itemView.findViewById(R.id.image_share_sport);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }
}
