package com.fm.modules.sqlite.models;


import androidx.annotation.NonNull;

import com.fm.modules.app.login.Logued;
import com.fm.modules.service.Constantes;
import com.fm.modules.service.RestTemplateEntity;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TarjetasService extends RestTemplateEntity<Tarjetas> implements Serializable {

    public TarjetasService() {
        super(new Tarjetas(), Tarjetas.class, Tarjetas[].class);
        // TODO Auto-generated constructor stub
    }

    private static final long serialVersionUID = 1L;

    private final String url = Constantes.DOMINIO;

    public List<TarjetasSaved> obtenerTarjetasDeUsuario(@NonNull Long idUsuario) {
        List<TarjetasSaved> tarjetas = new ArrayList<>();
        if (Logued.usuarioLogued != null && Logued.usuarioLogued.getUsuarioId() != null) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<TarjetasSaved[]> response = restTemplate.getForEntity(url.concat("/userCard/").concat(idUsuario.toString()), TarjetasSaved[].class);
                tarjetas = Arrays.asList(response.getBody());
            } catch (Exception e) {
                System.out.println("error obtenerTarjetasDeUsuario: " + getClass().getName() + e);
            }
        }
        return tarjetas;
    }

    public Tarjetas obtenerTarjetaPorId(Long id) {
        Tarjetas enti = getOneURL(url, id);
        return enti;
    }

    public Tarjetas obtenerTarjetaPorBody(Tarjetas objeto) {
        Tarjetas enti = getByBodyURL(url, objeto);
        return enti;
    }

    public TarjetaMsg realizarPago(Tarjetas objeto) {
        TarjetaMsg tarjetaMsg = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Tarjetas> request = new HttpEntity<>(objeto);
            ResponseEntity<TarjetaMsg> response = restTemplate.exchange(url.concat("/executePago"), HttpMethod.POST, request, TarjetaMsg.class);
            tarjetaMsg = response.getBody();
        } catch (Exception e) {
            System.out.println("error realizarPago: " + getClass().getName() + e);
            tarjetaMsg = null;
        }
        return tarjetaMsg;
    }

    public TarjetaMsg realizarPagoPorIdTarjeta(@NonNull Long idTarjeta, double val) {
        TarjetaMsg tarjetaMsg = null;
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<TarjetaMsg> response = restTemplate.getForEntity(url.concat("/pagoTarj/").concat(idTarjeta.toString().concat("/").concat(String.valueOf(val))), TarjetaMsg.class);
            tarjetaMsg = response.getBody();
        } catch (Exception e) {
            System.out.println("error realizarPagoPorIdTarjeta: " + getClass().getName() + e);
            tarjetaMsg = null;
        }
        return tarjetaMsg;
    }


    public void eliminarTarjetaPorId(Long id) {
        deleteURL(url, id);
    }


}
