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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fm.modules.Inicio;
import com.fm.modules.R;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.UsuarioService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedList;
import java.util.List;

public class Logon extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    String usuario = "", passw = "";
    TextInputEditText userInput;
    TextInputEditText passInput;
    Button buttonLogin;

    private boolean conected;
    private final String dominio = "http://192.168.1.18:8081";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // end firebase
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logon);
        userInput = (TextInputEditText) findViewById(R.id.edtUsuario);
        passInput = (TextInputEditText) findViewById(R.id.edtContra);
        buttonLogin = (Button) findViewById(R.id.butonLogin);
        userInput.setText("");
        passInput.setText("");
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                Toast.makeText(Logon.this,"Cargando",Toast.LENGTH_SHORT).show();
                loginFirebase(usuario,passw);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reiniciarAsynkProcess();
    }

    public void verificacionUser(){
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null){
                    // usuario registrado
                }
            }
        };
    }

    public void reiniciarAsynkProcess(){

    }

    public void loginFirebase(String user, String pass) {
        conected = isNetActive();
        if (conected) {
            firebaseAuth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        // accion de usuario autenticado
                        Toast.makeText(Logon.this,"yeah",Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(this,new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof FirebaseAuthInvalidUserException){
                        // usuario incorrecto o no existe
                        Toast.makeText(Logon.this,"nelecesars de usuario",Toast.LENGTH_LONG).show();
                    } else if (e instanceof FirebaseAuthInvalidCredentialsException){
                        // contraseña incorrecta
                        Toast.makeText(Logon.this,"neles con password",Toast.LENGTH_LONG).show();
                    } else{
                        // error de con el servidor o algo parecido
                        Toast.makeText(Logon.this,"neles de server",Toast.LENGTH_LONG).show();
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
}