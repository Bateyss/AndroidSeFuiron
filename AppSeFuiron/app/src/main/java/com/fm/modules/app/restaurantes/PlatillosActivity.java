package com.fm.modules.app.restaurantes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.RecyclerPlatillosAdapter;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.login.Logued;
import com.fm.modules.models.Image;
import com.fm.modules.models.Menu;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.Restaurante;

import java.util.ArrayList;
import java.util.List;

public class PlatillosActivity extends Fragment {
    private RecyclerView rvPlatillos;
    private AppCompatImageView imagenLogo;
    private View viewGlobal;
    private Menu menu;

    public PlatillosActivity(Menu menu) {
        this.menu = menu;
    }

    /*@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_platillos);
        rvPlatillos = (RecyclerView) findViewById(R.id.rvPlatillos);
        imagenLogo = (AppCompatImageView) findViewById(R.id.ivRestaurantLogoPlatillo);
        verPlatillos();
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_platillos, container, false);
        viewGlobal = view;
        rvPlatillos = (RecyclerView) view.findViewById(R.id.rvPlatillos);
        imagenLogo = (AppCompatImageView) view.findViewById(R.id.ivRestaurantLogoPlatillo);
        verPlatillos();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        verLogo();
    }

    public void verLogo() {
        Image image = null;
        List<Integer> integers = Logued.imagenesIDs;
        Restaurante res = GlobalRestaurantes.restauranteSelected;
        if (res != null) {
            if (integers != null && !integers.isEmpty()) {
                for (int i = 0; i < integers.size(); i++) {
                    if (res.getLogoDeRestaurante().intValue() == integers.get(i)) {
                        image = Logued.imagenes.get(i);
                    }
                }
            }
        }
        if (image != null) {
            Utilities.displayAppCompatImageFromBytea(image.getContent(), imagenLogo, viewGlobal.getContext());
        } else {
            Utilities.displayAppCompatImageFromBytea(null, imagenLogo, viewGlobal.getContext());
        }
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
                        if (p.getDisponible()) {
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
