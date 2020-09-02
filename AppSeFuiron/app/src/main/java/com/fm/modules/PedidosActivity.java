package com.fm.modules;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fm.modules.adapters.PedidosItemViewAdapter;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.PedidoService;
import com.fm.modules.service.UsuarioService;

import java.util.LinkedList;
import java.util.List;

public class PedidosActivity extends AppCompatActivity {

    UnderThreads underThreads = new UnderThreads();
    Conexion conexion = new Conexion();
    private boolean conected;
    private final String dominio = "http://192.168.1.18:8081";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        listView = (ListView) findViewById(R.id.PedidosList);
        conected = isNetActive();
        if (conected) {
            conexion.execute();
        } else {
            Toast.makeText(PedidosActivity.this, "No hay conexion", Toast.LENGTH_LONG);
            Log.e("error", "" + "no hay conexion");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reiniciarAsynkProcess();
    }

    public void reiniciarAsynkProcess() {
        underThreads.cancel(true);
        conexion.cancel(true);
        underThreads = new UnderThreads();
        conexion = new Conexion();
    }

    public boolean isNetActive() {
        boolean c = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                c = true;
            }
        } catch (Exception e) {
            Log.e("error", "" + "error al comprobar conexion");
            Log.e("error", "" + e);
            c = false;
        }
        return c;
    }

    public class Conexion extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean conextado = false;
            try {
                UsuarioService usuarioService = new UsuarioService();
                Usuario u  = usuarioService.obtenerUsuarioPorId(1L);
                if (u != null) {
                    conextado = true;
                }
            } catch (Exception e) {
                Log.e("Conexion", "error al comprobar conexion");
                Log.e("Conexion", "" + e);
            }
            return conextado;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            try {
                if (aBoolean) {
                    Toast.makeText(PedidosActivity.this, "conectado..", Toast.LENGTH_SHORT).show();
                    underThreads.execute();
                } else {
                    Toast.makeText(PedidosActivity.this, "problema con servidor ", Toast.LENGTH_LONG).show();
                    reiniciarAsynkProcess();
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }
    }

    public class UnderThreads extends AsyncTask<String, String, List<Pedido>> {


        @Override
        protected List<Pedido> doInBackground(String... strings) {
            List<Pedido> pedidoList = new LinkedList<>();
            try {
                PedidoService api = new PedidoService();
                pedidoList = api.obtenerPedidos();
            } catch (Exception ex) {
            }
            return pedidoList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Pedido> objects) {
            super.onPostExecute(objects);
            try {
                if (!objects.isEmpty()) {
                    PedidosItemViewAdapter adapter = new PedidosItemViewAdapter(objects, PedidosActivity.this, R.layout.list_item);
                    listView.setAdapter(adapter);
                    Toast.makeText(PedidosActivity.this, "Hay " + objects.size() + " pedidos", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PedidosActivity.this, "No HHay Pedids Pendientes", Toast.LENGTH_SHORT).show();
                    reiniciarAsynkProcess();
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}