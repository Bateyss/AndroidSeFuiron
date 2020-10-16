package com.fm.modules.app.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.fm.modules.R;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.menu.MenuBotton;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.UsuarioService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {

    private EditText pass1;
    private EditText pass2;
    private EditText pass3;
    private Button btnchangue;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private boolean isFirebased = false;
    private Usuario user = null;
    private Recoverys recoverys = new Recoverys();
    private AppCompatImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        // firebase
        firebaseAuth = FirebaseAuth.getInstance();
        authStateFirebase();
        // end firebase
        pass1 = (EditText) findViewById(R.id.changePassPass1);
        pass3 = (EditText) findViewById(R.id.changePassPass2);
        pass2 = (EditText) findViewById(R.id.changePassPass3);
        btnchangue = (Button) findViewById(R.id.changePassBtnChange);
        back = (AppCompatImageView) findViewById(R.id.ivBack);
        changueListener();
        onBack();
        backListener();
    }
    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalCarrito.toShopinCart = true;
                Intent i = new Intent(ChangePassActivity.this, MenuBotton.class);
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
                Intent i = new Intent(ChangePassActivity.this, MenuBotton.class);
                startActivity(i);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void changueListener() {
        btnchangue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validar()) {
                    if (!isFirebased) {
                        Usuario usuario = Logued.usuarioLogued;
                        user = usuario;
                        if (usuario != null) {
                            recoverys.execute();
                        } else {
                            Toast.makeText(ChangePassActivity.this, "Error de Nuevo", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    public class Recoverys extends AsyncTask<String, String, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            int v = 0;
            try {
                if (isNetActive()) {
                    Usuario usuario = user;
                    usuario.setPassword(Utilities.encrip(pass2.getText().toString()));
                    UsuarioService usuarioService = new UsuarioService();
                    Usuario usuario1 = usuarioService.actualizarUsuarioPorId(usuario);
                    if (usuario1 != null) {
                        v = 1;
                    }
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
                    Intent intent = new Intent(ChangePassActivity.this, ChangedPassLogued.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChangePassActivity.this, "Hubo un error", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ChangePassActivity.this, "con la Conexion", Toast.LENGTH_SHORT).show();
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
        recoverys.cancel(true);
        recoverys = new Recoverys();
    }

    private boolean validar() {
        boolean r = false;
        try {
            if ("".equals(pass1.getText().toString())) {
                Toast.makeText(ChangePassActivity.this, "Ingresa Contrasela Actual", Toast.LENGTH_SHORT).show();
                return false;
            }
            if ("".equals(pass2.getText().toString())) {
                Toast.makeText(ChangePassActivity.this, "Ingresa Contrasela Nueva", Toast.LENGTH_SHORT).show();
                return false;
            }
            if ("".equals(pass3.getText().toString())) {
                Toast.makeText(ChangePassActivity.this, "Confirma Contrasela Actual", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!pass2.getText().toString().equals(pass3.getText().toString())) {
                Toast.makeText(ChangePassActivity.this, "Error de Nuevo", Toast.LENGTH_SHORT).show();
                Toast.makeText(ChangePassActivity.this, "Los password", Toast.LENGTH_SHORT).show();
                Toast.makeText(ChangePassActivity.this, "No coinciden", Toast.LENGTH_SHORT).show();
                return false;
            }
            Usuario usuario = new Usuario();
            usuario = Logued.usuarioLogued;
            if (usuario != null) {
                String ps = Utilities.encrip(pass1.getText().toString());
                if (!usuario.getPassword().equals(ps)) {
                    Toast.makeText(ChangePassActivity.this, "Password Actual Incorrecto", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(ChangePassActivity.this, "Neles Usuario", Toast.LENGTH_SHORT).show();
                return false;
            }
            r = true;
        } catch (Exception ignore) {
        }
        return r;
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

    public void authStateFirebase() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    isFirebased = true;
                    Intent intent = new Intent(ChangePassActivity.this, LoguedFacebook.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}