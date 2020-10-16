package com.fm.modules.app.carrito;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.RecyclerPlatillosSeleccionadosAdapter;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.restaurantes.MenuDeRestauranteFragment;
import com.fm.modules.app.restaurantes.RestaurantePorCategoria;
import com.fm.modules.app.usuario.GlobalUsuario;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.models.Promociones;
import com.fm.modules.service.PromocionesService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CarritoActivity extends Fragment {

    private RecyclerView carritoRecicler;
    private TextView totalOrdenTxtVw;
    private TextView impuestoTxtVw;
    private TextView cargoFuimonosTxtVw;
    private TextView promocionTxtVw;
    private TextView totalaCancelarTxtVw;
    private Button btnCodigo;
    private Button btnMas;
    private Button btnTerminate;
    private View viewGlobal;
    private AppCompatImageView back;
    private EditText txtCodigo;
    private String codigoAProbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_carrito_actual, container, false);
        viewGlobal = view;
        carritoRecicler = (RecyclerView) view.findViewById(R.id.rvcarrito_compras);
        totalOrdenTxtVw = (TextView) view.findViewById(R.id.carritoTotal1);
        impuestoTxtVw = (TextView) view.findViewById(R.id.carritoTotal2);
        cargoFuimonosTxtVw = (TextView) view.findViewById(R.id.carritoTotal3);
        promocionTxtVw = (TextView) view.findViewById(R.id.carritoDescuento);
        totalaCancelarTxtVw = (TextView) view.findViewById(R.id.carritoTotal4);
        btnCodigo = (Button) view.findViewById(R.id.carritoBtnCodigo);
        btnMas = (Button) view.findViewById(R.id.carritoBtnMas);
        btnTerminate = (Button) view.findViewById(R.id.carritoBtnTerminar);
        back = (AppCompatImageView) view.findViewById(R.id.ivBack);
        txtCodigo = (EditText) view.findViewById(R.id.carritoTxtCodigo);
        btnTerminate.setEnabled(false);
        mostrarCarrito();
        btnListeners();
        backListener();
        onBack();
        btnCodigoListener();
        return view;
    }

    private void btnCodigoListener() {
        btnCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCodigo.getText().toString() != null && !"".equals(txtCodigo.getText().toString())) {
                    if (GlobalUsuario.codigoUsado == null) {
                        GlobalUsuario.codigoUsado = new ArrayList<>();
                    }
                    if (!GlobalUsuario.codigoUsado.contains(txtCodigo.getText().toString())) {
                        GlobalUsuario.codigoUsado.add(txtCodigo.getText().toString());
                        btnCodigo.setEnabled(false);
                        codigoAProbar = txtCodigo.getText().toString();
                        CargarPromos cargarPromos = new CargarPromos();
                        cargarPromos.execute();
                    } else {
                        Toast.makeText(viewGlobal.getContext(), "Ya haz usado este codigo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(viewGlobal.getContext(), "Ingresa Un Codigo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new MenuDeRestauranteFragment());
            }
        });
    }

    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                showFragment(new MenuDeRestauranteFragment());
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }

    private void mostrarCarrito() {
        try {
            List<PlatilloSeleccionado> lista = Logued.platillosSeleccionadosActuales;
            double total1 = 0.00;
            double total2 = 0;
            double total3 = 0;
            double descuento = 0;
            double total4 = 0;
            if (lista != null) {
                if (!lista.isEmpty()) {
                    for (PlatilloSeleccionado pl : lista) {
                        total1 = total1 + pl.getPrecio();
                    }
                    RecyclerPlatillosSeleccionadosAdapter adapter = new RecyclerPlatillosSeleccionadosAdapter(lista, viewGlobal.getContext(), getActivity());
                    carritoRecicler.setLayoutManager(new LinearLayoutManager(viewGlobal.getContext(), LinearLayoutManager.VERTICAL, false));
                    carritoRecicler.setAdapter(adapter);
                    //descuento = lista.get(0).getPlatillo().getMenu().getRestaurante().getDescuento();
                    btnTerminate.setEnabled(true);
                } else {
                    showFragment(new CarritoEmptyActivity());
                }
            } else {
                showFragment(new CarritoEmptyActivity());
            }
            // total2 = total1 * 0.13;
            // total3 = total1 * 0.05;
            // descuento = descuento * total1;
            if (GlobalUsuario.descuento != null) {
                descuento = GlobalUsuario.descuento;
            }
            total4 = total1 + total2 + total3 - descuento;
            DecimalFormat decimalFormat = new DecimalFormat("$ #,##0.00");
            totalOrdenTxtVw.setText(String.valueOf(decimalFormat.format(total1)));
            impuestoTxtVw.setText(String.valueOf(decimalFormat.format(total2)));
            cargoFuimonosTxtVw.setText(String.valueOf(decimalFormat.format(total3)));
            promocionTxtVw.setText(String.valueOf(decimalFormat.format(descuento)));
            totalaCancelarTxtVw.setText(String.valueOf(decimalFormat.format(total4)));
        } catch (Exception e) {
            System.out.println("error carrito: " + e);
        }
    }

    public void btnListeners() {
        btnCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new RestaurantePorCategoria());
                /*Intent i = new Intent(viewGlobal.getContext(), RestaurantePorCategoria.class);
                startActivity(i);*/
            }
        });
        btnTerminate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(viewGlobal.getContext(), ProcesarCarritoActivity.class);
                startActivity(i);
            }
        });
    }

    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private class CargarPromos extends AsyncTask<Long, String, List<Promociones>> {

        @Override
        protected List<Promociones> doInBackground(Long... longs) {
            List<Promociones> promociones = new ArrayList<>();
            try {
                PromocionesService promocionesService = new PromocionesService();
                promociones = promocionesService.obtenerPromocionesDeHoyNonDelay();
            } catch (Exception e) {
                System.out.println("*** error asynk imagePerfil: " + e);
            }
            return promociones;
        }

        @Override
        protected void onPostExecute(List<Promociones> promociones) {
            super.onPostExecute(promociones);
            if (!promociones.isEmpty()) {
                boolean encontrado = false;
                for (Promociones promocion : promociones) {
                    if (promocion.getCodigo() != null && codigoAProbar.equals(promocion.getCodigo())) {
                        encontrado = true;
                        if (promocion.getPorcentajeDescuento() != null) {
                            try {

                                List<PlatilloSeleccionado> lista = Logued.platillosSeleccionadosActuales;
                                DecimalFormat decimalFormat = new DecimalFormat("- $ #,##0.00");
                                DecimalFormat decimalFormat1 = new DecimalFormat("$ #,##0.00");
                                double total1 = 0;
                                if (lista != null) {
                                    if (!lista.isEmpty()) {
                                        for (PlatilloSeleccionado pl : lista) {
                                            total1 = total1 + pl.getPrecio();
                                        }
                                    }
                                }
                                double d = total1 * promocion.getPorcentajeDescuento().doubleValue();
                                promocionTxtVw.setText(decimalFormat.format(d));
                                double total = total1 - d;
                                totalaCancelarTxtVw.setText(decimalFormat1.format(total));
                                GlobalUsuario.descuento = d;
                            } catch (Exception e) {
                                System.out.println("error codigo promo: " + e);
                            }
                        }
                    }
                }
                if (!encontrado) {
                    Toast.makeText(viewGlobal.getContext(), "Codigo no Valido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(viewGlobal.getContext(), "No hay Promos", Toast.LENGTH_SHORT).show();
            }
            btnCodigo.setEnabled(true);
        }
    }

}