package com.fm.modules.app.localet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.login.Logued;
import com.fm.modules.sqlite.models.Direcciones;

import java.util.List;

//import com.fm.modules.app.restaurantes.RestauranteMenuActivity;

public class DireccionesViewAdapter extends RecyclerView.Adapter<DireccionesViewAdapter.HolderItemOption> {

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;
    Button ubicacionSelect;
    List<Direcciones> items;

    public DireccionesViewAdapter(List<Direcciones> lista, Context context, int resource, Button ubicacionSelect) {
        this.context = context;
        this.resource = resource;
        this.ubicacionSelect = ubicacionSelect;
        this.items = lista;
    }

    @NonNull
    @Override
    public HolderItemOption onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item_option_locations, parent, false);
        return new DireccionesViewAdapter.HolderItemOption(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderItemOption holder, int position) {
        try {
            final Direcciones direccion = (Direcciones) items.get(position);
            holder.image.setImageResource(R.drawable.mapa_ico);
            holder.name.setText(direccion.getNombreDireccion());
            holder.descripcion.setText(direccion.getDireccion());
            if (Logued.idDireccion != null) {
                if (Logued.idDireccion == direccion.getIdDireccion()) {
                    holder.checked.setImageResource(R.drawable.ic_checked_round_lime);
                } else {
                    holder.checked.setImageResource(R.drawable.ic_checked_round_white);
                }
            }
            holder.itemOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Logued.pedidoActual != null) {
                        Logued.pedidoActual.setDireccion(direccion.getDireccion());
                    }
                    Logued.idDireccion = direccion.getIdDireccion();
                    ubicacionSelect.setEnabled(true);
                    ubicacionSelect.setBackground(context.getResources().getDrawable(R.drawable.add_food_action_bacground_orange));
                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            System.out.println("Error DireccionesViewAdapter" + e);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class HolderItemOption extends RecyclerView.ViewHolder {
        public AppCompatImageView image;
        public ImageView checked;
        public AppCompatTextView name;
        public AppCompatTextView descripcion;
        public ConstraintLayout itemOption;

        public HolderItemOption(View view) {
            super(view);
            image = (AppCompatImageView) view.findViewById(R.id.ivOptionIcon);
            checked = (ImageView) view.findViewById(R.id.pagoBtnTarjetaCheck);
            name = (AppCompatTextView) view.findViewById(R.id.tvOptionName);
            descripcion = (AppCompatTextView) view.findViewById(R.id.tvOptionDescription);
            itemOption = (ConstraintLayout) view.findViewById(R.id.itemOptionLayout);
        }
    }
}

