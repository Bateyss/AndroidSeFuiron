package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.app.carrito.HabiliarAddMap;
import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.models.OpcionesDeSubMenu;
import com.google.android.material.button.MaterialButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RecyclerOpcionesDeSubMenuAdapter extends RecyclerView.Adapter<RecyclerOpcionesDeSubMenuAdapter.ViewHolder> {

    private List<OpcionesDeSubMenu> opcioneSubMenus;
    private Context context;
    MaterialButton materialButton;
    RecyclerSubMenuAdapter2 recycler;

    public RecyclerOpcionesDeSubMenuAdapter(List<OpcionesDeSubMenu> opcionesSubMenus, Context context, MaterialButton materialButton, RecyclerSubMenuAdapter2 recycler) {
        this.opcioneSubMenus = opcionesSubMenus;
        this.context = context;
        this.materialButton = materialButton;
        this.recycler = recycler;
    }

    @NonNull
    @Override
    public RecyclerOpcionesDeSubMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sub_menu_opciones, parent, false);
        return new RecyclerOpcionesDeSubMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerOpcionesDeSubMenuAdapter.ViewHolder holder, final int position) {
        holder.tvOpcionSubMenu.setText(opcioneSubMenus.get(position).getNombre());
        DecimalFormat decimalFormat = new DecimalFormat("$ #,##0.00");
        String price = "+ " + decimalFormat.format(opcioneSubMenus.get(position).getPrecio());
        holder.precio.setText(price);

        final OpcionesDeSubMenu opcion = opcioneSubMenus.get(position);
        final List<OpcionesDeSubMenu> ff = new ArrayList<>();
        if (!GlobalRestaurantes.opcionesDeSubMenusSeleccionados.isEmpty()) {
            for (OpcionesDeSubMenu op : GlobalRestaurantes.opcionesDeSubMenusSeleccionados) {
                try {
                    if (op.getSubMenu().getSubMenuId().intValue() == opcion.getSubMenu().getSubMenuId().intValue()) {
                        ff.add(op);
                    }
                } catch (Exception ignore) {
                }
            }
        }
        System.out.println("ff size : " + ff.size() + " de: " + opcion.getSubMenu().getMinimoOpcionesAEscoger());
        boolean enable = ff.size() >= opcion.getSubMenu().getMinimoOpcionesAEscoger();
        int x = 0;
        int y = -1;
        for (HabiliarAddMap hb : GlobalCarrito.habilitarAdd) {
            try {
                if (hb.getId().intValue() == opcion.getSubMenu().getSubMenuId().intValue()) {
                    y = x;
                }
            } catch (Exception ignore) {
            }
            x++;
        }
        if (y > -1) {
            GlobalCarrito.habilitarAdd.set(y, new HabiliarAddMap(opcion.getSubMenu().getSubMenuId(), enable));
        }
        boolean habil = true;
        for (HabiliarAddMap hb : GlobalCarrito.habilitarAdd) {
            if (!hb.isHabilitar()) {
                habil = false;
            }
            //System.out.println("habilitacion " + hb.isHabilitar() + " id sub menu: " + hb.getId());
        }
        materialButton.setEnabled(habil);
        holder.rdbOpcionSubMenuSelec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checked) {
                    holder.rdbOpcionSubMenuSelec.setChecked(false);
                    holder.checked = false;
                    int x = -1;
                    for (int i = 0; i < GlobalRestaurantes.opcionesDeSubMenusSeleccionados.size(); i++) {
                        if (GlobalRestaurantes.opcionesDeSubMenusSeleccionados.get(i).getOpcionesDeSubmenuId().intValue() == opcioneSubMenus.get(position).getOpcionesDeSubmenuId().intValue()) {
                            x = i;
                        }
                    }
                    if (x > -1) {
                        GlobalRestaurantes.opcionesDeSubMenusSeleccionados.remove(x);
                    }
                    notifyDataSetChanged();
                } else {
                    List<OpcionesDeSubMenu> ffff = new ArrayList<>();
                    for (OpcionesDeSubMenu op : GlobalRestaurantes.opcionesDeSubMenusSeleccionados) {
                        try {
                            if (op.getSubMenu().getSubMenuId().intValue() == opcion.getSubMenu().getSubMenuId().intValue()) {
                                ffff.add(op);
                            }
                        } catch (Exception ignore) {
                        }
                    }

                    try {
                        int tamanio = ffff.size() + 1;
                        opcion.cobrado = opcion.getSubMenu().isMenuCobrado() && tamanio >= opcion.getSubMenu().getCobrarAPartirDe();
                        System.out.println("opcion: " + opcion.getNombre() + " tamanio de lista? :" + tamanio + " sera cobrada? :" + opcion.cobrado);
                    } catch (Exception ignore) {
                        System.out.println(" error opcion: " + opcion.getNombre());
                        opcion.cobrado = false;
                    }
                    if (opcion.getSubMenu().getMaximoOpcionesAEscoger() > ffff.size()) {
                        GlobalRestaurantes.opcionesDeSubMenusSeleccionados.add(opcion);
                        holder.rdbOpcionSubMenuSelec.setChecked(true);
                        holder.checked = true;
                        notifyDataSetChanged();
                    } else {
                        holder.rdbOpcionSubMenuSelec.setChecked(false);
                        holder.checked = false;
                        Toast.makeText(context, "Maximo de Opciones", Toast.LENGTH_SHORT).show();
                    }
                }
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
        AppCompatRadioButton rdbOpcionSubMenuSelec;
        boolean checked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOpcionSubMenu = itemView.findViewById(R.id.tvOpcionSubMenu);
            rdbOpcionSubMenuSelec = itemView.findViewById(R.id.rdbOpcionSubMenuSelec);
            precio = itemView.findViewById(R.id.tvOpcionSubMenuPrecio);
            checked = false;
        }
    }


}
