package com.fm.modules.app.restaurantes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.PlatillosItemViewAdapter;
import com.fm.modules.adapters.RecyclerPlatillosAdapter;
import com.fm.modules.adapters.RecyclerPlatillosPorMenuAdapter;
import com.fm.modules.entities.RespuestaPlatilloPorMenu;
import com.fm.modules.models.Platillo;
import com.fm.modules.service.PlatilloService;

import java.util.ArrayList;
import java.util.List;

public class PlatillosActivity extends AppCompatActivity {
    private RecyclerView rvPlatillos;

    int idMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_platillos);
        rvPlatillos = (RecyclerView) findViewById(R.id.rvPlatillos);
        verPlatillos();
    }

    public void verPlatillos() {
        idMenu = getIntent().getIntExtra("idMenu",0);
        System.out.println("idMenu: "+idMenu);
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

            RecyclerPlatillosAdapter recyclerPlatillosAdapter = new RecyclerPlatillosAdapter(platilloList,PlatillosActivity.this);
            rvPlatillos.setLayoutManager(new LinearLayoutManager(PlatillosActivity.this, LinearLayoutManager.VERTICAL, false));
            rvPlatillos.setAdapter(recyclerPlatillosAdapter);
        }
    }

}
