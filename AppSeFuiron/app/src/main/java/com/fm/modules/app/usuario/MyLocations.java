package com.fm.modules.app.usuario;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.fm.modules.R;
import com.fm.modules.app.localet.DireccionesViewAdapter2;
import com.fm.modules.app.login.Logued;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.PedidoService;
import com.fm.modules.sqlite.models.Direcciones;

import java.util.ArrayList;
import java.util.List;

public class MyLocations extends Fragment {

    private ListView locationsRecyViw;
    private MyLocationes myLocationes = new MyLocationes();
    private List<Direcciones> direccionesGlobal;
    private View viewGlobal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_locations, container, false);
        viewGlobal = view;
        locationsRecyViw = (ListView) view.findViewById(R.id.mylocationrecyclerviwe);
        verLocations();
        return view;
    }

    private void verLocations() {
        myLocationes.execute();
    }

    private class MyLocationes extends AsyncTask<String, String, List<Direcciones>> {

        @Override
        protected List<Direcciones> doInBackground(String... strings) {
            List<Direcciones> direccionesList = new ArrayList<>();
            try {
                PedidoService pedidoService = new PedidoService();
                Usuario usuario = Logued.usuarioLogued;
                List<Pedido> pedidos = new ArrayList<>();
                if (usuario != null) {
                    pedidos = pedidoService.obtenerMyPedidos(usuario.getUsuarioId());
                }
                if (!pedidos.isEmpty()) {
                    Direcciones direcciones;
                    int i = 0;
                    for (Pedido pedido : pedidos) {
                        direcciones = new Direcciones();
                        direcciones.setIdDireccion(++i);
                        String[] spliter = {};
                        try {
                            spliter = pedido.getDireccion().split(";", 7);
                        } catch (Exception ignore) {
                        }
                        if (spliter.length > 3) {
                            direcciones.setCoordenadas(spliter[2]);
                            direcciones.setNombreDireccion(spliter[0]);
                            direccionesList.add(direcciones);
                        }
                    }
                    direccionesGlobal = direccionesList;
                }
            } catch (Exception e) {
                System.out.println("Error en UnderThreash:" + e.getMessage() + " " + e.getClass());
            }
            return direccionesList;
        }

        @Override
        protected void onPostExecute(List<Direcciones> direcciones) {
            super.onPostExecute(direcciones);
            try {
                if (!direcciones.isEmpty()) {
                    DireccionesViewAdapter2 adapter = new DireccionesViewAdapter2(direcciones, viewGlobal.getContext(), R.layout.holder_item_option);
                    locationsRecyViw.setAdapter(adapter);
                }
            } catch (Exception e) {
                System.out.println("Error Activity: " + e);
            }
            reiniciarMyLocations();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    public void reiniciarMyLocations() {
        myLocationes.cancel(true);
        myLocationes = new MyLocationes();
    }
}