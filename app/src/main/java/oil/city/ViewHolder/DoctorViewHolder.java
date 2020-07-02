package oil.city.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import oil.city.Interface.ItemClickListener;
import oil.city.R;

public class DoctorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView name, phone, time, adress;
    public ImageView admin_image, admin_icon_time, admin_icon_phone, admin_icon_loc;

    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public DoctorViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.admin_name);
//        time = itemView.findViewById(R.id.admin_hour);
//        adress = itemView.findViewById(R.id.admin_adress);
        phone = itemView.findViewById(R.id.admin_phone);

        admin_image = itemView.findViewById(R.id.admin_image);
//        admin_icon_loc = itemView.findViewById(R.id.admin_icon_loc);
//        admin_icon_time = itemView.findViewById(R.id.admin_icon_time);
        admin_icon_phone = itemView.findViewById(R.id.admin_icon_phone);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
//        listener.onClick(v, getAdapterPosition(),false);
    }
}



