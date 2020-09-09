package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.entities.RespuestaPlatilloPorMenu;
import com.fm.modules.models.PlatilloFavorito;

import java.util.List;

public class RecyclerPlatillosFavoritosAdapter  extends RecyclerView.Adapter<RecyclerPlatillosFavoritosAdapter.ViewHolder> {

    private List<PlatilloFavorito> items;
    private Context context;

    public RecyclerPlatillosFavoritosAdapter(List<PlatilloFavorito> platillosFavorito, Context context){
        this.items = platillosFavorito;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerPlatillosFavoritosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item_food, parent, false);
        return new RecyclerPlatillosFavoritosAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerPlatillosFavoritosAdapter.ViewHolder holder, int position) {
        holder.asignarDatos(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        AppCompatImageView ivFoodImage;
        TextView tvFoodName;
        TextView tvFoodDescription;
        TextView tvFoodPrice;
        AppCompatTextView btnAdd;

        public ViewHolder(View view){
            super(view);
            ivFoodImage = view.findViewById(R.id.ivFoodImage);
            tvFoodName = view.findViewById(R.id.tvFoodName);
            tvFoodDescription = view.findViewById(R.id.tvFoodDescription);
            tvFoodPrice = view.findViewById(R.id.tvFoodPrice);
            btnAdd = view.findViewById(R.id.btnAdd);
        }

        public void asignarDatos(final PlatilloFavorito platillosFavorito) {
            ivFoodImage.setImageResource(R.drawable.not_found);
            tvFoodName.setText(String.valueOf(platillosFavorito.getPlatillo().getNombre()));
            tvFoodDescription.setText(platillosFavorito.getPlatillo().getDescripcion());
            tvFoodPrice.setText("$ " +String.valueOf(platillosFavorito.getPlatillo().getPrecioBase()));
            //btnAdd.setText(platillo.getOrden());
            btnAdd.setVisibility(View.INVISIBLE);

            /*ivFoodImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, Platillo.class);
                    i.putExtra("idRestaurante", platillo.getPlatilloId());
                    System.out.println("ID RESTAURANTE: " + platillo.getPlatilloId());
                    context.startActivity(i);
                }
            });*/
        }
    }
}