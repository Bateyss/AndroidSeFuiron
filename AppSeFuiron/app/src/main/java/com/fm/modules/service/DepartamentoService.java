package com.fm.modules.service;

import androidx.annotation.NonNull;

import com.fm.modules.models.Departamento;

import java.io.Serializable;
import java.util.List;

public class DepartamentoService extends RestTemplateEntity<Departamento> implements Serializable {

    private static final long serialVersionUID = 1L;

    public DepartamentoService() {
        super(new Departamento(), Departamento.class, Departamento[].class);
    }

    private final String url = Constantes.URL_DEPARTAMENTOS;

    public List<Departamento> obtenerDepartamentos() {
        List<Departamento> lista = getListURL(url);
        return lista;
    }

    public List<Departamento> obtenerDepartamentosPorIdPais(@NonNull Long id) {
        List<Departamento> lista = getListURL(Constantes.DOMINIO.concat("/departamentosByPaisId/").concat(id.toString()));
        return lista;
    }

    public Departamento obtenerDepartamentoPorId(Long id) {
        Departamento enti = getOneURL(url, id);
        return enti;
    }

    public Departamento obtenerDepartamentoPorBody(Departamento objeto) {
        Departamento enti = getByBodyURL(url, objeto);
        return enti;
    }

    public Departamento crearDepartamento(Departamento objeto) {
        Departamento enti = createURL(url, objeto);
        return enti;
    }

    public Departamento actualizarDepartamentoPorId(Departamento objeto) {
        Departamento enti = updateURL(url, objeto.getDepartamentoId(), objeto);
        return enti;
    }

    public void eliminarDepartamentoPorId(Long id) {
        deleteURL(url, id);
    }
}
