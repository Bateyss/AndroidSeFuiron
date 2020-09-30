package com.fm.modules.app.carrito;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.sqlite.models.RecyclerTarjetasAdapter;
import com.fm.modules.sqlite.models.Tarjetas;
import com.fm.modules.sqlite.models.TarjetasSQLite;

import java.util.List;

public class PagoTargetaFragment extends Fragment {

    private View viewGlobal;
    private List<Tarjetas> tarjetas;
    private RecyclerView targetaRecycler;
    private ImageView targetaAgregar;
    private Button tarjetaUsar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pago_targeta, container, false);
        viewGlobal = view;
        targetaRecycler = (RecyclerView) view.findViewById(R.id.tarjetaRecycler);
        targetaAgregar = (ImageView) view.findViewById(R.id.tarjetaAgregar);
        tarjetaUsar = (Button) view.findViewById(R.id.tarjetaUsar);
        cargarTargetas();
        agregarListener();
        return view;
    }

    private void agregarListener() {
        targetaAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new AddTargetaFragment());
            }
        });
    }

    private void cargarTargetas() {
        TarjetasSQLite tarjetasSQLite = new TarjetasSQLite(viewGlobal.getContext());
        tarjetas = tarjetasSQLite.readAll();
        if (tarjetas != null && !tarjetas.isEmpty()) {
            RecyclerTarjetasAdapter recyclerTarjetasAdapter = new RecyclerTarjetasAdapter(tarjetas, viewGlobal.getContext(), getActivity());
            targetaRecycler.setLayoutManager(new LinearLayoutManager(viewGlobal.getContext(), LinearLayoutManager.HORIZONTAL, false));
            targetaRecycler.setAdapter(recyclerTarjetasAdapter);
        } else {
            Toast.makeText(viewGlobal.getContext(), "No hay Tarjetas", Toast.LENGTH_SHORT).show();
        }
    }

    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}