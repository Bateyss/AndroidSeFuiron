package com.fm.modules.app.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fm.modules.R;
import com.fm.modules.app.login.ChangePassActivity;
import com.fm.modules.app.login.Logon;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.login.RecoveryPasswordLogued;
import com.fm.modules.app.restaurantes.RestaurantePorCategoria;
import com.fm.modules.models.Usuario;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileFragment extends Fragment {

    private AppCompatImageView imagenPerfil;
    private AppCompatTextView profileName;
    private AppCompatTextView ProfileEmail;
    private AppCompatTextView changePasword;
    private AppCompatTextView recoveryPassword;
    private MaterialButton logOutBtn;
    private View viewGlobal;
    private Usuario usuario;
    private FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.act_user_profile, container, false);
        sharedPreferences = view.getContext().getSharedPreferences("LogonData", Context.MODE_PRIVATE);
        firebaseAuth = FirebaseAuth.getInstance();
        viewGlobal = view;
        imagenPerfil = (AppCompatImageView) view.findViewById(R.id.profileProfilePhoto);
        profileName = (AppCompatTextView) view.findViewById(R.id.profileProfileName);
        ProfileEmail = (AppCompatTextView) view.findViewById(R.id.profileProfileEmail);
        changePasword = (AppCompatTextView) view.findViewById(R.id.profileProfileChanguePassword);
        recoveryPassword = (AppCompatTextView) view.findViewById(R.id.profileProfileRecoveryPassword);
        logOutBtn = (MaterialButton) view.findViewById(R.id.profileProfileLogOut);
        verPerfil();
        signOut();
        recoveryListener();
        changueListener();
        onBack();
        return view;
    }

    private void changueListener() {
        changePasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewGlobal.getContext(), ChangePassActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void recoveryListener() {
        recoveryPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewGlobal.getContext(), RecoveryPasswordLogued.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void signOut() {
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    editor = sharedPreferences.edit();
                    editor.putString("email", "neles");
                    editor.putString("password", "neles");
                    editor.commit();
                } catch (Exception ignore) {
                }
                firebaseAuth.signOut();
                Logued.usuarioLogued = null;
                Intent intent = new Intent(viewGlobal.getContext(), Logon.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                viewGlobal.getContext().startActivity(intent);
            }
        });
    }

    private void verPerfil() {
        Usuario u = Logued.usuarioLogued;
        if (u != null) {
            usuario = u;
            Bitmap bitmap = Logued.imagenPerfil;
            if (bitmap != null) {
                imagenPerfil.setImageBitmap(bitmap);
            } else {
                imagenPerfil.setImageResource(R.drawable.ic_empty_profile_photo);
            }
            profileName.setText(u.getNombre());
            ProfileEmail.setText(u.getCorreoElectronico());
        }
    }

    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                showFragment(new RestaurantePorCategoria());
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }

    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}