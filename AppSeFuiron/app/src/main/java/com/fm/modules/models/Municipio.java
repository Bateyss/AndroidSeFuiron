package com.fm.modules.models;

import java.math.BigDecimal;

public class Municipio {

    private Long municipioId;
    private Departamento departamento;
    private String nombreMunicipio;
    private BigDecimal tarifa;
    private String tiempoEntrega;

    public Municipio() {
    }

    public Municipio(Long municipioId, Departamento departamento, String nombreMunicipio, BigDecimal tarifa,
                     String tiempoEntrega) {
        this.municipioId = municipioId;
        this.departamento = departamento;
        this.nombreMunicipio = nombreMunicipio;
        this.tarifa = tarifa;
        this.tiempoEntrega = tiempoEntrega;
    }

    public Long getMunicipioId() {
        return municipioId;
    }

    public void setMunicipioId(Long municipioId) {
        this.municipioId = municipioId;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

    public BigDecimal getTarifa() {
        return tarifa;
    }

    public void setTarifa(BigDecimal tarifa) {
        this.tarifa = tarifa;
    }

    public String getTiempoEntrega() {
        return tiempoEntrega;
    }

    public void setTiempoEntrega(String tiempoEntrega) {
        this.tiempoEntrega = tiempoEntrega;
    }

}
