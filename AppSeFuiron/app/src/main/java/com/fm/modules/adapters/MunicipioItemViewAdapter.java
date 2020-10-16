package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fm.modules.R;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.models.Municipio;

import java.util.List;

public class MunicipioItemViewAdapter extends ItemViewAdapterImagen<Municipio> {

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;


    public MunicipioItemViewAdapter(List<Municipio> lista, Context context, int resource) {
        super(lista, context);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View absView(int position, View convertView, ViewGroup parent) {
        final HolderItemMunicipio holder;

        try {
            if (convertView == null) {
                convertView = layoutInflater.from(context).inflate(R.layout.holder_item_municipios, parent, false);
                holder = new HolderItemMunicipio(convertView);
                convertView.setTag(holder);
                System.out.println("Holder Activo");
            } else {
                holder = (HolderItemMunicipio) convertView.getTag();
                System.out.println("Holder Inactivo");
            }

            final Municipio municipio = (Municipio) getItem(position);

            if (municipio.getNombreMunicipio() != null) {
                holder.itemOption.setText(municipio.getNombreMunicipio());
            } else {
                String ss = "none";
                holder.itemOption.setText(ss);
            }
            holder.iteCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalCarrito.municipioSelected = municipio;
                    if (municipio.getNombreMunicipio() != null) {
                        GlobalCarrito.direccion8 = municipio.getNombreMunicipio();
                        notifyDataSetChanged();
                    }
                }
            });

            if (municipio.getNombreMunicipio() != null && municipio.getNombreMunicipio().equals(GlobalCarrito.direccion8)) {
                holder.iteCardView.setBackgroundColor(context.getResources().getColor(R.color.opaqueLime));
                holder.itemOption.setBackgroundColor(context.getResources().getColor(R.color.opaqueLime));
            } else {
                holder.iteCardView.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.itemOption.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        } catch (Exception ignore) {

        }
        return convertView;
    }
}
