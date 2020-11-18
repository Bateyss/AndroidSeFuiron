package com.fm.modules;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fm.modules.app.login.Logon;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.menu.MenuBotton;
import com.fm.modules.models.Image;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.ImageService;
import com.fm.modules.service.UsuarioService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;

public class ComprobarLoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String usuario = "", passw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // firebase
        firebaseAuth = FirebaseAuth.getInstance();
        authStateFirebase();
        // end firebase
    }

    public void authStateFirebase() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    AccederFirebased accederFirebased1 = new AccederFirebased();
                    accederFirebased1.execute(user);
                } else {
                    Intent intent = new Intent(ComprobarLoginActivity.this, ComprobarSharedPreferencesActivity.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        borrarDatos();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void borrarDatos() {
        try {
            Logued.opcionesDeSubMenusEnPlatillosSeleccionados = new ArrayList<>();
            Logued.platillosSeleccionadosActuales = new ArrayList<>();
            Logued.pedidoActual = null;
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    public class AccederFirebased extends AsyncTask<FirebaseUser, String, Integer> {

        @Override
        protected Integer doInBackground(FirebaseUser... users) {
            int verifier = 0;
            try {
                Usuario u = new Usuario();
                u.setUsername(users[0].getEmail());
                u.setPassword(users[0].getUid());
                UsuarioService usuarioService = new UsuarioService();
                verifier = usuarioService.signIn(u);
                if (verifier > 0) {
                    u = usuarioService.obtenerUsuarioPorId((long) verifier);
                    Logued.usuarioLogued = u;
                }
                if (verifier == -1) {
                    verifier = registrarUsuario(usuarioService, users[0]);
                }
            } catch (Exception ex) {
                System.out.println("*** errrr***: " + ex);
                ex.printStackTrace();
            }
            return verifier;
        }

        public int registrarUsuario(UsuarioService usuarioService, FirebaseUser user) {
            int i = -6;
            try {
                Usuario u = new Usuario();
                u.setNombre(user.getDisplayName());
                u.setApellido("");
                u.setUsername(user.getUid());
                u.setPassword(user.getUid());
                u.setCorreoElectronico(user.getEmail());
                u.setCelular(user.getPhoneNumber());
                u.setFechaCreacion(new Date());
                u.setUltimoInicio(new Date());
                u.setHabilitado(true);
                u.setRegPago("EFECTIVO");
                u.setFechaDeMacimiento(new Date());
                Image image = new Image();
                ImageService imageService = new ImageService();
                Image inm = imageService.crearImagen(image);
                u.setImagenDePerfil(inm.getId());
                Usuario registered = usuarioService.crearUsuario(u);
                if (registered != null) {
                    Logued.usuarioLogued = registered;
                    i = -5;
                }
            } catch (Exception e) {
                System.out.println("erro register user from firebase: " + e);
            }
            return i;
        }

        @Override
        protected void onPostExecute(Integer res) {
            super.onPostExecute(res);
            try {
                if (res > 0) {
                    Intent intent = new Intent(ComprobarLoginActivity.this, MenuBotton.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ComprobarLoginActivity.this, Logon.class);
                    startActivity(intent);
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public void dialogo1() {
        Dialog dialog1 = new MaterialAlertDialogBuilder(ComprobarLoginActivity.this)
                .setView(R.layout.dialog_user_regstd)
                .setCancelable(true)
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intt = new Intent(ComprobarLoginActivity.this, MenuBotton.class);
                        intt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intt);
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Intent intt2 = new Intent(ComprobarLoginActivity.this, MenuBotton.class);
                        intt2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intt2);
                    }
                })
                .create();
        dialog1.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_shape));
        dialog1.show();
    }

    public void dialogo2() {
        Dialog dialog1 = new MaterialAlertDialogBuilder(ComprobarLoginActivity.this)
                .setView(R.layout.dialog_user_no_regstd)
                .setCancelable(true)
                .setPositiveButton("Regresar", null).create();
        dialog1.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_shape));
        dialog1.show();
    }

    public void dialog1() {
        Dialog dialog = new AlertDialog.Builder(ComprobarLoginActivity.this)
                .setView(R.layout.dialog_server_err)
                .setCancelable(true)
                .setPositiveButton("Regresar", null).create();
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_shape));
        dialog.show();
    }

    public void dialog2() {
        Dialog dialog1 = new MaterialAlertDialogBuilder(ComprobarLoginActivity.this)
                .setView(R.layout.dialog_user_err)
                .setCancelable(true)
                .setPositiveButton("Regresar", null).create();
        dialog1.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_shape));
        dialog1.show();
    }

    public void dialog3() {
        Dialog dialog2 = new MaterialAlertDialogBuilder(ComprobarLoginActivity.this)
                .setView(R.layout.dialog_pass_err)
                .setCancelable(true)
                .setPositiveButton("Regresar", null).create();
        dialog2.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_shape));
        dialog2.show();
    }
}