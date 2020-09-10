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
import com.fm.modules.models.Categoria;

import java.util.ArrayList;
import java.util.List;

//import com.fm.modules.app.restaurantes.RestauranteMenuActivity;

public class CategoriasRecyclerViewAdapter extends RecyclerView.Adapter<CategoriasRecyclerViewAdapter.HolderItemCategorias> {


    private Context context;
    List<Categoria> categorias = new ArrayList<>();

    public CategoriasRecyclerViewAdapter(List<Categoria> categorias,Context context) {
        this.context = context;
        this.categorias = categorias;
    }

    @NonNull
    @Override
    public CategoriasRecyclerViewAdapter.HolderItemCategorias onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item_category_unit,parent,false);
        return new CategoriasRecyclerViewAdapter.HolderItemCategorias(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItemCategorias holder, int position) {
        holder.catCountRestaurants.setText(categorias.get(position).getCategoriaId().toString());
        holder.catImage.setImageResource(R.drawable.ic_flan);
        holder.catName.setText(categorias.get(position).getNombreCategoria());
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    public class HolderItemCategorias extends RecyclerView.ViewHolder {
        AppCompatImageView catImage;
        AppCompatTextView catName;
        AppCompatTextView catCountRestaurants;

        public HolderItemCategorias(View view) {
            super(view);
            catImage = view.findViewById(R.id.ivCategoryIcon1);
            catName = view.findViewById(R.id.tvCategoryName1);
            catCountRestaurants = view.findViewById(R.id.tvRestaurantsCount1);
        }
    }

}

