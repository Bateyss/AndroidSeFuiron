package com.fm.modules;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.rt.restaurante.apis.UsuarioApi;
import com.rt.restaurante.models.Usuario;

import java.util.LinkedList;
import java.util.List;

public class Logon extends AppCompatActivity {

    UnderThreads underThreads = new UnderThreads();
    Conexion conexion = new Conexion();
    String usuario = "", passw = "";
    TextInputEditText userInput;
    TextInputEditText passInput;

    private boolean conected;
    private final String dominio = "http://192.168.1.18:8081";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logon);
        userInput = (TextInputEditText) findViewById(R.id.edtUsuario);
        passInput = (TextInputEditText) findViewById(R.id.edtContra);
        userInput.setText("");
        passInput.setText("");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reiniciarAsynkProcess();
    }

    public void reiniciarAsynkProcess(){
        underThreads.cancel(true);
        conexion.cancel(true);
        underThreads = new UnderThreads();
        conexion = new Conexion();
    }

    public void login(View view) {
        usuario = userInput.getText().toString();
        passw = passInput.getText().toString();
        if ("".equals(usuario)) {
            Toast.makeText(Logon.this, "Igrese usuario", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(passw)) {
            Toast.makeText(Logon.this, "Igrese contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        conected = isNetActive();
        if (conected) {
            conexion.execute();
        } else {
            Toast.makeText(Logon.this, "No hay conexion", Toast.LENGTH_LONG);
        }
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
                UsuarioApi usuarioApi = new UsuarioApi(dominio);
                List<Usuario> list = usuarioApi.findAll();
                if (!list.isEmpty()) {
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
                    Toast.makeText(Logon.this, "conectado..", Toast.LENGTH_SHORT).show();
                    underThreads.execute();
                } else {
                    Toast.makeText(Logon.this, "problema con servidor ", Toast.LENGTH_LONG).show();
                    reiniciarAsynkProcess();
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }
    }

    public class UnderThreads extends AsyncTask<String, String, List<Usuario>> {


        @Override
        protected List<Usuario> doInBackground(String... strings) {
            List<Usuario> usuarioList = new LinkedList<>();
            try {
                UsuarioApi api = new UsuarioApi(dominio);
                Usuario usuario1 = api.findByUserAndPass(usuario, passw);
                if (usuario1 != null) {
                    usuarioList.add(usuario1);
                }
            } catch (Exception ex) {
            }
            return usuarioList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Usuario> objects) {
            super.onPostExecute(objects);
            try {
                if (!objects.isEmpty()) {
                    Intent intent = new Intent(Logon.this, Inicio.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Logon.this, "Usuario o Contraseña Incorrectosce", Toast.LENGTH_SHORT).show();
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