package com.fm.modules.app.localet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fm.modules.R;
import com.fm.modules.adapters.ItemViewAdapterImagen;
import com.fm.modules.sqlite.models.Direcciones;

import java.util.List;

//import com.fm.modules.app.restaurantes.RestauranteMenuActivity;

public class DireccionesViewAdapter extends ItemViewAdapterImagen<Direcciones> {

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;

    public DireccionesViewAdapter(List<Direcciones> lista, Context context, int resource) {
        super(lista, context);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View absView(int position, View convertView, ViewGroup parent) {
        final HolderItemOption holder;

        try {
            if (convertView == null) {
                convertView = layoutInflater.from(context).inflate(R.layout.holder_item_option, parent, false);
                holder = new HolderItemOption(convertView);
                convertView.setTag(holder);
                System.out.println("Holder Activo");
            } else {
                holder = (HolderItemOption) convertView.getTag();
                System.out.println("Holder Inactivo");
            }
            final Direcciones direccion = (Direcciones) getItem(position);
            holder.image.setImageResource(R.drawable.ic_track_order_option);
            holder.name.setText(direccion.getNombreDireccion());

            holder.itemOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalLocation.locationSelected = direccion.getCoordenadas();
                    Intent intent = new Intent(context, Location.class);
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            System.out.println("Error ListaAdapterRestaurante" + e);
        }
        return convertView;
    }

}

