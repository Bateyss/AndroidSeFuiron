package com.fm.modules.app.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fm.modules.R;
import com.fm.modules.app.commons.utils.Generics;
import com.fm.modules.app.restaurantes.RestaurantePorCategoria;
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
import java.net.URL;
import java.util.Date;

public class Logon extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    CallbackManager callbackManagerFB;
    LoginButton loginButtonFB;

    private String usuario = "", passw = "";
    private TextInputEditText userInput;
    private TextInputEditText passInput;
    private Button buttonLogin;
    private ImageView buttonsing;
    ProgressBar progressBar;
    Acceder acceder = new Acceder();
    AccederFirebased accederFirebased = new AccederFirebased();

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
        userInput = (TextInputEditText) findViewById(R.id.etEmaillogin);
        passInput = (TextInputEditText) findViewById(R.id.etPasswordlogin);
        buttonLogin = (Button) findViewById(R.id.btnLogin);
        buttonsing = (ImageView) findViewById(R.id.btnFuimonosSignUp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        userInput.setText("");
        passInput.setText("");
        loginFuimonos();
        // end logon
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

    public class Acceder extends AsyncTask<String, String, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            int v = 0;
            try {
                if (isNetActive()) {
                    Usuario u = new Usuario();
                    u.setUsername(usuario);
                    u.setPassword(passw);

                    UsuarioService usuarioService = new UsuarioService();
                    v = usuarioService.signIn(u);
                    if (v > 0) {
                        u = usuarioService.obtenerUsuarioPorId((long) v);
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
                    Intent intent = new Intent(Logon.this, RestaurantePorCategoria.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                limpiar();
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
                ImageService imageService = new ImageService();
                byte[] arrayBytes = new byte[0];
                if (user.getPhotoUrl() != null) {
                    try {
                        URL url = new URL(user.getPhotoUrl().toString());
                        arrayBytes = Generics.downloadUrl(url);
                    } catch (Exception e) {
                        System.out.println("error imagen " + e);
                    }
                }
                image.setContent(arrayBytes);
                Image inm = imageService.crearImagen(image);
                u.setImagenDePerfil(inm.getId());
                Usuario registered = usuarioService.crearUsuario(u);
                if (registered != null) {
                    Logued.usuarioLogued = u;
                    i = -5;
                }
            } catch (Exception e) {
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
                    case -5:
                        dialogo1();
                        Thread.sleep(2 * 1000);
                        Intent intent = new Intent(Logon.this, RestaurantePorCategoria.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    case -6:
                        dialogo2();
                        break;
                }
                if (res > 0) {
                    Intent intent = new Intent(Logon.this, RestaurantePorCategoria.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                limpiar();
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
                    acceder.execute();
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
            PlatillosSeleccionadoSQLite platillosSeleccionadoSQLite = new PlatillosSeleccionadoSQLite(Logon.this);
            platillosSeleccionadoSQLite.truncate();
            TarjetasSQLite tarjetasSQLite = new TarjetasSQLite(Logon.this);
            tarjetasSQLite.truncate();
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}