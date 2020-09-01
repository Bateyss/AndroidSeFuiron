package com.fm.modules.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fm.modules.R;

import java.util.LinkedList;
import java.util.List;

public abstract class ItemViewAdapterImagen<T> extends BaseAdapter {

    private List<T> lista = new LinkedList<>();

    public List<T> getLista() {
        return lista;
    }


    public ItemViewAdapterImagen(List<T> lista) {
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return getLista().size();
    }

    @Override
    public Object getItem(int position) {
        return getLista().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return absView(position, convertView, parent);
    }

    public abstract View absView(int position, View convertView, ViewGroup parent);

    public class HolderImagen {

        TextView title;
        TextView description;

        public HolderImagen(View view) {
            title = (TextView) view.findViewById(R.id.itemTitle);
            description = (TextView) view.findViewById(R.id.itemText);
        }
    }

    public class HolderTexto {
        TextView title;
        TextView description;

        public HolderTexto(View view) {
            title = (TextView) view.findViewById(R.id.itemTitle);
            description = (TextView) view.findViewById(R.id.itemText);
        }
    }
}
