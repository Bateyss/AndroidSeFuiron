package com.fm.modules.app.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.fm.modules.R;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.UsuarioService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RecoveryPassword extends AppCompatActivity {

    private TextInputEditText email;
    private MaterialButton recoveryBtn;
    private Recovery recovery = new Recovery();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recover_password);
        //logon
        Logued.imagenPerfil = null;
        email = (TextInputEditText) findViewById(R.id.recoveryEmail);
        String tst = "";
        email.setText(tst);
        recoveryBtn = (MaterialButton) findViewById(R.id.recoveryBtnPassword);
        recoveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarEmail();
            }
        });
        // end logon
        onBack();
    }

    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                GlobalCarrito.toShopinCart = true;
                Intent i = new Intent(RecoveryPassword.this, Logon.class);
                startActivity(i);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void enviarEmail() {
        String userU = email.getText().toString();
        if (!"".equals(userU)) {
            try {
                if (Patterns.EMAIL_ADDRESS.matcher(userU).matches()) {
                    recovery.execute();
                } else {
                    Toast.makeText(RecoveryPassword.this, "Correo No Valido", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ignore) {
            }
        } else {
            Toast.makeText(RecoveryPassword.this, "Ingrese un correo", Toast.LENGTH_LONG).show();
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

    public class Recovery extends AsyncTask<String, String, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            int v = 0;
            try {
                if (isNetActive()) {
                    Usuario u = new Usuario();
                    u.setUsername(email.getText().toString());
                    u.setPassword("5");
                    UsuarioService usuarioService = new UsuarioService();
                    v = usuarioService.chansePass(u);
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
                if (res == 1) {
                    Intent intent = new Intent(RecoveryPassword.this, RecoveredPass.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(RecoveryPassword.this, "Hubo un error", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RecoveryPassword.this, "Intenta con", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RecoveryPassword.this, "un usuario correcto", Toast.LENGTH_SHORT).show();
                }
                reiniciarAsynkProcess();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    private void reiniciarAsynkProcess() {
        recovery.cancel(true);
        recovery = new Recovery();
    }
}