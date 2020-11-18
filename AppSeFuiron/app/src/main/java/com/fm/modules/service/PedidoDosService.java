package com.fm.modules.service;

import com.fm.modules.models.PedidoDos;

import java.io.Serializable;
import java.util.List;

public class PedidoDosService extends RestTemplateEntity<PedidoDos> implements Serializable {

    private static final long serialVersionUID = 1L;

    public PedidoDosService() {
        super(new PedidoDos(), PedidoDos.class, PedidoDos[].class);
    }

    private final String url = Constantes.URL_PEDIDO;

    public List<PedidoDos> obtenerPedidos() {
        List<PedidoDos> lista = getListURL(url);
        return lista;
    }

    public List<PedidoDos> obtenerMyPedidos(Long idUsuario) {
        List<PedidoDos> lista = getListURL(url.concat("/obtenerPedidoUsuario/").concat(idUsuario.toString()));
        return lista;
    }

    public PedidoDos obtenerPedidoPorId(Long id) {
        PedidoDos enti = getOneURL(url, id);
        return enti;
    }

    public PedidoDos obtenerPedidoPorBody(PedidoDos objeto) {
        PedidoDos enti = getByBodyURL(url, objeto);
        return enti;
    }

    public PedidoDos crearPedido(PedidoDos objeto) {
        PedidoDos enti = createURL(url, objeto);
        return enti;
    }

    public PedidoDos actualizarPedidoPorId(PedidoDos objeto) {
        PedidoDos enti = updateURL(url, objeto.getPedidoId(), objeto);
        return enti;
    }

    public void eliminarPedidoPorId(Long id) {
        deleteURL(url, id);
    }

}
