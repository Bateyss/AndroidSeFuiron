package com.fm.modules.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.fm.modules.R;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.restaurantes.MenuDeRestauranteFragment;
import com.fm.modules.models.Restaurante;

import java.util.ArrayList;
import java.util.List;

public class RestauranteItemViewAdapter extends ItemViewAdapterImagen<Restaurante> {

    private int resource;
    private LayoutInflater layoutInflater;
    private Context context;
    private FragmentActivity fragmentActivity;

    public RestauranteItemViewAdapter(List<Restaurante> lista, Context context, int resource, FragmentActivity fragmentActivity) {
        super(lista, context);
        this.context = context;
        this.resource = resource;
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    public View absView(int position, View convertView, ViewGroup parent) {
        final HolderRestaurantes holder;

        try {
            if (convertView == null) {
                convertView = layoutInflater.from(context).inflate(R.layout.holder_item_restaurant, parent, false);
                holder = new HolderRestaurantes(convertView);
                convertView.setTag(holder);
                System.out.println("Holder Activo");
            } else {
                holder = (HolderRestaurantes) convertView.getTag();
                System.out.println("Holder Inactivo");
            }

            final Restaurante restaurante = (Restaurante) getItem(position);
            holder.ivOutstandingImage.setImageResource(R.drawable.sample_outstanding_image);
            holder.ivRestaurantLogo.setImageResource(R.drawable.sample_outstanding_image);
            holder.tvRestaurantName.setText(restaurante.getNombreRestaurante());
            holder.tvLabelMinimalMount.setText(restaurante.getDepartamento().getNombreDepartamento());
            holder.tvMinimalMount.setText(String.valueOf(restaurante.getDepartamento().getPais().getNombrePais()));
            holder.restauranteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Restaurante rr = Logued.restauranteActual;
                    if (rr == null) {
                        Logued.restauranteActual = restaurante;
                        showFragment(new MenuDeRestauranteFragment());
                        /*Intent i = new Intent(context, RestauranteMenuActivity.class);
                        context.startActivity(i);*/
                    } else {
                        if (rr.getRestauranteId().intValue() == restaurante.getRestauranteId().intValue()) {
                            showFragment(new MenuDeRestauranteFragment());
                            /*Intent i = new Intent(context, RestauranteMenuActivity.class);
                            context.startActivity(i);*/
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
                                            /*Intent i = new Intent(context, RestauranteMenuActivity.class);
                                            context.startActivity(i);*/
                                        }
                                    })
                                    .setNegativeButton("Cancelar", null)
                                    .show();
                        }
                    }
                }
            });
            holder.verImagen(restaurante.getImagenDePortada());
            holder.verLogo(restaurante.getLogoDeRestaurante());
        } catch (Exception e) {
            System.out.println("Error ListaAdapterRestaurante" + e);
        }
        return convertView;
    }

    private void showFragment(Fragment fragment) {
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}

