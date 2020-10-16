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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.fm.modules.R;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.app.carrito.ProcesarCarritoActivity;
import com.fm.modules.app.menu.MenuBotton;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.UsuarioService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RecoveryPasswordLogued extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private MaterialTextView email;
    private MaterialButton recoveryBtn;
    private Recovery recovery = new Recovery();
    private boolean isFirebased = false;
    private Usuario usuario = null;
    private AppCompatImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_recover_password_logued);
        // firebase
        firebaseAuth = FirebaseAuth.getInstance();
        authStateFirebase();
        // end firebase
        //logon
        Logued.imagenPerfil = null;
        email = (MaterialTextView) findViewById(R.id.recoveryEmail_logued);
        recoveryBtn = (MaterialButton) findViewById(R.id.recoveryBtnPassword_logued);
        back = (AppCompatImageView) findViewById(R.id.ivBack);
        usuario = Logued.usuarioLogued;
        if (usuario != null) {
            email.setText(usuario.getCorreoElectronico());
        } else {
            String g = "neles";
            email.setText(g);
        }
        recoveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFirebased) {
                    enviarEmail();
                } else {
                    Intent intent = new Intent(RecoveryPasswordLogued.this, LoguedFacebook.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        // end logon
        backListener();
        onBack();
    }

    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalCarrito.toShopinCart = true;
                Intent i = new Intent(RecoveryPasswordLogued.this, MenuBotton.class);
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
                Intent i = new Intent(RecoveryPasswordLogued.this, MenuBotton.class);
                startActivity(i);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void enviarEmail() {
        if (usuario != null) {
            try {
                if (Patterns.EMAIL_ADDRESS.matcher(usuario.getCorreoElectronico()).matches()) {
                    recovery.execute();
                } else {
                    Toast.makeText(RecoveryPasswordLogued.this, "Correo No Valido", Toast.LENGTH_LONG).show();
                }
            } catch (Exception ignore) {
            }
        } else {
            Toast.makeText(RecoveryPasswordLogued.this, "No hay Usuario", Toast.LENGTH_SHORT).show();
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
                    u.setUsername(usuario.getCorreoElectronico());
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
                    Intent intent = new Intent(RecoveryPasswordLogued.this, RecoveredPassLogued.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(RecoveryPasswordLogued.this, "Hubo un error", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RecoveryPasswordLogued.this, "correo de la cuenta", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RecoveryPasswordLogued.this, "incorrecto", Toast.LENGTH_SHORT).show();
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

    public void authStateFirebase() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    isFirebased = true;
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