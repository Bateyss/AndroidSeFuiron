package com.fm.modules.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.fm.modules.R;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.app.menu.MenuBotton;
import com.google.android.material.button.MaterialButton;

public class ChangedPassLogued extends AppCompatActivity {

    private MaterialButton btnNext;
    private AppCompatImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_action_message);
        btnNext = (MaterialButton) findViewById(R.id.btnChangePassword);
        back = (AppCompatImageView) findViewById(R.id.ivBack);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangedPassLogued.this, MenuBotton.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        onBack();
        backListener();
    }

    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalCarrito.toShopinCart = true;
                Intent i = new Intent(ChangedPassLogued.this, MenuBotton.class);
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
                Intent i = new Intent(ChangedPassLogued.this, MenuBotton.class);
                startActivity(i);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}