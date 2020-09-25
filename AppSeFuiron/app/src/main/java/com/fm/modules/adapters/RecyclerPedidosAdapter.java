package com.fm.modules.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.fm.modules.R;
import com.fm.modules.app.carrito.PagoActivity;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.usuario.GlobalUsuario;
import com.fm.modules.models.Driver;
import com.fm.modules.models.OpcionesDeSubMenuSeleccionado;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.PlatilloSeleccionado;
import com.fm.modules.models.Restaurante;
import com.fm.modules.service.DriverService;
import com.fm.modules.service.OpcionSubMenuSeleccionadoService;
import com.fm.modules.service.PlatilloSeleccionadoService;
import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerPedidosAdapter extends RecyclerView.Adapter<RecyclerPedidosAdapter.ViewHolder> {

    private List<Pedido> items;
    private Context context;
    private FragmentActivity fragmentActivity;

    public RecyclerPedidosAdapter(List<Pedido> pedidos, Context context, FragmentActivity fragmentActivity) {
        this.items = pedidos;
        this.context = context;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_item_pedido, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.asignarDatos(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        AppCompatTextView title;
        AppCompatTextView subTitle;
        AppCompatTextView price;
        MaterialCardView cardViewItem;
        Driver driver = new Driver();
        private Long orderId = 0L;
        private MyPlatilloOrdered myPlatilloOrdered = new MyPlatilloOrdered();

        public ViewHolder(View view) {
            super(view);
            checkBox = (CheckBox) view.findViewById(R.id.myorderChekvbox);
            title = (AppCompatTextView) view.findViewById(R.id.myorderTitle);
            subTitle = (AppCompatTextView) view.findViewById(R.id.myorderSubTitle);
            price = (AppCompatTextView) view.findViewById(R.id.myorderPrice);
            cardViewItem = (MaterialCardView) view.findViewById(R.id.myorderCardItem);
        }

        public void asignarDatos(final Pedido pedido) {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            final DecimalFormat decimalFormat = new java.text.DecimalFormat("#,##0.00");
            String titulo = "Orden #" + pedido.getPedidoId();
            String precio = "$ " + decimalFormat.format(pedido.getTotalEnRestautante());
            String fecha = simpleDateFormat.format(pedido.getFechaOrdenado());
            checkBox.setChecked(pedido.isPedidoEntregado());
            checkBox.setEnabled(false);
            title.setText(titulo);
            subTitle.setText(fecha);
            price.setText(precio);
            orderId = pedido.getPedidoId();
            cardViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderId.intValue() != 0) {
                        cardViewItem.setEnabled(false);
                        myPlatilloOrdered.execute();
                    }
                }
            });
        }

        private class MyPlatilloOrdered extends AsyncTask<String, String, List<PlatilloSeleccionado>> {

            @Override
            protected List<PlatilloSeleccionado> doInBackground(String... strings) {
                List<PlatilloSeleccionado> platilloSeleccionados = new ArrayList<>();
                try {
                    DriverService driverService = new DriverService();
                    driver = driverService.obtenerDriverPorId(1L);
                    PlatilloSeleccionadoService platilloSeleccionadoService = new PlatilloSeleccionadoService();
                    if (orderId.intValue() != 0) {
                        platilloSeleccionados = platilloSeleccionadoService.obtenerPlatilloSeleccionadosPorPedido(orderId);
                        System.out.println("Platillos seleccionados por pedido " + platilloSeleccionados.size());
                    }
                    System.out.println("Platillos seleccionados por pedido");
                    if (!platilloSeleccionados.isEmpty()) {
                        GlobalUsuario.platilloSeleccionadosEnMyOrdenes = platilloSeleccionados;
                        cargarComplementos(platilloSeleccionados);
                    } else {
                        GlobalUsuario.platilloSeleccionadosEnMyOrdenes = new ArrayList<>();
                        GlobalUsuario.opcionesDeSubMenuSeleccionadosEnMyOrdenes = new ArrayList<>();
                    }
                } catch (Exception e) {
                    System.out.println("Error en UnderThreash:" + e.getMessage() + " " + e.getClass());
                }
                return platilloSeleccionados;
            }

            private void cargarComplementos(List<PlatilloSeleccionado> lista) {
                List<OpcionesDeSubMenuSeleccionado> opciones = new ArrayList<>();
                OpcionSubMenuSeleccionadoService opcionSubMenuSeleccionadoService = new OpcionSubMenuSeleccionadoService();
                for (PlatilloSeleccionado platillo : lista) {
                    List<OpcionesDeSubMenuSeleccionado> op = opcionSubMenuSeleccionadoService.obtenerOpcionSubMenusPosPlatilloSeleccionado(platillo.getPlatilloSeleccionadoId());
                    if (!op.isEmpty()) {
                        opciones.addAll(op);
                    }
                }
                GlobalUsuario.opcionesDeSubMenuSeleccionadosEnMyOrdenes = opciones;
            }

            @Override
            protected void onPostExecute(List<PlatilloSeleccionado> platilloSeleccionados) {
                super.onPostExecute(platilloSeleccionados);
                try {
                    if (!platilloSeleccionados.isEmpty()) {
                        Toast.makeText(context, "platillos seleccionados", Toast.LENGTH_SHORT).show();
                        for (PlatilloSeleccionado pla : platilloSeleccionados) {
                            if (!pla.getPlatillo().getDisponible()) {
                                Toast.makeText(context, "No Disponible", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        Restaurante restaurante = platilloSeleccionados.get(0).getPedido().getRestaurante();
                        if (restaurante != null) {
                            SimpleDateFormat sp = new SimpleDateFormat("HH:mm:ss");
                            Date actualDate = new Date();
                            String actualHuorString = sp.format(actualDate);
                            Date actualHour = sp.parse(actualHuorString);
                            Date restaurantCloseHour = sp.parse(restaurante.getHorarioDeCierre());
                            Date restaurantOpenHour = sp.parse(restaurante.getHorarioDeApertura());
                            if (restaurantCloseHour.getTime() < actualHour.getTime() || actualHour.getTime() < restaurantOpenHour.getTime()) {
                                Toast.makeText(context, "Restaurante Cerrado", Toast.LENGTH_SHORT).show();
                                /*Logued.opcionesDeSubMenusEnPlatillosSeleccionados = new ArrayList<>();
                                Logued.platillosSeleccionadosActuales = new ArrayList<>();
                                Logued.restauranteActual = null;
                                Logued.pedidoActual = null;*/
                                return;
                            }
                        }
                        Pedido pedidoNuevo = platilloSeleccionados.get(0).getPedido();
                        pedidoNuevo.setStatus(0);
                        pedidoNuevo.setFechaOrdenado(new Date());
                        pedidoNuevo.setFormaDePago("Efectivo");
                        pedidoNuevo.setPedidoPagado(false);
                        pedidoNuevo.setPedidoEntregado(false);
                        pedidoNuevo.setTiempoAdicional("00:00:00");
                        pedidoNuevo.setTiempoPromedioEntrega(restaurante != null ? restaurante.getTiempoEstimadoDeEntrega() : "00:00:00");
                        pedidoNuevo.setDrivers(driver);
                        Logued.pedidoActual = pedidoNuevo;
                        Logued.restauranteActual = restaurante;
                        List<PlatilloSeleccionado> platilloSeleccionados1 = new ArrayList<>();
                        List<OpcionesDeSubMenuSeleccionado> opcionesDeSubMenuSeleccionados = new ArrayList<>();
                        List<OpcionesDeSubMenuSeleccionado> opcionesDeSubMenuSeleccionados1 = GlobalUsuario.opcionesDeSubMenuSeleccionadosEnMyOrdenes;
                        for (PlatilloSeleccionado pla : platilloSeleccionados) {
                            if (pla.getPedido().getPedidoId().intValue() == orderId.intValue()) {
                                platilloSeleccionados1.add(pla);
                                if (opcionesDeSubMenuSeleccionados1 != null && !opcionesDeSubMenuSeleccionados1.isEmpty()) {
                                    for (OpcionesDeSubMenuSeleccionado opcione : opcionesDeSubMenuSeleccionados1) {
                                        if (opcione.getPlatilloSeleccionado().getPlatilloSeleccionadoId().intValue() == pla.getPlatilloSeleccionadoId().intValue()) {
                                            opcionesDeSubMenuSeleccionados.add(opcione);
                                        }
                                    }
                                }
                            }
                        }
                        Logued.opcionesDeSubMenusEnPlatillosSeleccionados = opcionesDeSubMenuSeleccionados;
                        Logued.platillosSeleccionadosActuales = platilloSeleccionados;
                        showFragment(new PagoActivity());
                    }
                } catch (Exception e) {
                    System.out.println("Error Activity: " + e);
                }
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
