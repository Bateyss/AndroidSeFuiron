package com.fm.modules.app.restaurantes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.PedidosActivity;
import com.fm.modules.R;
import com.fm.modules.adapters.RecyclerPlatillosFavoritosAdapter;
import com.fm.modules.adapters.RecyclerPlatillosPorMenuAdapter;
import com.fm.modules.adapters.RestauranteItemViewAdapter;
import com.fm.modules.models.PlatilloFavorito;
import com.fm.modules.models.Restaurante;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.PlatilloFavoritoService;
import com.fm.modules.service.RestauranteService;

import java.util.ArrayList;
import java.util.List;

public class RestaurantesActivity extends AppCompatActivity {

    UnderThreads underThreads = new UnderThreads();
    UnderThreadsTwo underThreadsTwo = new UnderThreadsTwo();
    //Conexion conexion = new RestaurantesActivity.Conexion();
    private boolean conected;
    private final String dominio = "http://192.168.1.2:8181";
    private ListView listView;
    private RecyclerView rvPlatillosFavoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_restaurants);
        getSupportActionBar().hide();

        listView = (ListView) findViewById(R.id.rvRestaurants);
        rvPlatillosFavoritos = findViewById(R.id.rvPlatillosFavoritos);
        conected = isNetActive();



        if(conected){
            //conexion.execute();
            System.out.println("hola Aqui");
            underThreads.execute();
            underThreadsTwo.execute();
        }else{
            Toast.makeText(RestaurantesActivity.this, "No hay conexion", Toast.LENGTH_LONG);
            Log.e("error", "" + "no hay conexion");
            System.out.println("No hay conexion: RestaurantActivity" );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_navigator_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuHome:
                Toast.makeText(RestaurantesActivity.this, "menuHome", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuShoppingCart:
                Toast.makeText(RestaurantesActivity.this, "menuShoppingCart", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuOptions:
                Toast.makeText(RestaurantesActivity.this, "menuOptions", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void reiniciarAsynkProcess(){
        underThreads.cancel(true);
        //conexion.cancel(true);
        underThreads = new UnderThreads();
        //conexion = new Conexion();
    }

    public boolean isNetActive() {
        boolean c = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public class UnderThreads extends AsyncTask<String, String , List<Restaurante>>{

        @Override
        protected List<Restaurante> doInBackground(String... strings) {
            List<Restaurante> restauranteList = new ArrayList<>();
            try {
                RestauranteService restauranteService = new RestauranteService();
                restauranteList = restauranteService.obtenerRestaurantes();

            }catch (Exception e){
                System.out.println("Error en UnderThreash:" +e.getMessage() +" " +e.getClass());
            }
            return restauranteList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Restaurante> restaurantes) {
            super.onPostExecute(restaurantes);
            try {
               if (!restaurantes.isEmpty()){
                   RestauranteItemViewAdapter adapter = new RestauranteItemViewAdapter(restaurantes, RestaurantesActivity.this, R.layout.frg_restaurants);
                   listView.setAdapter(adapter);
                   Toast.makeText(RestaurantesActivity.this, "Restaurantes Cargados" +restaurantes.size(), Toast.LENGTH_SHORT).show();
               }else{
                   Toast.makeText(RestaurantesActivity.this, "Restaurantes No Cargados" +restaurantes.size(), Toast.LENGTH_SHORT).show();
                   reiniciarAsynkProcess();
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


    public class UnderThreadsTwo extends AsyncTask<String, String , List<PlatilloFavorito>>{

        @Override
        protected List<PlatilloFavorito> doInBackground(String... strings) {
            List<PlatilloFavorito> platilloFavoritoList = new ArrayList<>();
            try {
                PlatilloFavoritoService platilloFavoritoService = new PlatilloFavoritoService();
                platilloFavoritoList = platilloFavoritoService.obtenerPlatilloFavoritos();
            }catch (Exception e){
                System.out.println("Error en UnderThreash:" +e.getMessage() +" " +e.getClass());
            }
            return platilloFavoritoList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<PlatilloFavorito> platilloFavorito) {
            super.onPostExecute(platilloFavorito);
            try {
                if (!platilloFavorito.isEmpty()){
                    RecyclerPlatillosFavoritosAdapter rvAdapter = new RecyclerPlatillosFavoritosAdapter(platilloFavorito, RestaurantesActivity.this);
                    rvPlatillosFavoritos.setLayoutManager(new LinearLayoutManager(RestaurantesActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    rvPlatillosFavoritos.setAdapter(rvAdapter);

                    Toast.makeText(RestaurantesActivity.this, "Plativos Favoritos Cargados" +platilloFavorito.size(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RestaurantesActivity.this, "Plativos Favoritos Cargados" +platilloFavorito.size(), Toast.LENGTH_SHORT).show();
                    reiniciarAsynkProcess();
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
