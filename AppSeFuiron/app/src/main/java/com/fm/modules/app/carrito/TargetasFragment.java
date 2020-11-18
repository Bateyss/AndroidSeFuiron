package com.fm.modules.app.carrito;

import android.content.Context;
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

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.restaurantes.RestaurantePorCategoria;
import com.fm.modules.sqlite.models.RecyclerTarjetasAdapter;
import com.fm.modules.sqlite.models.TarjetasSaved;
import com.fm.modules.sqlite.models.TarjetasService;

import java.util.ArrayList;
import java.util.List;

public class TargetasFragment extends Fragment {

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
            targetaAgregar.setVisibility(View.INVISIBLE);
            tarjetaUsar.setVisibility(View.INVISIBLE);
            onBack();
            leftArrowListener();
            cargarTargetas();
        } catch (Exception e) {
            System.out.println("error onCreateView" + getClass().getCanonicalName() + e);
        }
        return view;
    }

    private void leftArrowListener() {
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new RestaurantePorCategoria());
            }
        });
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
}