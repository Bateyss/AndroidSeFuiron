package com.fm.modules.app.carrito;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fm.modules.R;
import com.fm.modules.adapters.MunicipioItemViewAdapter;
import com.fm.modules.app.localet.Location;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.menu.MenuBotton;
import com.fm.modules.models.Departamento;
import com.fm.modules.models.Municipio;
import com.fm.modules.models.Pais;
import com.fm.modules.models.Pedido;
import com.fm.modules.service.DepartamentoService;
import com.fm.modules.service.MunicipioService;
import com.fm.modules.service.PaisService;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ProcesarCarritoActivity extends AppCompatActivity {

    private EditText direccion1;
    private EditText direccion2;
    private TextView direccion3;
    private EditText direccion4;
    private EditText direccion5;
    private EditText direccion6;
    private EditText direccion7;
    private EditText direccion8;
    private Button btnAgregar;
    private Button selectLocation;
    //private View viewGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procesar_carrito);
        direccion1 = (EditText) findViewById(R.id.direccionTxt1);
        direccion2 = (EditText) findViewById(R.id.direccionTxt2);
        direccion3 = (TextView) findViewById(R.id.direccionTxt3);
        direccion4 = (EditText) findViewById(R.id.direccionTxt4);
        direccion5 = (EditText) findViewById(R.id.direccionTxt5);
        direccion6 = (EditText) findViewById(R.id.direccionTxt6);
        direccion7 = (EditText) findViewById(R.id.direccionTxt7);
        direccion8 = (EditText) findViewById(R.id.direccionTxt8);
        btnAgregar = (Button) findViewById(R.id.direccionBtnAdd);
        selectLocation = (Button) findViewById(R.id.proceCarBtnSelecLc);
        listeneragregar();
        listenerSeleccionar();
        datosLast();
        onBack();
        cargarMunicipios();
    }

    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                GlobalCarrito.toShopinCart = true;
                Intent i = new Intent(ProcesarCarritoActivity.this, MenuBotton.class);
                startActivity(i);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }


    private void datosLast() {
        String d1 = GlobalCarrito.direccion1;
        String d2 = GlobalCarrito.direccion2;
        String d4 = GlobalCarrito.direccion4;
        String d5 = GlobalCarrito.direccion5;
        String d6 = GlobalCarrito.direccion6;
        String d7 = GlobalCarrito.direccion7;
        if (d1 != null) {
            direccion1.setText(d1);
        }
        if (d2 != null) {
            direccion2.setText(d2);
        }
        if (d4 != null) {
            direccion4.setText(d4);
        }
        if (d5 != null) {
            direccion5.setText(d5);
        }
        if (d6 != null) {
            direccion6.setText(d6);
        }
        if (d7 != null) {
            direccion7.setText(d7);
        }
        if (GlobalCarrito.direccion8 != null) {
            direccion8.setText(GlobalCarrito.direccion8);
        }
    }

    private void listenerSeleccionar() {
        selectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(direccion1.getText().toString())) {
                    GlobalCarrito.direccion1 = direccion1.getText().toString();
                }
                if (!"".equals(direccion2.getText().toString())) {
                    GlobalCarrito.direccion2 = direccion2.getText().toString();
                }
                if (!"".equals(direccion4.getText().toString())) {
                    GlobalCarrito.direccion4 = direccion4.getText().toString();
                }
                if (!"".equals(direccion5.getText().toString())) {
                    GlobalCarrito.direccion5 = direccion5.getText().toString();
                }
                if (!"".equals(direccion6.getText().toString())) {
                    GlobalCarrito.direccion6 = direccion6.getText().toString();
                }
                if (!"".equals(direccion7.getText().toString())) {
                    GlobalCarrito.direccion7 = direccion7.getText().toString();
                }
                //showFragment(new HomeFragment());
                Intent intent = new Intent(ProcesarCarritoActivity.this, Location.class);
                startActivity(intent);
            }
        });
    }

    /*private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }*/

    private void listeneragregar() {
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confimar();
            }
        });
    }

    private void confimar() {
        if (!validar()) {
            return;
        }
        StringBuilder stb = new StringBuilder();
        stb.append(direccion1.getText().toString());
        stb.append(" ; ");
        stb.append(direccion2.getText().toString());
        stb.append(" ; ");
        LatLng ln = GlobalCarrito.latLngSeleccionada;
        stb.append(ln.latitude);
        stb.append("::");
        stb.append(ln.longitude);
        stb.append(" ; ");
        stb.append(direccion4.getText().toString());
        stb.append(" ; ");
        stb.append(direccion5.getText().toString());
        stb.append(" ; ");
        stb.append(direccion6.getText().toString());
        stb.append(" ; ");
        stb.append(direccion7.getText().toString());
        String dir = stb.toString();
        Pedido ped = Logued.pedidoActual;
        if (ped != null) {
            ped.setDireccion(dir);
        }
        Logued.pedidoActual = ped;
        GlobalCarrito.toSales = true;
        Intent i = new Intent(ProcesarCarritoActivity.this, MenuBotton.class);
        startActivity(i);
    }

    private void cargarMunicipios() {
        MunicipiosAsync municipiosAsync = new MunicipiosAsync();
        municipiosAsync.execute();
    }

    private boolean validar() {
        boolean b = false;
        if ("".equals(direccion1.getText().toString())) {
            Toast.makeText(ProcesarCarritoActivity.this, "Ingrese una Direccion", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(direccion2.getText().toString())) {
            Toast.makeText(ProcesarCarritoActivity.this, "Ingrese una Colonia", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(direccion3.getText().toString())) {
            Toast.makeText(ProcesarCarritoActivity.this, "Ingrese una Referencia", Toast.LENGTH_SHORT).show();
            return false;
        }
        LatLng ln = GlobalCarrito.latLngSeleccionada;
        if (ln == null) {
            Toast.makeText(ProcesarCarritoActivity.this, "Selecciona una Ubicaion", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(direccion4.getText().toString())) {
            Toast.makeText(ProcesarCarritoActivity.this, "Ingrese Numero de Casa", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(direccion5.getText().toString())) {
            Toast.makeText(ProcesarCarritoActivity.this, "Ingrese Numero Referencia", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(direccion6.getText().toString())) {
            Toast.makeText(ProcesarCarritoActivity.this, "Ingrese un Pais", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(direccion7.getText().toString())) {
            Toast.makeText(ProcesarCarritoActivity.this, "Ingrese un Departamento", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (GlobalCarrito.municipioSelected == null) {
            Toast.makeText(ProcesarCarritoActivity.this, "Seleccione un Municipio", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private class MunicipiosAsync extends AsyncTask<String, String, List<Municipio>> {

        @Override
        protected List<Municipio> doInBackground(String... strings) {
            List<Municipio> list = new ArrayList<>();
            System.out.println("asynck municipios !!!!!!!!!!!!!!!!");
            try {
                MunicipioService municipioService = new MunicipioService();
                DepartamentoService departamentoService = new DepartamentoService();
                PaisService paisService = new PaisService();
                if (GlobalCarrito.direccion6 != null) {
                    List<Pais> paises = paisService.obtenerPaises();
                    if (!paises.isEmpty()) {
                        Long idPais = null;
                        if (!"".equals(GlobalCarrito.direccion6)) {
                            for (Pais pais : paises) {
                                if (pais.getNombrePais() != null) {
                                    if (GlobalCarrito.direccion6.toUpperCase().contains(pais.getNombrePais().toUpperCase())) {
                                        idPais = pais.getPaisId();
                                    }
                                }
                            }
                        }
                        if (idPais != null) {
                            List<Departamento> departamentos = departamentoService.obtenerDepartamentosPorIdPais(idPais);
                            if (!departamentos.isEmpty()) {
                                Long idDepartamento = null;
                                if (GlobalCarrito.direccion7 != null && !"".equals(GlobalCarrito.direccion7)) {
                                    for (Departamento departamento : departamentos) {
                                        if (departamento.getNombreDepartamento() != null) {
                                            if (departamento.getNombreDepartamento().toUpperCase().contains(GlobalCarrito.direccion7.toUpperCase())) {
                                                idDepartamento = departamento.getDepartamentoId();
                                            }
                                        }
                                    }
                                }
                                if (idDepartamento != null) {
                                    list = municipioService.obtenerMunicipiosPorIdDepartamento(idDepartamento);
                                }
                            }
                        }
                    }
                }

                if (!list.isEmpty()) {
                    GlobalCarrito.municipioList = list;
                } else {
                    list = municipioService.obtenerMunicipios();
                    GlobalCarrito.municipioList = list;
                }
            } catch (Exception ignore) {
            }
            System.out.println("asynck municipios FN !!!!!!!!!!!!!!!!");
            return list;
        }

        @Override
        protected void onPostExecute(final List<Municipio> municipios) {
            super.onPostExecute(municipios);
            if (!municipios.isEmpty()) {
                direccion8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder aBuilder = new AlertDialog.Builder(ProcesarCarritoActivity.this);
                        aBuilder.setTitle("Seleccionar Municipio");
                        MunicipioItemViewAdapter adapter = new MunicipioItemViewAdapter(municipios, ProcesarCarritoActivity.this, R.layout.holder_item_municipios);
                        aBuilder.setSingleChoiceItems(adapter, -1, null);
                        /*System.out.println("click opcion municipio !!!!!!!!!!!!!!!!");
                        System.out.println("click opcion numero: " + position);*/
                        /*System.out.println("click opcion municipio !!!!!!!!!!!!!!!!");
                        direccion8.setText(municipios.get(which).getNombreMunicipio());
                        GlobalCarrito.municipioSelected = municipios.get(which);*/
                        aBuilder.setNeutralButton("Seleccionar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (GlobalCarrito.direccion8 != null) {
                                    direccion8.setText(GlobalCarrito.direccion8);
                                }
                                dialog.dismiss();
                            }
                        });
                        AlertDialog mAlertDialog = aBuilder.create();
                        mAlertDialog.show();
                    }
                });
            } else {
                direccion8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ProcesarCarritoActivity.this, "Ubicacion no disponible", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}