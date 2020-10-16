package com.fm.modules.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.fm.modules.R;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.app.menu.MenuBotton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class LoguedFacebook extends AppCompatActivity {

    private ImageView pictureView;
    private TextView title;
    private AppCompatButton continueButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logued_facebook);
        firebaseAuth = FirebaseAuth.getInstance();
        authStateFirebase();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        pictureView = (ImageView) findViewById(R.id.loguedFbPhoto);
        title = (TextView) findViewById(R.id.loguedFbTitle);
        continueButton = (AppCompatButton) findViewById(R.id.loguedFbContinueBtn);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoguedFacebook.this, MenuBotton.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        onBack();
    }
    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                GlobalCarrito.toShopinCart = true;
                Intent i = new Intent(LoguedFacebook.this, MenuBotton.class);
                startActivity(i);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    public void authStateFirebase() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    try {
                        String tex = Profile.getCurrentProfile().getFirstName() + ", No puedes cambiar Password con cuenta Vinculada";
                        title.setText(tex);
                        Picasso.get().load(Profile.getCurrentProfile().getProfilePictureUri(500, 500)).placeholder(R.drawable.not_found).into(pictureView);
                    } catch (Exception ignore) {
                    }
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