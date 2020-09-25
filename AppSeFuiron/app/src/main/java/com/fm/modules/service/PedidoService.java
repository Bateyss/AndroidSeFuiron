package com.fm.modules.service;

import com.fm.modules.models.Pedido;

import java.io.Serializable;
import java.util.List;

public class PedidoService extends RestTemplateEntity<Pedido> implements Serializable {

    private static final long serialVersionUID = 1L;

    public PedidoService() {
        super(new Pedido(), Pedido.class, Pedido[].class);
    }

    private final String url = Constantes.URL_PEDIDO;

    public List<Pedido> obtenerPedidos() {
        List<Pedido> lista = getListURL(url);
        return lista;
    }

    public List<Pedido> obtenerMyPedidos(Long idUsuario) {
        List<Pedido> lista = getListURL(url.concat("/obtenerPedidoUsuario/").concat(idUsuario.toString()));
        return lista;
    }

    public Pedido obtenerPedidoPorId(Long id) {
        Pedido enti = getOneURL(url, id);
        return enti;
    }

    public Pedido obtenerPedidoPorBody(Pedido objeto) {
        Pedido enti = getByBodyURL(url, objeto);
        return enti;
    }

    public Pedido crearPedido(Pedido objeto) {
        Pedido enti = createURL(url, objeto);
        return enti;
    }

    public Pedido actualizarPedidoPorId(Pedido objeto) {
        Pedido enti = updateURL(url, objeto.getPedidoId(), objeto);
        return enti;
    }

    public void eliminarPedidoPorId(Long id) {
        deleteURL(url, id);
    }

}
