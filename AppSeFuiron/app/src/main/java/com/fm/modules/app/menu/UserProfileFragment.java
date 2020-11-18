package com.fm.modules.app.menu;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fm.modules.R;
import com.fm.modules.app.localet.PermissionUtilsStorageRead;
import com.fm.modules.app.localet.PermissionUtilsStorageWrite;
import com.fm.modules.app.login.ChangePassActivity;
import com.fm.modules.app.login.Logon;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.login.RecoveryPasswordLogued;
import com.fm.modules.app.restaurantes.RestaurantePorCategoria;
import com.fm.modules.models.Image;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.ImageService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

import org.springframework.util.FileCopyUtils;

import java.io.File;

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
    MaterialCardView flProfilePhoto;
    private String fileImagenProfile = null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final int ACCES_FILE_PERMISSION_REQUEST_CODE = 1;
    private boolean ACCES_FILE_PERMISSION_REQUEST_GARANTED = false;

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
        flProfilePhoto = view.findViewById(R.id.flProfilePhoto);
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
        flProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto(view);
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

    public void uploadPhoto(View view) {
        if (ACCES_FILE_PERMISSION_REQUEST_GARANTED) {
            Intent getIntent = new Intent();
            getIntent.setType("image/*");
            getIntent.setAction(Intent.ACTION_GET_CONTENT);
            getIntent.addCategory(Intent.CATEGORY_OPENABLE);
            Intent pickIntent = new Intent();
            pickIntent.setAction(Intent.ACTION_PICK);
            pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            // request code 200 es Galeria de fotos
            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
            startActivityForResult(chooserIntent, 1);
        } else {
            Toast.makeText(viewGlobal.getContext(), "No Puedes Subir Imagen", Toast.LENGTH_SHORT).show();
            Toast.makeText(viewGlobal.getContext(), "Primero", Toast.LENGTH_SHORT).show();
            Toast.makeText(viewGlobal.getContext(), "Concede Permisos", Toast.LENGTH_SHORT).show();
            enableFilesRead();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                String[] str = {MediaStore.Images.Media.DATA};
                Cursor cursor = viewGlobal.getContext().getContentResolver().query(data.getData(), str, null, null, null);
                cursor.moveToFirst();
                int id = cursor.getColumnIndex(str[0]);
                String path = cursor.getString(id);
                cursor.close();
                if (path != null) {
                    fileImagenProfile = path;
                    File file = new File(path);
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    } catch (Exception ignore) {
                    }
                    if (bitmap != null) {
                        imagenPerfil.setImageBitmap(bitmap);
                        ActualizarImagen actualizarImagen = new ActualizarImagen();
                        actualizarImagen.execute();
                    } else {
                        Toast.makeText(viewGlobal.getContext(), "Error", Toast.LENGTH_SHORT).show();
                        imagenPerfil.setImageResource(R.drawable.ic_confused_person);
                    }
                } else {
                    Toast.makeText(viewGlobal.getContext(), "Error", Toast.LENGTH_SHORT).show();
                    Toast.makeText(viewGlobal.getContext(), "Intenta con otra Galeria", Toast.LENGTH_LONG).show();
                    imagenPerfil.setImageResource(R.drawable.ic_confused_person);
                }
            } catch (Exception e) {
                System.out.println("erro open image: " + e);
            }
        } else {
            Toast.makeText(viewGlobal.getContext(), "Error", Toast.LENGTH_SHORT).show();
            Toast.makeText(viewGlobal.getContext(), "Intenta con otra Galeria", Toast.LENGTH_LONG).show();
            imagenPerfil.setImageResource(R.drawable.ic_confused_person);
        }
    }

    private void enableFilesRead() {
        boolean ab1 = ContextCompat.checkSelfPermission(viewGlobal.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean ab2 = ContextCompat.checkSelfPermission(viewGlobal.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (ab1 && ab2) {
            ACCES_FILE_PERMISSION_REQUEST_GARANTED = true;
        } else {
            PermissionUtilsStorageWrite.requestPermission((AppCompatActivity) getActivity(), ACCES_FILE_PERMISSION_REQUEST_CODE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            PermissionUtilsStorageRead.requestPermission((AppCompatActivity) getActivity(), ACCES_FILE_PERMISSION_REQUEST_CODE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, true);
            ab1 = ContextCompat.checkSelfPermission(viewGlobal.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            ab2 = ContextCompat.checkSelfPermission(viewGlobal.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            if (ab1 && ab2) {
                ACCES_FILE_PERMISSION_REQUEST_GARANTED = true;
            }
        }
    }

    public class ActualizarImagen extends AsyncTask<String, String, Boolean> {


        @Override
        protected Boolean doInBackground(String... strings) {
            boolean v = false;
            try {
                Image image = new Image();
                image.setContent(null);
                if (fileImagenProfile != null) {
                    try {
                        System.out.println("path: " + fileImagenProfile);
                        image.setContent(FileCopyUtils.copyToByteArray(new File(fileImagenProfile)));
                    } catch (Exception e) {
                        System.out.println("error imagen " + e);
                    }
                }
                ImageService imageService = new ImageService();
                if (Logued.usuarioLogued != null) {
                    if (Logued.usuarioLogued.getImagenDePerfil() != null) {
                        image.setId(Logued.usuarioLogued.getImagenDePerfil());
                        Image img = imageService.actualizarImagenPorId(image);
                        if (img != null) {
                            v = true;
                            if (Logued.imagenesIDs != null && !Logued.imagenesIDs.isEmpty()) {
                                int y = 0;
                                for (int x : Logued.imagenesIDs) {
                                    if (img.getId().intValue() == x) {
                                        Logued.imagenes.set(y, img);
                                    }
                                    y++;
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("*** errrr***: " + ex);
                ex.printStackTrace();
            }
            return v;
        }
    }
}