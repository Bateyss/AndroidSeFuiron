package com.fm.modules.app.restaurantes;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.BusquedaPlatillosViewAdapter;
import com.fm.modules.adapters.BusquedaRestaurantesViewAdapter;
import com.fm.modules.models.PlatillosNames;
import com.fm.modules.models.Restaurante;
import com.fm.modules.service.PlatilloService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BusquedaFragment extends Fragment {

    private String mParam1;
    private View viewGlobal;
    private EditText textSearch;
    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
    private ImageView closer;
    private List<PlatillosNames> platillosNamesGlobal;

    public BusquedaFragment(String mParam1) {
        this.mParam1 = mParam1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGlobal = inflater.inflate(R.layout.fragment_busqueda, container, false);
        recyclerView = viewGlobal.findViewById(R.id.recycler);
        recyclerView1 = viewGlobal.findViewById(R.id.recycler1);
        textSearch = viewGlobal.findViewById(R.id.etSearch_rest);
        closer = viewGlobal.findViewById(R.id.closer);
        if (mParam1 != null) {
            filtrarBusquedaList(mParam1);
            textSearch.setText(mParam1);
        }
        textChanged();
        closerListener();
        AsynckPLatilolosNames asynckPLatilolosNames = new AsynckPLatilolosNames();
        asynckPLatilolosNames.execute();
        return viewGlobal;
    }

    private void closerListener() {
        closer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new RestaurantePorCategoria());
            }
        });
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

    public void filtrarBusquedaList(String text) {
        List<Restaurante> lista = new ArrayList<>();
        List<PlatillosNames> list = new ArrayList<>();
        if (!"".equals(text) && GlobalRestaurantes.restaurantes != null) {
            for (Restaurante restaurant : GlobalRestaurantes.restaurantes) {
                if (restaurant.getNombreRestaurante().toUpperCase().contains(text.toUpperCase())) {
                    lista.add(restaurant);
                }
            }
            lista = filtrarRestaurantes(lista);
            if (!lista.isEmpty()) {
                verRestaurantesAbiertos(lista);
            }
            list = filtrarPlatillos(text);
            if (!list.isEmpty()) {
                BusquedaPlatillosViewAdapter busquedaRestaurantesViewAdapter = new BusquedaPlatillosViewAdapter(list, viewGlobal.getContext(), getActivity());
                recyclerView1.setLayoutManager(new LinearLayoutManager(viewGlobal.getContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView1.setAdapter(busquedaRestaurantesViewAdapter);
            }
        }


    }

    public List<PlatillosNames> filtrarPlatillos(String platilloTxt) {
        List<PlatillosNames> list = new ArrayList<>();
        if (platillosNamesGlobal != null && !platillosNamesGlobal.isEmpty()) {
            List<Integer> integers = new ArrayList<>();
            for (PlatillosNames platillo : platillosNamesGlobal) {
                try {
                    if (!integers.contains(platillo.getIdPlatillo().intValue()) &&
                            platillo.getNombre().toUpperCase().contains(platilloTxt.toUpperCase())) {
                        list.add(platillo);
                        integers.add(platillo.getIdPlatillo().intValue());
                    }
                } catch (Exception ignore) {
                }
            }
        }
        return list;
    }

    public List<Restaurante> filtrarRestaurantes(List<Restaurante> restauranteList) {
        List<Restaurante> list = new ArrayList<>();
        if (!restauranteList.isEmpty()) {
            List<Integer> integers = new ArrayList<>();
            for (Restaurante restaurante : restauranteList) {
                if (restaurante.getDisponible()) {
                    if (!integers.contains(restaurante.getRestauranteId().intValue())) {
                        list.add(restaurante);
                        integers.add(restaurante.getRestauranteId().intValue());
                    }
                }
            }
        }
        return list;
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
                BusquedaRestaurantesViewAdapter busquedaRestaurantesViewAdapter = new BusquedaRestaurantesViewAdapter(listaFiltrada, viewGlobal.getContext(), getActivity());
                recyclerView.setLayoutManager(new LinearLayoutManager(viewGlobal.getContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(busquedaRestaurantesViewAdapter);
            }
        } catch (Exception e) {
            System.out.println("error consultar restaurantes abiertos " + e);
        }
    }

    private class AsynckPLatilolosNames extends AsyncTask<String, String, List<PlatillosNames>> {

        @Override
        protected List<PlatillosNames> doInBackground(String... strings) {
            List<PlatillosNames> platillosNames = new ArrayList<>();
            try {
                if (GlobalRestaurantes.platillosNamesList == null || GlobalRestaurantes.platillosNamesList.isEmpty()) {
                    PlatilloService platilloService = new PlatilloService();
                    platillosNames = platilloService.platilloNombres();
                } else {
                    platillosNames = GlobalRestaurantes.platillosNamesList;
                }
            } catch (Exception ignore) {
            }
            return platillosNames;
        }

        @Override
        protected void onPostExecute(List<PlatillosNames> platillosNames) {
            super.onPostExecute(platillosNames);
            if (!platillosNames.isEmpty()) {
                platillosNamesGlobal = platillosNames;
                GlobalRestaurantes.platillosNamesList = platillosNames;
                if (mParam1 != null) {
                    filtrarBusquedaList(mParam1);
                }
            }
        }
    }

    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}