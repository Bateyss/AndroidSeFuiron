package com.fm.modules.app.carrito;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.fm.modules.R;
import com.fm.modules.adapters.DepartamentoItemViewAdapter;
import com.fm.modules.adapters.MunicipioItemViewAdapter;
import com.fm.modules.adapters.PaisItemViewAdapter;
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

public class NuevaUbicacionActivity extends AppCompatActivity {

    private EditText direccion1;
    private EditText direccion2;
    private EditText direccion4;
    private EditText direccion5;
    private Spinner direccion6;
    private Spinner direccion7;
    private Spinner direccion8;
    private Button btnAgregar;
    private Button selectLocation;
    //private View viewGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procesar_carrito);
        direccion1 = (EditText) findViewById(R.id.direccionTxt1);
        direccion2 = (EditText) findViewById(R.id.direccionTxt2);
        direccion4 = (EditText) findViewById(R.id.direccionTxt4);
        direccion5 = (EditText) findViewById(R.id.direccionTxt5);
        direccion6 = (Spinner) findViewById(R.id.direccionSpn6);
        direccion7 = (Spinner) findViewById(R.id.direccionSpn7);
        direccion8 = (Spinner) findViewById(R.id.direccionSpn8);
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
                Intent i = new Intent(NuevaUbicacionActivity.this, Location.class);
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
        LatLng ln = GlobalCarrito.latLngSeleccionada;
        if (ln != null) {
            String st = "Ubicacion Lista";
            selectLocation.setText(st);
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
                Intent intent = new Intent(NuevaUbicacionActivity.this, Location.class);
                startActivity(intent);
            }
        });
    }

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
        stb.append(GlobalCarrito.paisSelected.getNombrePais());
        stb.append(" ; ");
        stb.append(GlobalCarrito.departamentoSelected.getNombreDepartamento());
        stb.append(" ;");
        stb.append(GlobalCarrito.municipioSelected.getMunicipioId().intValue());
        String dir = stb.toString();
        Pedido ped = Logued.pedidoActual;
        if (ped != null) {
            ped.setDireccion(dir);
        }
        Logued.pedidoActual = ped;
        GlobalCarrito.toSales = true;
        Intent i = new Intent(NuevaUbicacionActivity.this, MenuBotton.class);
        startActivity(i);
    }

    private void cargarMunicipios() {
        MunicipiosAsync municipiosAsync = new MunicipiosAsync();
        municipiosAsync.execute();
    }

    private boolean validar() {
        boolean b = false;
        if ("".equals(direccion1.getText().toString())) {
            Toast.makeText(NuevaUbicacionActivity.this, "Ingrese una Direccion", Toast.LENGTH_SHORT).show();
            direccion1.setHintTextColor(Color.RED);
            return false;
        }
        if ("".equals(direccion2.getText().toString())) {
            Toast.makeText(NuevaUbicacionActivity.this, "Ingrese una Colonia", Toast.LENGTH_SHORT).show();
            direccion2.setHintTextColor(Color.RED);
            return false;
        }
        LatLng ln = GlobalCarrito.latLngSeleccionada;
        if (ln == null) {
            Toast.makeText(NuevaUbicacionActivity.this, "Selecciona una Ubicaion", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(direccion4.getText().toString())) {
            Toast.makeText(NuevaUbicacionActivity.this, "Ingrese Numero de Casa", Toast.LENGTH_SHORT).show();
            direccion4.setHintTextColor(Color.RED);
            return false;
        }
        if ("".equals(direccion5.getText().toString())) {
            Toast.makeText(NuevaUbicacionActivity.this, "Ingrese Numero Referencia", Toast.LENGTH_SHORT).show();
            direccion5.setHintTextColor(Color.RED);
            return false;
        }
        if (GlobalCarrito.paisSelected == null) {
            Toast.makeText(NuevaUbicacionActivity.this, "Selecciona un Pais", Toast.LENGTH_SHORT).show();
            direccion6.setBackgroundColor(Color.RED);
            return false;
        }
        if (GlobalCarrito.departamentoSelected == null) {
            Toast.makeText(NuevaUbicacionActivity.this, "Selecciona un Departamento", Toast.LENGTH_SHORT).show();
            direccion7.setBackgroundColor(Color.RED);
            return false;
        }
        if (GlobalCarrito.municipioSelected == null) {
            Toast.makeText(NuevaUbicacionActivity.this, "Seleccione un Municipio", Toast.LENGTH_SHORT).show();
            direccion8.setBackgroundColor(Color.RED);
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
                if (GlobalCarrito.municipioList == null || GlobalCarrito.municipioList.isEmpty()) {
                    GlobalCarrito.municipioList = municipioService.obtenerMunicipios();
                }
                if (GlobalCarrito.departamentoList == null || GlobalCarrito.departamentoList.isEmpty()) {
                    GlobalCarrito.departamentoList = departamentoService.obtenerDepartamentos();
                }
                if (GlobalCarrito.paisList == null || GlobalCarrito.paisList.isEmpty()) {
                    GlobalCarrito.paisList = paisService.obtenerPaises();
                }
                if (GlobalCarrito.direccion6 != null) {
                    if (!GlobalCarrito.paisList.isEmpty()) {
                        Long idPais = null;
                        if (!"".equals(GlobalCarrito.direccion6)) {
                            for (Pais pais : GlobalCarrito.paisList) {
                                if (pais.getNombrePais() != null) {
                                    if (GlobalCarrito.direccion6.toUpperCase().contains(pais.getNombrePais().toUpperCase())) {
                                        idPais = pais.getPaisId();
                                        GlobalCarrito.paisSelected = pais;
                                        GlobalCarrito.direccion10 = pais.getNombrePais();
                                    }
                                }
                            }
                        }
                        if (idPais != null) {
                            if (!GlobalCarrito.departamentoList.isEmpty()) {
                                Long idDepartamento = null;
                                if (GlobalCarrito.direccion7 != null && !"".equals(GlobalCarrito.direccion7)) {
                                    for (Departamento departamento : GlobalCarrito.departamentoList) {
                                        if (departamento.getNombreDepartamento() != null) {
                                            if (GlobalCarrito.direccion7.toUpperCase().contains(departamento.getNombreDepartamento().toUpperCase())) {
                                                idDepartamento = departamento.getDepartamentoId();
                                                GlobalCarrito.departamentoSelected = departamento;
                                                GlobalCarrito.direccion9 = departamento.getNombreDepartamento();
                                            }
                                        }
                                    }
                                }
                                if (idDepartamento != null) {
                                    if (!GlobalCarrito.municipioList.isEmpty()) {
                                        for (Municipio municipio : GlobalCarrito.municipioList) {
                                            if (municipio.getDepartamento() != null && municipio.getDepartamento().getDepartamentoId() != null && municipio.getDepartamento().getDepartamentoId().intValue() == idDepartamento.intValue()) {
                                                list.add(municipio);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ignore) {
            }
            System.out.println("asynck municipios FN !!!!!!!!!!!!!!!!");
            return list;
        }

        @Override
        protected void onPostExecute(final List<Municipio> municipios) {
            super.onPostExecute(municipios);

            if (!GlobalCarrito.paisList.isEmpty()) {
                PaisItemViewAdapter paisItemViewAdapter = new PaisItemViewAdapter(GlobalCarrito.paisList, NuevaUbicacionActivity.this, R.layout.holder_item_municipios, direccion6, direccion7, direccion8);
                direccion6.setAdapter(paisItemViewAdapter);
                if (GlobalCarrito.paisSelected != null) {
                    int x = 0;
                    for (Pais pais : GlobalCarrito.paisList) {
                        if (pais.getPaisId().intValue() == GlobalCarrito.paisSelected.getPaisId().intValue()) {
                            direccion6.setSelection(x);
                        }
                        x++;
                    }
                }
                if (!GlobalCarrito.departamentoList.isEmpty()) {
                    List<Departamento> departamentos = new ArrayList<>();
                    if (GlobalCarrito.paisSelected != null && GlobalCarrito.paisSelected.getPaisId() != null) {
                        for (Departamento departamento : GlobalCarrito.departamentoList) {
                            if (departamento.getPais() != null && departamento.getPais().getPaisId() != null) {
                                if (departamento.getPais().getPaisId() == GlobalCarrito.paisSelected.getPaisId().intValue()) {
                                    departamentos.add(departamento);
                                }
                            }
                        }
                    } else {
                        departamentos = GlobalCarrito.departamentoList;
                    }
                    DepartamentoItemViewAdapter departamentoItemViewAdapter = new DepartamentoItemViewAdapter(departamentos, NuevaUbicacionActivity.this, R.layout.holder_item_municipios, direccion7, direccion8);
                    direccion7.setAdapter(departamentoItemViewAdapter);
                    if (GlobalCarrito.departamentoSelected != null) {
                        int x = 0;
                        for (Departamento departamento : departamentos) {
                            if (departamento.getDepartamentoId().intValue() == GlobalCarrito.departamentoSelected.getDepartamentoId().intValue()) {
                                direccion7.setSelection(x);
                            }
                            x++;
                        }
                    }
                    if (!municipios.isEmpty()) {
                        MunicipioItemViewAdapter adapter = new MunicipioItemViewAdapter(municipios, NuevaUbicacionActivity.this, R.layout.holder_item_municipios, direccion8);
                        direccion8.setAdapter(adapter);
                    } else {
                        Toast.makeText(NuevaUbicacionActivity.this, "Ubicacion no disponible", Toast.LENGTH_SHORT).show();
                        GlobalCarrito.municipioSelected = null;
                    }
                }
            }
             /*
            if (!municipios.isEmpty()) {
                MunicipioItemViewAdapter adapter = new MunicipioItemViewAdapter(municipios, ProcesarCarritoActivity.this, R.layout.holder_item_municipios);
                direccion8.setAdapter(adapter);

                direccion8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MunicipioItemViewAdapter adapter = new MunicipioItemViewAdapter(municipios, ProcesarCarritoActivity.this, R.layout.holder_item_municipios);
                        final AlertDialog.Builder aBuilder = new AlertDialog.Builder(ProcesarCarritoActivity.this);
                        aBuilder.setTitle("Seleccionar Municipio");
                        aBuilder.setSingleChoiceItems(adapter, -1, null);
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
                        GlobalCarrito.municipioSelected = null;
                    }
                });
            }
                 */
        }
    }
}