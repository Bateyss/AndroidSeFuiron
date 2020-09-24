package com.fm.modules.app.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.RecyclerMenuOtionsAdapter;

import java.util.ArrayList;
import java.util.List;

public class OptionsFragment extends Fragment {

    private RecyclerView recyclerView;
    private View viewGlobal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_options, container, false);
        viewGlobal = view;
        recyclerView = (RecyclerView) view.findViewById(R.id.menufgOptions);
        cargarOptions();
        return view;
    }

    private void cargarOptions() {
        List<OptionsEntity> options = new ArrayList<>();
        options.add(new OptionsEntity(1, R.drawable.ic_profile_option, "Mi Perfil"));
        options.add(new OptionsEntity(2, R.drawable.ic_wallet_option, "Billetera"));
        options.add(new OptionsEntity(3, R.drawable.ic_addresses_option, "Direcciones"));
        options.add(new OptionsEntity(4, R.drawable.ic_orders_option, "Mis Ordenes"));
        options.add(new OptionsEntity(5, R.drawable.ic_favorites_option, "Favoritos"));
        options.add(new OptionsEntity(6, R.drawable.ic_support_option, "Soporte"));
        options.add(new OptionsEntity(7, R.drawable.ic_contact_option, "Contactanos"));
        options.add(new OptionsEntity(8, R.drawable.ic_track_order_option, "Sigue tu pedido"));
        options.add(new OptionsEntity(9, R.drawable.ic_log_out_option, "Cerrar sesion"));
        RecyclerMenuOtionsAdapter adapter = new RecyclerMenuOtionsAdapter(options, viewGlobal.getContext(), getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(viewGlobal.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

}