package com.fm.modules.app.restaurantes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.CategoriasRecyclerViewAdapter;
import com.fm.modules.adapters.RecyclerPlatillosFavoritosAdapter;
import com.fm.modules.adapters.RestauranteItemViewAdapter;
import com.fm.modules.app.commons.utils.RecyclerTouchListener;
import com.fm.modules.app.login.Logued;
import com.fm.modules.models.Categoria;
import com.fm.modules.models.Image;
import com.fm.modules.models.MenxCategoria;
import com.fm.modules.models.PlatilloFavorito;
import com.fm.modules.models.Restaurante;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.ImageService;
import com.fm.modules.service.MenuxCategoriaService;
import com.fm.modules.service.PlatilloFavoritoService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestaurantePorCategoria extends Fragment {

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
    private View inclide;
    private AppCompatImageView fotoPerfil;
    private CargarFoto cargarFoto;
    private View viewGlobal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_restaurants_categoria, container, false);
        viewGlobal = view;
        categoriasGlobal = new ArrayList<>();
        restaurantesGlobal = new ArrayList<>();
        categoriasFiltered = new ArrayList<>();
        menxCategoriaGlobal = new ArrayList<>();
        categoriasSelected = new ArrayList<>();
        restaurantesFiltered = new ArrayList<>();
        listViewCategorias = (RecyclerView) view.findViewById(R.id.rvCategorias_cat);
        listViewRestaurantes = (ListView) view.findViewById(R.id.rvRestaurants_res);
        rvPlatillosFavoritos = view.findViewById(R.id.rvPlatillosFavoritos_cat);
        textSearch = (EditText) view.findViewById(R.id.etSearch_rest);
        inclide = (View) view.findViewById(R.id.asConfigur);
        fotoPerfil = (AppCompatImageView) inclide.findViewById(R.id.ivProfilePhotoPrincipal);
        if (isNetActive()) {
            favoritos.execute();
            categoriasRestaurante.execute();
        }
        textChanged();
        categoryselected();
        profilePhoto();
        return view;
    }

    /*@Override
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
        inclide = (View) findViewById(R.id.asConfigur);
        fotoPerfil = (AppCompatImageView) inclide.findViewById(R.id.ivProfilePhotoPrincipal);
        if (isNetActive()) {
            favoritos.execute();
            categoriasRestaurante.execute();
        }
        textChanged();
        categoryselected();
        profilePhoto();
    }*/


    private void profilePhoto() {
        cargarFoto = new CargarFoto();
        Usuario usuario = Logued.usuarioLogued;
        if (usuario != null) {
            cargarFoto.execute(usuario.getImagenDePerfil());
        }
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
        listViewCategorias.addOnItemTouchListener(new RecyclerTouchListener(viewGlobal.getContext(), listViewCategorias, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Categoria categoria = categoriasFiltered.get(position);
                List<MenxCategoria> menxCategorias = GlobalRestaurantes.menxCategorias;
                List<Restaurante> restaurantes = new ArrayList<>();
                int idCategoria = categoria.getCategoriaId().intValue();
                List<Integer> integers = new ArrayList<>();
                if (menxCategorias != null && !menxCategorias.isEmpty()) {
                    for (MenxCategoria mx : menxCategorias) {
                        if (mx.getCategoria().getCategoriaId().intValue() == idCategoria) {
                            if (!integers.contains(mx.getMenu().getRestaurante().getRestauranteId().intValue())) {
                                restaurantes.add(mx.getMenu().getRestaurante());
                                integers.add(mx.getMenu().getRestaurante().getRestauranteId().intValue());
                            }
                        }
                    }
                }
                if (!restaurantes.isEmpty()) {
                    verRestaurantesAbiertos(restaurantes);
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
                RestauranteItemViewAdapter restauranteItemViewAdapter = new RestauranteItemViewAdapter(listaFiltrada, viewGlobal.getContext(), R.layout.holder_item_restaurant, getActivity());
                listViewRestaurantes.setAdapter(restauranteItemViewAdapter);
                if (listaFiltrada.isEmpty()) {
                    Toast.makeText(viewGlobal.getContext(), "Restaurantes no Disponibles", Toast.LENGTH_SHORT).show();
                    Toast.makeText(viewGlobal.getContext(), "Hora de Cierre", Toast.LENGTH_SHORT).show();
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
                if (restaurant.getNombreRestaurante().toUpperCase().contains(text.toUpperCase())) {
                    lista.add(restaurant);
                }
            }
            lista = filtrarRestaurantes(lista);
            if (!lista.isEmpty()) {
                verRestaurantesAbiertos(lista);
            } else {
                Toast.makeText(viewGlobal.getContext(), "sin resultados", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
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
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                c = true;
            }
        } catch (Exception e) {
            System.out.println("error isNetActive: " + e);
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
                GlobalRestaurantes.menxCategorias = menxCategorias;
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
                RestauranteItemViewAdapter restauranteItemViewAdapter = new RestauranteItemViewAdapter(restaurantesFiltered, viewGlobal.getContext(), R.layout.holder_item_restaurant, getActivity());
                listViewRestaurantes.setAdapter(restauranteItemViewAdapter);
                CategoriasRecyclerViewAdapter categoriasRecyclerViewAdapter = new CategoriasRecyclerViewAdapter(categoriasFiltered, viewGlobal.getContext());
                listViewCategorias.setLayoutManager(new LinearLayoutManager(viewGlobal.getContext(), LinearLayoutManager.HORIZONTAL, false));
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
                        RecyclerPlatillosFavoritosAdapter rvAdapter = new RecyclerPlatillosFavoritosAdapter(listaPlatillos, viewGlobal.getContext());
                        rvPlatillosFavoritos.setLayoutManager(new LinearLayoutManager(viewGlobal.getContext(), LinearLayoutManager.HORIZONTAL, false));
                        rvPlatillosFavoritos.setAdapter(rvAdapter);
                    } else {
                        rvPlatillosFavoritos.setLayoutManager(new LinearLayoutManager(viewGlobal.getContext(), LinearLayoutManager.HORIZONTAL, false));
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

    private class CargarFoto extends AsyncTask<Long, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(Long... longs) {

            Bitmap imagen = null;
            try {
                imagen = Logued.imagenPerfil;
                if (imagen == null) {
                    ImageService imageService = new ImageService();
                    Image image = new Image();
                    image = imageService.obtenerImagenPorId(longs[0]);
                    if (image != null) {
                        byte[] b = image.getContent();
                        if (b != null) {
                            imagen = BitmapFactory.decodeByteArray(b, 0, b.length);
                            Logued.imagenPerfil = imagen;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("*** error asynk imagePerfil: " + e);
            }
            return imagen;
        }

        @Override
        protected void onPostExecute(Bitmap image) {
            super.onPostExecute(image);
            if (image != null) {
                fotoPerfil.setImageBitmap(image);
            } else {
                fotoPerfil.setImageResource(R.drawable.ic_empty_profile_photo);
            }
        }
    }

    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}