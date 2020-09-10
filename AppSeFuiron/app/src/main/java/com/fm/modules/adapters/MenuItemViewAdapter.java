package com.fm.modules.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fm.modules.R;
import com.fm.modules.app.restaurantes.PlatillosActivity;
import com.fm.modules.app.restaurantes.RestauranteMenuActivity;
import com.fm.modules.models.Menu;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.Restaurante;

import java.util.List;

public class MenuItemViewAdapter extends ItemViewAdapterImagen<Menu>{

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;


    public MenuItemViewAdapter(List<Menu> lista, Context context, int resource) {
        super(lista);
        this.context = context;
        this.resource = resource;

    }

    @Override
    public View absView(int position, View convertView, ViewGroup parent) {
        final HolderMenu holder;

        try {
            if (convertView == null) {
                convertView = layoutInflater.from(context).inflate(R.layout.holder_menu, parent, false);
                holder = new HolderMenu(convertView);
                convertView.setTag(holder);
                System.out.println("Holder Activo");
            } else {
                holder = (HolderMenu) convertView.getTag();
                System.out.println("Holder Inactivo");
            }

            final Menu menu = (Menu) getItem(position);

            holder.tvMenuName.setText(menu.getNombreMenu());
            holder.cvMenuName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PlatillosActivity.class);
                    context.startActivity(i);
                }
            });

        }catch (Exception e){

        }
        return convertView;
    }
}
