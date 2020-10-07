package com.fm.modules.app.restaurantes;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.fm.modules.R;
import com.fm.modules.adapters.SectionsPagerAdapter;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.app.commons.utils.Generics;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.menu.MenuBotton;
import com.fm.modules.models.Image;
import com.fm.modules.models.Menu;
import com.fm.modules.models.OpcionesDeSubMenu;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.Restaurante;
import com.fm.modules.models.SubMenu;
import com.fm.modules.service.OpcionesDeSubMenuService;
import com.fm.modules.service.PlatilloService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RestauranteMenuActivity extends FragmentActivity {

    private TabLayout menuTab;
    private ViewPager viewPager;
    private AppCompatImageView imagenLogo;
    private BottomNavigationView bottomNavigationView;
    private AppCompatImageView back;
    private AppCompatTextView restauranteName;

    //private View viewGlobal;

    /*@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_restaurant_menu, container, false);
        viewGlobal = view;
        listView = (ListView) view.findViewById(R.id.lvMenus1);
        imagenLogo = (AppCompatImageView) view.findViewById(R.id.ivRestaurantLogoMenu);
        return view;
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.frg_restaurant_menu);
        imagenLogo = (AppCompatImageView) findViewById(R.id.ivRestaurantLogoMenu);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view_for_menus_restaurantes);
        back = (AppCompatImageView) findViewById(R.id.ivBack);
        restauranteName = (AppCompatTextView) findViewById(R.id.tvRestaurantName);
        viewPager = findViewById(R.id.lvMenus1);
        menuTab = findViewById(R.id.menuTab);
        backListener();
        if (isNetActive()) {
            cargarDatos();
        }
        navBottonControl();

    }

    private void navBottonControl() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.mmenuHome) {
                    Intent intent = new Intent(RestauranteMenuActivity.this, MenuBotton.class);
                    startActivity(intent);
                }
                if (item.getItemId() == R.id.mmenuShoppingCart) {
                    GlobalCarrito.toShopinCart = true;
                    Intent intent = new Intent(RestauranteMenuActivity.this, MenuBotton.class);
                    startActivity(intent);
                }
                if (item.getItemId() == R.id.mmenuOptions) {
                    GlobalCarrito.toMenu = true;
                    Intent intent = new Intent(RestauranteMenuActivity.this, MenuBotton.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    private void verElementos(List<Menu> menus) {
        System.out.println("********* colocando elementos " + menus.size());
        List<Fragment> tabs = new ArrayList<>();
        if (!menus.isEmpty()) {
            for (Menu m : menus) {
                tabs.add(new PlatillosActivity2(m));
            }
            if (!tabs.isEmpty()) {
                SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(menus, tabs, getSupportFragmentManager());
                viewPager.setAdapter(sectionsPagerAdapter);
                menuTab.setupWithViewPager(viewPager, true);
            }
        }
    }

    private void cargarDatos() {
        CategoraisDeRestaurante categoraisDeRestaurante = new CategoraisDeRestaurante();
        Restaurante restaurante = Logued.restauranteActual;
        if (restaurante != null) {
            restauranteName.setText(restaurante.getNombreRestaurante());
        }
        final Date anteriorDate = GlobalRestaurantes.horaActualizado;
        Date actualDate = Generics.getHour(new Date());
        if (anteriorDate == null) {
            categoraisDeRestaurante.execute();
        } else {
            if (anteriorDate.getTime() < actualDate.getTime()) {
                GlobalRestaurantes.horaActualizado = actualDate;
                categoraisDeRestaurante.execute();
            } else {
                List<Menu> menus = GlobalRestaurantes.menuList;
                int idRestaurante = 0;
                if (restaurante != null) {
                    idRestaurante = restaurante.getRestauranteId().intValue();
                }
                if (menus != null && !menus.isEmpty()) {
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
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        verLogo();
    }

    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestauranteMenuActivity.this, MenuBotton.class);
                startActivity(intent);
            }
        });
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

    public void verLogo() {
        Image image = null;
        List<Integer> integers = Logued.imagenesIDs;
        Restaurante res = GlobalRestaurantes.restauranteSelected;
        if (res != null) {
            if (integers != null && !integers.isEmpty()) {
                for (int i = 0; i < integers.size(); i++) {
                    if (res.getLogoDeRestaurante().intValue() == integers.get(i)) {
                        image = Logued.imagenes.get(i);
                    }
                }
            }
        }
        if (image != null) {
            //Utilities.displayAppCompatImageFromBytea(image.getContent(), imagenLogo, viewGlobal.getContext());
            Utilities.displayAppCompatImageFromBytea(image.getContent(), imagenLogo, RestauranteMenuActivity.this);
        } else {
            //Utilities.displayAppCompatImageFromBytea(null, imagenLogo, viewGlobal.getContext());
            Utilities.displayAppCompatImageFromBytea(null, imagenLogo, RestauranteMenuActivity.this);
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
