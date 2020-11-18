package com.fm.modules.app.carrito;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fm.modules.R;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.usuario.GlobalUsuario;
import com.fm.modules.models.Driver;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.service.DriverService;
import com.fm.modules.service.OpcionSubMenuSeleccionadoService;
import com.fm.modules.service.PedidoService;
import com.fm.modules.service.PlatilloSeleccionadoService;
import com.fm.modules.sqlite.models.TarjetaMsg;
import com.fm.modules.sqlite.models.Tarjetas;
import com.fm.modules.sqlite.models.TarjetasService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PagarTargetaNuevaFragment extends Fragment {

    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerDialogListener;
    private View viewGlobal;
    private EditText nombre;
    private EditText numero;
    private EditText codigo;
    private EditText fecha;
    private Button guardarButton;
    private Date fecha1 = null;
    private ImageView leftArrow;
    private Integer mesTarjeta;
    private Integer anioTarjeta;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_targeta, container, false);
        viewGlobal = view;
        nombre = (EditText) view.findViewById(R.id.targetaAddName);
        numero = (EditText) view.findViewById(R.id.targetaAddNumber);
        codigo = (EditText) view.findViewById(R.id.targetaAddCode);
        fecha = (EditText) view.findViewById(R.id.targetaAddDateEx);
        leftArrow = (ImageView) view.findViewById(R.id.leftArrowChoice);
        guardarButton = (Button) view.findViewById(R.id.targetaAddBtn);
        progressBar = view.findViewById(R.id.pagoProgress);
        mesTarjeta = null;
        anioTarjeta = null;
        datePicherLoader();
        listenerPagar();
        leftArrowListener();
        onBack();
        return view;
    }

    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                showFragment(new PagoTargetaFragment());
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }

    private void leftArrowListener() {
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new PagoTargetaFragment());
            }
        });
    }

    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void listenerPagar() {
        String txt = "Pagar";
        guardarButton.setText(txt);
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validar()) {
                    v.setEnabled(false);
                    PagarAsynck pagarAsynck = new PagarAsynck();
                    progressBar.setVisibility(View.VISIBLE);
                    pagarAsynck.execute();
                }
            }
        });
    }

    private boolean validar() {
        if ("".equals(nombre.getText().toString())) {
            Toast.makeText(viewGlobal.getContext(), "Ingrese Nombre", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(numero.getText().toString())) {
            Toast.makeText(viewGlobal.getContext(), "Ingrese Numero", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (numero.getText().toString().length() < 16) {
            numero.setTextColor(Color.RED);
            Toast.makeText(viewGlobal.getContext(), "Ingrese Numero Completo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(codigo.getText().toString())) {
            Toast.makeText(viewGlobal.getContext(), "Ingrese Codigo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (codigo.getText().toString().length() < 3) {
            codigo.setTextColor(Color.RED);
            Toast.makeText(viewGlobal.getContext(), "Ingrese Codigo", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mesTarjeta == null || anioTarjeta == null) {
            Toast.makeText(viewGlobal.getContext(), "Ingrese Fecha", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!compararFechas()) {
            Toast.makeText(viewGlobal.getContext(), "La Tergeta ha expirado", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean compararFechas() {
        boolean b = false;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            if (year <= anioTarjeta) {
                if (year < anioTarjeta || mes < mesTarjeta) {
                    b = true;
                }
            }
        } catch (Exception ignore) {
        }
        return b;
    }

    private void datePicherLoader() {
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // codigo para la fecha
                // datepicker
                //showDatePickDialog(view);
                showDatePickerNumber();
            }
        });
        datePickerDialogListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Calendar cal = Calendar.getInstance();
                cal.set(i, i1, i2);
                SimpleDateFormat sdt = new SimpleDateFormat("EEEE dd MMMM  yyyy");
                fecha.setText(sdt.format(cal.getTime()));
                fecha1 = cal.getTime();
            }
        };
    }

    private void showDatePickerNumber() {
        int x = anioActual();
        anioTarjeta = x;
        mesTarjeta = 1;
        Dialog dialog = new Dialog(viewGlobal.getContext());
        dialog.setTitle("Fecha");
        dialog.setContentView(R.layout.dialog_fecha);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_shape));
        NumberPicker numberPicker1 = dialog.findViewById(R.id.fechaDP1);
        numberPicker1.setMaxValue(12);
        numberPicker1.setMinValue(1);
        numberPicker1.setWrapSelectorWheel(false);
        numberPicker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mesTarjeta = newVal;
                if (anioTarjeta != null) {
                    String newFecha = mesTarjeta + "/" + anioTarjeta;
                    fecha.setText(newFecha);
                }
            }
        });
        NumberPicker numberPicker2 = dialog.findViewById(R.id.fechaDP2);
        numberPicker2.setMaxValue(x + 30);
        numberPicker2.setMinValue(x);
        numberPicker2.setWrapSelectorWheel(false);
        numberPicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                anioTarjeta = newVal;
                if (mesTarjeta != null) {
                    String newFecha = mesTarjeta + "/" + anioTarjeta;
                    fecha.setText(newFecha);
                }
            }
        });
        dialog.show();
    }

    public int anioActual() {
        int x = 0;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            x = calendar.get(Calendar.YEAR);
        } catch (Exception ignore) {
            x = 0;
        }
        return x;
    }

    private void showDatePickDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int mont = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(viewGlobal.getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                datePickerDialogListener,
                year,
                mont,
                day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    public boolean isNetActive() {
        boolean c = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    private class PagarAsynck extends AsyncTask<String, String, TarjetaMsg> {

        @Override
        protected TarjetaMsg doInBackground(String... strings) {
            TarjetaMsg tarjetaMsg = null;
            if (isNetActive()) {
                try {
                    if (Logued.usuarioLogued != null && Logued.usuarioLogued.getUsuarioId() != null) {
                        Tarjetas tarjetas = new Tarjetas();
                        tarjetas.setNumTarjeta(numero.getText().toString());
                        tarjetas.setCodSecu(codigo.getText().toString());
                        String fechaTarjeta = mesTarjeta + "/" + anioTarjeta;
                        tarjetas.setFechaTarjeta(fechaTarjeta);
                        tarjetas.setUsuario(Logued.usuarioLogued.getUsuarioId());
                        TarjetasService tarjetasService = new TarjetasService();
                        tarjetaMsg = tarjetasService.realizarPago(tarjetas);
                    }
                } catch (Exception ignore) {
                }

                if (tarjetaMsg != null && tarjetaMsg.getCodigo() != null && "0".equals(tarjetaMsg.getCodigo()))
                    try {
                        int b = 0;
                        if (Logued.pedidoActual != null) {
                            if (Logued.platillosSeleccionadosActuales != null && !Logued.platillosSeleccionadosActuales.isEmpty()) {
                                PedidoService pedidoService = new PedidoService();
                                Logued.pedidoActual.setPedidoId(0L);
                                Logued.pedidoActual.setFechaOrdenado(new Date());
                                DriverService driverService = new DriverService();
                                List<Driver> drivers = driverService.obtenerDrivers();
                                if (!drivers.isEmpty()) {
                                    Logued.pedidoActual.setDrivers(drivers.get(0));
                                    Logued.pedidoActual.setStatus(0);
                                }
                                Pedido per = pedidoService.crearPedido(Logued.pedidoActual);
                                if (per != null) {
                                    GlobalUsuario.descuento = null;
                                    b = 1;
                                    GlobalCarrito.pedidoRegistrado = per;
                                } else {
                                    b = 2;
                                }
                                if (b == 1) {
                                    PlatilloSeleccionadoService platilloSeleccionadoService = new PlatilloSeleccionadoService();
                                    for (PlatilloSeleccionado pla : Logued.platillosSeleccionadosActuales) {
                                        pla.setPedido(per);
                                        pla.setPlatilloSeleccionadoId(0L);
                                        PlatilloSeleccionado pls = platilloSeleccionadoService.crearPlatilloSeleccionado(pla);
                                        if (pls != null) {
                                            if (Logued.opcionesDeSubMenusEnPlatillosSeleccionados != null && !Logued.opcionesDeSubMenusEnPlatillosSeleccionados.isEmpty()) {
                                                OpcionSubMenuSeleccionadoService opcionSubMenuSeleccionadoService = new OpcionSubMenuSeleccionadoService();
                                                for (OpcionesDeSubMenuSeleccionado opc : Logued.opcionesDeSubMenusEnPlatillosSeleccionados) {
                                                    if (opc.getPlatilloSeleccionado().getPlatilloSeleccionadoId().intValue() == pla.getPlatilloSeleccionadoId().intValue()) {
                                                        opc.setPlatilloSeleccionado(pls);
                                                        opc.setOpcionesDeSubMenuSeleccionadoId(0L);
                                                        OpcionesDeSubMenuSeleccionado pp = opcionSubMenuSeleccionadoService.crearOpcionSubMenu(opc);
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
            }

            return tarjetaMsg;
        }

        @Override
        protected void onPostExecute(TarjetaMsg tarjeta) {
            super.onPostExecute(tarjeta);
            if (tarjeta != null) {
                if ("0".equals(tarjeta.getCodigo())) {
                    Intent intent = new Intent(viewGlobal.getContext(), PedidoRegistrado.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(viewGlobal.getContext(), "Denegado", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(viewGlobal.getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
            guardarButton.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}