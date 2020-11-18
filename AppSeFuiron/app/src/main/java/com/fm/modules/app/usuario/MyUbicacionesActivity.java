package com.fm.modules.app.usuario;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.app.localet.DireccionesViewAdapter;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.menu.MenuBotton;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.PedidoService;
import com.fm.modules.sqlite.models.Direcciones;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class MyUbicacionesActivity extends Fragment {

    View viewGlobal;
    RecyclerView myLocations;
    private AppCompatImageView back;
    MaterialCardView addLocation;
    Button ubicacionSelect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGlobal = inflater.inflate(R.layout.activity_ubicaciones, container, false);
        myLocations = viewGlobal.findViewById(R.id.listPlacesMap);
        back = (AppCompatImageView) viewGlobal.findViewById(R.id.ivBack);
        addLocation = viewGlobal.findViewById(R.id.addLocation);
        ubicacionSelect = viewGlobal.findViewById(R.id.ubicacionSelect);
        addLocation.setVisibility(View.INVISIBLE);
        ubicacionSelect.setVisibility(View.INVISIBLE);
        cargarLocations();
        onBack();
        backListener();
        return viewGlobal;
    }

    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                GlobalCarrito.toShopinCart = true;
                Intent i = new Intent(viewGlobal.getContext(), MenuBotton.class);
                startActivity(i);
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }

    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalCarrito.toShopinCart = true;
                Intent i = new Intent(viewGlobal.getContext(), MenuBotton.class);
                startActivity(i);
            }
        });
    }

    private void cargarLocations() {
        MyLocations myLocations = new MyLocations();
        myLocations.execute();
    }

    private class MyLocations extends AsyncTask<String, String, List<Direcciones>> {

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
                    List<String> dirs = new ArrayList<>();
                    for (Pedido pedido : pedidos) {
                        boolean newer = true;
                        for (String str : dirs) {
                            if (str.equals(pedido.getDireccion())) {
                                newer = false;
                            }
                        }
                        if (newer) {
                            dirs.add(pedido.getDireccion());
                            String[] spliter = {};
                            try {
                                spliter = pedido.getDireccion().split(";", 7);
                            } catch (Exception ignore) {
                            }
                            if (spliter.length > 3) {
                                direcciones = new Direcciones();
                                direcciones.setIdDireccion(++i);
                                direcciones.setDireccion(pedido.getDireccion());
                                direcciones.setCoordenadas(spliter[2]);
                                direcciones.setNombreDireccion(spliter[0]);
                                direccionesList.add(direcciones);
                            }
                        }
                    }
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
                    DireccionesViewAdapter adapter = new DireccionesViewAdapter(direcciones, viewGlobal.getContext(), R.layout.holder_item_option, ubicacionSelect);
                    myLocations.setLayoutManager(new LinearLayoutManager(viewGlobal.getContext(), LinearLayoutManager.VERTICAL, false));
                    myLocations.setAdapter(adapter);
                }
            } catch (Exception e) {
                System.out.println("Error Activity: " + e);
            }
        }

    }
}