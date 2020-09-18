package com.fm.modules.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.carrito.SeleccionarComplementos;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.models.Image;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.service.ImageService;
import com.fm.modules.sqlite.models.PlatillosSeleccionadoSQLite;

import java.util.ArrayList;
import java.util.List;

public class RecyclerPlatillosAdapter extends RecyclerView.Adapter<RecyclerPlatillosAdapter.ViewHolder> {

    private List<Platillo> items;
    private Context context;

    public RecyclerPlatillosAdapter(List<Platillo> platillos, Context context) {
        this.items = platillos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item_food, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.asignarDatos(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ivFoodImage;
        TextView tvFoodName;
        TextView tvFoodDescription;
        TextView tvFoodPrice;
        AppCompatTextView btnAdd;

        public ViewHolder(View view) {
            super(view);
            ivFoodImage = (AppCompatImageView) view.findViewById(R.id.ivFoodImage);
            tvFoodName = (TextView) view.findViewById(R.id.tvFoodName);
            tvFoodDescription = (TextView) view.findViewById(R.id.tvFoodDescription);
            tvFoodPrice = (TextView) view.findViewById(R.id.tvFoodPrice);
            btnAdd = (AppCompatTextView) view.findViewById(R.id.btnAddPlatillo);
        }

        public void asignarDatos(final Platillo platillo) {
            ivFoodImage.setImageResource(R.drawable.food_sample);
            tvFoodName.setText(platillo.getNombre());
            tvFoodDescription.setText(platillo.getDescripcion());
            tvFoodPrice.setText("$ " + String.valueOf(platillo.getPrecioBase()));
            //btnAdd.setText(platillo.getOrden());
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PlatillosSeleccionadoSQLite platillosSeleccionadoSQLite = new PlatillosSeleccionadoSQLite(context);
                    PlatilloSeleccionado platilloSeleccionado = new PlatilloSeleccionado();
                    /*
                     * al platillo seleccionado se le asignara un id segun sea el platillo con su id,
                     * en el caso de que el cliente agrega varios platillos al mismo pedido
                     * el id del platillo seleccionado se basara en el platillo uqe se pide
                     * */
                    platilloSeleccionado.setPlatilloSeleccionadoId(platillo.getPlatilloId());
                    platilloSeleccionado.setPlatillo(platillo);
                    platilloSeleccionado.setPrecio(platillo.getPrecioBase());
                    platilloSeleccionado.setNombre(platillo.getNombre());
                    Pedido pedido = new Pedido();
                    /*
                     * al pedido se le asignara un id segun sea el restaurante con su id,
                     * en el caso de que el cliente hace el varios pedidos a diferentes restaurantes
                     * el pedido en si se basara a que restaurante lo esta haciendo
                     * */
                    pedido.setPedidoId(platillo.getMenu().getRestaurante().getRestauranteId());
                    platilloSeleccionado.setPedido(pedido);
                    Long ll = platillosSeleccionadoSQLite.create(platilloSeleccionado);
                    platillosSeleccionadoSQLite.readId(ll);
                    GlobalRestaurantes.platillo = platillo;
                    Intent i = new Intent(context, SeleccionarComplementos.class);
                    GlobalRestaurantes.platilloSeleccionado = platillo;
                    i.putExtra("idPlatillo", platillo.getPlatilloId().intValue());
                    context.startActivity(i);
                }
            });
            verImagen(platillo.getImagen());
        }

        protected void verImagen(Long id) {
            CargarImagen cargarImagen = new CargarImagen();
            cargarImagen.execute(id);

        }

        private class CargarImagen extends AsyncTask<Long, String, Image> {

            @Override
            protected Image doInBackground(Long... longs) {
                Image image = null;
                try {
                    if (Logued.imagenesIDs == null) {
                        Logued.imagenes = new ArrayList<>();
                        Logued.imagenesIDs = new ArrayList<>();
                    }
                    List<Integer> integers = Logued.imagenesIDs;
                    if (!integers.contains(longs[0].intValue())) {
                        ImageService imageService = new ImageService();
                        image = imageService.obtenerImagenPorId(longs[0]);
                        if (image != null) {
                            Logued.imagenesIDs.add(image.getId().intValue());
                            Logued.imagenes.add(image);
                        }
                    } else {
                        for (int i = 0; i < integers.size(); i++) {
                            if (integers.get(i) == longs[0].intValue()) {
                                image = Logued.imagenes.get(i);
                            }
                        }
                    }
                } catch (
                        Exception e) {
                    System.out.println("error asynk image: " + e);
                }
                return image;
            }

            @Override
            protected void onPostExecute(Image image) {
                super.onPostExecute(image);
                if (image != null) {
                    Utilities.displayAppCompatImageFoodFromBytea(image.getContent(), ivFoodImage, context);
                    System.out.println("asynk display image ! !!!!!!!!!!!!!!!!");
                }
            }
        }
    }
}
