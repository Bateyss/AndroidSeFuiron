package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.app.carrito.HabiliarAddMap;
import com.fm.modules.models.OpcionesDeSubMenu;
import com.fm.modules.models.SubMenu;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class RecyclerSubMenuAdapter2 extends RecyclerView.Adapter<RecyclerSubMenuAdapter2.ViewHolder> {

    private Context context;
    private List<SubMenu> subMenuList;
    private List<OpcionesDeSubMenu> opcionesDeSubMenus;
    MaterialButton materialButton;


    public RecyclerSubMenuAdapter2(List<SubMenu> subMenuList, List<OpcionesDeSubMenu> opcionesDeSubMenus, Context context, MaterialButton materialButton) {
        this.context = context;
        this.subMenuList = subMenuList;
        this.opcionesDeSubMenus = opcionesDeSubMenus;
        this.materialButton = materialButton;
    }

    @NonNull
    @Override
    public RecyclerSubMenuAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sub_menu2, parent, false);
        return new RecyclerSubMenuAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerSubMenuAdapter2.ViewHolder holder, int position) {

        holder.tvSubMenu.setText(subMenuList.get(position).getTitulo());

        List<OpcionesDeSubMenu> opciones = new ArrayList<>();
        Long idSub = subMenuList.get(position).getSubMenuId();
        GlobalCarrito.habilitarAdd.add(new HabiliarAddMap(idSub, true));
        for (OpcionesDeSubMenu listaOpcionesSubMenu : opcionesDeSubMenus) {
            Long id = listaOpcionesSubMenu.getSubMenu().getSubMenuId();
            if (id.intValue() == idSub.intValue()) {
                opciones.add(listaOpcionesSubMenu);
            }
        }
        RecyclerOpcionesDeSubMenuAdapter adapter = new RecyclerOpcionesDeSubMenuAdapter(opciones, context, materialButton, this);
        holder.rvOpcionesDeSubMenu.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.rvOpcionesDeSubMenu.setAdapter(adapter);
        String tct = "min: " + subMenuList.get(position).getMinimoOpcionesAEscoger() + " max: " + subMenuList.get(position).getMaximoOpcionesAEscoger();
        holder.tvSubMenu2.setText(tct);
    }

    @Override
    public int getItemCount() {
        return subMenuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSubMenu;
        TextView tvSubMenu2;
        RecyclerView rvOpcionesDeSubMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubMenu = itemView.findViewById(R.id.tvSubMenu2);
            tvSubMenu2 = itemView.findViewById(R.id.tvSubMenu3);
            rvOpcionesDeSubMenu = itemView.findViewById(R.id.rvOpcionesDeSubMenu);
        }


    }

}
