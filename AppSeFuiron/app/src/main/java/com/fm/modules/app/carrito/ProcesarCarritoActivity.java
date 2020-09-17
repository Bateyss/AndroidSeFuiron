package com.fm.modules.app.carrito;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fm.modules.R;
import com.fm.modules.app.login.Logued;
import com.fm.modules.models.Pedido;

import java.util.ArrayList;
import java.util.List;

public class ProcesarCarritoActivity extends AppCompatActivity {

    EditText direccion1;
    EditText direccion2;
    EditText direccion3;
    EditText direccion4;
    EditText direccion5;
    EditText direccion6;
    EditText direccion7;
    Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procesar_carrito);
        direccion1 = (EditText) findViewById(R.id.direccionTxt1);
        direccion2 = (EditText) findViewById(R.id.direccionTxt2);
        direccion3 = (EditText) findViewById(R.id.direccionTxt3);
        direccion4 = (EditText) findViewById(R.id.direccionTxt4);
        direccion5 = (EditText) findViewById(R.id.direccionTxt5);
        direccion6 = (EditText) findViewById(R.id.direccionTxt6);
        direccion7 = (EditText) findViewById(R.id.direccionTxt7);
        btnAgregar = (Button) findViewById(R.id.direccionBtnAdd);
        listeneragregar();
    }

    private void listeneragregar() {
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confimar();
            }
        });
    }

    private void confimar() {
        if (!validar()) {
            return;
        }
        StringBuilder stb = new StringBuilder();
        stb.append(direccion1.getText().toString());
        stb.append(" ; ");
        stb.append(direccion2.getText().toString());
        stb.append(" ; ");
        stb.append(direccion3.getText().toString());
        stb.append(" ; ");
        stb.append(direccion4.getText().toString());
        stb.append(" ; ");
        stb.append(direccion5.getText().toString());
        stb.append(" ; ");
        stb.append(direccion6.getText().toString());
        stb.append(" ; ");
        stb.append(direccion7.getText().toString());
        String dir = stb.toString();
        List<Pedido> pedidos = Logued.pedidosActuales;
        List<Pedido> nuevos = new ArrayList<>();
        if (pedidos != null) {
            if (!pedidos.isEmpty()) {
                for (Pedido pedi : pedidos) {
                    pedi.setDireccion(dir);
                    nuevos.add(pedi);
                }
            }
        }
        Logued.pedidosActuales = nuevos;
        Intent i = new Intent(ProcesarCarritoActivity.this, PagoActivity.class);
        startActivity(i);
    }

    private boolean validar() {
        boolean b = false;
        if ("".equals(direccion1.getText().toString())) {
            Toast.makeText(ProcesarCarritoActivity.this, "Ingrese una Direccion", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(direccion2.getText().toString())) {
            Toast.makeText(ProcesarCarritoActivity.this, "Ingrese una Colonia", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(direccion3.getText().toString())) {
            Toast.makeText(ProcesarCarritoActivity.this, "Ingrese una Referencia", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(direccion4.getText().toString())) {
            Toast.makeText(ProcesarCarritoActivity.this, "Ingrese Numero de Casa", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(direccion5.getText().toString())) {
            Toast.makeText(ProcesarCarritoActivity.this, "Ingrese Numero Referencia", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(direccion6.getText().toString())) {
            Toast.makeText(ProcesarCarritoActivity.this, "Ingrese un Pais", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(direccion7.getText().toString())) {
            Toast.makeText(ProcesarCarritoActivity.this, "Ingrese un Departamento", Toast.LENGTH_SHORT).show();
            return false;
        }
        b = true;
        return b;
    }
}