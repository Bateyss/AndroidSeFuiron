package com.fm.modules.app.signup;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import com.fm.modules.R;
import com.fm.modules.app.commons.conectivity.Conectividad;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.menu.MenuBotton;
import com.fm.modules.models.Image;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.ImageService;
import com.fm.modules.service.UsuarioService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignUp extends AppCompatActivity {

    private TextInputEditText inputNombre;
    private TextInputEditText inputApellido;
    private TextInputEditText inputCorreo;
    private TextInputEditText inputPass;
    private TextInputEditText inputDateBorn;
    private TextInputEditText inputTelph;
    private TextInputEditText inputUsename;
    private MaterialButton buttonSign;
    private boolean networking;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerDialogListener;
    private CardView imageframeLayout;
    private Date fecha1 = null;
    private Registrar registrar = new Registrar();
    private String fileImagenProfile = null;
    private AppCompatImageView imageProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_signup);
        inputNombre = (TextInputEditText) findViewById(R.id.sgnupName);
        inputApellido = (TextInputEditText) findViewById(R.id.sgnupLastName);
        inputCorreo = (TextInputEditText) findViewById(R.id.sgnupEmail);
        inputPass = (TextInputEditText) findViewById(R.id.sgnupPassword);
        inputDateBorn = (TextInputEditText) findViewById(R.id.etsgnupBirthday);
        inputTelph = (TextInputEditText) findViewById(R.id.sgnupPhoneNumber);
        inputUsename = (TextInputEditText) findViewById(R.id.sgnupUsername);
        buttonSign = (MaterialButton) findViewById(R.id.btnSignUp);
        imageframeLayout = (CardView) findViewById(R.id.sgnflProfilePhoto);
        imageProfile = (AppCompatImageView) findViewById(R.id.signProfilePhoto);
        inputDateBorn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // codigo para la fecha
                // datepicker
                showDatePickDialog(view);
            }
        });
        datePickerDialogListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar cal = Calendar.getInstance();
                cal.set(i, i1, i2);
                SimpleDateFormat sdt = new SimpleDateFormat("EEEE dd MMMM  yyyy");
                inputDateBorn.setText(sdt.format(cal.getTime()));
                fecha1 = cal.getTime();
                System.out.println("fecha1" + fecha1);
            }
        };
        imageframeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto(view);
            }
        });
        networking = Conectividad.isNetActive(getSystemService(CONNECTIVITY_SERVICE));
        buttonSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetActive()) {
                    validadRegistro();
                } else {
                    Toast.makeText(SignUp.this, "No hay Conexion", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void showDatePickDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int mont = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                datePickerDialogListener,
                year,
                mont,
                day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }


    public void validadRegistro() {
        Usuario u = new Usuario();
        String b = "";
        if (b.equals(inputNombre.getText().toString())) {
            Toast.makeText(SignUp.this, "Ingrese Nombre", Toast.LENGTH_LONG).show();
            return;
        }
        if (b.equals(inputApellido.getText().toString())) {
            Toast.makeText(SignUp.this, "Ingrese Apellido", Toast.LENGTH_LONG).show();
            return;
        }
        if (b.equals(inputUsename.getText().toString())) {
            Toast.makeText(SignUp.this, "Ingrese Usuario", Toast.LENGTH_LONG).show();
            return;
        }
        if (b.equals(inputPass.getText().toString())) {
            Toast.makeText(SignUp.this, "Ingrese contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        if (b.equals(inputCorreo.getText().toString())) {
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(inputCorreo.getText().toString()).matches()) {
            Toast.makeText(SignUp.this, "Ingrese un Correo Valido", Toast.LENGTH_LONG).show();
            return;
        }
        if (b.equals(inputTelph.getText().toString())) {
            Toast.makeText(SignUp.this, "Ingrese Telefono", Toast.LENGTH_LONG).show();
            return;
        }
        if (b.equals(inputDateBorn.getText().toString())) {
            Toast.makeText(SignUp.this, "Ingrese Fecha de Nacimiento", Toast.LENGTH_LONG).show();
            return;
        }
        buttonSign.setEnabled(false);
        registrar.execute();
    }

    public void limpiar() {
        inputNombre.setText("");
        inputApellido.setText("");
        inputCorreo.setText("");
        inputPass.setText("");
        fecha1 = new Date();
        inputTelph.setText("");
        inputUsename.setText("");
        buttonSign.setText("");
    }

    public void reiniciarAsynkProcess() {
        registrar.cancel(true);
        registrar = new Registrar();
    }

    public void uploadPhoto(View view) {
        Intent openFile = new Intent(Intent.ACTION_GET_CONTENT);
        openFile.addCategory(Intent.CATEGORY_OPENABLE);
        openFile.setType("image/*");
        // request code 200 es Galeria de fotos
        Intent i = Intent.createChooser(openFile, "file");
        startActivityForResult(openFile, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] str = {MediaStore.Images.Media.DATA};
            Cursor cursor = SignUp.this.getContentResolver().query(data.getData(), str, null, null, null);
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
                    imageProfile.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(SignUp.this, "No se puede subir la imagen", Toast.LENGTH_SHORT).show();
                    imageProfile.setImageResource(R.drawable.ic_confused_person);
                }
            } else {
                Toast.makeText(SignUp.this, "No se puede subir la imagen", Toast.LENGTH_SHORT).show();
                imageProfile.setImageResource(R.drawable.ic_confused_person);
            }
        }
    }

    public void dialogo1() {
        AlertDialog dialog = new AlertDialog.Builder(SignUp.this)
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
        AlertDialog dialog = new AlertDialog.Builder(SignUp.this)
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

    public class Registrar extends AsyncTask<String, String, Boolean> {


        @Override
        protected Boolean doInBackground(String... strings) {
            boolean v = false;
            try {
                Usuario u = new Usuario();
                System.out.println("comienza a leer vistas");
                u.setNombre(inputNombre.getText().toString());
                u.setApellido(inputApellido.getText().toString());
                u.setUsername(inputUsename.getText().toString());
                u.setPassword(Utilities.encrip(inputPass.getText().toString()));
                u.setCorreoElectronico(inputCorreo.getText().toString());
                u.setCelular(inputTelph.getText().toString());
                u.setFechaCreacion(new Date());
                u.setUltimoInicio(new Date());
                u.setHabilitado(true);
                u.setRegPago("EFECTIVO");
                u.setFechaDeMacimiento(fecha1);

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
                Image img = imageService.crearImagen(image);
                u.setImagenDePerfil(img.getId());
                UsuarioService usuarioService = new UsuarioService();
                Usuario registrado = usuarioService.crearUsuario(u);
                if (registrado != null) {
                    Logued.usuarioLogued = registrado;
                    v = true;
                }
            } catch (Exception ex) {
                System.out.println("*** errrr***: " + ex);
                ex.printStackTrace();
            }
            return v;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean res) {
            super.onPostExecute(res);
            try {
                // usuario registrado, compartir en pantalla
                if (res) {
                    dialogo1();
                    Intent i = new Intent(SignUp.this, MenuBotton.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Thread.sleep(4 * 1000);
                    startActivity(i);
                } else {
                    dialogo2();
                    buttonSign.setEnabled(true);
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