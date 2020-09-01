package com.fm.modules.service;

import com.fm.modules.models.Driver;

import java.io.Serializable;
import java.util.List;

public class DriverService extends RestTemplateEntity<Driver> implements Serializable {

    public DriverService() {
        super(new Driver(), Driver.class, Driver[].class);
        // TODO Auto-generated constructor stub
    }

    private static final long serialVersionUID = 1L;

    private final String url = Constantes.URL_PEDIDO;

    public List<Driver> obtenerDrivers() {
        List<Driver> lista = getListURL(url);
        return lista;
    }

    public Driver obtenerDriverPorId(Long id) {
        Driver enti = getOneURL(url, id);
        return enti;
    }

    public Driver obtenerDriverPorBody(Driver objeto) {
        Driver enti = getByBodyURL(url, objeto);
        return enti;
    }

    public Driver crearDriver(Driver objeto) {
        Driver enti = createURL(url, objeto);
        return enti;
    }

    public Driver actualizarDriverPorId(Driver objeto) {
        Driver enti = updateURL(url, objeto.getDriverId(), objeto);
        return enti;
    }

    public void eliminarDriverPorId(Long id) {
        deleteURL(url, id);
    }

}
