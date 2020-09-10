package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fm.modules.R;
import com.fm.modules.models.Categoria;

import java.util.List;

//import com.fm.modules.app.restaurantes.RestauranteMenuActivity;

public class CategoriasViewAdapter extends ItemViewAdapterImagen<Categoria> {

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;

    public CategoriasViewAdapter(List<Categoria> lista, Context context, int resource) {
        super(lista);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View absView(int position, View convertView, ViewGroup parent) {
        final HolderItemCategorias holder;

        try {
            if (convertView == null) {
                convertView = layoutInflater.from(context).inflate(R.layout.holder_item_restaurant, parent, false);
                holder = new HolderItemCategorias(convertView);
                convertView.setTag(holder);
                System.out.println("Holder Activo");
            } else {
                holder = (HolderItemCategorias) convertView.getTag();
                System.out.println("Holder Inactivo");
            }

            final Categoria categoria = (Categoria) getItem(position);

            holder.catImage.setImageResource(R.drawable.ic_flan);
            holder.catName.setText(categoria.getNombreCategoria());
            int x = 11;
            holder.catCountRestaurants.setText(x);


        } catch (Exception e) {
            System.out.println("Error ListaAdapterRestaurante" + e);
        }
        return convertView;
    }

}

