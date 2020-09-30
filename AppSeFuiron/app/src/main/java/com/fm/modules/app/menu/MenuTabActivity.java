package com.fm.modules.app.menu;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.fm.modules.R;
import com.fm.modules.adapters.SectionsPagerAdapter;
import com.fm.modules.app.commons.utils.Generics;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.app.restaurantes.PlatillosActivity;
import com.fm.modules.models.Menu;
import com.fm.modules.models.OpcionesDeSubMenu;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.Restaurante;
import com.fm.modules.models.SubMenu;
import com.fm.modules.service.OpcionesDeSubMenuService;
import com.fm.modules.service.PlatilloService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MenuTabActivity extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabsRV;
    private View viewGlobal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_menu_tab, container, false);
        viewGlobal = view;
        viewPager = view.findViewById(R.id.view_pager);
        tabsRV = view.findViewById(R.id.tabs);
        cargarDatos();
        return view;
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
            Log.e("error", "" + "error al comprobar conexion");
            Log.e("error", "" + e);
            c = false;
        }
        return c;
    }

    private void cargarDatos() {
        CategoraisDeRestaurante categoraisDeRestaurante = new CategoraisDeRestaurante();
        final Date anteriorDate = GlobalRestaurantes.horaActualizado;
        Date actualDate = Generics.getHour(new Date());
        if (anteriorDate == null) {
            categoraisDeRestaurante.execute();
        } else {
            if (anteriorDate.getTime() < actualDate.getTime()) {
                GlobalRestaurantes.horaActualizado = actualDate;
                categoraisDeRestaurante.execute();
            } else {
                List<com.fm.modules.models.Menu> menus = GlobalRestaurantes.menuList;
                Restaurante restaurante = Logued.restauranteActual;
                int idRestaurante = 0;
                if (restaurante != null) {
                    idRestaurante = restaurante.getRestauranteId().intValue();
                }
                if (menus != null && !menus.isEmpty()) {
                    List<com.fm.modules.models.Menu> menuList = new ArrayList<>();
                    for (com.fm.modules.models.Menu m : menus) {
                        if (m.getRestaurante().getRestauranteId().intValue() == idRestaurante) {
                            if (!menuList.contains(m)) {
                                menuList.add(m);
                            }
                        }
                    }
                    verElementos(menuList);
                }
            }
        }
    }

    private void verElementos(List<Menu> menus) {
        System.out.println("********* colocando elementos " + menus.size());
        List<Fragment> tabs = new ArrayList<>();
        if (!menus.isEmpty()) {
            for (Menu m : menus) {
                tabs.add(new PlatillosActivity(m));
            }
            if (!tabs.isEmpty()) {
                SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(menus, tabs, getActivity().getSupportFragmentManager());
                viewPager.setAdapter(sectionsPagerAdapter);
                tabsRV.setupWithViewPager(viewPager, true);
            }
        }
    }

    private class CategoraisDeRestaurante extends AsyncTask<String, String, List<Menu>> {


        @Override
        protected List<com.fm.modules.models.Menu> doInBackground(String... strings) {
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
            Restaurante restaurante = Logued.restauranteActual;
            int idRestaurante = 0;
            if (restaurante != null) {
                idRestaurante = restaurante.getRestauranteId().intValue();
            }
            try {
                if (!menus.isEmpty()) {
                    List<Menu> menuList = new ArrayList<>();
                    for (Menu m : menus) {
                        if (m.getRestaurante().getRestauranteId().intValue() == idRestaurante) {
                            if (!menuList.contains(m)) {
                                menuList.add(m);
                            }
                        }
                    }
                    verElementos(menuList);
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