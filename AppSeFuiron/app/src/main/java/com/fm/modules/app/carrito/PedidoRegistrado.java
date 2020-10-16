package com.fm.modules.app.carrito;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.fm.modules.R;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.menu.MenuBotton;
import com.fm.modules.models.Pedido;

import java.util.ArrayList;

public class PedidoRegistrado extends AppCompatActivity {

    private Button inicioBtn;
    private TextView orderNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_registrado);
        inicioBtn = (Button) findViewById(R.id.orderToInicio1);
        orderNumber = (TextView) findViewById(R.id.order_number);
        inicioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logued.platillosSeleccionadosActuales = new ArrayList<>();
                Logued.pedidoActual = null;
                Logued.opcionesDeSubMenusEnPlatillosSeleccionados = new ArrayList<>();
                Logued.restauranteActual = null;
                Intent intent = new Intent(PedidoRegistrado.this, MenuBotton.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        Pedido pedido = GlobalCarrito.pedidoRegistrado;
        if (pedido != null) {
            String orden = "Orden #" + pedido.getPedidoId();
            orderNumber.setText(orden);
        }
        onBack();
    }

    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Intent i = new Intent(PedidoRegistrado.this, MenuBotton.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}