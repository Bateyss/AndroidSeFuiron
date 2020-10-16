package com.fm.modules.app.usuario;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.RecyclerPlatillosFavoritosAdapter;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.restaurantes.RestaurantePorCategoria;
import com.fm.modules.models.PlatilloFavorito;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.PlatilloFavoritoService;

import java.util.ArrayList;
import java.util.List;


public class MyFavorites extends Fragment {

    private View viewGlobal;
    private RecyclerView favoritesRecyView;
    private MyFavoritesData favorites = new MyFavoritesData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_favorites, container, false);
        viewGlobal = view;
        favoritesRecyView = (RecyclerView) view.findViewById(R.id.myFavoritesRecyclerviwe);
        verFavoritos();
        onBack();
        return view;
    }

    private void verFavoritos() {
        favorites.execute();
    }

    private class MyFavoritesData extends AsyncTask<String, String, List<PlatilloFavorito>> {

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
                        RecyclerPlatillosFavoritosAdapter rvAdapter = new RecyclerPlatillosFavoritosAdapter(listaPlatillos, viewGlobal.getContext(), getActivity());
                        favoritesRecyView.setLayoutManager(new LinearLayoutManager(viewGlobal.getContext(), LinearLayoutManager.VERTICAL, false));
                        favoritesRecyView.setAdapter(rvAdapter);
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

    public List<PlatilloFavorito> filtrarFavoritos(List<PlatilloFavorito> platilloFavoritoList) {
        List<PlatilloFavorito> platillos = new ArrayList<>();
        if (!platilloFavoritoList.isEmpty()) {
            Usuario user = Logued.usuarioLogued;
            if (user != null) {
                for (PlatilloFavorito f : platilloFavoritoList) {
                    if (user.getUsuarioId().intValue() == f.getUsuarios().getUsuarioId().intValue()) {
                        if (f.getPlatillo().getDisponible()) {
                            platillos.add(f);
                        }
                    }
                }
            }
        }
        return platillos;
    }
    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                showFragment(new RestaurantePorCategoria());
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }
    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}