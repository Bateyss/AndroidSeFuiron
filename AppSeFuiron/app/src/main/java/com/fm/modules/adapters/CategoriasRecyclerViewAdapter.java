package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.models.Categoria;
import com.fm.modules.models.Menu;
import com.fm.modules.models.Restaurante;
import com.google.android.material.card.MaterialCardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.fm.modules.app.restaurantes.RestauranteMenuActivity;

public class CategoriasRecyclerViewAdapter extends RecyclerView.Adapter<CategoriasRecyclerViewAdapter.HolderItemCategorias> {


    private Context context;
    List<Categoria> categorias = new ArrayList<>();

    public CategoriasRecyclerViewAdapter(List<Categoria> categorias, Context context) {
        this.context = context;
        this.categorias = categorias;
    }

    @NonNull
    @Override
    public CategoriasRecyclerViewAdapter.HolderItemCategorias onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item_category_unit, parent, false);
        return new CategoriasRecyclerViewAdapter.HolderItemCategorias(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItemCategorias holder, int position) {
        holder.catImage.setImageResource(R.drawable.ic_flan);
        holder.catName.setText(categorias.get(position).getNombreCategoria());
        List<Menu> menus = GlobalRestaurantes.menuCategorias;
        List<Restaurante> restaurantes = new ArrayList<>();
        int idCategoria = categorias.get(position).getCategoriaId().intValue();
        List<Integer> integers = new ArrayList<>();
        if (menus != null && !menus.isEmpty()) {
            SimpleDateFormat sp = new SimpleDateFormat("HH:mm:ss");
            Date actualDate = new Date();
            String actualHuorString = sp.format(actualDate);
            Date actualHour = null;
            for (Menu menu : menus) {
                if (menu.getCategoria().getCategoriaId().intValue() == idCategoria) {
                    if (!integers.contains(menu.getRestaurante().getRestauranteId().intValue())) {
                        try {
                            actualHour = sp.parse(actualHuorString);
                            Date restaurantCloseHour = sp.parse(menu.getRestaurante().getHorarioDeCierre());
                            Date restaurantOpenHour = sp.parse(menu.getRestaurante().getHorarioDeApertura());
                            if (restaurantCloseHour.getTime() > actualHour.getTime() && actualHour.getTime() > restaurantOpenHour.getTime()) {
                                restaurantes.add(menu.getRestaurante());
                                integers.add(menu.getRestaurante().getRestauranteId().intValue());
                            }
                        } catch (ParseException ignore) {
                        }
                    }
                }
            }
        }
        int cantidad = 0;
        if (!restaurantes.isEmpty()) {
            cantidad = restaurantes.size();
        }
        String srt = cantidad + " restaurantes";
        if (cantidad == 1) {
            srt = cantidad + " restaurante" ;
        }
        holder.catCountRestaurants.setText(srt);
        if (cantidad == 0) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
        }
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public class HolderItemCategorias extends RecyclerView.ViewHolder {
        AppCompatImageView catImage;
        AppCompatTextView catName;
        AppCompatTextView catCountRestaurants;
        MaterialCardView material1;

        public HolderItemCategorias(View view) {
            super(view);
            catImage = view.findViewById(R.id.ivCategoryIcon1);
            catName = view.findViewById(R.id.tvCategoryName1);
            catCountRestaurants = view.findViewById(R.id.tvRestaurantsCount1);
            material1 = view.findViewById(R.id.material1);
        }
    }

}

