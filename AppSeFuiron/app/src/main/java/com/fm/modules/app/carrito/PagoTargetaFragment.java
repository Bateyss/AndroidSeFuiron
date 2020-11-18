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
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.fm.modules.sqlite.models.RecyclerTarjetasAdapter;
import com.fm.modules.sqlite.models.TarjetaMsg;
import com.fm.modules.sqlite.models.TarjetasSaved;
import com.fm.modules.sqlite.models.TarjetasService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PagoTargetaFragment extends Fragment {

    private View viewGlobal;
    private RecyclerView targetaRecycler;
    private ImageView targetaAgregar;
    private Button tarjetaUsar;
    private ImageView leftArrow;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pago_targeta, container, false);
        try {
            viewGlobal = view;
            targetaRecycler = (RecyclerView) view.findViewById(R.id.tarjetaRecycler);
            targetaAgregar = (ImageView) view.findViewById(R.id.tarjetaAgregar);
            tarjetaUsar = (Button) view.findViewById(R.id.tarjetaUsar);
            leftArrow = (ImageView) view.findViewById(R.id.leftArrowChoicer);
            progressBar = view.findViewById(R.id.pagoProgress);
            agregarListener();
            onBack();
            leftArrowListener();
            cargarTargetas();
            pagarListener();
        } catch (Exception e) {
            System.out.println("error onCreateView" + getClass().getCanonicalName() + e);
        }
        return view;
    }

    private void pagarListener() {
        tarjetaUsar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Logued.pedidoActual != null) {
                    v.setEnabled(false);
                    if (Logued.idTarjetaSeleccionada != null && Logued.idTarjetaSeleccionada.intValue() > 0) {
                        PagarAsynck pagarAsynck = new PagarAsynck();
                        progressBar.setVisibility(View.VISIBLE);
                        pagarAsynck.execute();
                    }
                }
            }
        });
    }

    private void leftArrowListener() {
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new PagoActivity());
            }
        });
    }

    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                showFragment(new PagoActivity());
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }

    private void agregarListener() {
        targetaAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new PagarTargetaNuevaFragment());
            }
        });
    }

    private void cargarTargetas() {
        TarjetasAsync tarjetasAsync = new TarjetasAsync();
        tarjetasAsync.execute();
    }

    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public class TarjetasAsync extends AsyncTask<String, String, List<TarjetasSaved>> {

        @Override
        protected List<TarjetasSaved> doInBackground(String... strings) {
            List<TarjetasSaved> lista = new ArrayList<>();
            try {
                TarjetasService tarjetasService = new TarjetasService();
                if (Logued.usuarioLogued != null && Logued.usuarioLogued.getUsuarioId() != null) {
                    lista = tarjetasService.obtenerTarjetasDeUsuario(Logued.usuarioLogued.getUsuarioId());
                }
            } catch (Exception e) {
            }
            return lista;
        }

        @Override
        protected void onPostExecute(List<TarjetasSaved> tarjetas) {
            super.onPostExecute(tarjetas);
            if (tarjetas != null && !tarjetas.isEmpty()) {
                RecyclerTarjetasAdapter recyclerTarjetasAdapter = new RecyclerTarjetasAdapter(tarjetas, viewGlobal.getContext(), getActivity());
                targetaRecycler.setLayoutManager(new LinearLayoutManager(viewGlobal.getContext(), LinearLayoutManager.VERTICAL, false));
                targetaRecycler.setAdapter(recyclerTarjetasAdapter);
            }
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

    private class PagarAsynck extends AsyncTask<String, String, TarjetaMsg> {

        @Override
        protected TarjetaMsg doInBackground(String... strings) {
            TarjetaMsg tarjetaMsg = null;
            if (isNetActive()) {
                try {
                    TarjetasService tarjetasService = new TarjetasService();
                    ////
                    tarjetaMsg = tarjetasService.realizarPagoPorIdTarjeta(Logued.idTarjetaSeleccionada, Logued.pedidoActual.getTotalEnRestautante());
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
                                    System.out.println("**********************************************");
                                    System.out.println("pedido registrado :D");
                                    System.out.println("**********************************************");
                                    GlobalUsuario.descuento = null;
                                    b = 1;
                                    GlobalCarrito.pedidoRegistrado = per;
                                } else {
                                    System.out.println("**********************************************");
                                    System.out.println("pedido no registrado D:");
                                    System.out.println("**********************************************");
                                    b = 2;
                                }
                                if (b == 1) {
                                    PlatilloSeleccionadoService platilloSeleccionadoService = new PlatilloSeleccionadoService();
                                    for (PlatilloSeleccionado pla : Logued.platillosSeleccionadosActuales) {
                                        pla.setPedido(per);
                                        pla.setPlatilloSeleccionadoId(0L);
                                        PlatilloSeleccionado pls = platilloSeleccionadoService.crearPlatilloSeleccionado(pla);
                                        if (pls != null) {
                                            System.out.println("**********************************************");
                                            System.out.println("platillo registrado :)");
                                            System.out.println("**********************************************");
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
                                        } else {
                                            System.out.println("**********************************************");
                                            System.out.println("platillo no registrado :(");
                                            System.out.println("**********************************************");
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
            //guardarButton.setEnabled(true);
            tarjetaUsar.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}