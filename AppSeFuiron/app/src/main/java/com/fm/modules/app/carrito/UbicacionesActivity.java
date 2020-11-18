package com.fm.modules.app.carrito;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.localet.DireccionesViewAdapter;
import com.fm.modules.app.localet.Location;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.menu.MenuBotton;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.PedidoService;
import com.fm.modules.sqlite.models.Direcciones;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class UbicacionesActivity extends AppCompatActivity {

    RecyclerView myLocations;
    private AppCompatImageView back;
    MaterialCardView addLocation;
    Button ubicacionSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicaciones);
        myLocations = findViewById(R.id.listPlacesMap);
        back = (AppCompatImageView) findViewById(R.id.ivBack);
        addLocation = findViewById(R.id.addLocation);
        ubicacionSelect = findViewById(R.id.ubicacionSelect);
        cargarLocations();
        onBack();
        backListener();
        addLocationListener();
    }

    private void addLocationListener() {
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalCarrito.toShopinCart = true;
                Intent i = new Intent(UbicacionesActivity.this, Location.class);
                startActivity(i);
            }
        });
        ubicacionSelect.setEnabled(false);
        ubicacionSelect.setBackgroundColor(getResources().getColor(R.color.lightGray));
        ubicacionSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalCarrito.toSales = true;
                Intent i = new Intent(UbicacionesActivity.this, MenuBotton.class);
                startActivity(i);
            }
        });
    }

    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                GlobalCarrito.toShopinCart = true;
                Intent i = new Intent(UbicacionesActivity.this, MenuBotton.class);
                startActivity(i);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalCarrito.toShopinCart = true;
                Intent i = new Intent(UbicacionesActivity.this, MenuBotton.class);
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
                    DireccionesViewAdapter adapter = new DireccionesViewAdapter(direcciones, UbicacionesActivity.this, R.layout.holder_item_option, ubicacionSelect);
                    myLocations.setLayoutManager(new LinearLayoutManager(UbicacionesActivity.this, LinearLayoutManager.VERTICAL, false));
                    myLocations.setAdapter(adapter);
                }
            } catch (Exception e) {
                System.out.println("Error Activity: " + e);
            }
        }

    }
}