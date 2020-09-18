package com.fm.modules.app.carrito;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.adapters.RecyclerSubMenuAdapter2;
import com.fm.modules.app.commons.utils.Utilities;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.restaurantes.GlobalRestaurantes;
import com.fm.modules.models.Driver;
import com.fm.modules.models.Image;
import com.fm.modules.models.OpcionesDeSubMenu;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.models.SubMenu;
import com.fm.modules.service.ImageService;
import com.fm.modules.sqlite.models.OpcionesDeSubMenuSeleccionadoSQLite;
import com.fm.modules.sqlite.models.PedidoSQLite;
import com.fm.modules.sqlite.models.PlatillosSeleccionadoSQLite;
import com.google.android.material.button.MaterialButton;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class SeleccionarComplementos extends AppCompatActivity {

    RecyclerView rvComplementsArea;

    int idPlatillo;
    NumberPicker numberPicker;
    AppCompatTextView tvFoodName, tvFoodPrice;
    AppCompatImageView appCompatImageView;

    List<OpcionesDeSubMenu> opcionesDeSubMenusGlobal;
    List<SubMenu> subMenusGlobal;
    List<Platillo> platillosGlobal;
    MaterialButton btnAddCarrito;
    Platillo platilloActual;
    CargarImagen cargarImagen = new CargarImagen();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_food_complements);
        rvComplementsArea = (RecyclerView) findViewById(R.id.rvComplementsArea);
        numberPicker = (NumberPicker) findViewById(R.id.npFoodQuantity);
        tvFoodName = (AppCompatTextView) findViewById(R.id.tvFoodName);
        tvFoodPrice = (AppCompatTextView) findViewById(R.id.tvFoodPrice);
        appCompatImageView = (AppCompatImageView) findViewById(R.id.ivFoodImageAdicionales);
        btnAddCarrito = (MaterialButton) findViewById(R.id.btnAddToShoppingCart);
        opcionesDeSubMenusGlobal = new ArrayList<>();
        subMenusGlobal = new ArrayList<>();
        platillosGlobal = new ArrayList<>();
        idPlatillo = getIntent().getIntExtra("idPlatillo", 0);
        mostrarPlatillo(idPlatillo);
        mostrarComplementos(idPlatillo);
        agregarAlCarritoListener();
        cargarImg();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                    Intent i = new Intent(SeleccionarComplementos.this, CarritoActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(SeleccionarComplementos.this, "No Pudo :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public PlatilloSeleccionado registrarPlatillo() {
        /*
         * en caso se agregue cantidad a la tabla platillo seleccionado
         * se usara el codigo que esta como comentario en este metodo
         * */
        // final int cantidad = numberPicker.getValue();
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
        //platilloSeleccionado.setPrecio(platilloActual.getPrecioBase() * cantidad);
        platilloSeleccionado.setPrecio(platilloActual.getPrecioBase());
        PlatillosSeleccionadoSQLite platillosSeleccionadoSQLite = new PlatillosSeleccionadoSQLite(SeleccionarComplementos.this);
        Long idd = platillosSeleccionadoSQLite.create(platilloSeleccionado);
        platilloSeleccionado.setPlatilloSeleccionadoId(idd);
        List<PlatilloSeleccionado> list = Logued.platillosSeleccionadosActuales;
        if (list == null) {
            list = new LinkedList<>();
        }
        if (!GlobalRestaurantes.opcionesDeSubMenusSeleccionados.isEmpty()) {
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
        // final int cantidad = numberPicker.getValue();
        double adicional = 0;
        for (OpcionesDeSubMenu opcione : listOpcionesExtra) {
            OpcionesDeSubMenuSeleccionado op = new OpcionesDeSubMenuSeleccionado();
            op.setOpcionesDeSubMenuSeleccionadoId(opcione.getOpcionesDeSubmenuId());
            op.setOpcionesDeSubMenu(opcione);
            op.setPlatilloSeleccionado(platilloSeleccionado);
            op.setNombre(opcione.getNombre());
            OpcionesDeSubMenuSeleccionadoSQLite opcionesDeSubMenuSeleccionadoSQLite = new OpcionesDeSubMenuSeleccionadoSQLite(SeleccionarComplementos.this);
            Long opid = opcionesDeSubMenuSeleccionadoSQLite.create(op);
            op.setOpcionesDeSubMenuSeleccionadoId(opid);
            opcionesSeleccionadas.add(op);
            // adicional = adicional + (opcione.getPrecio() * cantidad);
            adicional = adicional + (opcione.getPrecio());
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
        List<Pedido> pedidos = Logued.pedidosActuales;
        if (pedidos == null) {
            pedidos = new ArrayList<>();
        }
        Pedido ped = null;
        for (Pedido pe : pedidos) {
            if (pe.getPedidoId().intValue() == platilloActual.getMenu().getRestaurante().getRestauranteId()) {
                ped = pe;
            }
        }
        PedidoSQLite pedidoSQLite = new PedidoSQLite(SeleccionarComplementos.this);
        if (ped == null) {
            ped = new Pedido();
            ped.setPedidoId(platilloActual.getMenu().getRestaurante().getRestauranteId());
            ped.setRestaurante(platilloActual.getMenu().getRestaurante());
            ped.setUsuario(Logued.usuarioLogued);
            Driver driver = new Driver();
            driver.setDriverId(0L);
            ped.setDrivers(driver);
            ped.setStatus(1);
            ped.setFormaDePago("Efectivo");
            ped.setTotalDePedido(0);
            ped.setTotalEnRestautante(0);
            ped.setTotalDeCargosExtra(0);
            ped.setTotalEnRestautanteSinComision(0);
            ped.setPedidoPagado(false);
            ped.setFechaOrdenado(new Date());
            ped.setTiempoPromedioEntrega(platilloActual.getMenu().getRestaurante().getTiempoEstimadoDeEntrega());
            ped.setPedidoEntregado(false);
            ped.setNotas("no confirmado");
            ped.setTiempoAdicional("00:00:00");
            ped.setDireccion("no direction");
            pedidos.add(ped);
            Logued.pedidosActuales = pedidos;
        }
        return ped;
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

    private void mostrarPlatillo(int idPlatillo) {
        if (!GlobalRestaurantes.platilloList.isEmpty()) {
            Platillo platillo = null;
            for (Platillo pl : GlobalRestaurantes.platilloList) {
                if (pl.getPlatilloId().intValue() == idPlatillo) {
                    platillo = pl;
                }
            }
            if (platillo != null) {
                final Platillo finalPlatillo = platillo;
                tvFoodName.setText(finalPlatillo.getNombre());
                tvFoodPrice.setText(String.valueOf(finalPlatillo.getPrecioBase()));
                numberPicker.setValueChangedListener(new ValueChangedListener() {
                    @Override
                    public void valueChanged(int value, ActionEnum action) {
                        double total;
                        total = Double.parseDouble(String.valueOf(finalPlatillo.getPrecioBase() * value));
                        tvFoodPrice.setText(String.valueOf(total));
                    }
                });
            }
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
                platilloActual = opcionesDeSubMenus.get(0).getSubMenu().getPlatillo();
                GlobalRestaurantes.opcionesDeSubMenusSeleccionados = new ArrayList<>();
            }
            RecyclerSubMenuAdapter2 recyclerSubMenuAdapter = new RecyclerSubMenuAdapter2(subMenus, opcionesDeSubMenus, SeleccionarComplementos.this);
            rvComplementsArea.setLayoutManager(new LinearLayoutManager(SeleccionarComplementos.this, LinearLayoutManager.VERTICAL, false));
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
                Utilities.displayAppCompatImageFoodFromBytea(image.getContent(), appCompatImageView, SeleccionarComplementos.this);
                System.out.println("asynk display image ! !!!!!!!!!!!!!!!!");
            }
        }
    }

}
