package oil.city.ViewHolder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;

import oil.city.Interface.BusItem;
import oil.city.Interface.ItemClickListener;
import oil.city.R;

public class BusViewHolder extends RecyclerView.ViewHolder {

    public TextView txt_bus_item, txt_bus_child_item, txt_bus_child_item2,txt_bus_child_item3;
    public RelativeLayout button;
    public ExpandableLinearLayout expandableLinearLayout;

    public BusItem itemListener;

    public void setBusItem(BusItem itemListener){
        this.itemListener = itemListener;
    }

    public BusViewHolder(@NonNull View itemView, boolean isExpandable) {
        super(itemView);
        if (isExpandable){
            txt_bus_item = itemView.findViewById(R.id.txt_bus_item);
            txt_bus_child_item = itemView.findViewById(R.id.txt_bus_child_item);
            txt_bus_child_item2 = itemView.findViewById(R.id.txt_bus_child_item2);
            txt_bus_child_item3 = itemView.findViewById(R.id.txt_bus_child_item3);

            button = itemView.findViewById(R.id.button);
            expandableLinearLayout = itemView.findViewById(R.id.expandableLayout);
        }else {
            txt_bus_item = itemView.findViewById(R.id.txt_bus_item);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onClick(v, getAdapterPosition());
            }
        });
    }
}
