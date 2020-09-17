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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.CategoriasRecyclerViewAdapter;
import com.fm.modules.adapters.RecyclerPlatillosFavoritosAdapter;
import com.fm.modules.adapters.RestauranteItemViewAdapter;
import com.fm.modules.app.commons.utils.RecyclerTouchListener;
import com.fm.modules.app.login.Logued;
import com.fm.modules.models.Categoria;
import com.fm.modules.models.MenxCategoria;
import com.fm.modules.models.PlatilloFavorito;
import com.fm.modules.models.Restaurante;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.MenuxCategoriaService;
import com.fm.modules.service.PlatilloFavoritoService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestaurantePorCategoria extends AppCompatActivity {

    private CategoriasRestaurante categoriasRestaurante = new CategoriasRestaurante();
    private Favoritos favoritos = new Favoritos();
    private ListView listViewRestaurantes;
    private RecyclerView listViewCategorias;
    private EditText textSearch;
    private List<Restaurante> restaurantesGlobal;
    private List<Restaurante> restaurantesFiltered;
    private List<Categoria> categoriasFiltered;
    private List<Categoria> categoriasGlobal;
    private List<MenxCategoria> menxCategoriaGlobal;
    private RecyclerView rvPlatillosFavoritos;
    private List<Categoria> categoriasSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_restaurants_categoria);
        categoriasGlobal = new ArrayList<>();
        restaurantesGlobal = new ArrayList<>();
        categoriasFiltered = new ArrayList<>();
        menxCategoriaGlobal = new ArrayList<>();
        categoriasSelected = new ArrayList<>();
        restaurantesFiltered = new ArrayList<>();
        listViewCategorias = (RecyclerView) findViewById(R.id.rvCategorias_cat);
        listViewRestaurantes = (ListView) findViewById(R.id.rvRestaurants_res);
        rvPlatillosFavoritos = findViewById(R.id.rvPlatillosFavoritos_cat);
        textSearch = (EditText) findViewById(R.id.etSearch_rest);
        if (isNetActive()) {
            favoritos.execute();
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
                categoriasSelected.add(categoriasFiltered.get(position));
                categoriasSelected = filtrarCategoriasPorNombre(categoriasFiltered);
                List<String> strings = new ArrayList<>();
                for (Categoria categoria : categoriasSelected) {
                    strings.add(categoria.getNombreCategoria());
                }
                if (!menxCategoriaGlobal.isEmpty()) {
                    List<Restaurante> restauranteList = restaurantesGlobal;
                    for (MenxCategoria menxCategoriax : menxCategoriaGlobal) {
                        if (strings.contains(menxCategoriax.getCategoria().getNombreCategoria())) {
                            restauranteList.add(menxCategoriax.getMenu().getRestaurante());
                        }
                    }
                    if (!restauranteList.isEmpty()) {
                        restaurantesFiltered = filtrarRestaurantes(restauranteList);
                        verRestaurantesAbiertos(restaurantesFiltered);
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
                    System.out.println(" ac " + actualHuorString + " ci " + re.getHorarioDeCierre() + " op " + re.getHorarioDeApertura());
                    if (restaurantCloseHour.getTime() > actualHour.getTime() && actualHour.getTime() > restaurantOpenHour.getTime()) {
                        listaFiltrada.add(re);
                    }
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
        favoritos.cancel(true);
        favoritos = new Favoritos();
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
            List<Integer> integers = new ArrayList<>();
            for (Restaurante restaurante : restauranteList) {
                System.out.println("filtro restaurante: " + restaurante.getNombreRestaurante());
                if (!integers.contains(restaurante.getRestauranteId().intValue())) {
                    list.add(restaurante);
                    integers.add(restaurante.getRestauranteId().intValue());
                }
            }
        }
        return list;
    }

    public List<Restaurante> filtrarRestaurantesDestacados(List<Restaurante> restauranteList) {
        List<Restaurante> list = new ArrayList<>();
        if (!restauranteList.isEmpty()) {
            for (Restaurante restaurante : restauranteList) {
                System.out.println("destav: " + restaurante.getDestacado());
                if (restaurante.getDestacado()) {
                    list.add(restaurante);
                }
            }
        }
        return list;
    }

    public List<Categoria> filtrarCategoriasPorNombre(List<Categoria> categoriaList) {
        List<Categoria> categoriaList1 = new ArrayList<>();
        if (!categoriaList.isEmpty()) {
            List<String> strings = new ArrayList<>();
            for (Categoria c : categoriaList) {
                if (!strings.contains(c.getNombreCategoria())) {
                    categoriaList1.add(c);
                    strings.add(c.getNombreCategoria());
                }
            }
        }
        return categoriaList1;
    }

    public List<PlatilloFavorito> filtrarFavoritos(List<PlatilloFavorito> platilloFavoritoList) {
        List<PlatilloFavorito> platillos = new ArrayList<>();
        if (!platilloFavoritoList.isEmpty()) {
            for (PlatilloFavorito f : platilloFavoritoList) {
                Usuario user = Logued.usuarioLogued;
                if (user != null) {
                    if (user.getUsuarioId().intValue() == f.getUsuarios().getUsuarioId().intValue()) {
                        platillos.add(f);
                    }
                }
            }
        }
        return platillos;
    }

    public class CategoriasRestaurante extends AsyncTask<String, String, List<MenxCategoria>> {

        @Override
        protected List<MenxCategoria> doInBackground(String... strings) {
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
                List<Integer> integers = new ArrayList<>();
                for (MenxCategoria menxCategoria : menxCategorias) {
                    if (!integers.contains(menxCategoria.getCategoria().getCategoriaId().intValue())) {
                        categoriasGlobal.add(menxCategoria.getCategoria());
                        integers.add(menxCategoria.getCategoria().getCategoriaId().intValue());
                    }
                }
                List<Restaurante> restauranteList = new ArrayList<>();
                for (MenxCategoria menxCategoria : menxCategorias) {
                    restauranteList.add(menxCategoria.getMenu().getRestaurante());
                }
                restaurantesGlobal = filtrarRestaurantes(restauranteList);
                GlobalRestaurantes.restaurantes = restaurantesGlobal;
                restaurantesFiltered = filtrarRestaurantesDestacados(restaurantesGlobal);
                categoriasFiltered = filtrarCategoriasPorNombre(categoriasGlobal);
                RestauranteItemViewAdapter restauranteItemViewAdapter = new RestauranteItemViewAdapter(restaurantesFiltered, RestaurantePorCategoria.this, R.layout.holder_item_restaurant);
                listViewRestaurantes.setAdapter(restauranteItemViewAdapter);
                CategoriasRecyclerViewAdapter categoriasRecyclerViewAdapter = new CategoriasRecyclerViewAdapter(categoriasFiltered, RestaurantePorCategoria.this);
                listViewCategorias.setLayoutManager(new LinearLayoutManager(RestaurantePorCategoria.this, LinearLayoutManager.HORIZONTAL, false));
                listViewCategorias.setAdapter(categoriasRecyclerViewAdapter);
            }
        }
    }

    public class Favoritos extends AsyncTask<String, String, List<PlatilloFavorito>> {

        @Override
        protected List<PlatilloFavorito> doInBackground(String... strings) {
            List<PlatilloFavorito> platilloFavoritoList = new ArrayList<>();
            try {
                PlatilloFavoritoService platilloFavoritoService = new PlatilloFavoritoService();
                platilloFavoritoList = platilloFavoritoService.obtenerPlatilloFavoritos();
            } catch (Exception e) {
                System.out.println("Error en UnderThreash:" + e.getMessage() + " " + e.getClass());
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
                if (!platilloFavorito.isEmpty()) {
                    List<PlatilloFavorito> listaPlatillos = filtrarFavoritos(platilloFavorito);
                    if (!listaPlatillos.isEmpty()) {
                        RecyclerPlatillosFavoritosAdapter rvAdapter = new RecyclerPlatillosFavoritosAdapter(listaPlatillos, RestaurantePorCategoria.this);
                        rvPlatillosFavoritos.setLayoutManager(new LinearLayoutManager(RestaurantePorCategoria.this, LinearLayoutManager.HORIZONTAL, false));
                        rvPlatillosFavoritos.setAdapter(rvAdapter);
                    }
                }
            } catch (Throwable throwable) {
                System.out.println("Error Activity: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}