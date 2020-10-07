package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.models.OpcionesDeSubMenu;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerOpcionesDeSubMenuAdapter extends RecyclerView.Adapter<RecyclerOpcionesDeSubMenuAdapter.ViewHolder> {

    private List<OpcionesDeSubMenu> opcioneSubMenus;
    private Context context;

    public RecyclerOpcionesDeSubMenuAdapter(List<OpcionesDeSubMenu> opcionesSubMenus, Context context) {
        this.opcioneSubMenus = opcionesSubMenus;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerOpcionesDeSubMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sub_menu_opciones, parent, false);
        return new RecyclerOpcionesDeSubMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerOpcionesDeSubMenuAdapter.ViewHolder holder, final int position) {
        holder.tvOpcionSubMenu.setText(opcioneSubMenus.get(position).getNombre());
        DecimalFormat decimalFormat = new DecimalFormat("$ #,##0.00");
        String price = "+ " + decimalFormat.format(opcioneSubMenus.get(position).getPrecio());
        holder.precio.setText(price);
        final RecyclerOpcionesDeSubMenuAdapter.ViewHolder sc = holder;
        holder.rdbOpcionSubMenuSelec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                List<OpcionesDeSubMenu> list = GlobalRestaurantes.opcionesDeSubMenusSeleccionados;
                if (list == null) {
                    list = new ArrayList<>();
                }
                boolean agregado = false;
                int addedPos = 0;
                for (int x = 0; x < list.size(); x++) {
                    if (list.get(x).getOpcionesDeSubmenuId().intValue() == opcioneSubMenus.get(position).getOpcionesDeSubmenuId().intValue()) {
                        agregado = true;
                        addedPos = x;
                    }
                }
                if (agregado) {
                    list.remove(addedPos);
                    sc.rdbOpcionSubMenuSelec.setChecked(false);
                } else {
                    List<OpcionesDeSubMenu> ll = new ArrayList<>();
                    for (OpcionesDeSubMenu op : list) {
                        if (op.getSubMenu().getSubMenuId().intValue() == opcioneSubMenus.get(position).getSubMenu().getSubMenuId().intValue()) {
                            ll.add(op);
                        }
                    }
                    if (ll.size() <= opcioneSubMenus.get(position).getSubMenu().getMaximoOpcionesAEscoger()) {
                        list.add(opcioneSubMenus.get(position));
                        sc.rdbOpcionSubMenuSelec.setChecked(true);
                    } else {
                        Toast.makeText(context, "No se puede agregar mas opciones", Toast.LENGTH_SHORT).show();
                        sc.rdbOpcionSubMenuSelec.setChecked(false);
                    }
                }
                GlobalRestaurantes.opcionesDeSubMenusSeleccionados = list;
            }
        });
    }

    @Override
    public int getItemCount() {
        return opcioneSubMenus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvOpcionSubMenu;
        TextView precio;
        RadioButton rdbOpcionSubMenuSelec;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOpcionSubMenu = itemView.findViewById(R.id.tvOpcionSubMenu);
            rdbOpcionSubMenuSelec = itemView.findViewById(R.id.rdbOpcionSubMenuSelec);
            precio = itemView.findViewById(R.id.tvOpcionSubMenuPrecio);
        }
    }


}
