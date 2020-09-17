package com.fm.modules.app.carrito;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.RecyclerPlatillosSeleccionadosAdapter;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.restaurantes.RestaurantePorCategoria;
import com.fm.modules.models.PlatilloSeleccionado;

import java.text.DecimalFormat;
import java.util.List;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView carritoRecicler;
    private TextView totalOrdenTxtVw;
    private TextView impuestoTxtVw;
    private TextView cargoFuimonosTxtVw;
    private TextView promocionTxtVw;
    private TextView totalaCancelarTxtVw;
    Button btnCodigo;
    Button btnMas;
    Button btnTerminate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_carrito_actual);
        carritoRecicler = (RecyclerView) findViewById(R.id.rvcarrito_compras);
        totalOrdenTxtVw = (TextView) findViewById(R.id.carritoTotal1);
        impuestoTxtVw = (TextView) findViewById(R.id.carritoTotal2);
        cargoFuimonosTxtVw = (TextView) findViewById(R.id.carritoTotal3);
        promocionTxtVw = (TextView) findViewById(R.id.carritoDescuento);
        totalaCancelarTxtVw = (TextView) findViewById(R.id.carritoTotal4);
        btnCodigo = (Button) findViewById(R.id.carritoBtnCodigo);
        btnMas = (Button) findViewById(R.id.carritoBtnMas);
        btnTerminate = (Button) findViewById(R.id.carritoBtnTerminar);
        mostrarCarrito();
        btnListeners();
    }

    private void mostrarCarrito() {
        try {
            List<PlatilloSeleccionado> lista = Logued.platillosSeleccionadosActuales;
            double total1 = 0.00;
            double total2 = 0;
            double total3 = 0;
            double descuento = 0;
            double total4 = 0;
            if (lista != null) {
                if (!lista.isEmpty()) {
                    for (PlatilloSeleccionado pl : lista) {
                        total1 = total1 + pl.getPrecio();
                    }
                    RecyclerPlatillosSeleccionadosAdapter adapter = new RecyclerPlatillosSeleccionadosAdapter(lista, CarritoActivity.this);
                    carritoRecicler.setLayoutManager(new LinearLayoutManager(CarritoActivity.this, LinearLayoutManager.VERTICAL, false));
                    carritoRecicler.setAdapter(adapter);
                    //descuento = lista.get(0).getPlatillo().getMenu().getRestaurante().getDescuento();
                }
            } else {
                Toast.makeText(CarritoActivity.this, "no hay carrito", Toast.LENGTH_SHORT).show();
            }
            total2 = total1 * 0.13;
            total3 = total1 * 0.05;
            // descuento = descuento * total1;
            total4 = total1 + total2 + total3 - descuento;
            DecimalFormat decimalFormat = new DecimalFormat("$ #,##0.00");
            totalOrdenTxtVw.setText(String.valueOf(decimalFormat.format(total1)));
            impuestoTxtVw.setText(String.valueOf(decimalFormat.format(total2)));
            cargoFuimonosTxtVw.setText(String.valueOf(decimalFormat.format(total3)));
            promocionTxtVw.setText(String.valueOf(decimalFormat.format(descuento)));
            totalaCancelarTxtVw.setText(String.valueOf(decimalFormat.format(total4)));
        } catch (Exception e) {
            System.out.println("error carrito: " + e);
        }
    }

    public void btnListeners() {
        btnCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CarritoActivity.this, RestaurantePorCategoria.class);
                startActivity(i);
            }
        });
        btnTerminate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CarritoActivity.this, ProcesarCarritoActivity.class);
                startActivity(i);
            }
        });
    }

}