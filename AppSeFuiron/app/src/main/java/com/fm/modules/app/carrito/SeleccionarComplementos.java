package com.fm.modules.app.carrito;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.RecyclerSubMenuAdapter2;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.app.restaurantes.MenuDeRestauranteFragment;
import com.fm.modules.models.Driver;
import com.fm.modules.models.Image;
import com.fm.modules.models.OpcionesDeSubMenu;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.PlatilloFavorito;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.models.SubMenu;
import com.fm.modules.service.ImageService;
import com.fm.modules.service.PlatilloFavoritoService;
import com.fm.modules.sqlite.models.OpcionesDeSubMenuSeleccionadoSQLite;
import com.fm.modules.sqlite.models.PedidoSQLite;
import com.fm.modules.sqlite.models.PlatillosSeleccionadoSQLite;
import com.google.android.material.button.MaterialButton;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SeleccionarComplementos extends Fragment {

    private RecyclerView rvComplementsArea;
    private NumberPicker numberPicker;
    private AppCompatTextView tvFoodName, tvFoodPrice;
    private AppCompatImageView appCompatImageView;

    private List<OpcionesDeSubMenu> opcionesDeSubMenusGlobal;
    private List<SubMenu> subMenusGlobal;
    private List<Platillo> platillosGlobal;
    private MaterialButton btnAddCarrito;
    private Platillo platilloActual;
    private CargarImagen cargarImagen = new CargarImagen();
    private View viewGlobal;
    private AppCompatImageView back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_food_complements, container, false);
        viewGlobal = view;
        rvComplementsArea = (RecyclerView) view.findViewById(R.id.rvComplementsArea);
        numberPicker = (NumberPicker) view.findViewById(R.id.npFoodQuantity);
        tvFoodName = (AppCompatTextView) view.findViewById(R.id.tvFoodName);
        tvFoodPrice = (AppCompatTextView) view.findViewById(R.id.tvFoodPrice);
        appCompatImageView = (AppCompatImageView) view.findViewById(R.id.ivFoodImageAdicionales);
        btnAddCarrito = (MaterialButton) view.findViewById(R.id.btnAddToShoppingCart);
        back = (AppCompatImageView) view.findViewById(R.id.ivBack);
        opcionesDeSubMenusGlobal = new ArrayList<>();
        subMenusGlobal = new ArrayList<>();
        platillosGlobal = new ArrayList<>();
        mostrarPlatillo();
        Platillo p = GlobalRestaurantes.platilloSeleccionado;
        int idPlatillo = 0;
        if (p != null) {
            idPlatillo = p.getPlatilloId().intValue();
            mostrarComplementos(idPlatillo);
        }
        agregarAlCarritoListener();
        cargarImg();
        backListener();
        onBack();
        return view;
    }

    private void backListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new MenuDeRestauranteFragment());
            }
        });
    }

    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                showFragment(new MenuDeRestauranteFragment());
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarImg();
    }

    private void cargarImg() {
        Platillo platillo = GlobalRestaurantes.platilloSeleccionado;
        cargarImagen.cancel(true);
        cargarImagen = new CargarImagen();
        if (platillo != null) {
            cargarImagen.execute(platillo.getImagen());
        }
    }

    private void agregarAlCarritoListener() {
        btnAddCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                PlatilloSeleccionado plas = registrarPlatillo();
                if (plas != null) {
                    final boolean[] ars = {false};
                    AlertDialog dialog = new AlertDialog.Builder(viewGlobal.getContext())
                            .setView(R.layout.dialog_save_plat_fav)
                            .setCancelable(true)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    guardarPlatilloFavorito();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showFragment(new CarritoActivity());
                                }
                            })
                            .show();
                } else {
                    Toast.makeText(viewGlobal.getContext(), "No Pudo :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void guardarPlatilloFavorito() {
        GuardarFavorito guardarFavorito = new GuardarFavorito();
        guardarFavorito.execute();
    }

    private class GuardarFavorito extends AsyncTask<Long, String, Boolean> {

        @Override
        protected Boolean doInBackground(Long... longs) {
            try {
                PlatilloFavoritoService platilloFavoritoService = new PlatilloFavoritoService();
                PlatilloFavorito platilloFavorito = new PlatilloFavorito();
                platilloFavorito.setPlatillo(platilloActual);
                if (Logued.usuarioLogued != null) {
                    platilloFavorito.setUsuarios(Logued.usuarioLogued);
                    platilloFavoritoService.crearPlatilloFavorito(platilloFavorito);
                }
            } catch (Exception e) {
                System.out.println("*** error asynk imagePerfil: " + e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            showFragment(new CarritoActivity());
        }
    }

    public PlatilloSeleccionado registrarPlatillo() {
        /*
         * en caso se agregue cantidad a la tabla platillo seleccionado
         * se usara el codigo que esta como comentario en este metodo
         * */
        final int cantidad = numberPicker.getValue();
        Pedido pedido = registrarPedido();
        System.out.println("*********************************************************************");
        System.out.println("pedido registrado :D");
        System.out.println("*********************************************************************");
        PlatilloSeleccionado platilloSeleccionado = new PlatilloSeleccionado();
        platilloSeleccionado.setPlatilloSeleccionadoId(platilloActual.getPlatilloId());
        System.out.println("platillo actual en platillo seleccionado");
        platilloSeleccionado.setPlatillo(platilloActual);
        platilloSeleccionado.setPedido(pedido);
        platilloSeleccionado.setNombre(platilloActual.getNombre());
        platilloSeleccionado.setCantidad(cantidad);
        platilloSeleccionado.setPrecio(platilloActual.getPrecioBase() * cantidad);
        PlatillosSeleccionadoSQLite platillosSeleccionadoSQLite = new PlatillosSeleccionadoSQLite(viewGlobal.getContext());
        Long idd = platillosSeleccionadoSQLite.create(platilloSeleccionado);
        platilloSeleccionado.setPlatilloSeleccionadoId(idd);
        List<PlatilloSeleccionado> list = Logued.platillosSeleccionadosActuales;
        if (list == null) {
            list = new ArrayList<>();
        }
        List<OpcionesDeSubMenu> opcionesSeleccionadas = GlobalRestaurantes.opcionesDeSubMenusSeleccionados;
        if (opcionesSeleccionadas == null) {
            opcionesSeleccionadas = new ArrayList<>();
        }
        if (!opcionesSeleccionadas.isEmpty()) {
            platilloSeleccionado = registrarOpcionesSeleccionadas(platilloSeleccionado);
        }
        list.add(platilloSeleccionado);
        Logued.platillosSeleccionadosActuales = list;
        System.out.println("*********************************************************************");
        System.out.println("platillo seleccionado registrado :D");
        System.out.println("*********************************************************************");
        return platilloSeleccionado;
    }

    public PlatilloSeleccionado registrarOpcionesSeleccionadas(PlatilloSeleccionado platilloSeleccionado) {
        final List<OpcionesDeSubMenu> listOpcionesExtra = GlobalRestaurantes.opcionesDeSubMenusSeleccionados;
        List<OpcionesDeSubMenuSeleccionado> opcionesSeleccionadas = Logued.opcionesDeSubMenusEnPlatillosSeleccionados;
        if (opcionesSeleccionadas == null) {
            opcionesSeleccionadas = new ArrayList<>();
        }
        /*
         * en caso se agregue cantidad a la tabla platillo seleccionado
         * se usara el codigo que esta como comentario en este metodo
         * */
        final int cantidad = numberPicker.getValue();
        double adicional = 0;
        for (OpcionesDeSubMenu opcione : listOpcionesExtra) {
            OpcionesDeSubMenuSeleccionado op = new OpcionesDeSubMenuSeleccionado();
            op.setOpcionesDeSubMenuSeleccionadoId(opcione.getOpcionesDeSubmenuId());
            op.setOpcionesDeSubMenu(opcione);
            op.setPlatilloSeleccionado(platilloSeleccionado);
            op.setNombre(opcione.getNombre());
            OpcionesDeSubMenuSeleccionadoSQLite opcionesDeSubMenuSeleccionadoSQLite = new OpcionesDeSubMenuSeleccionadoSQLite(viewGlobal.getContext());
            Long opid = opcionesDeSubMenuSeleccionadoSQLite.create(op);
            op.setOpcionesDeSubMenuSeleccionadoId(opid);
            opcionesSeleccionadas.add(op);
            adicional = adicional + (opcione.getPrecio() * cantidad);
            System.out.println("*********************************************************************");
            System.out.println("opcion de sub menu seleccioado registrado :D");
            System.out.println("*********************************************************************");
        }
        Logued.opcionesDeSubMenusEnPlatillosSeleccionados = opcionesSeleccionadas;
        double precioActual = platilloSeleccionado.getPrecio();
        platilloSeleccionado.setPrecio(precioActual + adicional);
        return platilloSeleccionado;
    }

    public Pedido registrarPedido() {
        Pedido pedido = Logued.pedidoActual;
        PedidoSQLite pedidoSQLite = new PedidoSQLite(viewGlobal.getContext());
        if (pedido == null) {
            pedido = new Pedido();
            pedido.setPedidoId(platilloActual.getMenu().getRestaurante().getRestauranteId());
            pedido.setRestaurante(platilloActual.getMenu().getRestaurante());
            pedido.setUsuario(Logued.usuarioLogued);
            Driver driver = new Driver();
            driver.setDriverId(0L);
            pedido.setDrivers(driver);
            pedido.setStatus(1);
            pedido.setFormaDePago("Efectivo");
            pedido.setTotalDePedido(0);
            pedido.setTotalEnRestautante(0);
            pedido.setTotalDeCargosExtra(0);
            pedido.setTotalEnRestautanteSinComision(0);
            pedido.setPedidoPagado(false);
            pedido.setFechaOrdenado(new Date());
            pedido.setTiempoPromedioEntrega(platilloActual.getMenu().getRestaurante().getTiempoEstimadoDeEntrega());
            pedido.setPedidoEntregado(false);
            pedido.setNotas("no confirmado");
            pedido.setTiempoAdicional("00:00:00");
            pedido.setDireccion("no direction");
            Logued.pedidoActual = pedido;
        }
        return pedido;
    }

    public Date getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR);
        calendar.set(year, month, day, hour, 0, 0);
        return calendar.getTime();
    }

    private void mostrarPlatillo() {
        Platillo platillo = GlobalRestaurantes.platilloSeleccionado;
        if (platillo != null) {
            final DecimalFormat decimalFormat = new DecimalFormat("$ #,##0.00");
            platilloActual = platillo;
            final Platillo finalPlatillo = platillo;
            tvFoodName.setText(finalPlatillo.getNombre());
            tvFoodPrice.setText(decimalFormat.format(finalPlatillo.getPrecioBase()));
            numberPicker.setValueChangedListener(new ValueChangedListener() {
                @Override
                public void valueChanged(int value, ActionEnum action) {
                    double total;
                    total = finalPlatillo.getPrecioBase() * value;
                    tvFoodPrice.setText(decimalFormat.format(total));
                }
            });
        }
    }


    private void mostrarComplementos(int idPlatillo) {
        if (!GlobalRestaurantes.opcionesDeSubMenuList.isEmpty()) {
            List<OpcionesDeSubMenu> opcionesDeSubMenus = new ArrayList<>();
            List<SubMenu> subMenus = new ArrayList<>();
            for (OpcionesDeSubMenu op : GlobalRestaurantes.opcionesDeSubMenuList) {
                if (op.getSubMenu().getPlatillo().getPlatilloId().intValue() == idPlatillo) {
                    opcionesDeSubMenus.add(op);
                }
            }
            List<Integer> integers = new ArrayList<>();
            for (OpcionesDeSubMenu op : opcionesDeSubMenus) {
                if (!integers.contains(op.getSubMenu().getSubMenuId().intValue())) {
                    subMenus.add(op.getSubMenu());
                    integers.add(op.getSubMenu().getSubMenuId().intValue());
                }
            }
            if (!opcionesDeSubMenus.isEmpty()) {
                GlobalRestaurantes.opcionesDeSubMenusSeleccionados = new ArrayList<>();
            }
            RecyclerSubMenuAdapter2 recyclerSubMenuAdapter = new RecyclerSubMenuAdapter2(subMenus, opcionesDeSubMenus, viewGlobal.getContext());
            rvComplementsArea.setLayoutManager(new LinearLayoutManager(viewGlobal.getContext(), LinearLayoutManager.VERTICAL, false));
            rvComplementsArea.setAdapter(recyclerSubMenuAdapter);
        }
    }

    private class CargarImagen extends AsyncTask<Long, String, Image> {

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
                Utilities.displayAppCompatImageFoodFromBytea(image.getContent(), appCompatImageView, viewGlobal.getContext());
                System.out.println("asynk display image ! !!!!!!!!!!!!!!!!");
            }
        }
    }

    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
