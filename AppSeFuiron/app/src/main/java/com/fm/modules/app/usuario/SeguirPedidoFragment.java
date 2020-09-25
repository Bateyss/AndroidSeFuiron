package com.fm.modules.app.usuario;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.fm.modules.R;
import com.fm.modules.app.login.Logued;
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
        StatusOne.setTextColor(getResources().getColor(R.color.gray));
        StatusTwo.setTextColor(getResources().getColor(R.color.gray));
        StatusThree.setTextColor(getResources().getColor(R.color.gray));
        StatusFour.setTextColor(getResources().getColor(R.color.gray));
        obtenerUltimoPedido();
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
                    pedido = pedidos.get(pedidos.size() - 1);
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
                        driverId.setText(String.valueOf(pedido.getDrivers().getDriverId()));
                        timeStimate.setText(pedido.getTiempoPromedioEntrega());
                        switch (pedido.getStatus()) {
                            case 1:
                                StatusOne.setTextColor(getResources().getColor(R.color.purple));
                                break;
                            case 2:
                                StatusOne.setTextColor(getResources().getColor(R.color.purple));
                                StatusTwo.setTextColor(getResources().getColor(R.color.purple));
                                break;
                            case 3:
                                StatusOne.setTextColor(getResources().getColor(R.color.purple));
                                StatusTwo.setTextColor(getResources().getColor(R.color.purple));
                                StatusThree.setTextColor(getResources().getColor(R.color.purple));
                                break;
                            case 4:
                                StatusOne.setTextColor(getResources().getColor(R.color.purple));
                                StatusTwo.setTextColor(getResources().getColor(R.color.purple));
                                StatusThree.setTextColor(getResources().getColor(R.color.purple));
                                StatusFour.setTextColor(getResources().getColor(R.color.purple));
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
}