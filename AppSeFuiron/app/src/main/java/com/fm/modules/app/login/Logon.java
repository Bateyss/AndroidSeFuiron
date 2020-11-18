package com.fm.modules.app.login;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fm.modules.R;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.menu.MenuBotton;
import com.fm.modules.app.signup.SignUp;
import com.fm.modules.models.Image;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.ImageService;
import com.fm.modules.service.UsuarioService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
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
    private MaterialCardView buttonsing;
    private MaterialCardView loginFacebook2;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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
        // dejar para que inicia configuracion automatica de inicio con faceboo
        loginButtonFB = (LoginButton) findViewById(R.id.btnFuimonosLoginFacebook);
        loginButtonFB.setVisibility(View.INVISIBLE);
        loginFacebook2 = findViewById(R.id.loginFacebook2);
        loginFacebook();
        // end facebook
        //logon
        Logued.imagenPerfil = null;
        userInput = (TextInputEditText) findViewById(R.id.etEmaillogin);
        passInput = (TextInputEditText) findViewById(R.id.etPasswordlogin);
        buttonLogin = (Button) findViewById(R.id.btnLogin);
        buttonsing = (MaterialCardView) findViewById(R.id.materialSignIn);
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
        onBack();
    }

    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                GlobalCarrito.toShopinCart = true;
                Intent i = new Intent(Logon.this, Logon.class);
                startActivity(i);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void sharedListener() {
        sharedPreferences = getSharedPreferences("LogonData", MODE_PRIVATE);
        String usuarioPref = sharedPreferences.getString("email", "neles");
        String passwPref = sharedPreferences.getString("password", "neles");
        if (!"neles".equals(usuarioPref) && !"neles".equals(passwPref)) {
            usuario = usuarioPref;
            passw = passwPref;
            inhabilatBotones();
            Acceder acceder = new Acceder();
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
                buttonLogin.setEnabled(false);
                if (validUserAndPass()) {
                    inhabilatBotones();
                    Acceder acceder = new Acceder();
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
        loginFacebook2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(Logon.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(callbackManagerFB, new FacebookCallback<LoginResult>() {
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
        });
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
                Toast.makeText(Logon.this, "Igrese Correo", Toast.LENGTH_LONG).show();
                userInput.setHintTextColor(Color.RED);
                buttonLogin.setEnabled(true);
                return false;
            }
            if ("".equals(passw)) {
                Toast.makeText(Logon.this, "Igrese contraseña", Toast.LENGTH_LONG).show();
                passInput.setHintTextColor(Color.RED);
                buttonLogin.setEnabled(true);
                return false;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(usuario).matches()) {
                Toast.makeText(Logon.this, "Ingrese un Correo Valido", Toast.LENGTH_LONG).show();
                userInput.setTextColor(Color.RED);
                buttonLogin.setEnabled(true);
                return false;
            }
            return true;
        } catch (Exception e) {
        }
        return b;
    }

    public void limpiar() {
        userInput.setHintTextColor(Color.BLACK);
        userInput.setTextColor(Color.BLACK);
        passInput.setTextColor(Color.BLACK);
        passInput.setHintTextColor(Color.BLACK);
        passInput.setText("");
    }

    public void loginFirebasewithFacebookToken(AccessToken accessToken) {
        if (isNetActive()) {
            AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
            firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // accion de usuario autenticado
                        FirebaseUser user = task.getResult().getUser();
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        // usuario incorrecto o no existe
                        dialog2();
                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // contraseña incorrecta
                        dialog3();
                    } else {
                        // error de con el servidor o algo parecido
                        dialog1();
                    }
                }
            });
        } else {
            dialog1();
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
        final Dialog dialog1 = new MaterialAlertDialogBuilder(Logon.this)
                .setView(R.layout.dialog_user_regstd)
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Intent intt2 = new Intent(Logon.this, MenuBotton.class);
                        intt2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intt2);
                    }
                })
                .create();
        dialog1.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_shape));
        dialog1.show();
        MaterialButton materialButton = dialog1.findViewById(R.id.btnDialog);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intt = new Intent(Logon.this, MenuBotton.class);
                intt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intt);
            }
        });
    }

    public void dialogo2() {
        final Dialog dialog1 = new MaterialAlertDialogBuilder(Logon.this)
                .setView(R.layout.dialog_user_no_regstd)
                .setCancelable(true)
                .create();
        dialog1.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_shape));
        dialog1.show();
        MaterialButton materialButton = dialog1.findViewById(R.id.btnDialog);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }

    public void inhabilatBotones() {
        buttonLogin.setEnabled(false);
        buttonLogin.setBackgroundColor(getResources().getColor(R.color.gray));
        buttonsing.setEnabled(false);
    }

    public void habilatBotones() {
        buttonLogin.setEnabled(true);
        buttonLogin.setBackgroundColor(getResources().getColor(R.color.orange));
        buttonsing.setEnabled(true);
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
                switch (res) {
                    case 0:
                        dialog1();
                        break;
                    case -1:
                        dialog2();
                        break;
                    case -2:
                        dialog3();
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
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    public void dialog1() {
        final Dialog dialog = new AlertDialog.Builder(Logon.this)
                .setView(R.layout.dialog_server_err)
                .setCancelable(true)
                .create();
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_shape));
        dialog.show();
        MaterialButton materialButton = dialog.findViewById(R.id.btnDialog);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void dialog2() {
        final Dialog dialog1 = new MaterialAlertDialogBuilder(Logon.this)
                .setView(R.layout.dialog_user_err)
                .setCancelable(true)
                .create();
        dialog1.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_shape));
        dialog1.show();
        MaterialButton materialButton = dialog1.findViewById(R.id.btnDialog);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }

    public void dialog3() {
        final Dialog dialog2 = new MaterialAlertDialogBuilder(Logon.this)
                .setView(R.layout.dialog_pass_err)
                .setCancelable(true)
                .create();
        dialog2.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_shape));
        dialog2.show();
        MaterialButton materialButton = dialog2.findViewById(R.id.btnDialog);
        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        AppCompatTextView fgrPass2 = dialog2.findViewById(R.id.xxrs);
        fgrPass2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Logon.this, RecoveryPassword.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
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
                switch (res) {
                    case 0:
                        dialog1();
                        break;
                    case -1:
                        dialog2();
                        break;
                    case -2:
                        dialog3();
                        break;
                    case -4:
                        Toast.makeText(Logon.this, "Usuario Inactivo", Toast.LENGTH_LONG).show();
                        break;
                    case -5:
                        dialogo1();
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
                    usuario = user.getEmail();
                    passw = user.getUid();
                    inhabilatBotones();
                    AccederFirebased accederFirebased1 = new AccederFirebased();
                    accederFirebased1.execute(user);
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
}