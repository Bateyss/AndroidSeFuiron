package com.fm.modules;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.login.Logon;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.menu.MenuBotton;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.UsuarioService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ComprobarSharedPreferencesActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String usuario = "", passw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedListener();
    }

    private void sharedListener() {
        sharedPreferences = getSharedPreferences("LogonData", MODE_PRIVATE);
        String usuarioPref = sharedPreferences.getString("email", "neles");
        String passwPref = sharedPreferences.getString("password", "neles");
        if (!"neles".equals(usuarioPref) && !"neles".equals(passwPref)) {
            usuario = usuarioPref;
            passw = passwPref;
            Acceder acceder = new Acceder();
            acceder.execute();
        } else {
            Intent intent = new Intent(ComprobarSharedPreferencesActivity.this, Logon.class);
            startActivity(intent);
        }
    }

    public class Acceder extends AsyncTask<String, String, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            int v = 0;
            try {
                if (isNetActive()) {
                    Usuario u = new Usuario();
                    u.setUsername(usuario);
                    u.setPassword(Utilities.encrip(passw));
                    UsuarioService usuarioService = new UsuarioService();
                    v = usuarioService.signIn(u);
                    if (v > 0) {
                        u = usuarioService.obtenerUsuarioPorId((long) v);
                        if (u != null) {
                            editor = sharedPreferences.edit();
                            editor.putString("email", usuario);
                            editor.putString("password", passw);
                            editor.apply();
                        } else {
                            editor = sharedPreferences.edit();
                            editor.putString("email", "neles");
                            editor.putString("password", "neles");
                            editor.commit();
                        }
                    }
                    Logued.usuarioLogued = u;
                }
            } catch (Exception ex) {
                System.out.println("*** errrr***: " + ex);
                ex.printStackTrace();
            }
            return v;
        }

        @Override
        protected void onPostExecute(Integer res) {
            super.onPostExecute(res);
            try {
                if (res > 0) {
                    Intent intent = new Intent(ComprobarSharedPreferencesActivity.this, MenuBotton.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(ComprobarSharedPreferencesActivity.this, Logon.class);
                    startActivity(intent);
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

    public void dialog1() {
        Dialog dialog = new AlertDialog.Builder(ComprobarSharedPreferencesActivity.this)
                .setView(R.layout.dialog_server_err)
                .setCancelable(true)
                .setPositiveButton("Regresar", null).create();
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_shape));
        dialog.show();
    }

    public void dialog2() {
        Dialog dialog1 = new MaterialAlertDialogBuilder(ComprobarSharedPreferencesActivity.this)
                .setView(R.layout.dialog_user_err)
                .setCancelable(true)
                .setPositiveButton("Regresar", null).create();
        dialog1.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_shape));
        dialog1.show();
    }

    public void dialog3() {
        Dialog dialog2 = new MaterialAlertDialogBuilder(ComprobarSharedPreferencesActivity.this)
                .setView(R.layout.dialog_pass_err)
                .setCancelable(true)
                .setPositiveButton("Regresar", null).create();
        dialog2.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_shape));
        dialog2.show();
    }
}