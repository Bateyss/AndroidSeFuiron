package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fm.modules.R;
import com.fm.modules.models.Platillo;

import java.util.List;

public class PlatillosItemViewAdapter extends ItemViewAdapterImagen<Platillo>{

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;

    public PlatillosItemViewAdapter(List<Platillo> lista, Context context, int resource) {
        super(lista,context);
        this.context = context;
        this.resource = resource;

    }


    @Override
    public View absView(int position, View convertView, ViewGroup parent) {
        final HolderItemFood holder;

        try {
            if(convertView == null){
                convertView = layoutInflater.from(context).inflate(R.layout.holder_item_food, parent, false);
                holder = new HolderItemFood(convertView);
                convertView.setTag(holder);
            }else{
                holder = (HolderItemFood) convertView.getTag();
                final Platillo platillo = (Platillo) getItem(position);

                holder.tvFoodName.setText(platillo.getNombre());
                holder.tvFoodDescription.setText(platillo.getDescripcion());
                holder.tvFoodPrice.setText("$: " +String.valueOf(platillo.getPrecioBase()));
            }
        }catch (Exception e){

        }

        return null;
    }
}
