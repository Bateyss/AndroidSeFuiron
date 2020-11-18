package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.fm.modules.R;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.models.Municipio;

import java.util.List;

public class MunicipioItemViewAdapter extends ItemViewAdapterImagen<Municipio> {

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;
    private Spinner spinner;


    public MunicipioItemViewAdapter(List<Municipio> lista, Context context, int resource, Spinner spinner) {
        super(lista, context);
        this.context = context;
        this.resource = resource;
        this.spinner = spinner;
    }

    @Override
    public View absView(final int position, View convertView, ViewGroup parent) {
        final HolderItemSpinner holder;

        try {
            if (convertView == null) {
                convertView = layoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                holder = new HolderItemSpinner(convertView);
                convertView.setTag(holder);
                System.out.println("Holder Activo");
            } else {
                holder = (HolderItemSpinner) convertView.getTag();
                System.out.println("Holder Inactivo");
            }

            final Municipio municipio = (Municipio) getItem(position);

            if (municipio.getNombreMunicipio() != null) {
                holder.itemOption.setText(municipio.getNombreMunicipio());
            } else {
                String ss = "none";
                holder.itemOption.setText(ss);
            }
            holder.itemOption.setTextColor(context.getResources().getColor(R.color.purple));
            holder.itemOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalCarrito.municipioSelected = municipio;
                    if (municipio.getNombreMunicipio() != null) {
                        GlobalCarrito.direccion8 = municipio.getNombreMunicipio();
                        spinner.setSelection(position);
                        notifyDataSetChanged();
                    }
                }
            });
            if (municipio.getNombreMunicipio() != null && municipio.getNombreMunicipio().equals(GlobalCarrito.direccion8)) {
                holder.itemOption.setBackgroundColor(context.getResources().getColor(R.color.opaqueLime));
            } else {
                holder.itemOption.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
            if (GlobalCarrito.municipioSelected == null && 0 == position) {
                holder.itemOption.setBackgroundColor(context.getResources().getColor(R.color.opaqueLime));
                GlobalCarrito.municipioSelected = municipio;
                GlobalCarrito.direccion8 = municipio.getNombreMunicipio();
            }
        } catch (Exception ignore) {

        }
        return convertView;
    }
}
