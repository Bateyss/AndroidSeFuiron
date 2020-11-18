package com.fm.modules.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.carrito.SeleccionarComplementos;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.models.Menu;
import com.fm.modules.models.OpcionesDeSubMenu;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.PlatillosNames;
import com.fm.modules.models.SubMenu;
import com.fm.modules.service.OpcionesDeSubMenuService;
import com.fm.modules.service.PlatilloService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.fm.modules.app.restaurantes.RestauranteMenuActivity;

public class BusquedaPlatillosViewAdapter extends RecyclerView.Adapter<BusquedaPlatillosViewAdapter.HolderItemCategorias> {


    private Context context;
    private List<PlatillosNames> platillos;
    private FragmentActivity fragmentActivity;

    public BusquedaPlatillosViewAdapter(List<PlatillosNames> platillos, Context context, FragmentActivity fragmentActivity) {
        this.context = context;
        this.platillos = platillos;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public BusquedaPlatillosViewAdapter.HolderItemCategorias onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_busqueda_restaurantes, parent, false);
        return new BusquedaPlatillosViewAdapter.HolderItemCategorias(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderItemCategorias holder, int position) {
        if (platillos.get(position) != null && platillos.get(position).getNombre() != null && platillos.get(position).getIdPlatillo() != null) {
            holder.nombre.setText(platillos.get(position).getNombre());
            holder.idPlatillo = platillos.get(position).getIdPlatillo();
            holder.nombre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.idPlatillo != null) {
                        holder.ejecutarAsynk();
                        holder.itemView.setEnabled(false);
                    } else {
                        Toast.makeText(context, "Platillo no disponible", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            holder.itemView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return platillos.size();
    }

    public class HolderItemCategorias extends RecyclerView.ViewHolder {
        TextView nombre;
        Long idPlatillo;

        public HolderItemCategorias(View view) {
            super(view);
            nombre = view.findViewById(R.id.nombre);
        }

        public void ejecutarAsynk() {
            AsynckPLatilolosNames asynckPLatilolosNames = new AsynckPLatilolosNames();
            asynckPLatilolosNames.execute();
        }

        private class AsynckPLatilolosNames extends AsyncTask<String, String, Platillo> {

            @Override
            protected Platillo doInBackground(String... strings) {
                Platillo platillo = null;
                try {
                    PlatilloService platilloService = new PlatilloService();
                    platillo = platilloService.obtenerPlatilloPorId(idPlatillo);
                } catch (Exception ignore) {
                }
                return platillo;
            }

            @Override
            protected void onPostExecute(Platillo platillo) {
                super.onPostExecute(platillo);
                boolean disponible = false;
                try {
                    if (platillo != null && platillo.getDisponible()) {
                        if (platillo.getMenu().getRestaurante().getDisponible()) {
                            SimpleDateFormat sp = new SimpleDateFormat("HH:mm:ss");
                            Date actualDate = new Date();
                            String actualHuorString = sp.format(actualDate);
                            Date actualHour = sp.parse(actualHuorString);
                            Date restaurantCloseHour = sp.parse(platillo.getMenu().getRestaurante().getHorarioDeCierre());
                            Date restaurantOpenHour = sp.parse(platillo.getMenu().getRestaurante().getHorarioDeApertura());
                            if (restaurantCloseHour.getTime() < actualHour.getTime() || actualHour.getTime() < restaurantOpenHour.getTime()) {
                                Toast.makeText(context, "Restaurante Cerrado", Toast.LENGTH_SHORT).show();
                            } else {
                                Logued.restauranteActual = platillo.getMenu().getRestaurante();
                                GlobalRestaurantes.platillo = platillo;
                                GlobalRestaurantes.platilloSeleccionado = platillo;
                                disponible = true;
                            }
                        } else {
                            Toast.makeText(context, "Restaurante no disponible", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Platillo no disponible", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception ignore) {
                    Toast.makeText(context, "Platillo no disponible", Toast.LENGTH_SHORT).show();
                }
                if (disponible) {
                    CategoraisDeRestaurante categoraisDeRestaurante = new CategoraisDeRestaurante();
                    categoraisDeRestaurante.execute();
                }
            }
        }

        private class CategoraisDeRestaurante extends AsyncTask<String, String, List<Menu>> {


            @Override
            protected List<Menu> doInBackground(String... strings) {
                List<Menu> subM = new ArrayList<>();
                try {
                    OpcionesDeSubMenuService opcionesDeSubMenuService = new OpcionesDeSubMenuService();
                    List<OpcionesDeSubMenu> opciones = opcionesDeSubMenuService.obtenerOpcionesDeSubMenu();
                    if (!opciones.isEmpty()) {
                        List<SubMenu> subMenus = new ArrayList<>();
                        List<Integer> ints = new ArrayList<>();
                        for (OpcionesDeSubMenu op : opciones) {
                            try {
                                if (!ints.contains(op.getSubMenu().getSubMenuId().intValue())) {
                                    subMenus.add(op.getSubMenu());
                                    ints.add(op.getSubMenu().getSubMenuId().intValue());
                                }
                            } catch (Exception ignore) {
                            }
                        }
                        GlobalRestaurantes.opcionesDeSubMenuList = opciones;
                        if (!subMenus.isEmpty()) {
                            GlobalRestaurantes.subMenuList = subMenus;
                        }
                    } else {
                        GlobalRestaurantes.opcionesDeSubMenuList = new ArrayList<>();
                        GlobalRestaurantes.subMenuList = new ArrayList<>();
                    }
                    PlatilloService platilloService = new PlatilloService();
                    List<Platillo> platillos = platilloService.obtenerPlatillos();
                    if (!platillos.isEmpty()) {
                        GlobalRestaurantes.platilloList = platillos;
                        List<Menu> menus = new ArrayList<>();
                        List<Integer> ints = new ArrayList<>();
                        for (Platillo pa : platillos) {
                            try {
                                if (!ints.contains(pa.getMenu().getMenuId().intValue())) {
                                    menus.add(pa.getMenu());
                                    ints.add(pa.getMenu().getMenuId().intValue());
                                }
                            } catch (Exception ignore) {
                            }
                        }
                        if (!menus.isEmpty()) {
                            GlobalRestaurantes.menuList = menus;
                            subM = menus;
                        }
                    } else {
                        GlobalRestaurantes.platilloList = new ArrayList<>();
                        GlobalRestaurantes.menuList = new ArrayList<>();
                    }

                } catch (Exception e) {
                    System.out.println("Error en UnderThreash:" + e.getMessage() + " " + e.getClass());
                }
                return subM;
            }

            @Override
            protected void onPostExecute(List<Menu> menus) {
                super.onPostExecute(menus);
                showFragment(new SeleccionarComplementos());
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }
        }
    }

    private void showFragment(Fragment fragment) {
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}

