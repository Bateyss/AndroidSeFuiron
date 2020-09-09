package com.fm.modules.app.restaurantes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.RecyclerPlatillosAdapter;
import com.fm.modules.adapters.RecyclerPlatillosPorMenuAdapter;
import com.fm.modules.entities.RespuestaPlatilloPorMenu;
import com.fm.modules.service.PlatilloService;

import java.util.ArrayList;
import java.util.List;

public class PlatillosActivity extends AppCompatActivity {

    UnderThreads underThreads = new UnderThreads();
    private RecyclerView rvPlatillos;

    String idMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_platillos);
        getSupportActionBar().hide();
        idMenu = getIntent().getStringExtra("idMenu");

        Toast.makeText(PlatillosActivity.this, "Menu: " +idMenu, Toast.LENGTH_SHORT).show();
        rvPlatillos = findViewById(R.id.rvPlatillos);

        underThreads.execute();
    }

    public class UnderThreads extends AsyncTask<String, String , List<RespuestaPlatilloPorMenu>> {


        @Override
        protected List<RespuestaPlatilloPorMenu> doInBackground(String... strings) {
            List<RespuestaPlatilloPorMenu> platilloList = new ArrayList<>();

            try {
                PlatilloService platilloService = new PlatilloService();
                platilloList = platilloService.platilloPorMenu(idMenu);

            }catch (Exception e){
                System.out.println("Error en UnderThreash:" +e.getMessage() +" " +e.getClass());
            }
            return platilloList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<RespuestaPlatilloPorMenu> platillos) {
            super.onPostExecute(platillos);
            try {
                if (!platillos.isEmpty()){
                    //MenuItemViewAdapter adapter = new MenuItemViewAdapter(menu, RestauranteMenuActivity.this, R.layout.frg_restaurants);
                    RecyclerPlatillosPorMenuAdapter adapter = new RecyclerPlatillosPorMenuAdapter(platillos, PlatillosActivity.this);
                    rvPlatillos.setLayoutManager(new LinearLayoutManager(PlatillosActivity.this, LinearLayoutManager.VERTICAL, false));
                    rvPlatillos.setAdapter(adapter);
                    Toast.makeText(PlatillosActivity.this, "Platillos Cargados" +platillos.size(), Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(RestaurantesActivity.this, "Restaurantes No Cargados" +restaurantes.size(), Toast.LENGTH_SHORT).show();
                    //reiniciarAsynkProcess();
                }
            }catch (Throwable throwable){
                System.out.println("Error Activity: " +throwable.getMessage());
                throwable.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
