package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fm.modules.R;
import com.fm.modules.models.Pedido;

import java.util.List;

public class PedidosItemViewAdapter extends ItemViewAdapterImagen<Pedido> {

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;

    public PedidosItemViewAdapter(List<Pedido> lista, Context context, int resource) {
        super(lista);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View absView(int position, View convertView, ViewGroup parent) {
        HolderTexto holder;
        try {
            if (convertView == null) {
                //LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //convertView = layoutInflater.inflate(R.layout.list_item,null);
                convertView = layoutInflater.from(context).inflate(R.layout.list_item, parent, false);
                holder = new HolderTexto(convertView);
                convertView.setTag(holder);
                System.out.println("holder activo");
            } else {
                holder = (HolderTexto) convertView.getTag();
                System.out.println("holder inactivo");
            }
            final Pedido pedido = (Pedido) getItem(position);
            holder.title.setText(pedido.getFormaDePago());
            holder.description.setText(pedido.getDireccion());
            //holder.title.setText("asdasdasdasd");
            //holder.description.setText("asdasdasasd");

        } catch (Exception e) {
            System.out.println("error listaAdapter" + e);
        }
        return convertView;
    }
}