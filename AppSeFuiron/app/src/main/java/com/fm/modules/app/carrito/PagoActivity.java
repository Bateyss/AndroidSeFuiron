package com.fm.modules.app.carrito;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fm.modules.R;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.menu.MenuBotton;
import com.fm.modules.app.usuario.GlobalUsuario;
import com.fm.modules.models.Driver;
import com.fm.modules.models.Municipio;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.service.DriverService;
import com.fm.modules.service.MunicipioService;
import com.fm.modules.service.OpcionSubMenuSeleccionadoService;
import com.fm.modules.service.PedidoService;
import com.fm.modules.service.PlatilloSeleccionadoService;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PagoActivity extends Fragment {

    private TextView txtTotal;
    private TextView pagoTotal;
    private TextView pagoRecargo;
    private TextView txtDireccion1;
    private TextView txtDireccion2;
    private MaterialCardView btnTarjeta;
    private MaterialCardView btnEfectivo;
    private Button btnPagar;
    private Pedido pedido;
    private List<PlatilloSeleccionado> platilloSeleccionados;
    private List<OpcionesDeSubMenuSeleccionado> opcionesSeleccionadas;
    private View viewGlobal;
    private MaterialTextView changueLocation;
    private ImageView checkTarjeta;
    private ImageView checkEfectivo;
    private AppCompatImageView back;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pago, container, false);
        viewGlobal = view;
        txtTotal = (TextView) view.findViewById(R.id.pagoTxtTotal);
        pagoTotal = (TextView) view.findViewById(R.id.pagoTotal);
        pagoRecargo = (TextView) view.findViewById(R.id.pagoRecargo);
        txtDireccion1 = (TextView) view.findViewById(R.id.pagoTxtDireccion);
        txtDireccion2 = (TextView) view.findViewById(R.id.pagoTxtDireccion2);
        btnTarjeta = (MaterialCardView) view.findViewById(R.id.pagoBtnTarjeta);
        btnEfectivo = (MaterialCardView) view.findViewById(R.id.pagoBtnEfectivo);
        btnPagar = (Button) view.findViewById(R.id.pagoBtnPagar);
        changueLocation = (MaterialTextView) view.findViewById(R.id.pagoChangeLocation);
        checkTarjeta = (ImageView) view.findViewById(R.id.pagoBtnTarjetaCheck);
        checkEfectivo = (ImageView) view.findViewById(R.id.pagoBtnEfectivoCheck);
        back = (AppCompatImageView) view.findViewById(R.id.ivBack);
        progressBar = view.findViewById(R.id.pagoProgress);
        pedido = Logued.pedidoActual;
        btnPagar.setEnabled(false);
        btnPagar.setBackgroundColor(getResources().getColor(R.color.lightGray));
        platilloSeleccionados = new ArrayList<>();
        opcionesSeleccionadas = new ArrayList<>();
        mostrarDatos();
        listenerBotones();
        changueLocationlistener();
        backListener();
        onBack();
        return view;
    }

    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new CarritoActivity());
            }
        });
    }

    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                showFragment(new CarritoActivity());
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }

    private void changueLocationlistener() {
        changueLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewGlobal.getContext(), UbicacionesActivity.class);
                startActivity(intent);
            }
        });
    }

    public void obtenerPedido() {
        if (pedido != null) {
            platilloSeleccionados = Logued.platillosSeleccionadosActuales;
            opcionesSeleccionadas = Logued.opcionesDeSubMenusEnPlatillosSeleccionados;
        }
    }

    private void listenerBotones() {
        btnTarjeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pedido != null) {
                    pedido.setFormaDePago("Tarjeta");
                    Logued.pedidoActual = pedido;
                    btnPagar.setBackgroundColor(getResources().getColor(R.color.orange));
                    showFragment(new PagoTargetaFragment());
                }
                Logued.pedidoActual = pedido;
                checkTarjeta.setImageResource(R.drawable.ic_checked_round_lime);
                checkEfectivo.setImageResource(R.drawable.ic_checked_round_white);
            }
        });
        btnEfectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pedido != null) {
                    pedido.setFormaDePago("Efectivo");
                    btnPagar.setEnabled(true);
                    btnPagar.setBackgroundColor(getResources().getColor(R.color.orange));
                }
                Logued.pedidoActual = pedido;
                checkTarjeta.setImageResource(R.drawable.ic_checked_round_white);
                checkEfectivo.setImageResource(R.drawable.ic_checked_round_lime);
            }
        });
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPagar.setEnabled(false);
                btnTarjeta.setEnabled(false);
                btnEfectivo.setEnabled(false);
                obtenerPedido();
                GuardarPedidoAsync guardarPedidoAsync = new GuardarPedidoAsync();
                progressBar.setVisibility(View.VISIBLE);
                guardarPedidoAsync.execute();
            }
        });
    }

    private void mostrarDatos() {
        try {
            List<PlatilloSeleccionado> lista = Logued.platillosSeleccionadosActuales;
            double total1 = 0.00;
            double total2 = 0;
            double total3 = 0;
            double descuento = 0;
            double total4 = 0;
            String direccion = "";
            if (lista != null) {
                if (!lista.isEmpty()) {
                    for (PlatilloSeleccionado pl : lista) {
                        total1 = total1 + pl.getPrecio();
                    }
                    direccion = lista.get(0).getPedido().getDireccion();
                }
            }
            String[] strings = direccion.split(" ; ", 7);
            if (strings.length > 3) {
                txtDireccion1.setText(strings[0]);
                String dir2 = strings[1] + " , " + strings[3];
                txtDireccion2.setText(dir2);
            }
            //total2 = total1 * 0.13;
            if (GlobalCarrito.municipioSelected != null && GlobalCarrito.municipioSelected.getTarifa() != null) {
                total3 = GlobalCarrito.municipioSelected.getTarifa().doubleValue();
            } else {
                CargarMunicipio cargarMunicipio = new CargarMunicipio();
                cargarMunicipio.execute();
            }
            if (GlobalUsuario.descuento != null) {
                descuento = GlobalUsuario.descuento;
            }
            // descuento = descuento * total1;
            total4 = total1 + total2 + total3 - descuento;
            pedido.setTotalDePedido(total1);
            pedido.setTotalEnRestautante(total4);
            pedido.setTotalDeCargosExtra(total3);
            pedido.setTotalEnRestautanteSinComision(total1 + total2 - descuento);
            DecimalFormat decimalFormat = new DecimalFormat("$ #,##0.00");
            String cargo = decimalFormat.format(total3);
            pagoRecargo.setText(cargo);
            txtTotal.setText(decimalFormat.format(total1));
            pagoTotal.setText(decimalFormat.format(total4));
        } catch (Exception e) {
            System.out.println("error carrito: " + e);
        }
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

    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public class GuardarPedidoAsync extends AsyncTask<String, String, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            int b = 0;
            try {
                if (pedido != null) {
                    if (!platilloSeleccionados.isEmpty()) {
                        PedidoService pedidoService = new PedidoService();
                        pedido.setPedidoId(0L);
                        pedido.setFechaOrdenado(new Date());
                        DriverService driverService = new DriverService();
                        List<Driver> drivers = driverService.obtenerDrivers();
                        if (!drivers.isEmpty()) {
                            pedido.setDrivers(drivers.get(0));
                            pedido.setStatus(0);
                        }
                        if (isNetActive()) {
                            Pedido per = pedidoService.crearPedido(pedido);
                            if (per != null) {
                                GlobalUsuario.descuento = null;
                                b = 1;
                                GlobalCarrito.pedidoRegistrado = per;
                            } else {
                                b = 2;
                            }
                            if (b == 1) {
                                PlatilloSeleccionadoService platilloSeleccionadoService = new PlatilloSeleccionadoService();
                                for (PlatilloSeleccionado pla : platilloSeleccionados) {
                                    pla.setPedido(per);
                                    pla.setPlatilloSeleccionadoId(0L);
                                    PlatilloSeleccionado pls = platilloSeleccionadoService.crearPlatilloSeleccionado(pla);
                                    if (pls != null) {
                                        b = 3;
                                        if (!opcionesSeleccionadas.isEmpty()) {
                                            OpcionSubMenuSeleccionadoService opcionSubMenuSeleccionadoService = new OpcionSubMenuSeleccionadoService();
                                            for (OpcionesDeSubMenuSeleccionado opc : opcionesSeleccionadas) {
                                                if (opc.getPlatilloSeleccionado().getPlatilloSeleccionadoId().intValue() == pla.getPlatilloSeleccionadoId().intValue()) {
                                                    opc.setPlatilloSeleccionado(pls);
                                                    opc.setOpcionesDeSubMenuSeleccionadoId(0L);
                                                    OpcionesDeSubMenuSeleccionado pp = opcionSubMenuSeleccionadoService.crearOpcionSubMenu(opc);
                                                    if (pp != null) {
                                                        b = 3;
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        b = 2;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("error asynk guardar pedido: " + e);
            }
            return b;
        }

        @Override
        protected void onPostExecute(Integer b) {
            super.onPostExecute(b);
            int procesed = b;
            switch (procesed) {
                case 0:
                    AlertDialog dialog = new AlertDialog.Builder(viewGlobal.getContext())
                            .setView(R.layout.dialog_server_err)
                            .setCancelable(true)
                            .setPositiveButton("Continuar", null)
                            .show();
                    break;
                case 2:
                    Intent intent = new Intent(viewGlobal.getContext(), PedidoNoRegistrado.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                case 3:
                    Intent intent2 = new Intent(viewGlobal.getContext(), PedidoRegistrado.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent2);
                    break;
            }
            btnPagar.setEnabled(false);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public class CargarMunicipio extends AsyncTask<String, String, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            int b = 0;
            MunicipioService municipioService = new MunicipioService();
            Municipio municipio = null;
            if (Logued.pedidoActual != null) {
                if (Logued.pedidoActual.getDireccion() != null) {
                    String[] spliter = {};
                    try {
                        spliter = pedido.getDireccion().split(";", 8);
                    } catch (Exception ignore) {
                    }
                    Long idMunicipio = null;
                    if (spliter.length > 7) {
                        idMunicipio = (long) Integer.parseInt(spliter[7]);
                    }
                    if (idMunicipio != null) {
                        municipio = municipioService.obtenerMunicipiosPorId(idMunicipio);
                    }
                }
            } else {
                List<Municipio> municipios = municipioService.obtenerMunicipios();
                if (!municipios.isEmpty()) {
                    municipio = municipios.get(0);
                }
            }
            if (municipio == null) {
                municipio = new Municipio();
                municipio.setTarifa(new BigDecimal(1.50));
            }
            GlobalCarrito.municipioSelected = municipio;
            return b;
        }

        @Override
        protected void onPostExecute(Integer b) {
            super.onPostExecute(b);
            GlobalCarrito.toSales = true;
            Intent i = new Intent(viewGlobal.getContext(), MenuBotton.class);
            startActivity(i);
        }
    }
}