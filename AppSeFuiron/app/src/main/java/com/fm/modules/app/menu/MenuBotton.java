package com.fm.modules.app.menu;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fm.modules.R;
import com.fm.modules.app.carrito.CarritoActivity;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.app.carrito.PagoActivity;
import com.fm.modules.app.carrito.SeleccionarComplementos;
import com.fm.modules.app.restaurantes.RestaurantePorCategoria;
import com.fm.modules.app.usuario.GlobalUsuario;
import com.fm.modules.models.Promociones;
import com.fm.modules.service.Constantes;
import com.fm.modules.service.PromocionesService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

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
            GlobalCarrito.toShopinCart = false;
            showFragment(new CarritoActivity());
        }
        boolean openSales = GlobalCarrito.toSales;
        if (openSales) {
            GlobalCarrito.toSales = false;
            showFragment(new PagoActivity());
        }
        boolean openMenu = GlobalCarrito.toMenu;
        if (openMenu) {
            GlobalCarrito.toMenu = false;
            showFragment(new OptionsFragment());
        }
        boolean openComplementos = GlobalCarrito.toComplementos;
        if (openComplementos) {
            GlobalCarrito.toComplementos = false;
            showFragment(new SeleccionarComplementos());
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
        contarPromociones();
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

    public void contarPromociones() {
        volleyMethod();
    }

    public void volleyMethod() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constantes.URL_PROMOCION.concat("/countDay");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("!!!!!!!!! dess response: " + response);
                        if (response != null) {
                            try {
                                int respuesta = Integer.parseInt(response);
                                if (GlobalUsuario.promociones == null && respuesta > 0) {
                                    GlobalUsuario.promociones = respuesta;
                                    promos();
                                }
                                if (GlobalUsuario.promociones != null && GlobalUsuario.promociones > 0 && respuesta > GlobalUsuario.promociones) {
                                    GlobalUsuario.promociones = respuesta;
                                    promos();
                                }
                            } catch (Exception ignore) {
                            }
                        }
                        contarPromociones();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //none
                contarPromociones();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
        getAvailableMemory();
    }


    private void getAvailableMemory() {
        try {
            ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            if (memoryInfo.lowMemory) {
                Toast.makeText(this, "Memoria Baja", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "limpiando..", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MenuBotton.this, MenuBotton.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } catch (Exception ignore) {
        }
    }

    public void promos() {
        CargarPromos cargarPromos = new CargarPromos();
        cargarPromos.execute();
    }

    private class CargarPromos extends AsyncTask<Long, String, List<Promociones>> {

        @Override
        protected List<Promociones> doInBackground(Long... longs) {
            List<Promociones> promociones = new ArrayList<>();
            try {
                PromocionesService promocionesService = new PromocionesService();
                promociones = promocionesService.obtenerPromocionesDeHoy();
            } catch (Exception e) {
                System.out.println("*** error asynk imagePerfil: " + e);
            }
            return promociones;
        }

        @Override
        protected void onPostExecute(List<Promociones> promociones) {
            super.onPostExecute(promociones);
            if (!promociones.isEmpty()) {
                int x = 0;
                for (Promociones promocion : promociones) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        CharSequence name = "Notification"+ x;
                        NotificationChannel channel = new NotificationChannel("NOTIFICACION" + x, name, NotificationManager.IMPORTANCE_DEFAULT);
                        NotificationManager notificationManager = (NotificationManager) MenuBotton.this.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.createNotificationChannel(channel);
                    }
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MenuBotton.this, "NOTIFICACION" + x);
                    builder.setSmallIcon(R.drawable.ic_app_logo);
                    if (promocion.getMensaje() != null) {
                        builder.setContentText(promocion.getMensaje() + "\n codigo: " + promocion.getCodigo());
                        builder.setContentTitle("Promos Fuimonos" + promocion.getMensaje());
                    } else {
                        builder.setContentTitle("Promos Fuimonos");
                    }
                    if (promocion.getCodigo() != null) {
                        builder.setContentText("codigo: " + promocion.getCodigo());
                    } else {
                        builder.setContentText("revisa tu correo");
                    }
                    builder.setColor(Color.BLUE);
                    builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    builder.setLights(Color.MAGENTA, 1000, 1000);
                    builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                    builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MenuBotton.this);
                    notificationManagerCompat.notify(x, builder.build());
                    x++;
                }
            }
        }
    }
}