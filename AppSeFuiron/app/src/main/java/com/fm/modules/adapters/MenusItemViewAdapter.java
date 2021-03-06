 package com.fm.modules.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fm.modules.R;
import com.fm.modules.app.restaurantes.PlatillosActivity;
import com.fm.modules.entities.RespuestaMenuPorRestaurantes;
import com.fm.modules.models.Menu;

import java.util.List;

public class MenusItemViewAdapter extends ItemViewAdapterImagen<Menu>{

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;


    public MenusItemViewAdapter(List<Menu> lista, Context context, int resource) {
        super(lista,context);
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

            final RespuestaMenuPorRestaurantes menu = (RespuestaMenuPorRestaurantes) getItem(position);

            /*
            holder.tvMenuName.setText(menu.getNombreMenu());
            holder.cvMenuName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, PlatillosActivity.class);
                    i.putExtra("idMenu", menu.getMenuId());
                    context.startActivity(i);
                }
            });
             */
        }catch (Exception e){

        }
        return convertView;
    }
}
