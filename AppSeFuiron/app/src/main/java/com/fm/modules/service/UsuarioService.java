package com.fm.modules.service;

import com.fm.modules.models.Usuario;

import java.io.Serializable;
import java.util.List;

public class UsuarioService extends RestTemplateEntity<Usuario> implements Serializable {

    public UsuarioService() {
        super(new Usuario(), Usuario.class, Usuario[].class);
    }

    private static final long serialVersionUID = 1L;

    private final String url = Constantes.URL_USUARIO;

    public List<Usuario> obtenerUsuarios() {
        List<Usuario> lista = getListURL(url);
        return lista;
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        Usuario enti = getOneURL(url, id);
        return enti;
    }

    public Usuario obtenerUsuarioPorBody(Usuario objeto) {
        Usuario enti = getByBodyURL(url, objeto);
        return enti;
    }

    public Usuario crearUsuario(Usuario objeto) {
        Usuario enti = createURL(url, objeto);
        return enti;
    }

    public Usuario actualizarUsuarioPorId(Usuario objeto) {
        Usuario enti = updateURL(url, objeto.getUsuarioId(), objeto);
        return enti;
    }

    public void eliminarUsuarioPorId(Long id) {
        deleteURL(url, id);
    }

}
