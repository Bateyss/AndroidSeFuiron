package com.fm.modules.app.localet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.fm.modules.R;
import com.fm.modules.app.carrito.GlobalCarrito;
import com.fm.modules.app.carrito.ProcesarCarritoActivity;
import com.fm.modules.app.login.Logued;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.PedidoService;
import com.fm.modules.sqlite.models.Direcciones;
import com.fm.modules.sqlite.models.DireccionesSQLite;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;

public class Location extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ListView savedPlaces;
    private PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Location lastLocation;
    private final LatLng defaulLocation = new LatLng(13.701283, -89.224245);
    private static final int DEFAULT_ZOOM = 15;
    private Button cancelar;
    private Button seleccionar;
    private List<Direcciones> direccionesGlobal;
    private MyLocations myLocations = new MyLocations();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        seleccionar = (Button) findViewById(R.id.mapBtnSeleccionar);
        cancelar = (Button) findViewById(R.id.mapBtncancelar);
        direccionesGlobal = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        savedPlaces = (ListView) findViewById(R.id.listPlacesMap);
        String apiKey = getString(R.string.google_maps_key);
        Places.initialize(getApplicationContext(), apiKey);
        placesClient = Places.createClient(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        seleccionar.setEnabled(false);
        seleccionar.setBackgroundColor(this.getResources().getColor(R.color.colorLightGray));
        actionCancelar();
        verUbicaciones();
    }

    /*@Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_location, container, false);
        seleccionar = (Button) view.findViewById(R.id.mapBtnSeleccionar);
        cancelar = (Button) view.findViewById(R.id.mapBtncancelar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        savedPlaces = (ListView) view.findViewById(R.id.listPlacesMap);
        String apiKey = getString(R.string.google_maps_key);
        Places.initialize(view.getContext(), apiKey);
        placesClient = Places.createClient(view.getContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());
        seleccionar.setEnabled(false);
        seleccionar.setBackgroundColor(this.getResources().getColor(R.color.colorLightGray));
        actionCancelar();
        return view;
    }*/

    private void verUbicaciones() {
        try {
            myLocations.execute();
        } catch (Exception e) {
            System.out.println("error ubicaciones: " + e);
        }
    }

    private void actionCancelar() {
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Location.this, ProcesarCarritoActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        configsMapa();
        onMapClick();
        onMarckClike();
        mMap.addMarker(new MarkerOptions().position(defaulLocation).title("Marker in San Salvador"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaulLocation, DEFAULT_ZOOM));
        String direccionSelected = GlobalLocation.locationSelected;
        if (direccionSelected != null) {
            String[] strings1 = {};
            try {
                strings1 = direccionSelected.split("::", 2);
            } catch (Exception ignore) {
            }
            if (strings1.length == 2) {
                try {
                    LatLng selectedCoords = new LatLng(Double.parseDouble(strings1[0]), Double.parseDouble(strings1[1]));
                    mMap.addMarker(new MarkerOptions().position(selectedCoords).title("Marker in Selected"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedCoords, DEFAULT_ZOOM));
                } catch (Exception e) {
                }
            }
        }
        Toast.makeText(Location.this, "Elegir Ubicacion o", Toast.LENGTH_SHORT).show();
        Toast.makeText(Location.this, "Seleccionar Marcador", Toast.LENGTH_SHORT).show();
        Toast.makeText(Location.this, "Para Continuar", Toast.LENGTH_SHORT).show();
        enableMyLocation();
    }

    private void onMapClick() {
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng latLng) {
                mMap.clear();
                // marcamos la posicion seleccionada
                mMap.addMarker(new MarkerOptions().position(latLng).title("new marker"));
                // movemos la camara a la posicion seleccionada
                // utilizamos el zoom que el usuario esta utilizando
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom));
                Toast.makeText(Location.this, "Seleccionar Marcador", Toast.LENGTH_SHORT).show();
                Toast.makeText(Location.this, "Para Continuar", Toast.LENGTH_SHORT).show();
                seleccionar.setEnabled(false);
                seleccionar.setBackgroundColor(Location.this.getResources().getColor(R.color.colorLightGray));
            }
        });
    }

    public void onMarckClike() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                final LatLng latLng = marker.getPosition();
                seleccionar.setEnabled(true);
                seleccionar.setBackgroundColor(Location.this.getResources().getColor(R.color.lime));
                seleccionar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Direcciones direccion = new Direcciones();
                        direccion.setIdDireccion(0);
                        direccion.setNombreDireccion("coordenada");
                        direccion.setCoordenadas(latLng.latitude + ";;" + latLng.longitude);
                        GlobalCarrito.latLngSeleccionada = latLng;
                        DireccionesSQLite direccionesSQLite = new DireccionesSQLite(Location.this);
                        direccionesSQLite.create(direccion);
                        Intent i = new Intent(Location.this, ProcesarCarritoActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                });
                return false;
            }
        });
    }

    private void configsMapa() {
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }

    private void enableMyLocation() {
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(Location.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(Location.this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        // [END maps_check_location_permission]
    }

    private class MyLocations extends AsyncTask<String, String, List<Direcciones>> {

        @Override
        protected List<Direcciones> doInBackground(String... strings) {
            List<Direcciones> direccionesList = new ArrayList<>();
            try {
                PedidoService pedidoService = new PedidoService();
                Usuario usuario = Logued.usuarioLogued;
                List<Pedido> pedidos = new ArrayList<>();
                if (usuario != null) {
                    pedidos = pedidoService.obtenerMyPedidos(usuario.getUsuarioId());
                }
                if (!pedidos.isEmpty()) {
                    Direcciones direcciones;
                    int i = 0;
                    for (Pedido pedido : pedidos) {
                        direcciones = new Direcciones();
                        direcciones.setIdDireccion(++i);
                        String[] spliter = {};
                        try {
                            spliter = pedido.getDireccion().split(";", 7);
                        } catch (Exception ignore) {
                        }
                        if (spliter.length > 3) {
                            direcciones.setCoordenadas(spliter[2]);
                            direcciones.setNombreDireccion(spliter[0]);
                            direccionesList.add(direcciones);
                        }
                    }
                    direccionesGlobal = direccionesList;
                }
            } catch (Exception e) {
                System.out.println("Error en UnderThreash:" + e.getMessage() + " " + e.getClass());
            }
            return direccionesList;
        }

        @Override
        protected void onPostExecute(List<Direcciones> direcciones) {
            super.onPostExecute(direcciones);
            try {
                if (!direcciones.isEmpty()) {
                    DireccionesViewAdapter adapter = new DireccionesViewAdapter(direcciones, Location.this, R.layout.holder_item_option);
                    savedPlaces.setAdapter(adapter);
                }
            } catch (Exception e) {
                System.out.println("Error Activity: " + e);
            }
            reiniciarMyLocations();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    public void reiniciarMyLocations() {
        myLocations.cancel(true);
        myLocations = new MyLocations();
    }
}