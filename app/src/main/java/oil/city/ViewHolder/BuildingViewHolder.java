package oil.city.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import oil.city.Interface.ItemClickListener;
import oil.city.R;

public class BuildingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView nameOfBuilding;
    public ImageView imageOfBuilding;

    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }


    public BuildingViewHolder(@NonNull View itemView) {
        super(itemView);

        nameOfBuilding = itemView.findViewById(R.id.building_name);
        imageOfBuilding = itemView.findViewById(R.id.building_image);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }
}
