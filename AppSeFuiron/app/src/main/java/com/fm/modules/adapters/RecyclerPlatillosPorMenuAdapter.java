package com.fm.modules.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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
import com.fm.modules.app.carrito.SeleccionarComplementos;
import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.entities.RespuestaMenuPorRestaurantes;
import com.fm.modules.entities.RespuestaPlatilloPorMenu;
import com.fm.modules.models.Platillo;

import java.util.List;

public class RecyclerPlatillosPorMenuAdapter  extends RecyclerView.Adapter<RecyclerPlatillosPorMenuAdapter.ViewHolder> {

    private List<RespuestaPlatilloPorMenu> items;
    private Context context;

    public RecyclerPlatillosPorMenuAdapter(List<RespuestaPlatilloPorMenu> platillos, Context context){
        this.items = platillos;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerPlatillosPorMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item_food, parent, false);
        return new RecyclerPlatillosPorMenuAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerPlatillosPorMenuAdapter.ViewHolder holder, int position) {
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

        public void asignarDatos(final RespuestaPlatilloPorMenu platilloSeleccionado) {
            ivFoodImage.setImageResource(R.drawable.not_found);
            tvFoodName.setText(platilloSeleccionado.getNombre());
            tvFoodDescription.setText(platilloSeleccionado.getDescripcion());
            tvFoodPrice.setText("$ " +String.valueOf(platilloSeleccionado.getPrecioBase()));
            //btnAdd.setText(platillo.getOrden());
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Agregado", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, SeleccionarComplementos.class);
                    /*i.putExtra("idPlatillo", GlobalRestaurantes.platillo.getPlatilloId());
                    i.putExtra("nombre" , GlobalRestaurantes.platillo.getNombre());
                    i.putExtra("descripcion", GlobalRestaurantes.platillo.getDescripcion());
                    i.putExtra("precio", GlobalRestaurantes.platillo.getPrecioBase());*/
                    Platillo platillo = new Platillo();

                    platillo.setPlatilloId(Long.valueOf(platilloSeleccionado.getPlatilloId()));
                    platillo.setNombre(platilloSeleccionado.getNombre());
                    platillo.setDescripcion(platilloSeleccionado.getDescripcion());
                    platillo.setPrecioBase(Double.valueOf(platilloSeleccionado.getPrecioBase()));
                    GlobalRestaurantes.platillo = platillo;
                    //GlobalRestaurantes.platillo = new Platillo();
                    //i.put
                    context.startActivity(i);
                }
            });
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