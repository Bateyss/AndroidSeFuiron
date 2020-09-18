package com.fm.modules.app.restaurantes;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.RecyclerPlatillosAdapter;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.login.Logued;
import com.fm.modules.models.Image;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.Restaurante;

import java.util.ArrayList;
import java.util.List;

public class PlatillosActivity extends AppCompatActivity {
    private RecyclerView rvPlatillos;
    private AppCompatImageView imagenLogo;

    int idMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_platillos);
        rvPlatillos = (RecyclerView) findViewById(R.id.rvPlatillos);
        imagenLogo = (AppCompatImageView) findViewById(R.id.ivRestaurantLogoPlatillo);
        verPlatillos();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
            Utilities.displayAppCompatImageFromBytea(image.getContent(), imagenLogo, PlatillosActivity.this);
        } else {
            Utilities.displayAppCompatImageFromBytea(null, imagenLogo, PlatillosActivity.this);
        }
    }

    public void verPlatillos() {
        idMenu = getIntent().getIntExtra("idMenu", 0);
        System.out.println("idMenu: " + idMenu);
        if (idMenu != 0) {
            List<Platillo> platilloList = new ArrayList<>();
            List<Integer> ints = new ArrayList<>();
            for (Platillo p : GlobalRestaurantes.platilloList) {
                if (p.getMenu().getMenuId().intValue() == idMenu) {
                    if (!ints.contains(p.getPlatilloId().intValue())) {
                        platilloList.add(p);
                        ints.add(p.getPlatilloId().intValue());
                    }
                }
            }

            RecyclerPlatillosAdapter recyclerPlatillosAdapter = new RecyclerPlatillosAdapter(platilloList, PlatillosActivity.this);
            rvPlatillos.setLayoutManager(new LinearLayoutManager(PlatillosActivity.this, LinearLayoutManager.VERTICAL, false));
            rvPlatillos.setAdapter(recyclerPlatillosAdapter);
        }
    }

}
