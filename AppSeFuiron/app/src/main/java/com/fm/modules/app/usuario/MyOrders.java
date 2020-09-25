package com.fm.modules.app.usuario;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.RecyclerPedidosAdapter;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.models.Menu;
import com.fm.modules.models.OpcionesDeSubMenu;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.SubMenu;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.OpcionesDeSubMenuService;
import com.fm.modules.service.PedidoService;
import com.fm.modules.service.PlatilloService;

import java.util.ArrayList;
import java.util.List;

public class MyOrders extends Fragment {

    private View viewGlobal;
    private RecyclerView recyclerViewMyOrders;
    private MyOrdenes myOrdenes = new MyOrdenes();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        viewGlobal = view;
        recyclerViewMyOrders = (RecyclerView) view.findViewById(R.id.myordersRecycler);
        verMyOrders();
        return view;
    }

    private void verMyOrders() {
        if (isNetActive()) {
            myOrdenes.execute();
        }
    }

    public boolean isNetActive() {
        boolean c = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                c = true;
            }
        } catch (Exception e) {
            System.out.println("error isNetActive: " + e);
            c = false;
        }
        return c;
    }

    private class MyOrdenes extends AsyncTask<String, String, List<Pedido>> {

        @Override
        protected List<Pedido> doInBackground(String... strings) {
            List<Pedido> pedidos = new ArrayList<>();
            try {
                PedidoService pedidoService = new PedidoService();
                Usuario usuario = Logued.usuarioLogued;
                if (usuario != null) {
                    pedidos = pedidoService.obtenerMyPedidos(usuario.getUsuarioId());
                }
                if (!pedidos.isEmpty()) {
                    cargarPlatos();
                }
            } catch (Exception e) {
                System.out.println("Error en UnderThreash:" + e.getMessage() + " " + e.getClass());
            }
            return pedidos;
        }

        private void cargarPlatos() {
            OpcionesDeSubMenuService opcionesDeSubMenuService = new OpcionesDeSubMenuService();
            List<OpcionesDeSubMenu> opciones = opcionesDeSubMenuService.obtenerOpcionesDeSubMenu();
            if (!opciones.isEmpty()) {
                System.out.println("********* opciones cargadas ***************");
                List<SubMenu> subMenus = new ArrayList<>();
                List<Integer> ints = new ArrayList<>();
                for (OpcionesDeSubMenu op : opciones) {
                    try {
                        if (!ints.contains(op.getSubMenu().getSubMenuId().intValue())) {
                            subMenus.add(op.getSubMenu());
                            ints.add(op.getSubMenu().getSubMenuId().intValue());
                        }
                    } catch (Exception ignore) {
                    }
                }
                GlobalRestaurantes.opcionesDeSubMenuList = opciones;
                if (!subMenus.isEmpty()) {
                    GlobalRestaurantes.subMenuList = subMenus;
                }
            } else {
                GlobalRestaurantes.opcionesDeSubMenuList = new ArrayList<>();
                GlobalRestaurantes.subMenuList = new ArrayList<>();
            }
            PlatilloService platilloService = new PlatilloService();
            List<Platillo> platillos = platilloService.obtenerPlatillos();
            if (!platillos.isEmpty()) {
                GlobalRestaurantes.platilloList = platillos;
                List<Menu> menus = new ArrayList<>();
                List<Integer> ints = new ArrayList<>();
                for (Platillo pa : platillos) {
                    try {
                        if (!ints.contains(pa.getMenu().getMenuId().intValue())) {
                            menus.add(pa.getMenu());
                            ints.add(pa.getMenu().getMenuId().intValue());
                        }
                    } catch (Exception ignore) {
                    }
                }
                if (!menus.isEmpty()) {
                    GlobalRestaurantes.menuList = menus;
                }
            } else {
                GlobalRestaurantes.platilloList = new ArrayList<>();
                GlobalRestaurantes.menuList = new ArrayList<>();
            }
        }

        @Override
        protected void onPostExecute(List<Pedido> pedidos) {
            super.onPostExecute(pedidos);
            try {
                if (!pedidos.isEmpty()) {
                    RecyclerPedidosAdapter rvAdapter = new RecyclerPedidosAdapter(pedidos, viewGlobal.getContext(), getActivity());
                    recyclerViewMyOrders.setLayoutManager(new LinearLayoutManager(viewGlobal.getContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerViewMyOrders.setAdapter(rvAdapter);
                }
            } catch (Exception e) {
                System.out.println("Error Activity: " + e);
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}