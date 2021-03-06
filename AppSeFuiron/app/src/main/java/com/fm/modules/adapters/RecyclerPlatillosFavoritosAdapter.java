package com.fm.modules.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.carrito.SeleccionarComplementos;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.models.Image;
import com.fm.modules.models.Menu;
import com.fm.modules.models.OpcionesDeSubMenu;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.PlatilloFavorito;
import com.fm.modules.models.Restaurante;
import com.fm.modules.models.SubMenu;
import com.fm.modules.service.ImageService;
import com.fm.modules.service.OpcionesDeSubMenuService;
import com.fm.modules.service.PlatilloService;
import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerPlatillosFavoritosAdapter extends RecyclerView.Adapter<RecyclerPlatillosFavoritosAdapter.ViewHolder> {

    private List<PlatilloFavorito> items;
    private Context context;
    private FragmentActivity fragmentActivity;

    public RecyclerPlatillosFavoritosAdapter(List<PlatilloFavorito> platillosFavorito, Context context, FragmentActivity fragmentActivity) {
        this.items = platillosFavorito;
        this.context = context;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public RecyclerPlatillosFavoritosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item_food, parent, false);
        return new RecyclerPlatillosFavoritosAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerPlatillosFavoritosAdapter.ViewHolder holder, int position) {
        holder.asignarDatos(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ivFoodImage;
        TextView tvFoodName;
        TextView tvFoodDescription;
        TextView tvFoodPrice;
        AppCompatTextView btnAdd;
        MaterialCardView cardView;

        public ViewHolder(View view) {
            super(view);
            ivFoodImage = view.findViewById(R.id.ivFoodImage);
            tvFoodName = view.findViewById(R.id.tvFoodName);
            tvFoodDescription = view.findViewById(R.id.tvFoodDescription);
            tvFoodPrice = view.findViewById(R.id.tvFoodPrice);
            btnAdd = view.findViewById(R.id.btnAddPlatillo);
            cardView = view.findViewById(R.id.cardFood);
        }

        public void asignarDatos(final PlatilloFavorito platillosFavorito) {
            DecimalFormat decimalFormat = new DecimalFormat("$ #,##0.00");
            ivFoodImage.setImageResource(R.drawable.not_found);
            tvFoodName.setText(String.valueOf(platillosFavorito.getPlatillo().getNombre()));
            tvFoodDescription.setText(platillosFavorito.getPlatillo().getDescripcion());
            tvFoodPrice.setText(decimalFormat.format(platillosFavorito.getPlatillo().getPrecioBase()));
            //btnAdd.setText(platillo.getOrden());
            btnAdd.setVisibility(View.INVISIBLE);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cardView.setEnabled(false);
                    if (platillosFavorito.getPlatillo().getMenu().getRestaurante() != null
                            && platillosFavorito.getPlatillo().getMenu().getRestaurante().getHorarioDeApertura() != null
                            && platillosFavorito.getPlatillo().getMenu().getRestaurante().getHorarioDeCierre() != null
                            && platillosFavorito.getPlatillo().getMenu().getRestaurante().getDisponible()) {
                        try {
                            SimpleDateFormat sp = new SimpleDateFormat("HH:mm:ss");
                            Date actualDate = new Date();
                            String actualHuorString = sp.format(actualDate);
                            Date actualHour = sp.parse(actualHuorString);
                            Date restaurantCloseHour = sp.parse(platillosFavorito.getPlatillo().getMenu().getRestaurante().getHorarioDeCierre());
                            Date restaurantOpenHour = sp.parse(platillosFavorito.getPlatillo().getMenu().getRestaurante().getHorarioDeApertura());
                            if (restaurantCloseHour.getTime() < actualHour.getTime() || actualHour.getTime() < restaurantOpenHour.getTime()) {
                                Toast.makeText(context, "Restaurante Cerrado", Toast.LENGTH_SHORT).show();
                                cardView.setEnabled(true);
                                return;
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "Ocurrio un error", Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, "Selecciona otro platillo", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (Logued.restauranteActual != null) {
                            if (Logued.restauranteActual.getRestauranteId() != null && platillosFavorito.getPlatillo().getMenu().getRestaurante().getRestauranteId() != null) {
                                if (Logued.restauranteActual.getRestauranteId().intValue() != platillosFavorito.getPlatillo().getMenu().getRestaurante().getRestauranteId().intValue()) {
                                    Logued.restauranteActual = platillosFavorito.getPlatillo().getMenu().getRestaurante();
                                    Logued.platillosSeleccionadosActuales = new ArrayList<>();
                                    Logued.opcionesDeSubMenusEnPlatillosSeleccionados = new ArrayList<>();
                                } else {
                                    Logued.restauranteActual = platillosFavorito.getPlatillo().getMenu().getRestaurante();
                                }
                            } else {
                                Logued.restauranteActual = platillosFavorito.getPlatillo().getMenu().getRestaurante();
                            }
                        } else {
                            Logued.restauranteActual = platillosFavorito.getPlatillo().getMenu().getRestaurante();
                        }
                        GlobalRestaurantes.platillo = platillosFavorito.getPlatillo();
                        GlobalRestaurantes.platilloSeleccionado = platillosFavorito.getPlatillo();
                        GlobalRestaurantes.restauranteSelected = platillosFavorito.getPlatillo().getMenu().getRestaurante();
                        PlatillosDataThread platillosDataThread = new PlatillosDataThread();
                        platillosDataThread.execute();
                    }
                }
            });
            verImagen(platillosFavorito.getPlatillo().getImagen());
        }

        protected void verImagen(Long id) {
            CargarImagenp cargarImagen = new CargarImagenp();
            cargarImagen.execute(id);
        }


        private class CargarImagenp extends AsyncTask<Long, String, Image> {

            @Override
            protected Image doInBackground(Long... longs) {
                Image image = null;
                try {
                    if (Logued.imagenesIDs == null) {
                        Logued.imagenes = new ArrayList<>();
                        Logued.imagenesIDs = new ArrayList<>();
                    }
                    List<Integer> integers = Logued.imagenesIDs;
                    if (!integers.contains(longs[0].intValue())) {
                        ImageService imageService = new ImageService();
                        image = imageService.obtenerImagenPorId(longs[0]);
                        if (image != null) {
                            Logued.imagenesIDs.add(image.getId().intValue());
                            Logued.imagenes.add(image);
                        }
                    } else {
                        for (int i = 0; i < integers.size(); i++) {
                            if (integers.get(i) == longs[0].intValue()) {
                                image = Logued.imagenes.get(i);
                            }
                        }
                    }
                } catch (
                        Exception e) {
                    System.out.println("error asynk image: " + e);
                }
                return image;
            }

            @Override
            protected void onPostExecute(Image image) {
                super.onPostExecute(image);
                if (image != null) {
                    Utilities.displayAppCompatImageFoodFromBytea(image.getContent(), ivFoodImage, context);
                } else {
                    Utilities.displayAppCompatImageFoodFromBytea(null, ivFoodImage, context);
                }
            }
        }
    }

    private void showFragment(Fragment fragment) {
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public class PlatillosDataThread extends AsyncTask<String, String, List<Menu>> {


        @Override
        protected List<Menu> doInBackground(String... strings) {
            List<Menu> subM = new ArrayList<>();
            try {
                OpcionesDeSubMenuService opcionesDeSubMenuService = new OpcionesDeSubMenuService();
                List<OpcionesDeSubMenu> opciones = opcionesDeSubMenuService.obtenerOpcionesDeSubMenu();
                if (!opciones.isEmpty()) {
                    System.out.println("********* opciones cargadas ***************");
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
                System.out.println("menus encontrados: " + menus.size());
                if (!menus.isEmpty()) {
                    showFragment(new SeleccionarComplementos());
                }
            } catch (Throwable throwable) {
                System.out.println("Error Activity: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        }
    }
}