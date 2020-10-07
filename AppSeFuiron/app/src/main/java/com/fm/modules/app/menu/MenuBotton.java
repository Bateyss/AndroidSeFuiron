package com.fm.modules.app.menu;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.fm.modules.R;
import com.fm.modules.app.carrito.CarritoActivity;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.app.carrito.PagoActivity;
import com.fm.modules.app.carrito.SeleccionarComplementos;
import com.fm.modules.app.restaurantes.RestaurantePorCategoria;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MenuBotton extends FragmentActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_botton);
        firebaseAuth = FirebaseAuth.getInstance();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.menuHome, R.id.menuShoppingCart, R.id.menuOptions)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        showFragment(new RestaurantePorCategoria());

        boolean openShopingCart = GlobalCarrito.toShopinCart;
        if (openShopingCart) {
            showFragment(new PagoActivity());
            GlobalCarrito.toShopinCart = false;
        }
        boolean openSales = GlobalCarrito.toSales;
        if (openSales) {
            showFragment(new PagoActivity());
            GlobalCarrito.toSales = false;
        }
        boolean openMenu = GlobalCarrito.toMenu;
        if (openMenu) {
            showFragment(new OptionsFragment());
            GlobalCarrito.toMenu = false;
        }
        boolean openComplementos = GlobalCarrito.toComplementos;
        if (openComplementos) {
            showFragment(new SeleccionarComplementos());
            GlobalCarrito.toComplementos = false;
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.mmenuHome) {
                    showFragment(new RestaurantePorCategoria());
                }
                if (item.getItemId() == R.id.mmenuShoppingCart) {
                    showFragment(new CarritoActivity());
                }
                if (item.getItemId() == R.id.mmenuOptions) {
                    showFragment(new OptionsFragment());
                }
                return false;
            }
        });
        onBack();
    }

    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                showFragment(new RestaurantePorCategoria());
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

}