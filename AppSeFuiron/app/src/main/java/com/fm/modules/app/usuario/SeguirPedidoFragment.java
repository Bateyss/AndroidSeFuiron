package com.fm.modules.app.usuario;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fm.modules.R;
import com.fm.modules.app.login.Logued;
import com.fm.modules.app.restaurantes.RestaurantePorCategoria;
import com.fm.modules.models.Pedido;
import com.fm.modules.models.Usuario;
import com.fm.modules.service.PedidoService;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class SeguirPedidoFragment extends Fragment {

    private View viewGlobal;
    private MaterialTextView driverName;
    private MaterialTextView driverId;
    private MaterialTextView timeStimate;
    private MaterialTextView StatusOne;
    private MaterialTextView StatusTwo;
    private MaterialTextView StatusThree;
    private MaterialTextView StatusFour;
    private ImageView pointer1;
    private ImageView pointer2;
    private ImageView pointer3;
    private ImageView pointer4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seguir_pedido, container, false);
        viewGlobal = view;
        driverName = (MaterialTextView) view.findViewById(R.id.seguirPedidoDriverName);
        driverId = (MaterialTextView) view.findViewById(R.id.seguirPedidoDriverId);
        timeStimate = (MaterialTextView) view.findViewById(R.id.seguirPedidoTimeStimated);
        StatusOne = (MaterialTextView) view.findViewById(R.id.seguirPedidoStatusOne);
        StatusTwo = (MaterialTextView) view.findViewById(R.id.seguirPedidoStatusTwo);
        StatusThree = (MaterialTextView) view.findViewById(R.id.seguirPedidoStatusThree);
        StatusFour = (MaterialTextView) view.findViewById(R.id.seguirPedidoStatusFour);
        pointer1 = (ImageView) view.findViewById(R.id.seguirPedidoPointer1);
        pointer2 = (ImageView) view.findViewById(R.id.seguirPedidoPointer2);
        pointer3 = (ImageView) view.findViewById(R.id.seguirPedidoPointer3);
        pointer4 = (ImageView) view.findViewById(R.id.seguirPedidoPointer4);
        StatusOne.setTextColor(getResources().getColor(R.color.gray));
        StatusTwo.setTextColor(getResources().getColor(R.color.gray));
        StatusThree.setTextColor(getResources().getColor(R.color.gray));
        StatusFour.setTextColor(getResources().getColor(R.color.gray));
        obtenerUltimoPedido();
        onBack();
        return view;
    }

    private void obtenerUltimoPedido() {
        MyOrdenes myOrdenes = new MyOrdenes();
        myOrdenes.execute();
    }

    private class MyOrdenes extends AsyncTask<String, String, Pedido> {

        @Override
        protected Pedido doInBackground(String... strings) {
            Pedido pedido = null;
            try {
                List<Pedido> pedidos = new ArrayList<>();
                PedidoService pedidoService = new PedidoService();
                Usuario usuario = Logued.usuarioLogued;
                if (usuario != null) {
                    pedidos = pedidoService.obtenerMyPedidos(usuario.getUsuarioId());
                }
                if (!pedidos.isEmpty()) {
                    int ultimoPedido = 0;
                    for (Pedido pr : pedidos) {
                        if (pr.getPedidoId().intValue() > ultimoPedido) {
                            ultimoPedido = pr.getPedidoId().intValue();
                            pedido = pr;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error en MyOrdenes:" + e);
            }
            return pedido;
        }

        @Override
        protected void onPostExecute(Pedido pedido) {
            super.onPostExecute(pedido);
            try {
                if (pedido != null) {
                    if (pedido.getDrivers() != null) {
                        driverName.setText(pedido.getDrivers().getNombreDriver());
                        String driverNumber = "# " + pedido.getDrivers().getDriverId().toString();
                        driverId.setText(driverNumber);
                        timeStimate.setText(pedido.getTiempoPromedioEntrega());
                        switch (pedido.getStatus()) {
                            case 0:
                            case 1:
                                StatusOne.setTextColor(getResources().getColor(R.color.purple));
                                pointer1.setImageDrawable(viewGlobal.getResources().getDrawable(R.drawable.ic_pointer));
                                break;
                            case 2:
                                StatusOne.setTextColor(getResources().getColor(R.color.purple));
                                StatusTwo.setTextColor(getResources().getColor(R.color.purple));
                                pointer1.setImageDrawable(viewGlobal.getResources().getDrawable(R.drawable.ic_pointer));
                                pointer2.setImageDrawable(viewGlobal.getResources().getDrawable(R.drawable.ic_pointer));
                                break;
                            case 3:
                                StatusOne.setTextColor(getResources().getColor(R.color.purple));
                                StatusTwo.setTextColor(getResources().getColor(R.color.purple));
                                StatusThree.setTextColor(getResources().getColor(R.color.purple));
                                pointer1.setImageDrawable(viewGlobal.getResources().getDrawable(R.drawable.ic_pointer));
                                pointer2.setImageDrawable(viewGlobal.getResources().getDrawable(R.drawable.ic_pointer));
                                pointer3.setImageDrawable(viewGlobal.getResources().getDrawable(R.drawable.ic_pointer));
                                break;
                            case 4:
                                StatusOne.setTextColor(getResources().getColor(R.color.purple));
                                StatusTwo.setTextColor(getResources().getColor(R.color.purple));
                                StatusThree.setTextColor(getResources().getColor(R.color.purple));
                                StatusFour.setTextColor(getResources().getColor(R.color.purple));
                                pointer1.setImageDrawable(viewGlobal.getResources().getDrawable(R.drawable.ic_pointer));
                                pointer2.setImageDrawable(viewGlobal.getResources().getDrawable(R.drawable.ic_pointer));
                                pointer3.setImageDrawable(viewGlobal.getResources().getDrawable(R.drawable.ic_pointer));
                                pointer4.setImageDrawable(viewGlobal.getResources().getDrawable(R.drawable.ic_pointer));
                                break;
                        }
                    } else {
                        String neles = "No hay";
                        driverName.setText(neles);
                        neles = "0";
                        driverId.setText(neles);
                        neles = "00:00:00";
                        timeStimate.setText(neles);
                    }
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

    public void onBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                showFragment(new RestaurantePorCategoria());
            }
        };
        getActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);
    }

    private void showFragment(Fragment fragment) {
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}