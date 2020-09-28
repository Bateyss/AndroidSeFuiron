package com.fm.modules.app.restaurantes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.fm.modules.R;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.login.Logued;
import com.fm.modules.models.Image;
import com.fm.modules.models.Restaurante;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RestauranteMenuActivity extends Fragment {

    private boolean conectec;
    private TabLayout menuTab;
    private ViewPager viewPager;
    private ListView listView;
    private AppCompatImageView imagenLogo;

    private View viewGlobal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_restaurant_menu, container, false);
        viewGlobal = view;
        listView = (ListView) view.findViewById(R.id.lvMenus);
        imagenLogo = (AppCompatImageView) view.findViewById(R.id.ivRestaurantLogoMenu);
        return view;
    }

    /*@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_restaurant_menu);
        idRestaurante = getIntent().getIntExtra("idRestaurante", 0);
        //rvFoods = findViewById(R.id.rvFoods);
        //initTab();
        listView = (ListView) findViewById(R.id.lvMenus);
        imagenLogo = (AppCompatImageView) findViewById(R.id.ivRestaurantLogoMenu);
        if (isNetActive()) {
            cargarDatos();
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
        verLogo();
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
            Utilities.displayAppCompatImageFromBytea(image.getContent(), imagenLogo, viewGlobal.getContext());
        } else {
            Utilities.displayAppCompatImageFromBytea(null, imagenLogo, viewGlobal.getContext());
        }
    }

    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
