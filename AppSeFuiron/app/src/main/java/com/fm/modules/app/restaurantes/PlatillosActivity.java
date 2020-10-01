package com.fm.modules.app.restaurantes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.RecyclerPlatillosAdapter;
import com.fm.modules.models.Menu;
import com.fm.modules.models.Platillo;

import java.util.ArrayList;
import java.util.List;

public class PlatillosActivity extends Fragment {
    private RecyclerView rvPlatillos;
    private View viewGlobal;
    private Menu menu;

    public PlatillosActivity(Menu menu) {
        this.menu = menu;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_platillos, container, false);
        viewGlobal = view;
        rvPlatillos = (RecyclerView) view.findViewById(R.id.rvPlatillos);
        verPlatillos();
        return view;
    }

    public void verPlatillos() {
        Menu m = menu;
        int idMenu = 0;
        if (m != null) {
            idMenu = m.getMenuId().intValue();
        }
        if (idMenu != 0) {
            List<Platillo> platilloList = new ArrayList<>();
            List<Integer> ints = new ArrayList<>();
            for (Platillo p : GlobalRestaurantes.platilloList) {
                if (p.getMenu().getMenuId().intValue() == idMenu) {
                    if (!ints.contains(p.getPlatilloId().intValue())) {
                        if (p.getDisponible() != null && p.getDisponible()) {
                            platilloList.add(p);
                            ints.add(p.getPlatilloId().intValue());
                        }
                    }
                }
            }

            RecyclerPlatillosAdapter recyclerPlatillosAdapter = new RecyclerPlatillosAdapter(platilloList, viewGlobal.getContext(), getActivity());
            rvPlatillos.setLayoutManager(new LinearLayoutManager(viewGlobal.getContext(), LinearLayoutManager.VERTICAL, false));
            rvPlatillos.setAdapter(recyclerPlatillosAdapter);
        }
    }

    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
