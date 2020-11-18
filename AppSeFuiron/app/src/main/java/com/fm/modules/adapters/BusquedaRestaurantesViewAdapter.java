package com.fm.modules.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.restaurantes.MenuDeRestauranteFragment;
import com.fm.modules.models.Restaurante;

import java.util.ArrayList;
import java.util.List;

//import com.fm.modules.app.restaurantes.RestauranteMenuActivity;

public class BusquedaRestaurantesViewAdapter extends RecyclerView.Adapter<BusquedaRestaurantesViewAdapter.HolderItemCategorias> {


    private Context context;
    private List<Restaurante> restaurantes;
    private FragmentActivity fragmentActivity;

    public BusquedaRestaurantesViewAdapter(List<Restaurante> restaurantes, Context context, FragmentActivity fragmentActivity) {
        this.context = context;
        this.restaurantes = restaurantes;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public BusquedaRestaurantesViewAdapter.HolderItemCategorias onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_busqueda_restaurantes, parent, false);
        return new BusquedaRestaurantesViewAdapter.HolderItemCategorias(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItemCategorias holder, int position) {
        if (restaurantes.get(position) != null && restaurantes.get(position).getNombreRestaurante() != null) {
            holder.nombre.setText(restaurantes.get(position).getNombreRestaurante());
            final Restaurante restaurante = restaurantes.get(position);
            holder.nombre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Restaurante rr = Logued.restauranteActual;
                    if (rr == null) {
                        Logued.restauranteActual = restaurante;
                        showFragment(new MenuDeRestauranteFragment());
                    } else {
                        if (rr.getRestauranteId().intValue() == restaurante.getRestauranteId().intValue()) {
                            showFragment(new MenuDeRestauranteFragment());
                        } else {
                            AlertDialog dialog = new AlertDialog.Builder(context)
                                    .setTitle("Cambiar de Restaurante")
                                    .setMessage("Esta apunto de cambiar de restaurante")
                                    .setCancelable(true)
                                    .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Logued.restauranteActual = restaurante;
                                            Logued.platillosSeleccionadosActuales = new ArrayList<>();
                                            Logued.opcionesDeSubMenusEnPlatillosSeleccionados = new ArrayList<>();
                                            showFragment(new MenuDeRestauranteFragment());
                                        }
                                    })
                                    .setNegativeButton("Cancelar", null)
                                    .show();
                        }
                    }
                }
            });
        } else {
            holder.nombre.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return restaurantes.size();
    }

    public class HolderItemCategorias extends RecyclerView.ViewHolder {
        TextView nombre;

        public HolderItemCategorias(View view) {
            super(view);
            nombre = view.findViewById(R.id.nombre);
        }
    }

    private void showFragment(Fragment fragment) {
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}

