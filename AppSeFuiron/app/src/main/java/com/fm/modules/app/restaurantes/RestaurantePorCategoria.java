package com.fm.modules.app.restaurantes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.CategoriasRecyclerViewAdapter;
import com.fm.modules.adapters.RestauranteItemViewAdapter;
import com.fm.modules.app.commons.utils.RecyclerTouchListener;
import com.fm.modules.models.Categoria;
import com.fm.modules.models.MenxCategoria;
import com.fm.modules.models.Restaurante;
import com.fm.modules.service.CategoriaService;
import com.fm.modules.service.MenuxCategoriaService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestaurantePorCategoria extends AppCompatActivity {

    private CategoriasRestaurante categoriasRestaurante = new CategoriasRestaurante();
    private ListView listViewRestaurantes;
    private RecyclerView listViewCategorias;
    private EditText textSearch;
    private List<Restaurante> restaurantesGlobal;
    private List<Categoria> categoriasFiltered;
    private List<Categoria> categoriasGlobal;
    private List<String> stringsCategorias;
    private List<MenxCategoria> menxCategoriaGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_restaurants_categoria);
        categoriasGlobal = new ArrayList<>();
        restaurantesGlobal = new ArrayList<>();
        stringsCategorias = new ArrayList<>();
        categoriasFiltered = new ArrayList<>();
        menxCategoriaGlobal = new ArrayList<>();
        listViewCategorias = (RecyclerView) findViewById(R.id.rvCategorias_cat);
        listViewRestaurantes = (ListView) findViewById(R.id.rvRestaurants_res);
        textSearch = (EditText) findViewById(R.id.etSearch_rest);
        if (isNetActive()) {
            categoriasRestaurante.execute();
        }
        textChanged();
        categoryselected();
    }

    public void textChanged() {
        textSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = charSequence.toString();
                filtrarBusquedaList(s);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void categoryselected() {
        listViewCategorias.addOnItemTouchListener(new RecyclerTouchListener(RestaurantePorCategoria.this, listViewCategorias, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                stringsCategorias.add(categoriasFiltered.get(position).getNombreCategoria());
                if (!menxCategoriaGlobal.isEmpty()) {
                    List<Restaurante> restauranteList = restaurantesGlobal;
                    for (MenxCategoria menxCategoriax : menxCategoriaGlobal){
                        String s = menxCategoriax.getCategoria().getNombreCategoria();
                        if (stringsCategorias.contains(s)){
                            restauranteList.add(menxCategoriax.getMenu().getRestaurante());
                        }
                    }
                    if (!restauranteList.isEmpty()){
                        restauranteList = filtrarRestaurantes(restauranteList);
                        verRestaurantesAbiertos(restauranteList);
                        Toast.makeText(RestaurantePorCategoria.this, "filtrado :)", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    public void verRestaurantesAbiertos(List<Restaurante> lista) {
        List<Restaurante> listaFiltrada = new ArrayList<>();
        try {
            if (!lista.isEmpty()) {
                SimpleDateFormat sp = new SimpleDateFormat("HH:mm:ss");
                Date actualDate = new Date();
                String actualHuorString = sp.format(actualDate);
                Date actualHour = sp.parse(actualHuorString);
                for (Restaurante re : lista) {
                    Date restaurantCloseHour = sp.parse(re.getHorarioDeCierre());
                    Date restaurantOpenHour = sp.parse(re.getHorarioDeApertura());
                    System.out.println(" ac "+actualHuorString+" ci "+re.getHorarioDeCierre()+" op "+re.getHorarioDeApertura());
                    if (restaurantCloseHour.getTime() > actualHour.getTime() && actualHour.getTime() > restaurantOpenHour.getTime()) {
                        listaFiltrada.add(re);}
                }
                RestauranteItemViewAdapter restauranteItemViewAdapter = new RestauranteItemViewAdapter(listaFiltrada, RestaurantePorCategoria.this, R.layout.holder_item_restaurant);
                listViewRestaurantes.setAdapter(restauranteItemViewAdapter);
                if (listaFiltrada.isEmpty()) {
                    Toast.makeText(RestaurantePorCategoria.this, "Restaurantes no Disponibles", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RestaurantePorCategoria.this, "Hora de Cierre", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            System.out.println("error consultar restaurantes abiertos " + e);
        }
    }


    public void filtrarBusquedaList(String text) {
        List<Restaurante> lista = new ArrayList<>();
        if (!"".equals(text)) {
            for (Restaurante restaurant : restaurantesGlobal) {
                if (restaurant.getNombreRestaurante().contains(text)) {
                    lista.add(restaurant);
                }
            }
            lista = filtrarRestaurantes(lista);
            if (!lista.isEmpty()) {
                verRestaurantesAbiertos(lista);
            } else {
                Toast.makeText(RestaurantePorCategoria.this, "sin resultados", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        reiniciarAsynk();
    }

    public void reiniciarAsynk() {
        categoriasRestaurante.cancel(true);
        categoriasRestaurante = new CategoriasRestaurante();
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

    public List<Restaurante> filtrarRestaurantes(List<Restaurante> restauranteList) {
        List<Restaurante> list = new ArrayList<>();
        if (!restauranteList.isEmpty()) {
            for (Restaurante restaurante : restauranteList) {
                if (!list.contains(restaurante)) list.add(restaurante);
            }
        }
        return list;
    }

    public List<Categoria> filtrarCategorias(List<Categoria> categoriaList) {
        List<Categoria> categoriaList1 = new ArrayList<>();
        if (!categoriaList.isEmpty()) {
            for (Categoria c : categoriaList) {
                String s = c.getNombreCategoria();
                if (!categoriaList1.contains(c)) categoriaList1.add(c);
            }
        }
        return categoriaList1;
    }

    public class CategoriasRestaurante extends AsyncTask<List<Integer>, String, List<MenxCategoria>> {

        @Override
        protected List<MenxCategoria> doInBackground(List<Integer>... integers) {
            List<MenxCategoria> lista = new ArrayList<>();
            try {
                List<MenxCategoria> listCateg = new ArrayList<>();
                MenuxCategoriaService menuxCategoriaService = new MenuxCategoriaService();
                lista = menuxCategoriaService.obtenerRestaurantesxCateg();
            } catch (Exception e) {
            }
            return lista;
        }

        @Override
        protected void onPostExecute(List<MenxCategoria> menxCategorias) {
            super.onPostExecute(menxCategorias);
            if (!menxCategorias.isEmpty()) {
                menxCategoriaGlobal = menxCategorias;
                for (MenxCategoria menxCategoria : menxCategorias) {
                    restaurantesGlobal.add(menxCategoria.getMenu().getRestaurante());
                }
                for (MenxCategoria menxCategoria : menxCategorias) {
                    categoriasGlobal.add(menxCategoria.getCategoria());
                }
                categoriasFiltered = filtrarCategorias(categoriasGlobal);
                List<Restaurante> restauranteList = filtrarRestaurantes(restaurantesGlobal);
                RestauranteItemViewAdapter restauranteItemViewAdapter = new RestauranteItemViewAdapter(restauranteList, RestaurantePorCategoria.this, R.layout.holder_item_restaurant);
                listViewRestaurantes.setAdapter(restauranteItemViewAdapter);
                CategoriasRecyclerViewAdapter categoriasRecyclerViewAdapter = new CategoriasRecyclerViewAdapter(categoriasFiltered, RestaurantePorCategoria.this);
                listViewCategorias.setLayoutManager(new LinearLayoutManager(RestaurantePorCategoria.this, LinearLayoutManager.HORIZONTAL, false));
                listViewCategorias.setAdapter(categoriasRecyclerViewAdapter);
            }
        }
    }
}