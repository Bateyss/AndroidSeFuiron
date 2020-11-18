package com.fm.modules.service;

import com.fm.modules.entities.RespuestaPlatilloPorMenu;
import com.fm.modules.models.Platillo;
import com.fm.modules.models.PlatillosNames;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PlatilloService extends RestTemplateEntity<Platillo> implements Serializable {

    private static final long serialVersionUID = 1L;

    public PlatilloService() {
        super(new Platillo(), Platillo.class, Platillo[].class);
    }

    private final String url = Constantes.URL_PLATILLO;

    public List<Platillo> obtenerPlatillos() {
        List<Platillo> lista = getListURL(url);
        return lista;
    }

    public Platillo obtenerPlatilloPorId(Long id) {
        Platillo enti = getOneURL(url, id);
        return enti;
    }

    public Platillo obtenerPlatilloPorBody(Platillo objeto) {
        Platillo enti = getByBodyURL(url, objeto);
        return enti;
    }

    public Platillo crearPlatillo(Platillo objeto) {
        Platillo enti = createURL(url, objeto);
        return enti;
    }

    public Platillo actualizarPlatillo(Platillo objeto) {
        Platillo enti = updateURL(url, objeto.getPlatilloId(), objeto);
        return enti;
    }

    public void eliminarPlatilloPorId(Long id) {
        deleteURL(url, id);
    }

    public List<RespuestaPlatilloPorMenu> platilloPorMenu(String idMenu) {
        List<RespuestaPlatilloPorMenu> list = new LinkedList<>();
        try {
            RestTemplate restTemplat = new RestTemplate();
            ResponseEntity<RespuestaPlatilloPorMenu[]> response = restTemplat.getForEntity(Constantes.DOMINIO.concat("/platilloPorMenu/").concat(idMenu), RespuestaPlatilloPorMenu[].class);
            list = Arrays.asList(response.getBody());
        } catch (Exception e) {
            System.out.println("error eliminarPlatilloPorId: " + e.getMessage() + " " + e.getClass());
        }
        return list;
    }

    public List<PlatillosNames> platilloNombres() {
        List<PlatillosNames> list = new LinkedList<>();
        try {
            RestTemplate restTemplat = new RestTemplate();
            ResponseEntity<PlatillosNames[]> response = restTemplat.getForEntity(url.concat("/names"), PlatillosNames[].class);
            list = Arrays.asList(response.getBody());
        } catch (Exception e) {
            System.out.println("error platilloNombres: " + e.getMessage() + " " + e.getClass());
        }
        return list;
    }

}
