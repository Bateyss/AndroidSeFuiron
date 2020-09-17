package com.fm.modules.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fm.modules.R;
import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.app.restaurantes.RestauranteMenuActivity;
import com.fm.modules.models.Restaurante;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestauranteItemViewAdapter extends ItemViewAdapterImagen<Restaurante> {

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;

    public RestauranteItemViewAdapter(List<Restaurante> lista, Context context, int resource) {
        super(lista);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View absView(int position, View convertView, ViewGroup parent) {
        final HolderRestaurantes holder;

        try {
            if (convertView == null) {
                convertView = layoutInflater.from(context).inflate(R.layout.holder_item_restaurant, parent, false);
                holder = new HolderRestaurantes(convertView);
                convertView.setTag(holder);
                System.out.println("Holder Activo");
            } else {
                holder = (HolderRestaurantes) convertView.getTag();
                System.out.println("Holder Inactivo");
            }

            final Restaurante restaurante = (Restaurante) getItem(position);


            holder.ivOutstandingImage.setImageResource(R.drawable.sample_outstanding_image);
            holder.tvRestaurantName.setText(restaurante.getNombreRestaurante());
            holder.tvLabelMinimalMount.setText(restaurante.getDepartamento().getNombreDepartamento());
            holder.tvMinimalMount.setText(String.valueOf(restaurante.getDepartamento().getPais().getNombrePais()));
            Picasso.get().load("http://netlima.com/casas/negocios/Pizza_Hut_logo.svg.png").placeholder(R.drawable.ic_profile_header_background).into(holder.ivRestaurantLogo);
            holder.ivOutstandingImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GlobalRestaurantes.restauranteSelected = restaurante;
                    Intent i = new Intent(context, RestauranteMenuActivity.class);
                    i.putExtra("idRestaurante", restaurante.getRestauranteId().intValue());
                    context.startActivity(i);
                }
            });
            holder.verImagen(restaurante.getImagenDePortada());
            holder.verLogo(restaurante.getLogoDeRestaurante());
        } catch (Exception e) {
            System.out.println("Error ListaAdapterRestaurante" + e);
        }
        return convertView;
    }


}

