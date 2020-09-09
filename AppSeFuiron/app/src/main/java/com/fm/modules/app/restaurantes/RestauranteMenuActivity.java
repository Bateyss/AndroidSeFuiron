package com.fm.modules.app.restaurantes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.fm.modules.R;
import com.fm.modules.adapters.MenuPorRestauranteItemViewAdapter;
import com.fm.modules.entities.RespuestaMenuPorRestaurantes;
import com.fm.modules.models.Menu;
import com.fm.modules.service.MenuService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RestauranteMenuActivity extends AppCompatActivity {

    UnderThreads underThreads = new UnderThreads();

    private boolean conectec;
    private final String dominio = "http://192.168.1.2:8181";
    private TabLayout menuTab;
    private ViewPager viewPager;
    private Bundle bundle;
    private Intent intent;
    private RecyclerView rvFoods;
    private ListView listView;

    Long idRestaurante;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_restaurant_menu);
        getSupportActionBar().hide();
        idRestaurante = getIntent().getLongExtra("idRestaurante" , 1L);;
        Toast.makeText(RestauranteMenuActivity.this, "Menu del Restaurante: " +idRestaurante, Toast.LENGTH_SHORT).show();
        //rvFoods = findViewById(R.id.rvFoods);
        //initTab();
        listView = findViewById(R.id.lvMenus);

        underThreads.execute();

    }

    public void initTab(){
        menuTab.addTab(menuTab.newTab().setText("hola"));
        MenuService menuService = new MenuService();
        List<Menu> list = menuService.obtenerMenus();
        for(int i = 1; i < list.size(); i++){
            menuTab.addTab(menuTab.newTab().setText(list.get(i).getNombreMenu()));
        }
    }


    public class UnderThreads extends AsyncTask<String, String , List<RespuestaMenuPorRestaurantes>> {


        @Override
        protected List<RespuestaMenuPorRestaurantes> doInBackground(String... strings) {
            List<RespuestaMenuPorRestaurantes> menuList = new ArrayList<>();

            try {
                MenuService menuService = new MenuService();
                //menuList = (List<Menu>) menuService.obtenerMenus();
                menuList = menuService.menuPorRestaurantes(idRestaurante);

            }catch (Exception e){
                System.out.println("Error en UnderThreash:" +e.getMessage() +" " +e.getClass());
            }
            return menuList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<RespuestaMenuPorRestaurantes> menu) {
            super.onPostExecute(menu);
            try {
                if (!menu.isEmpty()){
                    MenuPorRestauranteItemViewAdapter adapter = new MenuPorRestauranteItemViewAdapter(menu, RestauranteMenuActivity.this, R.layout.frg_restaurants);
                    listView.setAdapter(adapter);
                    Toast.makeText(RestauranteMenuActivity.this, "Menus Cargados" +menu.size(), Toast.LENGTH_SHORT).show();
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
