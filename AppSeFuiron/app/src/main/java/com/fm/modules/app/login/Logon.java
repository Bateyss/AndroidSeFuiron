package com.fm.modules.app.login;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fm.modules.R;
import com.fm.modules.app.restaurantes.RestaurantePorCategoria;
import com.fm.modules.app.signup.SignUp;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.UsuarioService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class Logon extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private String usuario = "", passw = "";
    private TextInputEditText userInput;
    private TextInputEditText passInput;
    private Button buttonLogin;
    private ImageView buttonsing;
    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutPass;
    ProgressBar progressBar;
    Acceder acceder = new Acceder();

    private boolean conected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        // firebase
        firebaseAuth = FirebaseAuth.getInstance();
        // end firebase
        // inputLayoutEmail = (TextInputLayout) findViewById(R.id.inputEmail);
        userInput = (TextInputEditText) findViewById(R.id.etEmaillogin);
        // inputLayoutPass = (TextInputLayout) findViewById(R.id.inputEmail);
        passInput = (TextInputEditText) findViewById(R.id.etPasswordlogin);
        buttonLogin = (Button) findViewById(R.id.btnLogin);
        buttonsing = (ImageView) findViewById(R.id.btnFuimonosSignUp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        userInput.setText("");
        passInput.setText("");
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

    public void verificacionUser() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // usuario registrado
                }
            }
        };
    }

    public void reiniciarAsynkProcess() {
        acceder.cancel(true);
        acceder = new Acceder();
    }

    public void loginFirebase(String user, String pass) {
        conected = isNetActive();
        if (conected) {
            firebaseAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // accion de usuario autenticado
                        Toast.makeText(Logon.this, "yeah", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseAuthInvalidUserException) {
                        // usuario incorrecto o no existe
                        Toast.makeText(Logon.this, "neles de usuario", Toast.LENGTH_LONG).show();
                        AlertDialog dialog = new AlertDialog.Builder(Logon.this)
                                .setView(R.layout.dialog_user_err)
                                .setCancelable(true)
                                .setPositiveButton("Ok", null)
                                .show();
                        /*
                        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                                Logon.this
                        );
                        View botonView = LayoutInflater.from(getApplicationContext()).inflate(
                                R.layout.frg_error_pass,
                                (LinearLayout) findViewById(R.id.passDialogerror)
                        );
                        botonView.findViewById(R.id.btnClosePassErr).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bottomSheetDialog.dismiss();
                            }
                        });
                         */
                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // contraseña incorrecta
                        Toast.makeText(Logon.this, "neles con password", Toast.LENGTH_LONG).show();
                        AlertDialog dialog = new AlertDialog.Builder(Logon.this)
                                .setView(R.layout.dialog_pass_err)
                                .setCancelable(true)
                                .setPositiveButton("Ok", null)
                                .show();
                    } else {
                        // error de con el servidor o algo parecido
                        Toast.makeText(Logon.this, "neles de server", Toast.LENGTH_LONG).show();
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

    public void singInFirebase(String user, String pass) {
        conected = isNetActive();
        if (conected) {
            firebaseAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // accion de usuario creado
                        Toast.makeText(Logon.this, "Usuario Creado", Toast.LENGTH_LONG).show();
                    } else {
                        // accion de usuario creado
                        Toast.makeText(Logon.this, "Usuario no Creado", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // error de con el servidor o algo parecido
                    Toast.makeText(Logon.this, "neles de server", Toast.LENGTH_LONG).show();
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

    public class Acceder extends AsyncTask<String, String, Integer> {


        @Override
        protected Integer doInBackground(String... strings) {
            int v = 0;

            try {
                if (isNetActive()) {
                    System.out.println("comienza a leer vistas");
                    Usuario u = new Usuario();
                    u.setUsername(usuario);
                    u.setPassword(passw);

                    UsuarioService usuarioService = new UsuarioService();
                    v = usuarioService.signIn(u);
                    if (v > 0){
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
                if (res > 0){
                    Intent intent = new Intent(Logon.this, RestaurantePorCategoria.class);
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
}