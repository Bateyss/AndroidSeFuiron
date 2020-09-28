package com.fm.modules.app.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fm.modules.R;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.menu.MenuBotton;
import com.fm.modules.app.signup.SignUp;
import com.fm.modules.models.Image;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.ImageService;
import com.fm.modules.service.UsuarioService;
import com.fm.modules.sqlite.models.OpcionesDeSubMenuSeleccionadoSQLite;
import com.fm.modules.sqlite.models.PedidoSQLite;
import com.fm.modules.sqlite.models.PlatillosSeleccionadoSQLite;
import com.fm.modules.sqlite.models.TarjetasSQLite;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Logon extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    CallbackManager callbackManagerFB;
    LoginButton loginButtonFB;

    private String usuario = "", passw = "";
    private TextInputEditText userInput;
    private TextInputEditText passInput;
    private AppCompatTextView forgotPass;
    private Button buttonLogin;
    private AppCompatTextView buttonsing;
    ProgressBar progressBar;
    Acceder acceder = new Acceder();
    AccederFirebased accederFirebased = new AccederFirebased();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private boolean conected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        // firebase
        firebaseAuth = FirebaseAuth.getInstance();
        authStateFirebase();
        // end firebase
        // facebook
        callbackManagerFB = CallbackManager.Factory.create();
        loginButtonFB = (LoginButton) findViewById(R.id.btnFuimonosLoginFacebook);
        loginFacebook();
        // end facebook
        //logon
        Logued.imagenPerfil = null;
        userInput = (TextInputEditText) findViewById(R.id.etEmaillogin);
        passInput = (TextInputEditText) findViewById(R.id.etPasswordlogin);
        buttonLogin = (Button) findViewById(R.id.btnLogin);
        buttonsing = (AppCompatTextView) findViewById(R.id.tvWithoutAccount);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        forgotPass = (AppCompatTextView) findViewById(R.id.tvForgotPassword);

        userInput.setText("");
        passInput.setText("");
        Logued.pedidoActual = null;
        Logued.opcionesDeSubMenusEnPlatillosSeleccionados = new ArrayList<>();
        Logued.platillosSeleccionadosActuales = new ArrayList<>();
        Logued.restauranteActual = null;
        loginFuimonos();
        forgotListener();
        sharedListener();
        // end logon
    }

    private void sharedListener() {
        sharedPreferences = getSharedPreferences("LogonData", MODE_PRIVATE);
        String usuarioPref = sharedPreferences.getString("email", "neles");
        String passwPref = sharedPreferences.getString("password", "neles");
        if (!"neles".equals(usuarioPref) && !"neles".equals(passwPref)) {
            usuario = usuarioPref;
            passw = passwPref;
            acceder.execute();
        }
    }

    private void forgotListener() {
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Logon.this, RecoveryPassword.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void loginFuimonos() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Logon.this, "Cargando ", Toast.LENGTH_SHORT).show();
                if (validUserAndPass()) {
                    acceder.execute();
                }
            }
        });
        buttonsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Logon.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    public void loginFacebook() {
        loginButtonFB.setReadPermissions("email");
        loginButtonFB.registerCallback(callbackManagerFB, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                loginFirebasewithFacebookToken(accessToken);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManagerFB.onActivityResult(requestCode, resultCode, data);
    }

    public boolean validUserAndPass() {
        boolean b = false;
        usuario = userInput.getText().toString();
        passw = passInput.getText().toString();
        try {
            if ("".equals(usuario)) {
                Toast.makeText(Logon.this, "Igrese usuario", Toast.LENGTH_LONG).show();
                return false;
            }
            if ("".equals(passw)) {
                Toast.makeText(Logon.this, "Igrese contraseña", Toast.LENGTH_LONG).show();
                return false;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(usuario).matches()) {
                Toast.makeText(Logon.this, "Ingrese un Correo Valido", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        } catch (Exception e) {
        }
        return b;
    }

    public void limpiar() {
        userInput.setText("");
        passInput.setText("");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reiniciarAsynkProcess();
    }

    public void reiniciarAsynkProcess() {
        acceder.cancel(true);
        acceder = new Acceder();
        accederFirebased.cancel(true);
        accederFirebased = new AccederFirebased();
    }

    public void loginFirebasewithFacebookToken(AccessToken accessToken) {
        conected = isNetActive();
        if (conected) {
            AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // accion de usuario autenticado
                        Toast.makeText(Logon.this, "yeah", Toast.LENGTH_LONG).show();
                        FirebaseUser user = task.getResult().getUser();
                        try {
                            Logued.imagenPerfil = Picasso.get().load(user.getPhotoUrl()).get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        accederFirebased.execute(user);
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        // usuario incorrecto o no existe
                        AlertDialog dialog = new AlertDialog.Builder(Logon.this)
                                .setView(R.layout.dialog_user_err)
                                .setCancelable(true)
                                .setPositiveButton("Ok", null)
                                .show();
                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // contraseña incorrecta
                        AlertDialog dialog = new AlertDialog.Builder(Logon.this)
                                .setView(R.layout.dialog_pass_err)
                                .setCancelable(true)
                                .setPositiveButton("Ok", null)
                                .show();
                    } else {
                        // error de con el servidor o algo parecido
                        AlertDialog dialog = new AlertDialog.Builder(Logon.this)
                                .setView(R.layout.dialog_server_err)
                                .setCancelable(true)
                                .setPositiveButton("Ok", null)
                                .show();
                    }
                }
            });
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

    public void dialogo1() {
        AlertDialog dialog = new AlertDialog.Builder(Logon.this)
                .setView(R.layout.dialog_user_regstd)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    public void dialogo2() {
        AlertDialog dialog = new AlertDialog.Builder(Logon.this)
                .setView(R.layout.dialog_user_no_regstd)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    public void inhabilatBotones() {
        buttonLogin.setEnabled(false);
        buttonLogin.setBackgroundColor(getResources().getColor(R.color.gray));
        buttonsing.setEnabled(false);
        buttonsing.setBackgroundColor(getResources().getColor(R.color.gray));
    }

    public void habilatBotones() {
        buttonLogin.setEnabled(true);
        buttonLogin.setBackgroundColor(getResources().getColor(R.color.orange));
        buttonsing.setEnabled(true);
        buttonsing.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public class Acceder extends AsyncTask<String, String, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inhabilatBotones();
        }

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
                switch (res) {
                    case 0:
                        Toast.makeText(Logon.this, "Error de servidor", Toast.LENGTH_LONG).show();
                        AlertDialog dialog = new AlertDialog.Builder(Logon.this)
                                .setView(R.layout.dialog_server_err)
                                .setCancelable(true)
                                .setPositiveButton("Ok", null)
                                .show();
                        break;
                    case -1:
                        Toast.makeText(Logon.this, "Error de Username", Toast.LENGTH_LONG).show();
                        AlertDialog dialog1 = new AlertDialog.Builder(Logon.this)
                                .setView(R.layout.dialog_user_err)
                                .setCancelable(true)
                                .setPositiveButton("Ok", null)
                                .show();
                        break;
                    case -2:
                        Toast.makeText(Logon.this, "Error de Password", Toast.LENGTH_LONG).show();
                        AlertDialog dialog2 = new AlertDialog.Builder(Logon.this)
                                .setView(R.layout.dialog_pass_err)
                                .setCancelable(true)
                                .setPositiveButton("Ok", null)
                                .show();
                        break;
                    case -4:
                        Toast.makeText(Logon.this, "Usuario Inactivo", Toast.LENGTH_LONG).show();
                        break;
                }
                if (res > 0) {
                    Intent intent = new Intent(Logon.this, MenuBotton.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                limpiar();
                habilatBotones();
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

    public class AccederFirebased extends AsyncTask<FirebaseUser, String, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inhabilatBotones();
        }

        @Override
        protected Integer doInBackground(FirebaseUser... users) {
            int verifier = 0;
            try {
                Usuario u = new Usuario();
                u.setUsername(users[0].getUid());
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
                image.setId(0L);
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
                switch (res) {
                    case 0:
                        Toast.makeText(Logon.this, "Error de servidor", Toast.LENGTH_LONG).show();
                        AlertDialog dialog = new AlertDialog.Builder(Logon.this)
                                .setView(R.layout.dialog_server_err)
                                .setCancelable(true)
                                .setPositiveButton("Continuar", null)
                                .show();
                        break;
                    case -1:
                        Toast.makeText(Logon.this, "Error de Username", Toast.LENGTH_LONG).show();
                        AlertDialog dialog1 = new AlertDialog.Builder(Logon.this)
                                .setView(R.layout.dialog_user_err)
                                .setCancelable(true)
                                .setPositiveButton("Continuar", null)
                                .show();
                        break;
                    case -2:
                        Toast.makeText(Logon.this, "Error de Password", Toast.LENGTH_LONG).show();
                        AlertDialog dialog2 = new AlertDialog.Builder(Logon.this)
                                .setView(R.layout.dialog_pass_err)
                                .setCancelable(true)
                                .setPositiveButton("Continuar", null)
                                .show();
                        break;
                    case -4:
                        Toast.makeText(Logon.this, "Usuario Inactivo", Toast.LENGTH_LONG).show();
                        break;
                    case -5:
                        dialogo1();
                        Thread.sleep(4 * 1000);
                        Intent intent = new Intent(Logon.this, MenuBotton.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    case -6:
                        dialogo2();
                        break;
                }
                if (res > 0) {
                    Intent intent = new Intent(Logon.this, MenuBotton.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                limpiar();
                habilatBotones();
                reiniciarAsynkProcess();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public void authStateFirebase() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    usuario = user.getUid();
                    passw = user.getUid();
                    accederFirebased.execute(user);
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
            OpcionesDeSubMenuSeleccionadoSQLite opcionesDeSubMenuSeleccionadoSQLite = new OpcionesDeSubMenuSeleccionadoSQLite(Logon.this);
            opcionesDeSubMenuSeleccionadoSQLite.truncate();
            PedidoSQLite pedidoSQLite = new PedidoSQLite(Logon.this);
            pedidoSQLite.truncate();
            pedidoSQLite.close();
            PlatillosSeleccionadoSQLite platillosSeleccionadoSQLite = new PlatillosSeleccionadoSQLite(Logon.this);
            platillosSeleccionadoSQLite.truncate();
            platillosSeleccionadoSQLite.close();
            TarjetasSQLite tarjetasSQLite = new TarjetasSQLite(Logon.this);
            tarjetasSQLite.truncate();
            tarjetasSQLite.close();
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
}