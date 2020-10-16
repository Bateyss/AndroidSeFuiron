package com.fm.modules.service;

import com.fm.modules.models.Promociones;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.List;

public class PromocionesService extends RestTemplateEntity<Promociones> implements Serializable {


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public PromocionesService() {
        super(new Promociones(), Promociones.class, Promociones[].class);
    }

    private final String url = Constantes.URL_PROMOCION;

    public List<Promociones> obtenerPromociones() {
        List<Promociones> lista = getListURL(url);
        return lista;
    }

    public Promociones obtenerPromocionsPorId(Long id) {
        Promociones enti = getOneURL(url, id);
        return enti;
    }

    public Promociones obtenerPromocionsporBody(Promociones objeto) {
        Promociones enti = getByBodyURL(url, objeto);
        return enti;
    }

    public Promociones crearPromocions(Promociones objeto) {
        Promociones enti = createURL(url, objeto);
        return enti;
    }

    public Promociones actualizarPromocion(Promociones objeto) {
        Promociones enti = updateURL(url, objeto.getPromocionId(), objeto);
        return enti;
    }

    public void eliminarPromocion(Long id) {
        deleteURL(url, id);
    }

    public Integer obtenerPromocionesCantidad() {
        Integer cantidad = 0;
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Integer> response = restTemplate.getForEntity(url.concat("/countDay"), Integer.class);
            cantidad = response.getBody();
        } catch (Exception e) {
            System.out.println("error absRest obtenerPromocionesCantidad: " + getClass().getName() + e);
            e.printStackTrace();
        }
        return cantidad;
    }

    public List<Promociones> obtenerPromocionesDeHoy() {
        List<Promociones> lista = getListURL(url.concat("/ofDay"));
        return lista;
    }

    public List<Promociones> obtenerPromocionesDeHoyNonDelay() {
        List<Promociones> lista = getListURL(url.concat("/ofDayNonDelay"));
        return lista;
    }

}
