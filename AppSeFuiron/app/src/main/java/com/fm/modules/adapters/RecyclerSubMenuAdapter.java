package com.fm.modules.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.entities.RespuestaOpcionSubMenuPorPlatillo;
import com.fm.modules.models.SubMenu;
import com.fm.modules.service.OpcionSubMenuService;

import java.util.List;

public class RecyclerSubMenuAdapter extends RecyclerView.Adapter<RecyclerSubMenuAdapter.ViewHolder> {

    private Context context;
    private List<SubMenu> subMenuList;
    private List<RespuestaOpcionSubMenuPorPlatillo> opcionesDeSubMenus;

    private OpcionSubMenuService opcionesSubMenuService = new OpcionSubMenuService();

    public RecyclerSubMenuAdapter(List<SubMenu> subMenuList, Context context, List<RespuestaOpcionSubMenuPorPlatillo> opcionesDeSubMenus) {
        this.context = context;
        this.subMenuList = subMenuList;
        this.opcionesDeSubMenus = opcionesDeSubMenus;
    }

    public RecyclerSubMenuAdapter(Context context, List<RespuestaOpcionSubMenuPorPlatillo> opcionesDeSubMenus){
        this.context = context;
        this.opcionesDeSubMenus = opcionesDeSubMenus;
    }

    public RecyclerSubMenuAdapter(List<RespuestaOpcionSubMenuPorPlatillo> opcionesDeSubMenus, Context context){
        this.context = context;
        this.opcionesDeSubMenus = opcionesDeSubMenus;
    }

    @NonNull
    @Override
    public RecyclerSubMenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sub_menu, parent, false);
        return new RecyclerSubMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerSubMenuAdapter.ViewHolder holder, int position) {
        //holder.asignarDatos(opcionesDeSubMenus.get(position));
        System.out.println("POSICION: " +position);
        if(position == 0){
            holder.asignarDatos(opcionesDeSubMenus.get(position));
        }else if(opcionesDeSubMenus.get(position).getTitulo().equals(opcionesDeSubMenus.get(position - 1).getTitulo())){
            holder.asignarDatos2(opcionesDeSubMenus.get(position));
        }else{
            holder.asignarDatos2(opcionesDeSubMenus.get(position));
        }
        //holder.asignarDatos(subMenuList.get(position));
        //System.out.println("CUANTO TIENE: " +opcionesDeSubMenus.size());
        //RecyclerOpcionesDeSubMenuAdapter adapter = new RecyclerOpcionesDeSubMenuAdapter(opcionesDeSubMenus);
        //holder.rvOpcionesDeSubMenu.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        //holder.rvOpcionesDeSubMenu.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return opcionesDeSubMenus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSubMenu;
        TextView tvOpcionSubMenu;
        RecyclerView rvOpcionesDeSubMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSubMenu = itemView.findViewById(R.id.tvSubMenu);
            tvOpcionSubMenu = itemView.findViewById(R.id.tvOpcionSubMenu);
            //rvOpcionesDeSubMenu = itemView.findViewById(R.id.rvOpcionesDeSubMenu);
        }

        public void asignarDatos(final RespuestaOpcionSubMenuPorPlatillo respuestaOpcionSubMenuPorPlatillo){

            tvSubMenu.setText(respuestaOpcionSubMenuPorPlatillo.getTitulo());
            //tvOpcionSubMenu.setText(respuestaOpcionSubMenuPorPlatillo.getNombre());
        }

        public void asignarDatos2(final RespuestaOpcionSubMenuPorPlatillo respuestaOpcionSubMenuPorPlatillo){
            tvOpcionSubMenu.setText(respuestaOpcionSubMenuPorPlatillo.getNombre());
        }
    }

}
