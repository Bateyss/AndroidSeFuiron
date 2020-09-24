package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.models.Pedido;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class RecyclerPedidosAdapter extends RecyclerView.Adapter<RecyclerPedidosAdapter.ViewHolder> {

    private List<Pedido> items;
    private Context context;

    public RecyclerPedidosAdapter(List<Pedido> pedidos, Context context) {
        this.items = pedidos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item_pedido, parent, false);
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

        CheckBox checkBox;
        AppCompatTextView title;
        AppCompatTextView subTitle;
        AppCompatTextView price;

        public ViewHolder(View view) {
            super(view);
            checkBox = (CheckBox) view.findViewById(R.id.myorderChekvbox);
            title = (AppCompatTextView) view.findViewById(R.id.myorderTitle);
            subTitle = (AppCompatTextView) view.findViewById(R.id.myorderSubTitle);
            price = (AppCompatTextView) view.findViewById(R.id.myorderPrice);
        }

        public void asignarDatos(final Pedido pedido) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            final DecimalFormat decimalFormat = new java.text.DecimalFormat("#,##0.00");
            checkBox.setChecked(pedido.isPedidoEntregado());
            String titulo = "Orden #" + pedido.getPedidoId();
            String precio = "$ " + decimalFormat.format(pedido.getTotalEnRestautante());
            String fecha = simpleDateFormat.format(pedido.getFechaOrdenado());
            title.setText(titulo);
            subTitle.setText(fecha);
            price.setText(precio);
        }
    }
}
