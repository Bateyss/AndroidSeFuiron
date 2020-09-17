package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.login.Logued;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.sqlite.models.OpcionesDeSubMenuSeleccionadoSQLite;

import java.util.List;

public class RecyclerPlatillosSeleccionadosAdapter extends RecyclerView.Adapter<RecyclerPlatillosSeleccionadosAdapter.ViewHolder> {

    private List<PlatilloSeleccionado> items;
    private Context context;

    public RecyclerPlatillosSeleccionadosAdapter(List<PlatilloSeleccionado> platillosSeleccionadoList, Context context) {
        this.items = platillosSeleccionadoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item_food_selected, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlatilloSeleccionado p = items.get(position);
        holder.asignarDatos(p);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFoodName;
        TextView tvFoodDescription;
        TextView tvFoodPrice;
        TextView tvFoodQuantity;
        AppCompatTextView btnDelete;

        public ViewHolder(View view) {
            super(view);
            tvFoodName = view.findViewById(R.id.tvFoodNameSelected);
            tvFoodDescription = view.findViewById(R.id.tvFoodDescriptionSelected);
            tvFoodPrice = view.findViewById(R.id.tvFoodPriceSelected);
            tvFoodQuantity = view.findViewById(R.id.tvFoodQuantitySelected);
            btnDelete = view.findViewById(R.id.btnDelPlatillo);
        }

        public void asignarDatos(final PlatilloSeleccionado platilloSeleccionado) {
            tvFoodName.setText(platilloSeleccionado.getNombre());

            List<OpcionesDeSubMenuSeleccionado> ll = Logued.opcionesDeSubMenusEnPlatillosSeleccionados;
            StringBuilder stb = new StringBuilder();
            stb.append("");
            double adicionales = 0;
            if (ll != null) {
                if (!ll.isEmpty()) {
                    for (OpcionesDeSubMenuSeleccionado op : ll) {
                        if (op.getPlatilloSeleccionado().getPlatilloSeleccionadoId().intValue() == platilloSeleccionado.getPlatilloSeleccionadoId().intValue()) {
                            stb.append(op.getNombre());
                            // adicionales = adicionales + op.getOpcionesDeSubMenu().getPrecio();
                        }
                    }
                }
            }
            /*
             * en caso se agregue cantidad a la tabla platillo seleccionado
             * se usara el codigo que esta como comentario en este metodo
             * */
            //double platilloMasAdicional = platilloSeleccionado.getPlatillo().getPrecioBase() + adicionales;
            //int cantidad = (int) (platilloSeleccionado.getPrecio()/platilloMasAdicional);
            String opcioness = stb.toString();
            tvFoodDescription.setText(opcioness);
            String precio = "$ " + platilloSeleccionado.getPrecio();
            // String cantida = "x" + cantidad;
            String cantida = "x1";
            tvFoodPrice.setText(precio);
            tvFoodQuantity.setText(cantida);


            //btnDelete.setOnClickListener(new View.OnClickListener() {
            //     @Override
            //    public void onClick(View view) {
            //       PlatillosSeleccionadoSQLite platillosSeleccionadoSQLite = new PlatillosSeleccionadoSQLite(context);
            //       PlatilloSeleccionado platilloSeleccionado = new PlatilloSeleccionado();
            //       /*
            //        * al platillo seleccionado se le asignara un id segun sea el platillo con su id,
            //        * en el caso de que el cliente agrega varios platillos al mismo pedido
            //        * el id del platillo seleccionado se basara en el platillo uqe se pide
            //        * */
            //       platilloSeleccionado.setPlatilloSeleccionadoId(platillo.getPlatilloId());
            //      platilloSeleccionado.setPlatillo(platillo);
            //      platilloSeleccionado.setPrecio(platillo.getPrecioBase());
            //      platilloSeleccionado.setNombre(platillo.getNombre());
            //      Pedido pedido = new Pedido();
            //       /*
            //        * al pedido se le asignara un id segun sea el restaurante con su id,
            //        * en el caso de que el cliente hace el varios pedidos a diferentes restaurantes
            //       * el pedido en si se basara a que restaurante lo esta haciendo
            //       * */
            //      pedido.setPedidoId(platillo.getMenu().getRestaurante().getRestauranteId());
            //      platilloSeleccionado.setPedido(pedido);
            //       platillosSeleccionadoSQLite.create(platilloSeleccionado);
            //       Toast.makeText(context, "Agregado", Toast.LENGTH_SHORT).show();
            //       Intent i = new Intent(context, SeleccionarComplementos.class);
            //       i.putExtra("idPlatillo", platillo.getPlatilloId().intValue());
            //       context.startActivity(i);
            //   }
            //});
        }
    }
}
